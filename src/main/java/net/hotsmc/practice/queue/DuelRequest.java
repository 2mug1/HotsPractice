package net.hotsmc.practice.queue;

import lombok.Getter;
import lombok.Setter;
import net.hotsmc.practice.kit.KitType;

@Getter
@Setter
public abstract class DuelRequest {

    private KitType kitType;

    public DuelRequest(KitType kitType){
        this.kitType = kitType;
    }

    public abstract void accept();
}
