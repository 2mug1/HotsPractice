package net.hotsmc.practice.scoreboard;

import net.hotsmc.core.scoreboard.animate.LoadingDotString;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.queue.DuelQueue;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class QueueScoreboard extends PlayerScoreboard {

    private LoadingDotString dotString;

    public QueueScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
        dotString = new LoadingDotString();
        setUpdateInterval(15);
    }

    @Override
    void onSetup() {
        PlayerData playerData = practicePlayer.getPlayerData();
        if(playerData == null)return;

        Team line1 = scoreboard.registerNewTeam("Line1");
        line1.addEntry(ChatColor.WHITE.toString());
        line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.WHITE.toString()).setScore(8);

        Team online = scoreboard.registerNewTeam("Online");
        online.addEntry(ChatColor.DARK_BLUE.toString());
        online.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "Online: ");
        online.setSuffix("" + ChatColor.WHITE + HotsPractice.countOnline());
        obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(7);

        Team inQueue = scoreboard.registerNewTeam("Inqueue");
        inQueue.addEntry(ChatColor.DARK_AQUA.toString());
        inQueue.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "In Queues: ");
        inQueue.setSuffix("" + ChatColor.WHITE + HotsPractice.countQueue());
        obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(6);

        Team ingame = scoreboard.registerNewTeam("Ingame");
        ingame.addEntry(ChatColor.BLUE.toString());
        ingame.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "In Fights: ");
        ingame.setSuffix("" + ChatColor.WHITE + HotsPractice.countInGame());
        obj.getScore(ChatColor.BLUE.toString()).setScore(5);

        Team point = scoreboard.registerNewTeam("Point");
        point.addEntry(ChatColor.AQUA.toString());
        point.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "Points: ");
        point.setSuffix("" + ChatColor.WHITE + playerData.getPoint());
        obj.getScore(ChatColor.AQUA.toString()).setScore(4);

        Team latency = scoreboard.registerNewTeam("Latency");
        latency.addEntry(ChatColor.MAGIC.toString());
        latency.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Latency: ");
        latency.setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
        obj.getScore(ChatColor.MAGIC.toString()).setScore(3);

        Team queue = scoreboard.registerNewTeam("Queue");
        queue.addEntry(ChatColor.BOLD.toString());
        queue.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "Queue: ");
        queue.setSuffix(ChatColor.WHITE + HotsPractice.getQueueManager().getPlayerOfQueue(practicePlayer).getKitType().name() + dotString.next());
        obj.getScore(ChatColor.BOLD.toString()).setScore(2);

        Team line2 = scoreboard.registerNewTeam("Line2");
        line2.addEntry(ChatColor.STRIKETHROUGH.toString());
        line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
    }

    @Override
    void onUpdate() {
        scoreboard.getTeam("Online").setSuffix("" + ChatColor.WHITE + HotsPractice.countOnline());
        scoreboard.getTeam("Inqueue").setSuffix("" + ChatColor.WHITE + HotsPractice.countQueue());
        scoreboard.getTeam("Ingame").setSuffix("" + ChatColor.WHITE + HotsPractice.countInGame());
        scoreboard.getTeam("Latency").setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
        DuelQueue duelQueue = HotsPractice.getQueueManager().getPlayerOfQueue(practicePlayer);
        if (duelQueue != null) {
            scoreboard.getTeam("Queue").setSuffix(ChatColor.WHITE + duelQueue.getKitType().name() + dotString.next());
        }
    }
}
