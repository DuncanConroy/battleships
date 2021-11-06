package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard

class Player(val name: String) {

    private lateinit var _gameBoard: GameBoard
    val gameBoard: GameBoard
        get() {
            return _gameBoard
        }

    fun init(gameBoard: GameBoard) {
        this._gameBoard = gameBoard
    }
}
