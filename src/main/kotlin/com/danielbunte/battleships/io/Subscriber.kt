package com.danielbunte.battleships.io

import com.danielbunte.battleships.match.Match

interface Subscriber {
    fun receive(dto: Dto)
    fun subscribeToMatch(match: Match)
}