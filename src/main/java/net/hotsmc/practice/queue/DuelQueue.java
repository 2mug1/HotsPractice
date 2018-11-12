package net.hotsmc.practice.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.task.DuelGame;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public class DuelQueue {

    private RankedType rankedType;

    private KitType kitType;

    private PracticePlayer queuePlayer;

    public void startGame(PracticePlayer practicePlayer, RankedType rankedType, KitType kitType) {
        DuelQueueManager queueManager = HotsPractice.getQueueManager();
        queueManager.removeQueue(queueManager.getPlayerOfQueue(queuePlayer));
        if(practicePlayer.getPlayer().getOpenInventory() != null){
            practicePlayer.getPlayer().closeInventory();
        }
        practicePlayer.getInventory().clear();
        queuePlayer.getInventory().clear();
        ChatUtility.sendMessage(queuePlayer, HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName());
        ChatUtility.sendMessage(practicePlayer, HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(queuePlayer.getPlayer()).getColorName());
        DuelGame duelGame = new DuelGame(queuePlayer, practicePlayer, rankedType, kitType, HotsPractice.getArenaFactory().create(kitType));
        duelGame.start();
    }
}
