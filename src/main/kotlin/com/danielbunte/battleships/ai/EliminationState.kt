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
 *
 * This state kicks in, as soon as a ship is hit and attempts to fire in the right direction, until
 * the ship is completely destroyed.
 */
class EliminationState(
    private val aiClient: AIClient,
    private val previousState: ExplorationState,
    private val self: Player,
    private val opponent: Player,
    private val match: Match
) : State, Subscriber {

    // TODO: Tests! This is more or less the only class not done by TDD standards, due to so much randomization

    private var currentState: State? = this
    private var next: State? = null
    private var lastAttack: Pair<String, Boolean> = "" to false
    private var previousHit = ""
    private var attackFlow = AttackFlow.RIGHT
    private var isActive = false

    init {
        subscribeToMatch(match)
    }

    override fun run(): State? {
        isActive = true
        // get last attempt coordinates
        val lastAttempt = getLastAttempt()
        // specify direction for next attacks -> right, down, left, up
        val nextCoordinate = getNextCoordinate(lastAttempt)
        // next attack, until destroyed, then go on to next state
        match.attemptAttack(self, opponent, nextCoordinate)

        // As the code is synchronous, the "receive" will be called by the match before this return statement. Therefore, currentState holds the correct next state
        return currentState
    }

    private fun getNextCoordinate(lastAttempt: String): String {
        var coordinate: String
        var lastHit = lastAttempt
        var loops = 0
        do {
            val (x, y) = self.gameBoard.convertCoordinates(lastHit)
            var newX = x
            var newY = y
            when (attackFlow) {
                AttackFlow.RIGHT -> newX = x + 1
                AttackFlow.LEFT -> newX = x - 1
                AttackFlow.UP -> newY = y - 1
                AttackFlow.DOWN -> newY = y + 1
            }

            coordinate = aiClient.blockCoordinate(self.gameBoard, newX, newY)
            if (coordinate.isEmpty()) {
                lastHit = previousHit.ifEmpty {
                    previousState.lastAttempt
                }
                attackFlow = rotateAttackFlow()
            }
            ++loops
            if (loops > AttackFlow.values().size) {
                coordinate = aiClient.getAndBlockRandomCoordinate()
                exit()
            }
        } while (coordinate.isEmpty())

        return coordinate
    }

    private fun rotateAttackFlow(): AttackFlow {
        val values = AttackFlow.values()
        var next = attackFlow.ordinal + 1
        if (next >= values.size) {
            next = 0
        }
        return values[next]
    }

    private fun getLastAttempt(): String {
        val (coordinates, success) = lastAttack
        if (success) return coordinates

        return previousState.lastAttempt
    }

    override fun setNextState(next: State?) {
        this.next = next
    }

    override fun receive(dto: Dto) {
        if (!isActive) return
        when (dto) {
            is AttackResultDto -> handleAttackResult(dto)
        }
    }

    private fun handleAttackResult(dto: AttackResultDto) {
        if (dto.attackingPlayerName !== self.name) return

        if (dto.hitResult == "DESTROYED") {
            exit()
        }

        lastAttack = dto.coordinates to (dto.hitResult == "HIT")
        if (lastAttack.second) {
            previousHit = lastAttack.first
        }
    }

    private fun exit() {
        currentState = next
        previousHit = ""
        lastAttack = "" to false
        isActive = false
    }

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }
}

enum class AttackFlow {
    RIGHT,
    DOWN,
    LEFT,
    UP
}
