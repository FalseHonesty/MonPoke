package dev.falsehonesty.monpoke

import kotlin.system.exitProcess

fun main() {
    val game = MonPokeGame()
    while (game.gamePhase != GamePhase.FINISHED) {
        // If we run out of data before finishing the game, we've reached an invalid state and should panic.
        val command = readLine() ?: exitProcess(1)

        // The issues command was invalid, so we should panic and return with exit code 1 as specified.
        if (!game.executeCommand(command))
            exitProcess(1)
    }
}
