package net.hotsmc.practice.knockback;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.ladder.Ladder;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KnockbackManager {

    private List<Knockback> knockbacks = new ArrayList<>();

    public Knockback getKnockbackByLadderType(LadderType ladderType){
        for(Knockback knockback : knockbacks){
            if(knockback.getLadderType() == ladderType){
                return knockback;
            }
        }
        return null;
    }

    private Knockback load(LadderType ladderType, ConfigCursor configCursor){
        return new Knockback(configCursor, ladderType, configCursor.getDouble("horizontal"), configCursor.getDouble("vertical"));
    }

    public void loadKnockbacks(){
        knockbacks.clear();
        for(LadderType ladderType : LadderType.values()){
            Knockback knockback = load(ladderType, new ConfigCursor(new FileConfig(new File(HotsPractice.getInstance().getDataFolder().getPath() + "/knockback/" + ladderType.name() + ".yml")), "Multiplier"));
            knockbacks.add(knockback);
            HotsPractice.getInstance().getLogger().info("Loaded Knockback Data: " + ladderType.name() + " - Horizontal: " + knockback.getHorizontalMultiplier() + " Vertical: " + knockback.getVerticalMultiplier());
        }
    }

    public void updateHorizontal(CommandSender commandSender, LadderType type, double horizontal){
        Knockback knockback = getKnockbackByLadderType(type);
        double old = knockback.getHorizontalMultiplier();
        ConfigCursor cursor = knockback.getCursor();
        cursor.set("horizontal", horizontal);
        cursor.save();
        knockback.setHorizontalMultiplier(horizontal);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Horizontal multiplier knockback for " + type.name() + " has been updated: " + old + "-->" + horizontal);
    }

    public void updateVertical(CommandSender commandSender, LadderType type, double vertical){
        Knockback knockback = getKnockbackByLadderType(type);
        double old = knockback.getVerticalMultiplier();
        ConfigCursor cursor = knockback.getCursor();
        cursor.set("vertical", vertical);
        cursor.save();
        knockback.setVerticalMultiplier(vertical);
        ChatUtility.sendMessage(commandSender, ChatColor.YELLOW + "Vertical multiplier knockback for " + type.name() + " has been updated: " + old + "-->" + vertical);
    }

    public void sendKnockbackInfo(CommandSender commandSender, LadderType type){
        Knockback knockback = getKnockbackByLadderType(type);
        ChatUtility.sendMessage(commandSender, knockback.toString());
    }
}
