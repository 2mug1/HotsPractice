package net.hotsmc.practice.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.utility.WorldDataUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

@Getter
@AllArgsConstructor
public class Arena {

    private Location spawn1;
    private Location spawn2;
    private Location defaultSpawn;

    public void unload(){
        World world = spawn1.getWorld();
        String worldName = world.getName();
        Bukkit.getServer().unloadWorld(world, false);
        new BukkitRunnable() {
            @Override
            public void run() {
                WorldDataUtility.deleteWorld(new File(HotsPractice.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/" + worldName));
                this.cancel();
            }
        }.runTaskLater(HotsPractice.getInstance(), 20*3);
    }
}
