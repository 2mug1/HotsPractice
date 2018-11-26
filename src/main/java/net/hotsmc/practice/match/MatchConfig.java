package net.hotsmc.practice.match;

import lombok.Getter;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.PositionInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
public class MatchConfig {

    ConfigCursor configCursor;
    private int pregameTime;
    private int endgameTime;
    private int enderpearlCooldownTime;
    private Location lobbyLocation;

    public MatchConfig(ConfigCursor configCursor){
        this.configCursor = configCursor;
    }

    public MatchConfig load(){
        pregameTime = configCursor.getInt("PreGameTime");
        endgameTime = configCursor.getInt("EndGameTime");
        enderpearlCooldownTime = configCursor.getInt("EnderpearlCooldown");
        if(configCursor.exists("Lobby")) {
            lobbyLocation = configCursor.getLocation("Lobby");
        }
        return this;
    }

    public void setLobbyLocation(Player player){
        PositionInfo positionInfo = new PositionInfo(player);
        configCursor.setLocation("Lobby", positionInfo);
        configCursor.save();
        ChatUtility.sendMessage(player, ChatColor.GREEN + "Successfully updated lobby location: " + positionInfo.locationFormat());
    }
}
