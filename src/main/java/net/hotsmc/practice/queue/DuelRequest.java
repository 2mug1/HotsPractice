package net.hotsmc.practice.queue;

import lombok.Getter;
import lombok.Setter;
import net.hotsmc.practice.ladder.LadderType;

@Getter
@Setter
public abstract class DuelRequest {

    private LadderType ladderType;

    public DuelRequest(LadderType ladderType){
        this.ladderType = ladderType;
    }

    public abstract void accept();
}
