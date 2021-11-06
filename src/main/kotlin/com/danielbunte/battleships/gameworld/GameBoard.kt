package com.danielbunte.battleships.gameworld

import java.lang.Integer.parseInt
import java.util.*

class GameBoard(width: Int, height: Int) {

    private val charCodeA = "A"[0].code

    fun getCoordinates(coordinates: String): Pair<Int, Int> {
        val cleanedCoordinates = coordinates.replace(" ", "").uppercase(Locale.getDefault())
        val x = cleanedCoordinates[0].code - charCodeA
        val y = parseInt(cleanedCoordinates.substring(1)) - 1
        return x to y
    }

    fun getCoordinates(inputX: Int, inputY: Int): String {
        val x = Char(inputX + charCodeA)
        val y = inputY + 1
        return "$x$y"
    }
}
