package net.hotsmc.practice;

import lombok.Getter;
import lombok.Setter;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.PositionInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PracticeConfig {

    ConfigCursor configCursor;
    private int pregameTime;
    private int endgameTime;
    private int enderpearlCooldownTime;
    private Location lobbyLocation;
    private Location kitEditLocation;

    public PracticeConfig(ConfigCursor configCursor){
        this.configCursor = configCursor;
    }

    public PracticeConfig load(){
        pregameTime = configCursor.getInt("PreGameTime");
        endgameTime = configCursor.getInt("EndGameTime");
        enderpearlCooldownTime = configCursor.getInt("EnderpearlCooldown");
        if(configCursor.exists("Lobby")) {
            lobbyLocation = configCursor.getLocation("Lobby");
        }
        if(configCursor.exists("KitEdit")) {
            kitEditLocation = configCursor.getLocation("KitEdit");
        }
        return this;
    }

    public void updateLobbyLocation(Player player){
        PositionInfo positionInfo = new PositionInfo(player);
        configCursor.setLocation("Lobby", positionInfo);
        configCursor.save();
        setLobbyLocation(positionInfo.toLocation());
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully updated Lobby location: " + positionInfo.locationFormat());
    }

    public void updateKitEditLocation(Player player){
        PositionInfo positionInfo = new PositionInfo(player);
        configCursor.setLocation("KitEdit", positionInfo);
        configCursor.save();
        setKitEditLocation(positionInfo.toLocation());
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully updated Kit Edit location: " + positionInfo.locationFormat());
    }
}
