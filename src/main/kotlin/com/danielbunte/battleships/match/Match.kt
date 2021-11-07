package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.io.AttackResultConverter

class Match(
    private val hitCalculator: HitCalculator = HitCalculator(),
    private val turnCoordinator: TurnCoordinator = TurnCoordinator(),
    private val attackResultConverter: AttackResultConverter = AttackResultConverter()
) {

    val isActive: Boolean
        get() {
            return placementComplete
        }
    private val maxPlayers = 2
    private val players: MutableList<Player> = mutableListOf()
    private var placementComplete: Boolean = false
    private val subscribers = mutableListOf<Subscriber>()

    fun addPlayer(player: Player) {
        if (players.size == maxPlayers) {
            throw IllegalArgumentException("Too many players!")
        }
        players.add(player)
    }

    fun attemptAttack(attackingPlayer: Player, targetPlayer: Player, coordinates: String): Pair<GameResult, Player?> {
        // to be honest, I don't like the flow of this method atm. needs refactoring with early returns and no checks for gameResult!=null

        var gameResult: GameResult? = null
        var winner: Player? = null
        var nextPlayer: Player? = null
        var hitResult: HitResult? = null
        if (!placementComplete) {
            gameResult = GameResult.SHIP_PLACEMENT
        }

        if (gameResult == null) {
            val turnResult = turnCoordinator.makeTurn(attackingPlayer)
            nextPlayer = turnResult.second
            if (turnResult.first == TurnResult.NOT_YOUR_TURN) {
                gameResult = GameResult.NOT_YOUR_TURN
            }
        }

        if (gameResult == null) {
            hitResult = hitCalculator.attemptAttack(targetPlayer.gameBoard, coordinates)
            targetPlayer.gameBoard.updateCellState(coordinates, hitResult)
            if (hitResult == HitResult.DESTROYED && calculateActiveShips(targetPlayer) == 0) {
                gameResult = GameResult.WON
                winner = attackingPlayer
            }
        }

        if (gameResult == null) {
            gameResult = GameResult.ONGOING
        }
        informSubscribers(attackingPlayer, targetPlayer, coordinates, gameResult, nextPlayer, hitResult, winner)
        return gameResult to winner
    }

    private fun informSubscribers(
        attackingPlayer: Player,
        targetPlayer: Player,
        coordinates: String,
        gameResult: GameResult,
        nextPlayer: Player?,
        hitResult: HitResult?,
        winner: Player?
    ) {
        val attackResult = attackResultConverter.convert(attackingPlayer, targetPlayer, coordinates, gameResult, nextPlayer, hitResult, winner)
        subscribers.forEach {
            it.receive(attackResult)
        }
    }

    private fun calculateActiveShips(targetPlayer: Player) = targetPlayer.gameBoard.allShips
        .filterNot { it.health == 0 }
        .size

    fun initGame(gameBoard: GameBoard, ships: List<Ship>) {
        players.forEach { player ->
            player.init(gameBoard.copy(), ships.map { it.copy(cells = mutableListOf()) })
        }

        turnCoordinator.init(players)
    }

    fun placeShip(player: Player, ship: Ship, coordinates: String, horizontal: Boolean): Boolean {
        if (placementComplete) return false
        val result = player.gameBoard.placeShip(coordinates, ship, horizontal)

        placementComplete = isPlacementComplete()

        return result
    }

    private fun isPlacementComplete() = players.none {
        it.gameBoard.allShips.size != it.shipyard.size
    }

    fun subscribe(subscriber: Subscriber) {
        subscribers.add(subscriber)
    }
}
