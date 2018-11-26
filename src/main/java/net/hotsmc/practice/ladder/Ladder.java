package net.hotsmc.practice.ladder;

import lombok.Getter;
import net.hotsmc.practice.config.ConfigCursor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Ladder {

    private LadderType ladderType;
    private List<ItemStack> items;
    private List<ItemStack> armors;

    public Ladder(LadderType ladderType, ConfigCursor configCursor){
        this.ladderType = ladderType;
        this.items = configCursor.getItemStackList("Items");
        this.armors = configCursor.getItemStackList("Armors");
    }

    public Ladder(LadderType ladderType, List<ItemStack> items, List<ItemStack> armors){
        this.ladderType = ladderType;
        this.items = items;
        this.armors = armors;
    }

    public Ladder setItems(List<ItemStack> items){
        this.items = items;
        return this;
    }

    public Ladder setArmors(List<ItemStack> armors){
        this.armors = armors;
        return this;
    }

}
