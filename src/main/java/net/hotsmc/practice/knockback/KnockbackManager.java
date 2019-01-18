package net.hotsmc.practice.knockback;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KnockbackManager {

    private List<KnockbackProfile> knockbackProfiles = new ArrayList<>();

    public KnockbackProfile getKnockbackByLadderType(LadderType ladderType){
        for(KnockbackProfile knockbackProfile : knockbackProfiles){
            if(knockbackProfile.getLadderType() == ladderType){
                return knockbackProfile;
            }
        }
        return null;
    }

    private KnockbackProfile load(LadderType ladderType, ConfigCursor configCursor){
        return new KnockbackProfile(configCursor, ladderType, configCursor.getDouble("horizontal"), configCursor.getDouble("vertical"), configCursor.getDouble("air"), configCursor.getDouble("sprint"), configCursor.getDouble("fishing-rod-horizontal"), configCursor.getDouble("fishing-rod-vertical"));
    }

    public void load(){
        knockbackProfiles.clear();
        for(LadderType ladderType : LadderType.values()){
            KnockbackProfile knockbackProfile = load(ladderType, new ConfigCursor(new FileConfig(new File(HotsPractice.getInstance().getDataFolder().getPath() + "/knockback/" + ladderType.name() + ".yml")), "Multiplier"));
            knockbackProfiles.add(knockbackProfile);
            HotsPractice.getInstance().getLogger().info("Loaded KnockbackProfile Data: " + ladderType.name() + " - Horizontal: " + knockbackProfile.getHorizontalMultiplier() + " Vertical: " + knockbackProfile.getVerticalMultiplier());
        }
    }

    public void updateHorizontal(CommandSender commandSender, LadderType type, double horizontal){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getHorizontalMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("horizontal", horizontal);
        cursor.save();
        knockbackProfile.setHorizontalMultiplier(horizontal);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Horizontal multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + horizontal);
    }

    public void updateVertical(CommandSender commandSender, LadderType type, double vertical){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getVerticalMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("vertical", vertical);
        cursor.save();
        knockbackProfile.setVerticalMultiplier(vertical);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Vertical multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + vertical);
    }

    public void updateAir(CommandSender commandSender, LadderType type, double air){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getAirMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("air", air);
        cursor.save();
        knockbackProfile.setAirMultiplier(air);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Air multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + air);
    }

    public void updateSprint(CommandSender commandSender, LadderType type, double sprint){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getSprintMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("sprint", sprint);
        cursor.save();
        knockbackProfile.setSprintMultiplier(sprint);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Sprint multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + sprint);
    }

    public void updateFishingRodHorizontal(CommandSender commandSender, LadderType type, double fishingrod_horizontal){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getFishingRodHorizontalMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("fishing-rod-horizontal", fishingrod_horizontal);
        cursor.save();
        knockbackProfile.setFishingRodHorizontalMultiplier(fishingrod_horizontal);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Fishing-Rod Horizontal multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + fishingrod_horizontal);
    }

    public void updateFishingRodVertical(CommandSender commandSender, LadderType type, double fishingrod_vertical){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        double old = knockbackProfile.getFishingRodVerticalMultiplier();
        ConfigCursor cursor = knockbackProfile.getCursor();
        cursor.set("fishing-rod-vertical", fishingrod_vertical);
        cursor.save();
        knockbackProfile.setFishingRodVerticalMultiplier(fishingrod_vertical);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Fishing-Rod Vertical multiplier knockbackProfile for " + type.name() + " has been updated: " + old + "-->" + fishingrod_vertical);
    }

    public void sendKnockbackInfo(CommandSender commandSender, LadderType type){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        ChatUtility.sendMessage(commandSender, knockbackProfile.toString());
    }
}
