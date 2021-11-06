package com.danielbunte.battleships.match

import com.danielbunte.battleships.gameworld.GameBoard

class HitCalculator {

    fun attemptAttack(gameBoard: GameBoard, coordinates: String): HitResult {
        gameBoard.getShipAt(coordinates)?.let{
            if (it.health == 0 || --it.health == 0){
                return HitResult.DESTROYED
            }
            return HitResult.HIT
        }

        return HitResult.MISS
    }

}
