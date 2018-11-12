package net.hotsmc.practice.kit;

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
import java.util.Collections;
import java.util.List;

@Getter
public class PlayerKitData {

    private KitType kitType;
    private String uuid;
    private List<KitData> kitDataList;

    public PlayerKitData(KitType kitType, String uuid){
        this.kitType = kitType;
        this.uuid = uuid;
        this.kitDataList = new ArrayList<>(7);
    }

    public PlayerKitData load(){
        for(int i = 0; i < 7; i++){
            kitDataList.add(i,null);
        }

        String dir = HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/" + kitType.name() + "/";
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
                kitDataList.add(Integer.valueOf(num), new KitData(kitType, cursor));
            }
        }
        return this;
    }

    public void save(int saveIndex, Player player){
        List<ItemStack> items = Lists.newArrayList();
        List<ItemStack> armors = Lists.newArrayList();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            items.add(inv.getItem(i));
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        Collections.addAll(armors, armor);
        KitData kitData = new KitData(kitType, items, armors);
        kitDataList.remove(saveIndex);
        kitDataList.add(saveIndex, kitData);
        File file = new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + player.getUniqueId().toString() + "/" + kitType.name() + "/" + saveIndex + ".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigCursor configCursor = new ConfigCursor(new FileConfig(file), "Kit");
        configCursor.setItemStackList(items, "Items");
        configCursor.setItemStackList(armors, "Armors");
        configCursor.save();
    }

    public void delete(int saveIndex, String uuid){
        kitDataList.remove(saveIndex);
        kitDataList.add(saveIndex, null);
        File file = new File(HotsPractice.getInstance().getDataFolder().getPath() + "/playerkit/" + uuid + "/" + kitType.name() + "/" + saveIndex + ".yml");
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

    public void setKit(int saveIndex, Player player){
        KitData kitData = kitDataList.get(saveIndex);
        setItems(kitData.getItems(), player);
        setArmors(kitData.getArmors(), player);
        ChatUtility.sendMessage(player,ChatColor.YELLOW + "You have loaded kit #" + saveIndex);
    }
}
