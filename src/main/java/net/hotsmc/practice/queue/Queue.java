package net.hotsmc.practice.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.match.impl.DuelMatch;
import net.hotsmc.practice.match.RankedType;
import net.hotsmc.practice.ladder.LadderType;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public class Queue {

    private RankedType rankedType;

    private LadderType ladderType;

    private PracticePlayer queuePlayer;

    private final long start = System.currentTimeMillis();

    public void startGame(PracticePlayer practicePlayer, RankedType rankedType, LadderType ladderType) {
        QueueManager queueManager = HotsPractice.getQueueManager();
        queueManager.removeQueue(queueManager.getPlayerOfQueue(queuePlayer));
        if(practicePlayer.getPlayer().getOpenInventory() != null){
            practicePlayer.getPlayer().closeInventory();
        }
        practicePlayer.getInventory().clear();
        queuePlayer.getInventory().clear();
        queuePlayer.sendMessage(ChatColor.GRAY + "Starting match (" + HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ")");
        practicePlayer.sendMessage(ChatColor.GRAY + "Starting match (" + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ")");
        if(rankedType == RankedType.RANKED) {
            queuePlayer.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Yours Elo " + ChatColor.GRAY + "(" + ladderType + ChatColor.GRAY + ")");
            queuePlayer.sendMessage(HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ": " + ChatColor.WHITE + queuePlayer.getPlayerData().getElo(ladderType));
            queuePlayer.sendMessage(HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ": " + ChatColor.WHITE + practicePlayer.getPlayerData().getElo(ladderType));
            practicePlayer.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Yours Elo " + ChatColor.GRAY + "(" + ladderType + ChatColor.GRAY + ")");
            practicePlayer.sendMessage(HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ": " + ChatColor.WHITE + queuePlayer.getPlayerData().getElo(ladderType));
            practicePlayer.sendMessage(HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + ": " + ChatColor.WHITE + practicePlayer.getPlayerData().getElo(ladderType));
        }
        DuelMatch duelGame = new DuelMatch(queuePlayer, practicePlayer, rankedType, ladderType, HotsPractice.getArenaFactory().create(ladderType));
        duelGame.start();
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }
}
