package net.hotsmc.practice.knockback;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.ladder.LadderType;
import org.bukkit.ChatColor;

@Data
@AllArgsConstructor
public class KnockbackProfile {

    private ConfigCursor cursor;

    private LadderType ladderType;

    //水平
    private double horizontalMultiplier;

    //垂直
    private double verticalMultiplier;

    //Air
    private double airMultiplier;

    //Sprint
    private double sprintMultiplier;

    private double fishingRodHorizontalMultiplier;

    private double fishingRodVerticalMultiplier;

    public String toString(){
       return ChatColor.YELLOW + "There are knockback multipliers for " + ladderType.name() + " - Horizontal: " + horizontalMultiplier + " Vertical: " + verticalMultiplier + "Air: " + airMultiplier + " Sprint: " + sprintMultiplier + "Fishing-Rod Horizontal: " + fishingRodHorizontalMultiplier + " Fishing-Rod Vertical: " + fishingRodVerticalMultiplier;
    }
}