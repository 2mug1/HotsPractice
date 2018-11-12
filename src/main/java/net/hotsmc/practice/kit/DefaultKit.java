package net.hotsmc.practice.kit;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class DefaultKit {

    private JavaPlugin plugin;

    private List<KitData> defaultKits;

    public DefaultKit(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
        this.defaultKits = new ArrayList<>();
    }

    public void load(){
        defaultKits.add(0, new KitData(KitType.NoDebuff, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/NoDebuff.yml")), "Kit")));
        defaultKits.add(1, new KitData(KitType.Debuff, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Debuff.yml")), "Kit")));
        defaultKits.add(2, new KitData(KitType.MCSG, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/MCSG.yml")), "Kit")));
        defaultKits.add(3, new KitData(KitType.OCTC, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/OCTC.yml")), "Kit")));
        defaultKits.add(4, new KitData(KitType.Gapple, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Gapple.yml")), "Kit")));
        defaultKits.add(5, new KitData(KitType.Archer, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Archer.yml")), "Kit")));
        defaultKits.add(6, new KitData(KitType.Combo, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Combo.yml")), "Kit")));
        defaultKits.add(7, new KitData(KitType.Soup, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Soup.yml")), "Kit")));
        defaultKits.add(8, new KitData(KitType.BuildUHC, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/BuildUHC.yml")), "Kit")));
        defaultKits.add(9, new KitData(KitType.Axe, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Axe.yml")), "Kit")));
        defaultKits.add(10, new KitData(KitType.GappleSG, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/GappleSG.yml")), "Kit")));
    }

    public KitData getKitData(KitType kitType){
        for(KitData kitData: defaultKits){
            if(kitData.getKitType() == kitType){
                return kitData;
            }
        }
        return null;
    }

    public void update(String type, Player player) {
        ConfigCursor configCursor;
        List<ItemStack> items = Lists.newArrayList();
        List<ItemStack> armors = Lists.newArrayList();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            items.add(inv.getItem(i));
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        Collections.addAll(armors, armor);

        if (type.equalsIgnoreCase("NoDebuff")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/NoDebuff.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Debuff")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Debuff.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("MCSG")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/MCSG.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("OCTC")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/OCTC.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Gapple")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Gapple.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Archer")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Archer.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Combo")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Combo.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Soup")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Soup.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("BuildUHC")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/BuildUHC.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("Axe")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/Axe.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        if (type.equalsIgnoreCase("GappleSG")) {
            configCursor = new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/GappleSG.yml")), "Kit");
            configCursor.setItemStackList(items, "Items");
            configCursor.setItemStackList(armors, "Armors");
            configCursor.save();
        }
        ChatUtility.sendMessage(player, ChatColor.GRAY + "Successfully kit data has been updated from your inventory: " + ChatColor.GREEN + type);
    }
}
