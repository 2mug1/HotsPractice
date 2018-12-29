package net.hotsmc.practice.ladder;

import lombok.Getter;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.player.PracticePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Ladder {

    private ConfigCursor cursor;
    private LadderType ladderType;
    private List<ItemStack> items;
    private List<ItemStack> armors;
    private String name = null;

    public Ladder(LadderType ladderType, ConfigCursor configCursor){
        this.cursor = configCursor;
        this.ladderType = ladderType;
        this.items = configCursor.getItemStackList("Items");
        this.armors = configCursor.getItemStackList("Armors");
        if(configCursor.exists("Name")){
            this.name = configCursor.getString("Name");
        }
    }

    public Ladder(LadderType ladderType, List<ItemStack> items, List<ItemStack> armors, ConfigCursor configCursor) {
        this.ladderType = ladderType;
        this.items = items;
        this.armors = armors;
        this.cursor = configCursor;
    }

    public Ladder setItems(List<ItemStack> items){
        this.items = items;
        return this;
    }

    public Ladder setArmors(List<ItemStack> armors){
        this.armors = armors;
        return this;
    }

    public boolean isRenamed(){
        return name != null;
    }

    public void changeName(PracticePlayer player, String name, int number){
        cursor.set("Name", name);
        cursor.save();
        this.name = name;
        player.sendMessage(ChatColor.YELLOW + "Successfully renamed kit #" + number + "'s name to " + name);
    }

}
