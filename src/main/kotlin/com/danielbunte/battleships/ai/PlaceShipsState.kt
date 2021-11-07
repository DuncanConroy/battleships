package com.danielbunte.battleships.ai

import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.io.Dto
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player
import java.util.*

/**
 * In a real-world scenario, the AI wouldn't have access to the player and match objects directly,
 * but to keep effort low, we're taking this shortcut here.
 * The AI should work completely independent of the inner game objects.
 */
class PlaceShipsState(
    private val aiClient: AIClient,
    private val player: Player,
    private val match: Match
) : State, Subscriber {

    private val shipsToPlace = player.shipyard.sortedByDescending { it.length }.toMutableList()
    private var next: State? = null

    init {
        subscribeToMatch(match)
    }

    override fun run(): State? {
        var success: Boolean

        if (shipsToPlace.isEmpty()) return next

        val ship = shipsToPlace.removeAt(0)
        do {
            val coordinates = aiClient.getRandomCoordinate()
            val horizontal = Random().nextBoolean()
            success = match.placeShip(player, ship, coordinates, horizontal)
        } while (!success)

        return this
    }

    override fun setNextState(next: State?) {
        this.next = next
    }

    override fun receive(dto: Dto) {
        when (dto) {
            is AttackResultDto -> return
        }
    }

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }

}
