package com.danielbunte.battleships.io

/**
 * This class is intended to be used for third-party apps, as well.
 * Using Strings makes it easy for consumers to understand the meaning of the fields.
 */
data class AttackResultDto(
    val attackingPlayerName: String,
    val targetPlayerName: String,
    val coordinates: String,
    val hitResult: String,
    val gameResult: String,
    val nextPlayer: String,
    val targetPlayerBoardView: List<List<String>>,
    val winner: String
) : Dto()