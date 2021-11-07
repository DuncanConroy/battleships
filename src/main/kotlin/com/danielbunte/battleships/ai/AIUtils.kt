package com.danielbunte.battleships.ai

import com.danielbunte.battleships.gameworld.GameBoard
import java.util.*

fun randomCoordinate(gameBoard: GameBoard): String {
    val x = Random().nextInt(gameBoard.width)
    val y = Random().nextInt(gameBoard.height)
    return gameBoard.convertCoordinates(x, y)
}