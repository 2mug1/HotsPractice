package net.hotsmc.practice.commands;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.core.player.PlayerRank;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.ArenaManager;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.NumberUtility;
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
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setlobby - Update lobby location");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice create <arena> - Create a new arena");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setdefaultspawn <arena> - Set arena spawn of default");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setspawn1 <arena> - Set arena spawn of 1");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setspawn2 <arena> - Set arena spawn of 2");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkit <ladder> - Update default ladder data from your inventory");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkitedit <ladder> - Update location of kit editor each ladder");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkbhor <ladder> <double> - Update horizontal multiplier for knockback");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice setkbver <ladder> <double> - Update vertical multiplier for knockback");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice kb <ladder> - View knockback info");
                ChatUtility.sendMessage(player, ChatColor.YELLOW + "/practice reload - Reload knockbacks and default ladders");
            }
            else if(args.length == 1){
                if(args[0].equalsIgnoreCase("setlobby")){
                    HotsPractice.getInstance().getMatchConfig().upddateLobbyLocation(player);
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    HotsPractice.getInstance().reload(sender);
                }
                return true;
            }
            else if(args.length == 2){
                ArenaManager arenaManager = HotsPractice.getInstance().getManagerHandler().getArenaManager();
                String arenaName = args[1];
                if(args[0].equalsIgnoreCase("create")){
                    arenaManager.createArenaData(arenaName, player);
                }
                else if(args[0].equalsIgnoreCase("setdefaultspawn")){
                    arenaManager.updateDefaultSpawn(arenaName, player);
                }
                else if(args[0].equalsIgnoreCase("setspawn1")){
                    arenaManager.updateSpawn(arenaName, player, 1);
                }
                else if(args[0].equalsIgnoreCase("setspawn2")){
                    arenaManager.updateSpawn(arenaName, player, 2);
                }
                else if(args[0].equalsIgnoreCase("setkit")){
                    HotsPractice.getInstance().getDefaultLadder().update(args[1], player);
                }
                else if(args[0].equalsIgnoreCase("setkitedit")) {
                    List<String> types = new ArrayList<>(LadderType.values().length);
                    for (LadderType ladderType : LadderType.values()) {
                        types.add(ladderType.name());
                    }
                    if (!types.contains(args[1])) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String type : types) {
                            stringBuilder.append(type).append(" ");
                        }
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid ladder type.");
                        ChatUtility.sendMessage(player, ChatColor.RED + "Ladders: " + stringBuilder);
                    } else {
                        HotsPractice.getInstance().getManagerHandler().getLadderEditManager().updateKitEditLocation(player, LadderType.valueOf(args[1]));
                    }
                }
                else if(args[0].equalsIgnoreCase("kb")){
                    List<String> types = new ArrayList<>(LadderType.values().length);
                    for (LadderType ladderType : LadderType.values()) {
                        types.add(ladderType.name());
                    }
                    if (!types.contains(args[1])) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String type : types) {
                            stringBuilder.append(type).append(" ");
                        }
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid ladder type.");
                        ChatUtility.sendMessage(player, ChatColor.RED + "Ladder: " + stringBuilder);
                    } else {
                        HotsPractice.getInstance().getManagerHandler().getKnockbackManager().sendKnockbackInfo(sender, LadderType.valueOf(args[1]));
                    }
                }
            }
            else if(args.length == 3){
                if(args[0].equalsIgnoreCase("setkbhor")){
                    List<String> types = new ArrayList<>(LadderType.values().length);
                    for (LadderType ladderType : LadderType.values()) {
                        types.add(ladderType.name());
                    }
                    if (!types.contains(args[1])) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String type : types) {
                            stringBuilder.append(type).append(" ");
                        }
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid ladder type.");
                        ChatUtility.sendMessage(player, ChatColor.RED + "Ladder: " + stringBuilder);
                    }
                    else if(NumberUtility.isDouble(args[2])){
                        HotsPractice.getInstance().getManagerHandler().getKnockbackManager().updateHorizontal(sender, LadderType.valueOf(args[1]), Double.parseDouble(args[2]));
                    }else{
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid multiplier.");
                    }
                }
                else if(args[0].equalsIgnoreCase("setkbver")){
                    List<String> types = new ArrayList<>(LadderType.values().length);
                    for (LadderType ladderType : LadderType.values()) {
                        types.add(ladderType.name());
                    }
                    if (!types.contains(args[1])) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String type : types) {
                            stringBuilder.append(type).append(" ");
                        }
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid ladder type.");
                        ChatUtility.sendMessage(player, ChatColor.RED + "Ladder: " + stringBuilder);
                    }
                    else if(NumberUtility.isDouble(args[2])){
                        HotsPractice.getInstance().getManagerHandler().getKnockbackManager().updateVertical(sender, LadderType.valueOf(args[1]), Double.parseDouble(args[2]));
                    }else{
                        ChatUtility.sendMessage(player, ChatColor.RED + "Invalid multiplier.");
                    }
                }
            }
        }
        return true;
    }
}
