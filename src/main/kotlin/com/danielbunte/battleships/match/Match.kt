package com.danielbunte.battleships.match

class Match {
    private val maxPlayers = 2
    private val players: MutableList<Player> = mutableListOf()

    fun addPlayer(player: Player) {
        if (players.size == maxPlayers) {
            throw IllegalArgumentException("Too many players!")
        }
        players.add(player)
    }


}
