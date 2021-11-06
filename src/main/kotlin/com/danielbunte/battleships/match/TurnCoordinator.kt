package com.danielbunte.battleships.match

class TurnCoordinator(private val players: List<Player>) {

    private var currentPlayer: Int = 0

    fun canMakeTurn(player: Player): Pair<TurnResult, Player> {
        if (players[currentPlayer] !== player) {
            return TurnResult.NOT_YOUR_TURN to players[currentPlayer]
        }

        if (currentPlayer++ == players.size - 1) {
            currentPlayer = 0
        }

        return TurnResult.PROCEED to players[currentPlayer]
    }

}
