package org.techern.minecraft.ModernHardcoreQuesting.team;

import org.techern.minecraft.ModernHardcoreQuesting.network.NetworkManager;
import org.techern.minecraft.ModernHardcoreQuesting.network.message.TeamStatsMessage;

import java.util.*;

public class TeamStats {

    private static Map<String, TeamStats> clientTeams;
    private static TeamStats[] clientTeamsList;
    private static TeamComparator teamComparator = new TeamComparator();
    private String name;
    private int players;
    private int lives;
    private float progress;

    public TeamStats(String name, int players, int lives, float progress) {
        this.name = name;
        this.players = players;
        this.lives = lives;
        this.progress = progress;
    }

    public static void refreshTeam(Team team) {
        NetworkManager.sendToAllPlayers(new TeamStatsMessage(team));
    }

    public static void updateTeams(List<TeamStats> stats) {
        clientTeams = new HashMap<>();
        for (TeamStats stat : stats) {
            if (stat.getPlayers() > 0)
                clientTeams.put(stat.name, stat);
        }
        updateTeams();
    }

    public static void updateTeam(TeamStats stat) {
        if (clientTeams == null) clientTeams = new HashMap<>();
        if (stat.getPlayers() > 0)
            clientTeams.put(stat.name, stat);
        else
            clientTeams.remove(stat.name);
        updateTeams();
    }

    private static void updateTeams() {
        clientTeamsList = new TeamStats[clientTeams.size()];
        int id = 0;
        for (TeamStats teamStats : clientTeams.values())
            clientTeamsList[id++] = teamStats;

        Arrays.sort(clientTeamsList, teamComparator);
    }

    public static TeamStats[] getTeamStats() {
        return clientTeamsList;
    }

    public String getName() {
        return name;
    }

    public int getPlayers() {
        return players;
    }

    public int getLives() {
        return lives;
    }

    public float getProgress() {
        return progress;
    }

    private static class TeamComparator implements Comparator<TeamStats> {

        @Override
        public int compare(TeamStats o1, TeamStats o2) {
            return ((Float) o2.progress).compareTo(o1.progress);
        }
    }
}
