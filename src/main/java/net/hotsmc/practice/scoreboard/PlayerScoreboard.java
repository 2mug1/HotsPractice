package net.hotsmc.practice.scoreboard;

import lombok.Setter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class PlayerScoreboard extends BukkitRunnable {

    private static final String DISPLAY_NAME = "" + ChatColor.YELLOW + ChatColor.BOLD + "Hots Practice" + ChatColor.GRAY + " (JP)";

    protected PracticePlayer practicePlayer;

    Scoreboard scoreboard;
    Objective obj;

    @Setter
    private long updateInterval;

    PlayerScoreboard(PracticePlayer practicePlayer){
        this.practicePlayer = practicePlayer;
        updateInterval = 1;
    }

    String checkNameLength(String name) {
        if (name.length() > 12) {
            return name.substring(0, 12);
        }
        return name;
    }

    abstract void onSetup();

    abstract void onUpdate();

    public void setup(){
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        obj = scoreboard.registerNewObjective("hotsduel", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(DISPLAY_NAME);

       onSetup();
    }

    private void update(){
        onUpdate();
        practicePlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void start(){
        this.runTaskTimer(HotsPractice.getInstance(), 0, updateInterval);
    }

    public void stop(){
        this.cancel();
    }

    @Override
    public void run() {
        if(practicePlayer.getPlayer() == null || !practicePlayer.getPlayer().isOnline()){
            this.cancel();
            return;
        }
        update();
    }
}
