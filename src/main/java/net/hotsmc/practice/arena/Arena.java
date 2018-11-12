package net.hotsmc.practice.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.utility.WorldDataUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;

@Getter
@AllArgsConstructor
public class Arena {

    private Location spawn1;
    private Location spawn2;
    private Location defaultSpawn;

    public void unload(){
        Bukkit.getServer().unloadWorld(spawn1.getWorld(), false);
        WorldDataUtility.deleteWorld(new File(HotsPractice.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/" + spawn1.getWorld().getName()));
    }
}
