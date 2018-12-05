package net.hotsmc.practice.menus.kit;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.ladder.Ladder;
import net.hotsmc.practice.ladder.LadderType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitChestInventory {

    private LadderType type;
    private Inventory inventory;

    public KitChestInventory(LadderType type) {
        this.type = type;
        this.inventory = Bukkit.createInventory(null, 36, "Kit Chest " + type.name());
    }

    private void setItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public void open(Player player) {
        Ladder ladder = HotsPractice.getInstance().getDefaultLadder().getKitData(type);
        List<ItemStack> armors = ladder.getArmors();

        List<ItemStack> items = ladder.getItems();

        setItem(0, armors.get(3));
        setItem(9, armors.get(2));
        setItem(18, armors.get(1));
        setItem(27, armors.get(0));

        for(int i = 1; i < items.size(); i++){
            if(i == 9 || i == 18 || i == 27){
                i++;
            }
            setItem(i, items.get(i));
        }

        if(type == LadderType.NoDebuff || type == LadderType.Debuff){
            setItem(10, new ItemStack(Material.GRILLED_PORK, 64));
            setItem(11, new ItemStack(Material.COOKED_BEEF, 64));
        }

        player.openInventory(inventory);
    }
}
