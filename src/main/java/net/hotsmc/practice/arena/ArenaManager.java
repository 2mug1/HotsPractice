package net.hotsmc.practice.arena;

import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.PositionInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ArenaManager {

    private JavaPlugin plugin;

    private List<ArenaData> normalArenas;
    private ArenaData sumoArena;
    private ArenaData spleefArena;
    private ArenaData parkourArena;

    public ArenaManager(JavaPlugin plugin){
        this.plugin = plugin;
        this.normalArenas = new ArrayList<>();
    }

    public void load(){
        String dir = plugin.getDataFolder().getPath() + "/arenadata/normalarena/";
        File dir2 = new File(dir);
        String[] list = dir2.list();

        if (list != null) {
            String[] arrayOfString1;
            int j = (arrayOfString1 = list).length;
            for (int i = 0; i < j; i++) {
                String filename = arrayOfString1[i];
                File loadFile = new File(dir + filename);
                FileConfig fileConfig = new FileConfig(loadFile);
                ConfigCursor cursor = new ConfigCursor(fileConfig, "arenadata");
                String arenaName = filename.substring(0, filename.indexOf(".yml"));

                IgnoreWorldLocation spawn1 = new IgnoreWorldLocation(
                        cursor.getDouble("Spawns.Spawn1.x"),
                        cursor.getDouble("Spawns.Spawn1.y"),
                        cursor.getDouble("Spawns.Spawn1.z"),
                        cursor.getDouble("Spawns.Spawn1.yaw"),
                        cursor.getDouble("Spawns.Spawn1.pitch"));

                IgnoreWorldLocation spawn2 = new IgnoreWorldLocation(
                        cursor.getDouble("Spawns.Spawn2.x"),
                        cursor.getDouble("Spawns.Spawn2.y"),
                        cursor.getDouble("Spawns.Spawn2.z"),
                        cursor.getDouble("Spawns.Spawn2.yaw"),
                        cursor.getDouble("Spawns.Spawn2.pitch"));

                IgnoreWorldLocation defaultSpawn = new IgnoreWorldLocation(
                        cursor.getDouble("DefaultSpawn.x"),
                        cursor.getDouble("DefaultSpawn.y"),
                        cursor.getDouble("DefaultSpawn.z"),
                        cursor.getDouble("DefaultSpawn.yaw"),
                        cursor.getDouble("DefaultSpawn.pitch"));

                //追加
                normalArenas.add(new ArenaData(arenaName, spawn1, spawn2, defaultSpawn));
                System.out.println("読み込みました: " + arenaName);
            }
        }

        ConfigCursor cursor;
        IgnoreWorldLocation spawn1;
        IgnoreWorldLocation spawn2;
        IgnoreWorldLocation defaultSpawn;

        cursor =  new ConfigCursor(new FileConfig(new File(HotsPractice.getInstance().getDataFolder() + "/arenadata/sumoarena.yml")), "arenadata");
        spawn1 = new IgnoreWorldLocation(
                cursor.getDouble("Spawns.Spawn1.x"),
                cursor.getDouble("Spawns.Spawn1.y"),
                cursor.getDouble("Spawns.Spawn1.z"),
                cursor.getDouble("Spawns.Spawn1.yaw"),
                cursor.getDouble("Spawns.Spawn1.pitch"));

        spawn2 = new IgnoreWorldLocation(
                cursor.getDouble("Spawns.Spawn2.x"),
                cursor.getDouble("Spawns.Spawn2.y"),
                cursor.getDouble("Spawns.Spawn2.z"),
                cursor.getDouble("Spawns.Spawn2.yaw"),
                cursor.getDouble("Spawns.Spawn2.pitch"));

        defaultSpawn = new IgnoreWorldLocation(
                cursor.getDouble("DefaultSpawn.x"),
                cursor.getDouble("DefaultSpawn.y"),
                cursor.getDouble("DefaultSpawn.z"),
                cursor.getDouble("DefaultSpawn.yaw"),
                cursor.getDouble("DefaultSpawn.pitch"));

        sumoArena = new ArenaData("sumoarena", spawn1, spawn2, defaultSpawn);

        //------------------------------------------------------------------------------------------------------------------------------------

        cursor = new ConfigCursor(new FileConfig(new File(HotsPractice.getInstance().getDataFolder() + "/arenadata/spleefarena.yml")), "arenadata");

        spawn1 = new IgnoreWorldLocation(
                cursor.getDouble("Spawns.Spawn1.x"),
                cursor.getDouble("Spawns.Spawn1.y"),
                cursor.getDouble("Spawns.Spawn1.z"),
                cursor.getDouble("Spawns.Spawn1.yaw"),
                cursor.getDouble("Spawns.Spawn1.pitch"));

        spawn2 = new IgnoreWorldLocation(
                cursor.getDouble("Spawns.Spawn2.x"),
                cursor.getDouble("Spawns.Spawn2.y"),
                cursor.getDouble("Spawns.Spawn2.z"),
                cursor.getDouble("Spawns.Spawn2.yaw"),
                cursor.getDouble("Spawns.Spawn2.pitch"));

        defaultSpawn = new IgnoreWorldLocation(
                cursor.getDouble("DefaultSpawn.x"),
                cursor.getDouble("DefaultSpawn.y"),
                cursor.getDouble("DefaultSpawn.z"),
                cursor.getDouble("DefaultSpawn.yaw"),
                cursor.getDouble("DefaultSpawn.pitch"));
        spleefArena = new ArenaData("spleefarena", spawn1, spawn2, defaultSpawn);
    }

    /**
     * 存在しているか
     * @param arenaName
     * @return
     */
    public boolean existsArenaData(String arenaName){
        return new File(plugin.getDataFolder().getPath() +  "/arenadata/" + arenaName + ".yml").exists();
    }

    /**
     * ノーマルアリーナ取得
     * @param arenaName
     * @return
     */
    public ArenaData getNormalArena(String arenaName){
        for(ArenaData arenaData : normalArenas){
            if(arenaData.getArenaName().equalsIgnoreCase(arenaName)){
                return arenaData;
            }
        }
        return null;
    }

    /**
     * アリーナ用のデータファイルを作成
     * @param arenaName
     * @param player
     */
    public void createArenaData(String arenaName, Player player) {
        if (existsArenaData(arenaName)) {
            ChatUtility.sendMessage(player, ChatColor.RED + "That name of arena data has already existed");
            return;
        }
        File file = new File(plugin.getDataFolder().getPath() + "/arenadata/" + arenaName + ".yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            ChatUtility.sendMessage(player, "Failed to create file of arena data");
        }
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully created the '" + arenaName + "'");
    }

    /**
     * 既定のスポーン位置を更新
     * @param arenaName
     * @param player
     */
    public void updateDefaultSpawn(String arenaName, Player player) {
        if (!existsArenaData(arenaName)) {
            ChatUtility.sendMessage(player, ChatColor.RED + "That name of arena data not found");
            return;
        }
        File file = new File(plugin.getDataFolder().getPath() + "/arenadata/" + arenaName + ".yml");
        ConfigCursor configCursor = new ConfigCursor(new FileConfig(file), "arenadata");
        PositionInfo positionInfo = new PositionInfo(player);
        configCursor.setLocation("DefaultSpawn", positionInfo);
        configCursor.save();
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully " + arenaName + " of default spawn has been updated to " + positionInfo.locationFormat());
    }


    /**
     * 2箇所のスポーン位置を更新
     * @param arenaName
     * @param player
     * @param number
     */
    public void updateSpawn(String arenaName, Player player, int number) {
        if (!existsArenaData(arenaName)) {
            ChatUtility.sendMessage(player, ChatColor.RED + "That name of arena data not found");
            return;
        }
        if (number > 2) {
            ChatUtility.sendMessage(player, ChatColor.RED + "Spawn location of maximum value is 2!");
        } else {
            File file = new File(plugin.getDataFolder().getPath() + "/arenadata/" + arenaName + ".yml");
            ConfigCursor configCursor = new ConfigCursor(new FileConfig(file), "arenadata");
            PositionInfo positionInfo = new PositionInfo(player);
            configCursor.setLocation("Spawns.Spawn" + number, positionInfo);
            configCursor.save();
            ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully " + arenaName + " of spawn " + number + " has been updated to " + positionInfo.locationFormat());
        }
    }
}
