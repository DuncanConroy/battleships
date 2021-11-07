package com.danielbunte.battleships.match.io

import com.danielbunte.battleships.gameworld.CellState
import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.HitResult

class AttackResultConverter {

    fun convert(boardView: List<List<CellState>>): AttackResultDto {
        return AttackResultDto("", "", "", HitResult.MISS, GameResult.NOT_YOUR_TURN)
    }

}
