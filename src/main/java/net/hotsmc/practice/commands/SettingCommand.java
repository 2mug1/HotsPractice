package net.hotsmc.practice.commands;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.core.player.PlayerRank;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.ArenaManager;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SettingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            HotsPlayer hotsPlayer = HotsCore.getHotsPlayer(player);
            //権限レベル確認
            if (PlayerRank.Administrator.getPermissionLevel() > hotsPlayer.getPlayerRank().getPermissionLevel()) {
                ChatUtility.sendMessage(player, ChatColor.RED + "You don't have permission.");
                return true;
            }
            if(args.length == 0){
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setlobby");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice create <arena>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setdefaultspawn <arena>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setspawn1 <arena>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setspawn2 <arena>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkit <kit_type>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkitedit <kit_type>");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "Kits: " + "debuff, nodebuff, mcsg, octc, gapple, archer, combo, soup, builduhc, axe, gapplesg");
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("setlobby")){
                    HotsPractice.getGameConfig().setLobbyLocation(player);
                }
                return true;
            }
            if(args.length == 2){
                ArenaManager arenaManager = HotsPractice.getArenaManager();
                String arenaName = args[1];
                if(args[0].equalsIgnoreCase("create")){
                    arenaManager.createArenaData(arenaName, player);
                }
                if(args[0].equalsIgnoreCase("setdefaultspawn")){
                    arenaManager.updateDefaultSpawn(arenaName, player);
                }
                if(args[0].equalsIgnoreCase("setspawn1")){
                    arenaManager.updateSpawn(arenaName, player, 1);
                }
                if(args[0].equalsIgnoreCase("setspawn2")){
                    arenaManager.updateSpawn(arenaName, player, 2);
                }
                if(args[0].equalsIgnoreCase("setkit")){
                    HotsPractice.getDefaultKit().update(args[1], player);
                }
                if(args[0].equalsIgnoreCase("setkitedit")){
                    List<String> types = new ArrayList<>(KitType.values().length);
                    for(KitType kitType : KitType.values()){
                        types.add(kitType.name());
                    }
                    if(!types.contains(args[1])){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(String type : types){
                            stringBuilder.append(type).append(" ");
                        }
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid kit type.");
                        ChatUtility.sendMessage(player, ChatColor.RED + "Kit Type: " + stringBuilder);
                        return true;
                    }
                    HotsPractice.getKitEditManager().updateKitEditLocation(player, KitType.valueOf(args[1]));
                }
            }
        }
        return true;
    }
}
