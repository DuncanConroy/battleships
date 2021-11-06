package com.danielbunte.battleships.gameworld

data class Ship(
    val name: String,
    val length: Int
) {
    var health: Int = length
}
