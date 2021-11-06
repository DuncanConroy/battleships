package com.danielbunte.battleships.match

class Match(private val hitCalculator: HitCalculator, private val turnCalculator: TurnCoordinator) {

    private val maxPlayers = 2
    private val players: MutableList<Player> = mutableListOf()

    fun addPlayer(player: Player) {
        if (players.size == maxPlayers) {
            throw IllegalArgumentException("Too many players!")
        }
        players.add(player)
    }

    fun attemptAttack(attackingPlayer: Player, targetPlayer: Player, coordinates: String): Pair<GameResult, Player?> {
        if (turnCalculator.canMakeTurn(attackingPlayer).first == TurnResult.NOT_YOUR_TURN) {
            return GameResult.NOT_YOUR_TURN to null
        }

        val hitResult = hitCalculator.attemptAttack(targetPlayer.gameBoard, coordinates)
        if (hitResult == HitResult.DESTROYED && calculateActiveShips(targetPlayer) == 0) {
            return GameResult.WON to attackingPlayer
        }

        return GameResult.ONGOING to null
    }

    private fun calculateActiveShips(targetPlayer: Player) = targetPlayer.gameBoard.allShips
        .filterNot { it.health == 0 }
        .size

}
