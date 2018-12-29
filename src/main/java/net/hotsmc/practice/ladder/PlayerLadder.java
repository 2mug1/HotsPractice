package net.hotsmc.practice.ladder;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class PlayerLadder {

    private LadderType ladderType;
    private String uuid;
    private Ladder[] ladders;

    public PlayerLadder(LadderType ladderType, String uuid){
        this.ladderType = ladderType;
        this.uuid = uuid;
        this.ladders = new Ladder[7];
    }

    public PlayerLadder load(){

        Arrays.fill(ladders, null);

        String dir = HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/" + ladderType.name() + "/";
        File dir2 = new File(dir);
        String[] list = dir2.list();
        if (list != null) {
            String[] arrayOfString1;
            int j = (arrayOfString1 = list).length;
            for (int i = 0; i < j; i++) {
                String filename = arrayOfString1[i];
                File loadFile = new File(dir + filename);
                FileConfig fileConfig = new FileConfig(loadFile);
                ConfigCursor cursor = new ConfigCursor(fileConfig, "Kit");
                String num = filename.substring(0, filename.indexOf(".yml"));
                ladders[Integer.valueOf(num)] = new Ladder(ladderType, cursor);
            }
        }
        return this;
    }

    public void save(int saveIndex, Player player) {
        List<ItemStack> items = Lists.newArrayList();
        List<ItemStack> armors = Lists.newArrayList();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            items.add(inv.getItem(i));
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        Collections.addAll(armors, armor);
        File file = new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + player.getUniqueId().toString() + "/" + ladderType.name() + "/" + saveIndex + ".yml");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    ConfigCursor configCursor = new ConfigCursor(new FileConfig(file), "Kit");
                    configCursor.setItemStackList(items, "Items");
                    configCursor.setItemStackList(armors, "Armors");
                    configCursor.save();
                    Ladder ladder = new Ladder(ladderType, items, armors, configCursor);
                    ladders[saveIndex] = ladder;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(int saveIndex, String uuid){
        ladders[saveIndex] = null;
        File file = new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/" + ladderType.name() + "/" + saveIndex + ".yml");
        file.delete();
    }

    public void setItems(List<ItemStack> items, Player player) {
        Inventory inventory = player.getInventory();
        inventory.clear();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            inventory.setItem(i, items.get(i));
        }
        player.updateInventory();
    }

    public void setArmors(List<ItemStack> armors, Player player) {
        EntityEquipment entityEquipment = player.getEquipment();
        entityEquipment.setHelmet(armors.get(3));
        entityEquipment.setChestplate(armors.get(2));
        entityEquipment.setLeggings(armors.get(1));
        entityEquipment.setBoots(armors.get(0));
        player.updateInventory();
    }

    public void setKit(int saveIndex, Player player) {
        Ladder ladder = ladders[saveIndex];
        setItems(ladder.getItems(), player);
        setArmors(ladder.getArmors(), player);
        if (!ladder.isRenamed()) {
            ChatUtility.sendMessage(player, ChatColor.YELLOW + "You have loaded kit #" + saveIndex);
        }else{
            ChatUtility.sendMessage(player, ChatColor.YELLOW + "You have loaded kit " + ladder.getName());
        }
    }
}
