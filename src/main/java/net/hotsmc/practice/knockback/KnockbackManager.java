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
        return new KnockbackProfile(configCursor, ladderType, configCursor.getDouble("horizontal"), configCursor.getDouble("vertical"));
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

    public void sendKnockbackInfo(CommandSender commandSender, LadderType type){
        KnockbackProfile knockbackProfile = getKnockbackByLadderType(type);
        ChatUtility.sendMessage(commandSender, knockbackProfile.toString());
    }
}
