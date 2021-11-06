package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard
import com.danielbunte.battleships.gameworld.Ship

class Player(val name: String) {

    private lateinit var _shipyard: List<Ship>
    val shipyard: List<Ship>
        get() {
            return _shipyard
        }
    private lateinit var _gameBoard: GameBoard
    val gameBoard: GameBoard
        get() {
            return _gameBoard
        }

    fun init(gameBoard: GameBoard, shipyard: List<Ship>) {
        this._gameBoard = gameBoard
        this._shipyard = shipyard
    }
}
