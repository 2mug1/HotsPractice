package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.PracticePlayer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class KitEditScoreboard extends PlayerScoreboard {

    public KitEditScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        Team line1 = scoreboard.registerNewTeam("Line1");
        line1.addEntry(ChatColor.WHITE.toString());
        line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.WHITE.toString()).setScore(4);

        Team kiteditor = scoreboard.registerNewTeam("KitEditor");
        kiteditor.addEntry(ChatColor.DARK_AQUA.toString());
        kiteditor.setPrefix("Kit Editor");
        obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(3);

        Team ladder = scoreboard.registerNewTeam("Ladder");
        ladder.addEntry(ChatColor.AQUA.toString());
        ladder.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Ladder: ");
        ladder.setSuffix(""+ ChatColor.WHITE + practicePlayer.getEditKitType());
        obj.getScore(ChatColor.AQUA.toString()).setScore(2);

        Team line2 = scoreboard.registerNewTeam("Line2");
        line2.addEntry(ChatColor.STRIKETHROUGH.toString());
        line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
    }

    @Override
    void onUpdate() {

    }
}
