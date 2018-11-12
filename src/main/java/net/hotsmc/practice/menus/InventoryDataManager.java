package net.hotsmc.practice.menus;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryDataManager {

    private List<PlayerInventory> playerInventories;

    public InventoryDataManager(){
        playerInventories = new ArrayList<>();
    }

    public boolean exsitsPlayerInventory(String uuid){
        return getPlayerInventoryByUUID(uuid) != null;
    }

    public PlayerInventory getPlayerInventoryByUUID(String uuid){
        for(PlayerInventory playerInventory : playerInventories){
            if(playerInventory.getUuid().equals(uuid)){
                return playerInventory;
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
        PlayerInventory playerInventory = getPlayerInventoryByUUID(player.getUniqueId().toString());
        if(exsitsPlayerInventory(uuid)){
            playerInventories.remove(playerInventory);
        }
        List<ItemStack> items = Lists.newArrayList();
        List<ItemStack> armors = Lists.newArrayList();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            items.add(inv.getItem(i));
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        Collections.addAll(armors, armor);
        playerInventories.add(new PlayerInventory(uuid, player.getName(), items, armors, bd1.doubleValue(), bd2.doubleValue(), duration_time));
    }

    public void removePlayerInventory(PlayerInventory playerInventory) {
        playerInventories.remove(playerInventory);
    }
}
