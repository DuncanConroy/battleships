package com.danielbunte.battleships.match.io

import com.danielbunte.battleships.gameworld.CellState
import com.danielbunte.battleships.io.AttackResultDto
import com.danielbunte.battleships.match.GameResult
import com.danielbunte.battleships.match.HitResult
import com.danielbunte.battleships.match.Player

class AttackResultConverter {

    fun convert(
        attackingPlayer: Player,
        targetPlayer: Player,
        coordinates: String,
        gameResult: GameResult,
        nextPlayer: Player?,
        hitResult: HitResult?,
        winner: Player?
    ) = AttackResultDto(
        attackingPlayerName = attackingPlayer.name,
        targetPlayerName = targetPlayer.name,
        coordinates = coordinates,
        gameResult = gameResult.toString(),
        nextPlayer = nextPlayer?.name ?: "",
        hitResult = hitResult?.name ?: "",
        winner = winner?.name ?: "",
        targetPlayerBoardView = convertBoardView(targetPlayer.gameBoard.getBoardView())
    )

    private fun convertBoardView(boardView: List<List<CellState>>) = boardView
        .map { columns ->
            columns.map { it.name }
        }
}
