package com.danielbunte.battleships.io

import com.danielbunte.battleships.match.Match

class PrintResultSubscriber : Subscriber {


    override fun receiveAttackResult(attackResult: AttackResultDto) {
        println(
            """
            Attacker             : ${attackResult.attackingPlayerName}
            Target               : ${attackResult.targetPlayerName}
            Coordinates          : ${attackResult.coordinates}
            Outcome              : ${attackResult.hitResult}
            Game state           : ${attackResult.gameResult}
            Target player's board:
            ${createPrintableBoard(attackResult)}
        """.trimIndent()
        )
    }

    fun createPrintableBoard(dto: AttackResultDto): String {
        var result = ""
        val width = dto.targetPlayerBoardView.size
        val height = dto.targetPlayerBoardView[0].size
        repeat(height) { y ->
            repeat(width) { x ->
                result += mapToSymbol(dto.targetPlayerBoardView[x][y])
                if (x != height - 1) {
                    result += " "
                }
            }
            result += "\n"
        }

        return result.dropLast(1)
    }

    private fun mapToSymbol(keyword: String) = when (keyword) {
        "WATER" -> "~"
        "MISS" -> "o"
        "HIT" -> "x"
        "DESTROYED" -> "#"
        else -> "?"
    }

    override fun registerToMatch(match: Match) {
        match.subscribe(this)
    }
}