package net.hotsmc.practice.arena;

import com.google.common.collect.Lists;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.WorldDataUtility;
import org.bukkit.*;

import java.io.File;
import java.util.*;

public class ArenaFactory {


    public ArenaFactory(){
    }

    public Arena create(LadderType ladderType){
        World world;
        Location spawn1;
        Location spawn2;
        Location defaultSpawn;
        String randomWorldName;
        ArenaData arenaData;

        //すもう
        if(ladderType == LadderType.Sumo){
            randomWorldName = UUID.randomUUID().toString();
            WorldDataUtility.copyWorld(new File(HotsPractice.getInstance().getDataFolder() + "/worlds/sumoarena"),
                    new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + randomWorldName));
            Bukkit.createWorld(new WorldCreator(randomWorldName));
            world = Bukkit.getWorld(randomWorldName);
            arenaData = HotsPractice.getArenaManager().getSumoArena();
            spawn1 = new Location(world,
                    arenaData.getSpawn1().getX(),
                    arenaData.getSpawn1().getY(),
                    arenaData.getSpawn1().getZ(),
                    arenaData.getSpawn1().getYaw(),
                    arenaData.getSpawn1().getPitch());
            spawn2 = new Location(world,
                    arenaData.getSpawn2().getX(),
                    arenaData.getSpawn2().getY(),
                    arenaData.getSpawn2().getZ(),
                    arenaData.getSpawn2().getYaw(),
                    arenaData.getSpawn2().getPitch());
            defaultSpawn = new Location(world,
                    arenaData.getDefaultSpawn().getX(),
                    arenaData.getDefaultSpawn().getY(),
                    arenaData.getDefaultSpawn().getZ(),
                    arenaData.getDefaultSpawn().getYaw(),
                    arenaData.getDefaultSpawn().getPitch());
            return new Arena(spawn1, spawn2, defaultSpawn);
        }

        //スプリーフ
        if(ladderType == LadderType.Spleef){
           randomWorldName = UUID.randomUUID().toString();
            WorldDataUtility.copyWorld(new File(HotsPractice.getInstance().getDataFolder() + "/worlds/spleefarena"),
                    new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + randomWorldName));
            Bukkit.createWorld(new WorldCreator(randomWorldName));
            world = Bukkit.getWorld(randomWorldName);
            arenaData = HotsPractice.getArenaManager().getSpleefArena();
            spawn1 = new Location(world,
                    arenaData.getSpawn1().getX(),
                    arenaData.getSpawn1().getY(),
                    arenaData.getSpawn1().getZ(),
                    arenaData.getSpawn1().getYaw(),
                    arenaData.getSpawn1().getPitch());
            spawn2 = new Location(world,
                    arenaData.getSpawn2().getX(),
                    arenaData.getSpawn2().getY(),
                    arenaData.getSpawn2().getZ(),
                    arenaData.getSpawn2().getYaw(),
                    arenaData.getSpawn2().getPitch());
            defaultSpawn = new Location(world,
                    arenaData.getDefaultSpawn().getX(),
                    arenaData.getDefaultSpawn().getY(),
                    arenaData.getDefaultSpawn().getZ(),
                    arenaData.getDefaultSpawn().getYaw(),
                    arenaData.getDefaultSpawn().getPitch());
            return new Arena(spawn1, spawn2, defaultSpawn);
        }

        List<String> arenaNames = Lists.newArrayList();
        arenaNames.add("arena1");
        arenaNames.add("arena2");
        arenaNames.add("arena3");
        arenaNames.add("arena4");
        arenaNames.add("arena5");
        arenaNames.add("arena6");
        Collections.shuffle(arenaNames);
        //普通
        String arenaName = arenaNames.get(0);
        randomWorldName = UUID.randomUUID().toString();
        WorldDataUtility.copyWorld(new File(HotsPractice.getInstance().getDataFolder() + "/worlds/arenas/" + arenaName),
                new File(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/" + randomWorldName));
        Bukkit.createWorld(new WorldCreator(randomWorldName));
        world = Bukkit.getWorld(randomWorldName);
        arenaData = HotsPractice.getArenaManager().getNormalArena(arenaName);
        spawn1 = new Location(world,
                arenaData.getSpawn1().getX(),
                arenaData.getSpawn1().getY(),
                arenaData.getSpawn1().getZ(),
                arenaData.getSpawn1().getYaw(),
                arenaData.getSpawn1().getPitch());
        spawn2 = new Location(world,
                arenaData.getSpawn2().getX(),
                arenaData.getSpawn2().getY(),
                arenaData.getSpawn2().getZ(),
                arenaData.getSpawn2().getYaw(),
                arenaData.getSpawn2().getPitch());
        defaultSpawn = new Location(world,
                arenaData.getDefaultSpawn().getX(),
                arenaData.getDefaultSpawn().getY(),
                arenaData.getDefaultSpawn().getZ(),
                arenaData.getDefaultSpawn().getYaw(),
                arenaData.getDefaultSpawn().getPitch());
        return new Arena(spawn1, spawn2, defaultSpawn);
    }
}
