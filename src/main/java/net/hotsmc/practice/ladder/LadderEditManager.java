package net.hotsmc.practice.ladder;

import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.PositionInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LadderEditManager {

    private ConfigCursor kiteditLocationsCursor;

    private Map<LadderType, Location> kiteditLocations;

    public LadderEditManager(ConfigCursor configCursor){
        this.kiteditLocationsCursor = configCursor;
    }

    public void load(){
        kiteditLocations = new HashMap<>();
        for(LadderType ladderType : LadderType.values()){
            kiteditLocations.put(ladderType, kiteditLocationsCursor.getLocation(ladderType.name()));
        }
    }

    public void updateKitEditLocation(Player player, LadderType ladderType){
        PositionInfo positionInfo = new PositionInfo(player);
        kiteditLocationsCursor.setLocation(ladderType.name(), positionInfo);
        kiteditLocationsCursor.save();
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Kit edit location has been updated: " + ladderType.name() + "/" + positionInfo.locationFormat());
    }

    public void teleport(Player player, LadderType ladderType){
        player.teleport(kiteditLocations.get(ladderType));
    }
}
