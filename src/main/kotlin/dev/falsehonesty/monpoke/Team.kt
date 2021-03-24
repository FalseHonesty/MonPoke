package dev.falsehonesty.monpoke

class Team(val teamID: String) {
    /**
     * A map of all the monpoke currently on this team. There is no maximum number of allowed monpoke.
     * When a monpoke is defeated, it will be removed from this structure.
     *
     * The keys of the map are the pokeID provided in the creation phase of the game.
     */
    private val availableMonPoke = mutableMapOf<String, MonPoke>()

    /**
     * The current monpoke for a team is the one it has active in battle.
     *
     * If the value is null, then there is no current mon poke in battle so this team must
     * choose a new monpoke on their next turn.
     */
    private var currentMonPoke: MonPoke? = null

    /**
     * Create a new monpoke to add to this team with the provided stats
     *
     * @return the success of the operation
     */
    fun createMonPoke(pokeID: String, hp: Int, attack: Int): Boolean {
        if (hasMonPoke(pokeID))
            return false

        val monPoke = MonPoke(pokeID, hp, attack)
        availableMonPoke[pokeID] = monPoke

        return true
    }

    /**
     * Switch to a new monpoke with the provided id.
     *
     * @return the success of the operation
     */
    fun switchToMonPoke(pokeID: String): Boolean {
        if (!hasMonPoke(pokeID))
            return false
        currentMonPoke = availableMonPoke[pokeID]
        return true
    }

    /**
     * Attack the other team's current monpoke with this team's current monpoke.
     * If either team does not currently have a selected monpoke, this operation will fail.
     * This function handles the entire attack behavior, such as decrementing HP, defeating the enemy, etc.
     *
     * @return the success of the operation
     */
    fun attackTeam(opposingTeam: Team): Boolean {
        if (!hasCurrentMonPoke())
            return false
        if (!opposingTeam.hasCurrentMonPoke())
            return false
        val opposingMonPoke = opposingTeam.currentMonPoke!!
        val friendlyMonPoke = currentMonPoke!!
        opposingMonPoke.hp -= friendlyMonPoke.attack
        println("${friendlyMonPoke.pokeID} attacked ${opposingMonPoke.pokeID} for ${friendlyMonPoke.attack} damage!")

        if (opposingMonPoke.hp <= 0) {
            opposingTeam.currentMonPoke = null
            opposingTeam.availableMonPoke.remove(opposingMonPoke.pokeID)
            println("${opposingMonPoke.pokeID} has been defeated!")
        }

        return true
    }

    /**
     * @return whether this team has any more monpoke in reserve. If not, then we are defeated.
     */
    fun hasMonPokeRemaining(): Boolean {
        return availableMonPoke.isNotEmpty()
    }

    private fun hasCurrentMonPoke(): Boolean {
        return currentMonPoke != null
    }

    private fun hasMonPoke(pokeID: String): Boolean {
        return availableMonPoke.containsKey(pokeID)
    }
}
