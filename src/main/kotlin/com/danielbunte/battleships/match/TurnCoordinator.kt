package com.danielbunte.battleships.match

class TurnCoordinator {

    private val maxPlayers = 2
    private val players: MutableList<Player> = mutableListOf()
    private var currentPlayer: Int = 0

    fun addPlayer(player: Player) {
        if (players.size == maxPlayers) {
            throw IllegalArgumentException("Too many players!")
        }
        players.add(player)
    }

    fun makeTurn(player: Player, coordinates: String): Pair<TurnResult, Player> {
        if(players[currentPlayer] !== player){
            return TurnResult.NOT_YOUR_TURN to players[currentPlayer]
        }

        if (currentPlayer++ == players.size - 1) {
            currentPlayer = 0
        }

        return TurnResult.NOT_YOUR_TURN to players[currentPlayer]
    }

}
