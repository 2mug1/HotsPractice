package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.events.SumoEventGame;
import net.hotsmc.practice.game.games.GameState;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class SumoEventWaitingScoreboard extends PlayerScoreboard {

    public SumoEventWaitingScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        SumoEventGame sumoEventGame = (SumoEventGame) HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
        if(sumoEventGame != null){
            Team line1 = scoreboard.registerNewTeam("Line1");
            line1.addEntry(ChatColor.DARK_GRAY.toString());
            line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.DARK_GRAY.toString()).setScore(7);

            Team event = scoreboard.registerNewTeam("Event");
            event.addEntry(ChatColor.AQUA.toString());
            event.setPrefix("" + ChatColor.GOLD + "Event " + ChatColor.WHITE + "(Sumo)");
            obj.getScore(ChatColor.AQUA.toString()).setScore(6);

            Team eventHost = scoreboard.registerNewTeam("EventHost");
            eventHost.addEntry(getEntry("" + ChatColor.YELLOW + "Host: " + ChatColor.WHITE));
            eventHost.setSuffix(getEntry(sumoEventGame.getEventName()));
            obj.getScore(getEntry("" + ChatColor.YELLOW + "Host: " + ChatColor.WHITE)).setScore(5);

            Team players = scoreboard.registerNewTeam("Players");
            players.addEntry(ChatColor.RED.toString());
            players.setPrefix("" + ChatColor.YELLOW + "Players: ");
            players.setSuffix("" + ChatColor.WHITE + sumoEventGame.getWinningPlayers().size() + "/" + sumoEventGame.getMaxPlayers());
            obj.getScore(ChatColor.RED.toString()).setScore(4);

            Team state = scoreboard.registerNewTeam("State");
            state.addEntry(ChatColor.WHITE.toString());
            state.setPrefix("" + ChatColor.YELLOW + "State: ");
            state.setSuffix(sumoEventGame.getState().getName());
            obj.getScore(ChatColor.WHITE.toString()).setScore(3);

            Team latency = scoreboard.registerNewTeam("Latency");
            latency.addEntry(ChatColor.MAGIC.toString());
            latency.setPrefix("" + ChatColor.YELLOW + "Latency: ");
            latency.setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
            obj.getScore(ChatColor.MAGIC.toString()).setScore(2);

            Team line2 = scoreboard.registerNewTeam("Line2");
            line2.addEntry(ChatColor.STRIKETHROUGH.toString());
            line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
        }
    }

    @Override
    void onUpdate() {
        SumoEventGame sumoEventGame = (SumoEventGame) HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
        if (sumoEventGame != null) {
            scoreboard.getTeam("Players").setSuffix("" + ChatColor.WHITE + sumoEventGame.getWinningPlayers().size() + "/" + sumoEventGame.getMaxPlayers());
            scoreboard.getTeam("State").setSuffix(sumoEventGame.getState().getName());
        }
        scoreboard.getTeam("Latency").setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
    }
}
