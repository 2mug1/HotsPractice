package net.hotsmc.practice.match;

import com.google.common.collect.Lists;
import net.hotsmc.core.utility.InventoryUtility;
import net.hotsmc.practice.match.MatchInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchInventoryManager {

    private List<MatchInventory> playerInventories;

    public MatchInventoryManager(){
        playerInventories = new ArrayList<>();
    }

    public boolean exsitsPlayerInventory(String uuid){
        return getPlayerInventoryByUUID(uuid) != null;
    }

    public MatchInventory getPlayerInventoryByUUID(String uuid){
        for(MatchInventory matchInventory : playerInventories){
            if(matchInventory.getUuid().equals(uuid)){
                return matchInventory;
            }
        }
        return null;
    }

    public void addPlayerInventory(Player player, String duration_time){
        String uuid = player.getUniqueId().toString();
        BigDecimal bd;
        bd = new BigDecimal(player.getHealth());
        BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_DOWN);
        bd = new BigDecimal(player.getFoodLevel());
        BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_DOWN);
        MatchInventory matchInventory = getPlayerInventoryByUUID(player.getUniqueId().toString());
        if(exsitsPlayerInventory(uuid)){
            playerInventories.remove(matchInventory);
        }
        PlayerInventory inv = player.getInventory();
        ItemStack[] items = InventoryUtility.fixInventoryOrder(inv.getContents());
        ItemStack[] armors = inv.getArmorContents();
        playerInventories.add(new MatchInventory(uuid, player.getName(), items, armors, bd1.doubleValue(), bd2.doubleValue(), duration_time));
    }

    public void removePlayerInventory(MatchInventory matchInventory) {
        playerInventories.remove(matchInventory);
    }
}
