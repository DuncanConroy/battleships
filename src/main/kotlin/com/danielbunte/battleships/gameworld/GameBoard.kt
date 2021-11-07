package com.danielbunte.battleships.gameworld

import com.danielbunte.battleships.match.HitResult
import java.lang.Integer.parseInt
import java.util.*

/**
 * The GameBoard holds cells of the "grid". The grid layout is as follows:
 *     A    B    C    D ..
 * 1
 * 2
 * 3
 * 4
 * ..
 */
class GameBoard(val width: Int, val height: Int) {

    private val charCodeA = "A"[0].code

    // colums -> rows -> cells
    private val cells: List<MutableList<Cell>> = (0..width).map {
        (0..height).map {
            Cell(null)
        }.toMutableList()
    }

    private val ships: MutableSet<Ship> = mutableSetOf()
    val allShips: List<Ship>
        get() {
            return ships.toList()
        }

    fun convertCoordinates(coordinates: String): Pair<Int, Int> {
        val cleanedCoordinates = coordinates.replace(Regex("[^a-zA-Z0-9]"), "").uppercase(Locale.getDefault())
        val x = cleanedCoordinates[0].code - charCodeA
        val y = parseInt(cleanedCoordinates.substring(1)) - 1
        return x to y
    }

    fun convertCoordinates(inputX: Int, inputY: Int): String {
        val x = Char(inputX + charCodeA)
        val y = inputY + 1
        return "$x$y"
    }

    fun placeShip(coordinates: String, ship: Ship, horizontal: Boolean): Boolean {
        val xy = convertCoordinates(coordinates)
        if (!canPlace(xy, ship, horizontal)) {
            return false
        }

        ships.add(ship)
        updateCells(xy, ship, horizontal)
        return true
    }

    private fun updateCells(xy: Pair<Int, Int>, ship: Ship, horizontal: Boolean) {
        var x = xy.first
        var y = xy.second
        repeat(ship.length) {
            cells[x][y] = Cell(ship)
            ship.cells.add(x to y)
            if (horizontal) {
                x++
            } else {
                y++
            }
        }
    }

    private fun canPlace(xy: Pair<Int, Int>, ship: Ship, horizontal: Boolean): Boolean {
        var x = xy.first
        var y = xy.second
        repeat(ship.length) {
            if (x > width - 1 || y > height - 1 || y < 0) {
                return false
            }

            cells[x][y].ship?.let {
                return false
            }

            if (horizontal) {
                x++
            } else {
                y++
            }
        }

        return true
    }

    fun getShipAt(coordinates: String): Ship? {
        val xy = convertCoordinates(coordinates)
        return cells[xy.first][xy.second].ship
    }

    fun copy() = GameBoard(width, height)

    fun updateCellState(coordinates: String, state: HitResult) {
        val xy = convertCoordinates(coordinates)
        when (state) {
            HitResult.HIT -> cells[xy.first][xy.second].state = CellState.HIT
            HitResult.MISS -> cells[xy.first][xy.second].state = CellState.MISS
            HitResult.DESTROYED -> {
                val cell = cells[xy.first][xy.second]
                if (cell.ship?.health == 0) {
                    cell.ship.cells.forEach {
                        cells[it.first][it.second].state = CellState.DESTROYED
                    }
                } else {
                    cell.state = CellState.DESTROYED
                }
            }
        }
    }

    fun getCellState(coordinates: String): CellState {
        val xy = convertCoordinates(coordinates)
        return cells[xy.first][xy.second].state
    }

    fun getBoardView() = cells.map { columns ->
        columns.map { it.state }
    }
}

data class Cell(val ship: Ship?, var state: CellState = CellState.WATER)