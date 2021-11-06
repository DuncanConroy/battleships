package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship

class Match(private val hitCalculator: HitCalculator, private val turnCoordinator: TurnCoordinator) {

    private val maxPlayers = 2
    private val players: MutableList<Player> = mutableListOf()
    private var placementComplete: Boolean = false

    fun addPlayer(player: Player) {
        if (players.size == maxPlayers) {
            throw IllegalArgumentException("Too many players!")
        }
        players.add(player)
    }

    fun attemptAttack(attackingPlayer: Player, targetPlayer: Player, coordinates: String): Pair<GameResult, Player?> {
        if (!placementComplete) {
            return GameResult.SHIP_PLACEMENT to null
        }

        if (turnCoordinator.canMakeTurn(attackingPlayer).first == TurnResult.NOT_YOUR_TURN) {
            return GameResult.NOT_YOUR_TURN to null
        }

        val hitResult = hitCalculator.attemptAttack(targetPlayer.gameBoard, coordinates)
        // TODO: update gameBoard with hitResult
        if (hitResult == HitResult.DESTROYED && calculateActiveShips(targetPlayer) == 0) {
            return GameResult.WON to attackingPlayer
        }

        return GameResult.ONGOING to null
    }

    private fun calculateActiveShips(targetPlayer: Player) = targetPlayer.gameBoard.allShips
        .filterNot { it.health == 0 }
        .size

    fun initGame(gameBoard: GameBoard, ships: List<Ship>) {
        players.forEach { player ->
            player.init(gameBoard.copy(), ships.map { it.copy() })
        }

        turnCoordinator.init(players)
    }

    fun placeShip(playerA: Player, ship: Ship, coordinates: String, horizontal: Boolean) {
        // TODO: test/throw if placement is completed already
        playerA.gameBoard.placeShip(coordinates, ship, horizontal)

        placementComplete = isPlacementComplete()
    }

    private fun isPlacementComplete() = players.none {
        it.gameBoard.allShips.size != it.shipyard.size
    }
}
