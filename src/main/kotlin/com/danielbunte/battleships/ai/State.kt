package com.danielbunte.battleships.ai

interface State {

    fun run(): State?
    fun setNextState(next: State?)
}