package net.hotsmc.practice.queue;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.kit.KitType;

import java.util.List;

public class DuelQueueManager {

    @Getter
    private List<DuelQueue> duelQueues;

    public DuelQueueManager(){
        duelQueues = Lists.newArrayList();
    }

    /**
     * 並んでいるか
     * @param kitType
     * @return
     */
    public DuelQueue getQueue(RankedType rankedType, KitType kitType){
        for(DuelQueue duelQueue : duelQueues){
            if (rankedType == duelQueue.getRankedType() && duelQueue.getKitType() == kitType){
                if(duelQueue.getQueuePlayer() != null && duelQueue.getQueuePlayer().getPlayer().isOnline()){
                    return duelQueue;
                }
            }
        }
        return null;
    }

    /**
     * プレイヤーのQueueを取得
     * @return
     */
    public DuelQueue getPlayerOfQueue(PracticePlayer practicePlayer){
        for(DuelQueue duelQueue : duelQueues){
            if(duelQueue.getQueuePlayer().getPlayer().getName().equals(practicePlayer.getPlayer().getName())){
                return duelQueue;
            }
        }
        return null;
    }

    /**
     * Queueを追加
     * @param duelQueue
     */
    public void addQueue(DuelQueue duelQueue){
        duelQueues.add(duelQueue);
    }

    /**
     * Queueを削除
     * @param duelQueue
     */
    public void removeQueue(DuelQueue duelQueue){
        duelQueues.remove(duelQueue);
    }

    public int countQueue(RankedType rankedType, KitType kitType){
        int count = 0;
        for(DuelQueue duelQueue : duelQueues) {
            if (rankedType == duelQueue.getRankedType() && duelQueue.getKitType() == kitType) {
                count++;
            }
        }
        return count;
    }
}
