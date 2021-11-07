package com.danielbunte.battleships

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BattleshipsApplication

fun main(args: Array<String>) {
    runApplication<BattleshipsApplication>(*args)
}
