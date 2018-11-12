package net.hotsmc.practice.kit;

import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.PositionInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KitEditManager {

    private ConfigCursor kiteditLocationsCursor;

    private Map<KitType, Location> kiteditLocations;

    public KitEditManager(ConfigCursor configCursor){
        this.kiteditLocationsCursor = configCursor;
    }

    public void load(){
        kiteditLocations = new HashMap<>();
        for(KitType kitType : KitType.values()){
            kiteditLocations.put(kitType, kiteditLocationsCursor.getLocation(kitType.name()));
        }
    }

    public void updateKitEditLocation(Player player, KitType kitType){
        PositionInfo positionInfo = new PositionInfo(player);
        kiteditLocationsCursor.setLocation(kitType.name(), positionInfo);
        kiteditLocationsCursor.save();
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Kit edit location has been updated: " + kitType.name() + "/" + positionInfo.locationFormat());
    }

    public void teleport(Player player, KitType kitType){
        player.teleport(kiteditLocations.get(kitType));
    }
}
