package com.danielbunte.battleships.match

class TurnCoordinator {

    private lateinit var players: List<Player>
    private var currentPlayer: Int = 0

    fun makeTurn(player: Player): Pair<TurnResult, Player> {
        if (players[currentPlayer] !== player) {
            return TurnResult.NOT_YOUR_TURN to players[currentPlayer]
        }

        if (currentPlayer++ == players.size - 1) {
            currentPlayer = 0
        }

        return TurnResult.PROCEED to players[currentPlayer]
    }

    fun init(players: List<Player>) {
        this.players = players
    }

}
