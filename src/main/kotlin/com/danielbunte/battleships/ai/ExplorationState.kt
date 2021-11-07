package com.danielbunte.battleships.ai

import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.io.Dto
import com.danielbunte.battleships.io.Subscriber
import com.danielbunte.battleships.match.Match
import com.danielbunte.battleships.match.Player

/**
 * In a real-world scenario, the AI wouldn't have access to the player and match objects directly,
 * but to keep effort low, we're taking this shortcut here.
 * The AI should work completely independent of the inner game objects.
 */
class ExplorationState(
    private val self: Player,
    private val opponent: Player,
    private val match: Match
) : State, Subscriber {

    private var currentState: State? = this
    private var next: State? = null

    private var _lastAttempt: String? = null
    val lastAttempt: String
        get() {
            return _lastAttempt!!
        }

    private var _attackedCoordinates = mutableListOf<String>()
    val attackedCoordinates: List<String>
        get() {
            return _attackedCoordinates.toList()
        }

    init {
        subscribeToMatch(match)
    }

    override fun run(): State? {
        val coordinates = randomCoordinate(self.gameBoard)
        match.attemptAttack(self, opponent, coordinates)
        _lastAttempt = coordinates

        // As the code is synchronous, the "receive" will be called by the match before the return statement. Therefore, currentState holds the correct next state
        return currentState
    }

    override fun setNextState(next: State?) {
        this.next = next
    }

    override fun receive(dto: Dto) {
        when (dto) {
            is AttackResultDto -> handleAttackResult(dto)
        }
    }

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }

    private fun handleAttackResult(dto: AttackResultDto) {
        if (dto.attackingPlayerName !== self.name) return

        if (dto.hitResult == "HIT") {
            currentState = next
        }
    }
}