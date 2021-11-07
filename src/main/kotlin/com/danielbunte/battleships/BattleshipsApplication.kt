package com.danielbunte.battleships

import com.danielbunte.battleships.simulator.GameSimulator

//@SpringBootApplication
//open class BattleshipsApplication

fun main(args: Array<String>) {
    val simulator = GameSimulator()
    simulator.start()
//    runApplication<BattleshipsApplication>(*args)
}
