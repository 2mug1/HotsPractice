package net.hotsmc.practice.command;

import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.match.MatchState;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GroundFixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            CraftPlayer cplayer = (CraftPlayer) player;
            //1.8は利用できない
            if (cplayer.getHandle().playerConnection.networkManager.getVersion() == 47) {
                ChatUtility.sendMessage(player, ChatColor.RED + "Ground Fix Command is unavailable 1.8");
                return true;
            }

            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
            if (practicePlayer == null) return true;

            if (practicePlayer.isInMatch()) {
                Match match = practicePlayer.getInMatch();
                if(match.getState() == MatchState.Teleporting || match.getState() == MatchState.EndGame){
                    practicePlayer.sendMessage(ChatColor.RED + "Failed to fix a player sync.");
                    return true;
                }
                for(PracticePlayer gamePlayer : match.getGamePlayers()){
                    if(gamePlayer != practicePlayer && !HotsCore.getHotsPlayer(gamePlayer.getPlayer()).isVanish()){
                        practicePlayer.hidePlayer(gamePlayer);
                        practicePlayer.showPlayer(gamePlayer);
                    }
                }
            } else {
                for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                    if (!all.isInMatch() && !HotsCore.getHotsPlayer(all.getPlayer()).isVanish()) {
                        if (all != practicePlayer) {
                            practicePlayer.hidePlayer(all);
                            practicePlayer.showPlayer(all);
                        }
                    }
                }
            }
            practicePlayer.sendMessage(ChatColor.GREEN + "Successfully fixed player sync.");
        }
        return true;
    }
}
