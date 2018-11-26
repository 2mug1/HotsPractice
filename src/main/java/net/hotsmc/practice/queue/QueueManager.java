package net.hotsmc.practice.queue;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.match.RankedType;
import net.hotsmc.practice.ladder.LadderType;

import java.util.List;

public class QueueManager {

    @Getter
    private List<Queue> queues;

    public QueueManager(){
        queues = Lists.newArrayList();
    }

    /**
     * 並んでいるか
     * @param ladderType
     * @return
     */
    public Queue getQueue(RankedType rankedType, LadderType ladderType){
        for(Queue queue : queues){
            if (rankedType == queue.getRankedType() && queue.getLadderType() == ladderType){
                if(queue.getQueuePlayer() != null && queue.getQueuePlayer().getPlayer().isOnline()){
                    return queue;
                }
            }
        }
        return null;
    }

    /**
     * プレイヤーのQueueを取得
     * @return
     */
    public Queue getPlayerOfQueue(PracticePlayer practicePlayer){
        for(Queue queue : queues){
            if(queue.getQueuePlayer().getPlayer().getName().equals(practicePlayer.getPlayer().getName())){
                return queue;
            }
        }
        return null;
    }

    /**
     * Queueを追加
     * @param queue
     */
    public void addQueue(Queue queue){
        queues.add(queue);
    }

    /**
     * Queueを削除
     * @param queue
     */
    public void removeQueue(Queue queue){
        queues.remove(queue);
    }

    public int countQueue(RankedType rankedType, LadderType ladderType){
        int count = 0;
        for(Queue queue : queues) {
            if (rankedType == queue.getRankedType() && queue.getLadderType() == ladderType) {
                count++;
            }
        }
        return count;
    }
}
