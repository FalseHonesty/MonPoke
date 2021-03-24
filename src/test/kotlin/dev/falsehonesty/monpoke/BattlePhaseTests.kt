package dev.falsehonesty.monpoke

import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BattlePhaseTests {
    private val battleGame = MonPokeGame()

    @BeforeTest
    fun setup() {
        battleGame.executeCommand("CREATE team1 poke1 10 5")
        battleGame.executeCommand("CREATE team1 poke2 10 5")
        battleGame.executeCommand("CREATE team2 poke1 1 1")
        battleGame.executeCommand("CREATE team2 poke2 1 1")
        battleGame.executeCommand("ICHOOSEYOU poke1")
    }

    @Test
    fun `a team can't attack if they haven't chosen a monpoke`() {
        assertFalse(battleGame.executeCommand("ATTACK"))
    }

    @Test
    fun `defeating a monpoke should make the opposing team choose a new monpoke`() {
        battleGame.executeCommand("ICHOOSEYOU poke1")
        battleGame.executeCommand("ATTACK")
        assertFalse(battleGame.executeCommand("ATTACK"))
    }

    @Test
    fun `the only valid commands are ICHOOSEYOU and ATTACK`() {
        assertFalse(battleGame.executeCommand("INVALID randomtext"))
    }

    @Test
    fun `defeating all monpoke should end the game`() {
        battleGame.executeCommand("ICHOOSEYOU poke1")
        battleGame.executeCommand("ATTACK")
        battleGame.executeCommand("ICHOOSEYOU poke2")
        battleGame.executeCommand("ATTACK")
        assertEquals(GamePhase.FINISHED, battleGame.gamePhase)
    }
}
