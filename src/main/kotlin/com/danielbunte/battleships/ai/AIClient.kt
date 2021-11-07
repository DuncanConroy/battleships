package com.danielbunte.battleships.ai

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.io.Dto
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import java.util.*

class AIClient(val self: Player) : Subscriber {

    private lateinit var opponent: Player
    private lateinit var states: List<State>
    private var currentState: State? = null

    private val allCoordinates: List<String> = initCoordinates(self.gameBoard)

    private fun initCoordinates(gameBoard: GameBoard): List<String> {
        val coordinates = mutableListOf<String>()
        repeat(gameBoard.width) { x ->
            repeat(gameBoard.height) { y ->
                coordinates.add(gameBoard.convertCoordinates(x, y))
            }
        }

        return coordinates
    }

    var attackedCoordinates = mutableListOf<String>()

    fun initWithMatch(match: Match, opponent: Player) {
        this.opponent = opponent
        subscribeToMatch(match)

        val placeShipsState = PlaceShipsState(this, self, match)
        val explorationState = ExplorationState(this, self, opponent, match)
        val eliminationState = EliminationState(this, explorationState, self, opponent, match)
        placeShipsState.setNextState(explorationState)
        explorationState.setNextState(eliminationState)
        eliminationState.setNextState(explorationState)
        states = listOf(placeShipsState, explorationState, eliminationState)
        currentState = placeShipsState
    }

    fun tick() {
        currentState?.let {
            currentState = it.run()
        }
    }

    fun getAndBlockRandomCoordinate(): String {
        val freeCoordinates = allCoordinates - attackedCoordinates.toSet()
        val index = Random().nextInt(freeCoordinates.size)
        val coordinates = freeCoordinates[index]

        attackedCoordinates.add(coordinates)
        return coordinates
    }

    fun blockCoordinate(gameBoard: GameBoard, x: Int, y: Int): String {
        if (x < 0 || y < 0 || x > gameBoard.width - 1 || y > gameBoard.height - 1) return ""
        val coordinate = gameBoard.convertCoordinates(x, y)
        if (attackedCoordinates.contains(coordinate)) return ""
        attackedCoordinates.add(coordinate)

        return coordinate
    }

    override fun receive(dto: Dto) {
        when (dto) {
            is AttackResultDto -> {
                if (dto.gameResult == "WON") {
                    currentState = null
                }
            }
        }
    }

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }

    fun getRandomCoordinate() = allCoordinates[Random().nextInt(allCoordinates.size)]
}