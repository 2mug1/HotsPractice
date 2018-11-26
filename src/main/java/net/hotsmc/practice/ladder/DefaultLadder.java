package net.hotsmc.practice.ladder;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
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
public class DefaultLadder {

    private JavaPlugin plugin;

    private List<Ladder> defaultKits;

    public DefaultLadder(JavaPlugin javaPlugin){
        this.plugin = javaPlugin;
        this.defaultKits = new ArrayList<>();
    }

    public void loadLadders(){
        defaultKits.clear();
        for(LadderType ladderType : LadderType.values()){
            defaultKits.add(new Ladder(ladderType, new ConfigCursor(new FileConfig(new File(plugin.getDataFolder().getPath() + "/defaultkit/" + ladderType.name() + ".yml")), "Kit")));
            HotsPractice.getInstance().getLogger().info("Loaded Default Ladder Data: " + ladderType.name());
        }
    }

    public Ladder getKitData(LadderType ladderType){
        for(Ladder ladder : defaultKits){
            if(ladder.getLadderType() == ladderType){
                return ladder;
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
        ChatUtility.sendMessage(player, ChatColor.GRAY + "Successfully ladder data has been updated from your inventory: " + ChatColor.GREEN + type);
    }
}
