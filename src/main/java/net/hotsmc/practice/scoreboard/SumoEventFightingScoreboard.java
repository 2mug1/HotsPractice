package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.events.EventGameState;
import net.hotsmc.practice.game.events.SumoEventGame;
import net.hotsmc.practice.game.games.GameState;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class SumoEventFightingScoreboard extends PlayerScoreboard {


    public SumoEventFightingScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        SumoEventGame sumoEventGame = (SumoEventGame) HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
        if (sumoEventGame != null) {
            Team line1 = scoreboard.registerNewTeam("Line1");
            line1.addEntry(ChatColor.DARK_GRAY.toString());
            line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.DARK_GRAY.toString()).setScore(12);

            Team event = scoreboard.registerNewTeam("Event");
            event.addEntry(ChatColor.AQUA.toString());
            event.setPrefix("" + ChatColor.GOLD + "Event " + ChatColor.WHITE + "(Sumo)");
            obj.getScore(ChatColor.AQUA.toString()).setScore(11);

            Team eventHost = scoreboard.registerNewTeam("EventHost");
            eventHost.addEntry(getEntry("" + ChatColor.YELLOW + "Host: " + ChatColor.WHITE));
            eventHost.setSuffix(getEntry(sumoEventGame.getEventName()));
            obj.getScore(getEntry("" + ChatColor.YELLOW + "Host: " + ChatColor.WHITE)).setScore(10);

            Team players = scoreboard.registerNewTeam("Players");
            players.addEntry(ChatColor.RED.toString());
            players.setPrefix("" + ChatColor.YELLOW + "Players: ");
            players.setSuffix("" + ChatColor.WHITE + sumoEventGame.getWinningPlayers().size() + "/" + sumoEventGame.getMaxPlayers());
            obj.getScore(ChatColor.RED.toString()).setScore(9);

            Team state = scoreboard.registerNewTeam("State");
            state.addEntry(ChatColor.WHITE.toString());
            state.setPrefix("" + ChatColor.YELLOW + "State: ");
            state.setSuffix(sumoEventGame.getState().getName());
            obj.getScore(ChatColor.WHITE.toString()).setScore(8);

            Team latency = scoreboard.registerNewTeam("Latency");
            latency.addEntry(ChatColor.MAGIC.toString());
            latency.setPrefix("" + ChatColor.YELLOW + "Latency: ");
            latency.setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
            obj.getScore(ChatColor.MAGIC.toString()).setScore(7);

            Team duration = scoreboard.registerNewTeam("Duration");
            duration.addEntry(ChatColor.BLUE.toString());
            if (sumoEventGame.getState() == EventGameState.PREPARING) {
                duration.setPrefix("" + ChatColor.YELLOW + "Starting: ");
                duration.setSuffix("" + ChatColor.WHITE + sumoEventGame.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(sumoEventGame.getTime()));
            }
            obj.getScore(ChatColor.BLUE.toString()).setScore(6);

            Team line2 = scoreboard.registerNewTeam("Line2");
            line2.addEntry(ChatColor.STRIKETHROUGH.toString());
            line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(5);

            Team fighting = scoreboard.registerNewTeam("Fighting");
            fighting.addEntry(" §7vs ");
            fighting.setPrefix(getEntry("§e" + sumoEventGame.fightingPlayers[0].getName()));
            fighting.setSuffix(getEntry("§e" + sumoEventGame.fightingPlayers[1].getName()));
            obj.getScore(" §7vs ").setScore(4);

            Team ping = scoreboard.registerNewTeam("Ping");
            ping.addEntry("§7| ");
            ping.setPrefix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[0].getPing() + "ms§7) "));
            ping.setSuffix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[1].getPing() + "ms§7)"));
            obj.getScore("§7| ").setScore(3);

            Team cps = scoreboard.registerNewTeam("Cps");
            cps.addEntry(" §7| ");
            cps.setPrefix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[0].getCps() + " CPS§7)"));
            cps.setSuffix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[1].getCps() + " CPS§7)"));
            obj.getScore(" §7| ").setScore(2);

            Team line3 = scoreboard.registerNewTeam("Line3");
            line3.addEntry(ChatColor.DARK_GREEN.toString());
            line3.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line3.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.DARK_GREEN.toString()).setScore(1);
        }
    }

    @Override
    void onUpdate() {
        SumoEventGame sumoEventGame = (SumoEventGame) HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
        if (sumoEventGame != null) {
            scoreboard.getTeam("Players").setSuffix("" + ChatColor.WHITE + sumoEventGame.getWinningPlayers().size() + "/" + sumoEventGame.getMaxPlayers());
            scoreboard.getTeam("State").setSuffix(sumoEventGame.getState().getName());
            Team duration = scoreboard.getTeam("Duration");
            if (sumoEventGame.getState() == EventGameState.PREPARING) {
                duration.setPrefix("" + ChatColor.YELLOW + "Starting in ");
                duration.setSuffix("" + ChatColor.WHITE + sumoEventGame.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(sumoEventGame.getTime()));
            }
            Team fighting = scoreboard.getTeam("Fighting");
            Team ping = scoreboard.getTeam("Ping");
            Team cps = scoreboard.getTeam("Cps");
            if(sumoEventGame.fightingPlayers[0] != null && sumoEventGame.fightingPlayers[1] != null) {
                fighting.setPrefix(getEntry("§e" + sumoEventGame.fightingPlayers[0].getName()));
                fighting.setSuffix(getEntry("§e" + sumoEventGame.fightingPlayers[1].getName()));
                ping.setPrefix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[0].getPing() + "ms§7) "));
                ping.setSuffix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[1].getPing() + "ms§7)"));
                cps.setPrefix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[0].getCps() + " CPS§7)"));
                cps.setSuffix(getEntry("§7(§e" + sumoEventGame.fightingPlayers[1].getCps() + " CPS§7)"));
            }
        }
        scoreboard.getTeam("Latency").setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
    }
}