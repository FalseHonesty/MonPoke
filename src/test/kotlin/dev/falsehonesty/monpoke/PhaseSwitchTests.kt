package dev.falsehonesty.monpoke

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class PhaseSwitchTests {
    private val game = MonPokeGame()

    @Test
    fun `creating monpoke keeps the game in the creation phase`() {
        assertEquals(GamePhase.CREATION, game.gamePhase)
        game.executeCommand("CREATE team1 poke1 10 5")
        game.executeCommand("CREATE team2 poke1 10 5")
        assertEquals(GamePhase.CREATION, game.gamePhase)
    }

    @Test
    fun `choosing a monpoke switches to the battle phase of the game`() {
        game.executeCommand("CREATE team1 poke1 10 5")
        game.executeCommand("CREATE team2 poke1 10 5")

        game.executeCommand("ICHOOSEYOU poke1")
        assertNotEquals(GamePhase.CREATION, game.gamePhase)
    }

    @Test
    fun `you can't start the battle phase of the game with only a single team`() {
        game.executeCommand("CREATE team1 poke1 10 5")

        assertFalse(game.executeCommand("ICHOOSEYOU poke1"))
    }

    @Test
    fun `choosing a monpoke switches to the next team's turn`() {
        game.executeCommand("CREATE team1 poke1 10 5")
        game.executeCommand("CREATE team2 poke2 10 5")

        game.executeCommand("ICHOOSEYOU poke1")
        assertEquals(GamePhase.TEAM2_TURN, game.gamePhase)
    }

    @Test
    fun `attacking switches to the next team's turn`() {
        game.executeCommand("CREATE team1 poke1 10 5")
        game.executeCommand("CREATE team2 poke2 10 5")

        game.executeCommand("ICHOOSEYOU poke1")
        game.executeCommand("ICHOOSEYOU poke2")

        game.executeCommand("ATTACK")
        assertEquals(GamePhase.TEAM2_TURN, game.gamePhase)
    }
}
