package com.danielbunte.battleships.gameworld

import java.lang.Integer.parseInt
import java.util.*

class GameBoard(width: Int, height: Int) {

    private val charCodeA = "A"[0].code

    fun getCoordinates(coordinates: String): Pair<Int, Int> {
        val cleanedCoordinates = coordinates.replace(" ", "").uppercase(Locale.getDefault())
        val x = cleanedCoordinates[0].code - charCodeA
        val y = parseInt(cleanedCoordinates[1].toString()) - 1
        return x to y
    }

}
