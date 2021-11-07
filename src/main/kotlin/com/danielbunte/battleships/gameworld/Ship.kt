package com.danielbunte.battleships.gameworld

data class Ship(
    val name: String,
    val length: Int,
    var cells: MutableList<Pair<Int, Int>> = mutableListOf()
) {
    var health: Int = length
}
