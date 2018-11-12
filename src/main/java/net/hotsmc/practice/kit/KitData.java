package net.hotsmc.practice.kit;

import lombok.Getter;
import net.hotsmc.practice.config.ConfigCursor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class KitData {

    private KitType kitType;
    private List<ItemStack> items;
    private List<ItemStack> armors;

    public KitData(KitType kitType, ConfigCursor configCursor){
        this.kitType = kitType;
        this.items = configCursor.getItemStackList("Items");
        this.armors = configCursor.getItemStackList("Armors");
    }

    public KitData(KitType kitType, List<ItemStack> items, List<ItemStack> armors){
        this.kitType = kitType;
        this.items = items;
        this.armors = armors;
    }

    public KitData setItems(List<ItemStack> items){
        this.items = items;
        return this;
    }

    public KitData setArmors(List<ItemStack> armors){
        this.armors = armors;
        return this;
    }

}
