package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.games.Game;
import net.hotsmc.practice.game.games.GameState;
import net.hotsmc.practice.game.games.PartyFFAGame;
import net.hotsmc.practice.game.games.PartyTeamGame;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class PartyGameTeamScoreboard extends PlayerScoreboard {

    public PartyGameTeamScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        Team line1 = scoreboard.registerNewTeam("Line1");
        line1.addEntry(ChatColor.WHITE.toString());
        line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.WHITE.toString()).setScore(9);

        Team ladder = scoreboard.registerNewTeam("Ladder");
        ladder.addEntry(ChatColor.AQUA.toString());
        ladder.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Ladder: ");
        ladder.setSuffix(""+ ChatColor.WHITE + HotsPractice.getGameManager().getPlayerOfGame(practicePlayer).getKitType());
        obj.getScore(ChatColor.AQUA.toString()).setScore(8);

        Team mode = scoreboard.registerNewTeam("Mode");
        mode.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Party Fight:"));
        mode.setSuffix(getEntry(ChatColor.WHITE + " Team"));
        obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Party Fight:")).setScore(7);

        Team yourTeam = scoreboard.registerNewTeam("YourTeam");
        yourTeam.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Your Team: "));
        obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Your Team: ")).setScore(6);

        Team duration = scoreboard.registerNewTeam("Duration");
        duration.addEntry(ChatColor.BLUE.toString());
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        if(game != null) {
            if (game.getState() == GameState.PreGame) {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Starting: ");
                duration.setSuffix("" + ChatColor.WHITE + game.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(game.getTime()));
            }
        }
        obj.getScore(ChatColor.BLUE.toString()).setScore(5);

        Team blank = scoreboard.registerNewTeam("Blank");
        blank.addEntry(" ");
        obj.getScore(" ").setScore(4);

        Team green = scoreboard.registerNewTeam("Green");
        green.addEntry(getEntry("" + ChatColor.GREEN + "Green: "));
        obj.getScore(getEntry("" + ChatColor.GREEN + "Green: ")).setScore(3);

        Team red = scoreboard.registerNewTeam("Red");
        red.addEntry(getEntry("" + ChatColor.RED + "Red: "));
        obj.getScore(getEntry("" + ChatColor.RED + "Red: ")).setScore(2);

        Team line2 = scoreboard.registerNewTeam("Line2");
        line2.addEntry(ChatColor.STRIKETHROUGH.toString());
        line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
    }

    @Override
    void onUpdate() {
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        PartyTeamGame teamGame = (PartyTeamGame) game;
        Team duration = scoreboard.getTeam("Duration");
        if(game != null) {
            if (game.getState() == GameState.PreGame) {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Starting: ");
                duration.setSuffix("" + ChatColor.WHITE + game.getStartCooldown().getTimeLeft() + "s");
            } else {
                duration.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Duration: ");
                duration.setSuffix("" + ChatColor.WHITE + TimeUtility.timeFormat(game.getTime()));
            }
            net.hotsmc.practice.other.Team myTeam = teamGame.getMyTeam(practicePlayer);
            if(myTeam != null) {
                scoreboard.getTeam("YourTeam").setSuffix(myTeam.getPrefix() + myTeam.getTeamName());
            }
            net.hotsmc.practice.other.Team green = teamGame.teams[0];
            if(green != null) {
                scoreboard.getTeam("Green").setSuffix(getEntry("" + ChatColor.WHITE + green.getAlivePlayers().size()));
            }
            net.hotsmc.practice.other.Team red = teamGame.teams[1];
            if(red != null) {
                scoreboard.getTeam("Red").setSuffix(getEntry("" + ChatColor.WHITE + red.getAlivePlayers().size()));
            }
        }
    }
}
