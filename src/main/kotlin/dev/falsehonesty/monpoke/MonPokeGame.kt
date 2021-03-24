package dev.falsehonesty.monpoke

class MonPokeGame {
    /**
     * List of the teams in the game. There must never be more than 2 teams,
     * nor should the game be able to start with less than 2.
     *
     * Teams are stored as a list rather than a map of ID to Team because of it makes it simple to keep track of
     * which team is team 1 vs 2, and since there will only ever be at most 2 values, it will be simpler and faster to
     * skip the hashing step of a HashMap.
     */
    private val teams = mutableListOf<Team>()

    /**
     * The game's "phase" keeps track of what stage of the game we're in, whether that means the creation phase
     * or which team's turn it is, etc.
     */
    var gamePhase = GamePhase.CREATION

    /**
     * @return the success of the command execution. If an invalid command was issued, this function will return false.
     */
    fun executeCommand(command: String): Boolean {
        val split = command.split(" ")

        // The first portion of the command is the "command type". The valid values of this type are
        // CREATE, ATTACK, and ICHOOSEYOU. They each accept different following arguments and each require
        // a different phase of the game.
        when (split[0]) {
            "CREATE" -> {
                if (gamePhase != GamePhase.CREATION)
                    return false
                // Normally we would do checks to ensure these values exist and are well-formatted, but
                // for this scenario those assumptions are guaranteed.
                val hp = split[3].toInt()
                if (hp < 1)
                    return false
                val attack = split[4].toInt()
                if (attack < 1)
                    return false

                val status = createMonPoke(split[1], split[2], hp, attack)
                if (status)
                    println("${split[2]} has been assigned to team ${split[1]}!")
                return status
            }
            "ICHOOSEYOU" -> {
                if (gamePhase == GamePhase.CREATION) {
                    // We need exactly 2 teams to play
                    if (teams.size != 2)
                        return false
                    gamePhase = GamePhase.TEAM1_TURN
                }

                val currentTeam = getCurrentTeam() ?: return false
                val status = currentTeam.switchToMonPoke(split[1])
                if (status)
                    println("${split[1]} has entered the battle!")
                switchToNextTurn()
                return status
            }
            "ATTACK" -> {
                if (gamePhase == GamePhase.CREATION)
                    return false

                val currentTeam = getCurrentTeam() ?: return false
                val opposingTeam = getOpposingTeam() ?: return false
                val status = currentTeam.attackTeam(opposingTeam)
                if (!status)
                    return false

                if (!opposingTeam.hasMonPokeRemaining()) {
                    gamePhase = GamePhase.FINISHED
                    println("${currentTeam.teamID} is the winner!")
                }

                switchToNextTurn()
                return true
            }
            else -> return false
        }
    }

    /**
     * @return the team whose turn it currently is
     */
    private fun getCurrentTeam(): Team? {
        return getTeamForTurn(gamePhase)
    }

    /**
     * @return the team whose turn it current is NOT
     */
    private fun getOpposingTeam(): Team? {
        return if (gamePhase == GamePhase.TEAM1_TURN) {
            getTeamForTurn(GamePhase.TEAM2_TURN)
        } else {
            getTeamForTurn(GamePhase.TEAM1_TURN)
        }
    }

    private fun getTeamForTurn(turn: GamePhase): Team? {
        if (turn == GamePhase.TEAM1_TURN)
            return teams[0]
        if (turn == GamePhase.TEAM2_TURN)
            return teams[1]
        return null
    }

    private fun switchToNextTurn() {
        if (gamePhase == GamePhase.TEAM1_TURN)
            gamePhase = GamePhase.TEAM2_TURN
        else if (gamePhase == GamePhase.TEAM2_TURN)
            gamePhase = GamePhase.TEAM1_TURN
    }

    private fun createMonPoke(teamID: String, pokeID: String, hp: Int, attack: Int): Boolean {
        var team = teams.firstOrNull { it.teamID == teamID }
        if (team == null) {
            if (teams.size == 2)
                return false
            team = Team(teamID)
            teams.add(team)
        }

        return team.createMonPoke(pokeID, hp, attack)
    }
}
