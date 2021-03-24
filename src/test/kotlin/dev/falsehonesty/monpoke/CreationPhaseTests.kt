package dev.falsehonesty.monpoke

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreationPhaseTests {
    private val game = MonPokeGame()

    @Test
    fun `monpoke can be created`() {
        assertTrue(game.executeCommand("CREATE team1 poke1 10 5"))
    }

    @Test
    fun `multiple monpoke can be on the same team`() {
        assertTrue(game.executeCommand("CREATE team1 poke1 10 5"))
        assertTrue(game.executeCommand("CREATE team1 poke2 10 5"))
        assertTrue(game.executeCommand("CREATE team1 poke3 10 5"))
    }

    @Test
    fun `exactly two teams can be created`() {
        assertTrue(game.executeCommand("CREATE team1 poke1 10 5"))
        assertTrue(game.executeCommand("CREATE team2 poke1 10 5"))
        assertFalse(game.executeCommand("CREATE team3 poke1 10 5"))
    }

    @Test
    fun `monpoke can be created on any team in any order`() {
        assertTrue(game.executeCommand("CREATE team1 poke1 10 5"))
        assertTrue(game.executeCommand("CREATE team2 poke2 10 5"))
        assertTrue(game.executeCommand("CREATE team1 poke3 10 5"))
        assertTrue(game.executeCommand("CREATE team1 poke2 10 5"))
        assertTrue(game.executeCommand("CREATE team2 poke4 10 5"))
    }

    @Test
    fun `monpoke must have valid stats`() {
        assertFalse(game.executeCommand("CREATE team1 poke1 1 0"))
        assertFalse(game.executeCommand("CREATE team1 poke1 0 1"))
        assertFalse(game.executeCommand("CREATE team1 poke1 0 0"))
    }
}
