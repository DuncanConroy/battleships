package com.danielbunte.battleships.simulator

import com.danielbunte.battleships.ai.AIClient
import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship
import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.io.Dto
import com.danielbunte.battleships.io.PrintResultSubscriber
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player

class GameSimulator : Subscriber {

    private var isActive = true
    private var roundsPlayed = 0

    fun start() {
        val playerA = Player("Kotlin")
        val playerB = Player("Rust")
        val match = Match()
        match.addPlayer(playerA)
        match.addPlayer(playerB)
        val fleet = listOf(
            Ship("Carrier", 5),
            Ship("Battleship", 4),
            Ship("Cruiser", 3),
            Ship("Submarine", 3),
            Ship("Destroyer", 2),
        )
        match.initGame(GameBoard(10, 10), fleet)

        val aiClientA = AIClient(playerA)
        val aiClientB = AIClient(playerB)
        aiClientA.initWithMatch(match, playerB)
        aiClientB.initWithMatch(match, playerA)

        val printer = PrintResultSubscriber()
        printer.subscribeToMatch(match)
        this.subscribeToMatch(match)

        while (isActive) {
            roundsPlayed++
            aiClientA.tick()
            aiClientB.tick()
        }
    }

    override fun receive(dto: Dto) {
        when (dto) {
            is AttackResultDto -> {
                if (dto.gameResult == "WON") {
                    println()
                    println("Player \"${dto.attackingPlayerName}\" has won the match after $roundsPlayed rounds!")
                    isActive = false
                }
            }
        }
    }

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }
}