package com.danielbunte.battleships.io

import com.danielbunte.battleships.match.Match
import java.util.*

class PrintResultSubscriber : Subscriber {

    override fun receive(dto: Dto) {
        when (dto) {
            is AttackResultDto -> printAttackResult(dto)
        }
    }

    fun printAttackResult(dto: AttackResultDto) {
        println(
            """
            |Attacker              : ${dto.attackingPlayerName}
            |Target                : ${dto.targetPlayerName}
            |Coordinates           : ${dto.coordinates.uppercase(Locale.getDefault())}
            |Outcome               : ${dto.hitResult}
            |Game state            : ${dto.gameResult}
            |Target player's board :
            |${createPrintableBoard(dto, true)}
            |
        """.trimMargin("|")
        )
    }

    fun createPrintableBoard(dto: AttackResultDto, prefix: Boolean = false): String {
        var result = ""
        val width = dto.targetPlayerBoardView.size
        val height = dto.targetPlayerBoardView[0].size

        if (prefix) {
            repeat(width) {
                result += "${Char("A"[0].code + it)} "
            }
            result.dropLast(1)
            result = "    $result\n"
        }

        repeat(height) { y ->
            repeat(width) { x ->
                if (prefix && x == 0) {
                    result += "${y + 1}".padStart(3) + " "
                }
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

    override fun subscribeToMatch(match: Match) {
        match.subscribe(this)
    }
}