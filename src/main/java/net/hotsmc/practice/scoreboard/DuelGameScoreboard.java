package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.games.GameState;
import net.hotsmc.practice.game.games.DuelGame;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class DuelGameScoreboard extends PlayerScoreboard {

    public DuelGameScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        DuelGame game = (DuelGame) HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        if (game != null) {
            Team line1 = scoreboard.registerNewTeam("Line1");
            line1.addEntry(ChatColor.WHITE.toString());
            line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.WHITE.toString()).setScore(8);

            Team ladder = scoreboard.registerNewTeam("Ladder");
            ladder.addEntry(ChatColor.AQUA.toString());
            ladder.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Ladder: ");
            ladder.setSuffix("" + ChatColor.WHITE + HotsPractice.getGameManager().getPlayerOfGame(practicePlayer).getKitType());
            obj.getScore(ChatColor.AQUA.toString()).setScore(7);

            Team opponent = scoreboard.registerNewTeam("Opponent");
            opponent.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Opponent: " + ChatColor.WHITE));
            opponent.setSuffix(getEntry(practicePlayer.getOpponent()));
            obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Opponent: " + ChatColor.WHITE)).setScore(6);

            Team duration = scoreboard.registerNewTeam("Duration");
            duration.addEntry(ChatColor.BLUE.toString());
            if (game.getState() == GameState.PreGame) {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Starting: ");
                duration.setSuffix("" + ChatColor.WHITE + game.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(game.getTime()));
            }
            obj.getScore(ChatColor.BLUE.toString()).setScore(5);

            Team blank = scoreboard.registerNewTeam("Blank");
            blank.addEntry(ChatColor.UNDERLINE.toString());
            blank.setPrefix(" ");
            obj.getScore(ChatColor.UNDERLINE.toString()).setScore(4);

            Team yourPing = scoreboard.registerNewTeam("YourPing");
            yourPing.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Your Ping: "));
            yourPing.setSuffix(getEntry("" + ChatColor.WHITE + practicePlayer.getPing() + "ms"));
            obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Your Ping: ")).setScore(3);

            PracticePlayer opponentPlayer = game.getOpponent(practicePlayer);
            if (opponentPlayer != null) {
                Team opponentPing = scoreboard.registerNewTeam("TheirPing");
                opponentPing.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Their Ping: "));
                opponentPing.setSuffix(getEntry("" + ChatColor.WHITE + opponentPlayer.getPing() + "ms"));
                obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Their Ping: ")).setScore(2);
            }
            Team line2 = scoreboard.registerNewTeam("Line2");
            line2.addEntry(ChatColor.STRIKETHROUGH.toString());
            line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
            line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
            obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
        }
    }

    @Override
    void onUpdate() {
        DuelGame game = (DuelGame) HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        Team duration = scoreboard.getTeam("Duration");
        if(game != null) {
            if (game.getState() == GameState.PreGame) {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Starting: ");
                duration.setSuffix("" + ChatColor.WHITE + game.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(game.getTime()));
            }
            scoreboard.getTeam("YourPing").setSuffix(getEntry("" + ChatColor.WHITE + practicePlayer.getPing() + "ms"));
            PracticePlayer opponentPlayer = game.getOpponent(practicePlayer);
            if (opponentPlayer != null) {
                scoreboard.getTeam("TheirPing").setSuffix(getEntry("" + ChatColor.WHITE + opponentPlayer.getPing() + "ms"));
            }
        }


    }
}
