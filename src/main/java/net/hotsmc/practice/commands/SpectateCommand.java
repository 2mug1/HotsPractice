package net.hotsmc.practice.commands;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.games.Game;
import net.hotsmc.practice.game.games.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
            if(practicePlayer == null)return true;
            if(args.length == 1){
                if(practicePlayer.isInGame()){
                    practicePlayer.sendMessage(ChatColor.RED + "You are playing the game.");
                    return true;
                }
                if(practicePlayer.isInParty()){
                    practicePlayer.sendMessage(ChatColor.RED + "You are belong to the party.");
                    return true;
                }
                if(practicePlayer.isInEvent()){
                    practicePlayer.sendMessage(ChatColor.RED + "You are playing the event.");
                    return true;
                }
                if(practicePlayer.isEnableKitEdit()){
                    practicePlayer.sendMessage(ChatColor.RED + "You are editing a kit.");
                    return true;
                }
                if(practicePlayer.isEnableSpectate()){
                    practicePlayer.sendMessage(ChatColor.RED + "You are spectating.");
                    return true;
                }
                String targetName = args[0];
                if(targetName.equalsIgnoreCase(player.getName())){
                    practicePlayer.sendMessage(ChatColor.RED + "Can't spectate yourself.");
                    return true;
                }
                Player target = Bukkit.getPlayer(targetName);
                if(target == null){
                    practicePlayer.sendMessage(ChatColor.RED + "Can't find player.");
                    return true;
                }
                PracticePlayer targetPlayer = HotsPractice.getPracticePlayer(target);
                if(targetPlayer == null){
                    practicePlayer.sendMessage(ChatColor.RED + "Can't find player.");
                    return true;
                }
                if(targetPlayer.isInEvent()){
                    player.sendMessage(ChatColor.RED + targetName + " is in " + targetPlayer.getInEventGame().getEventName() + "'s event.");
                    return true;
                }
                if(targetPlayer.isInGame()){
                    Game game = targetPlayer.getInGame();
                    if(game.getState() == GameState.Teleporting || game.getState() == GameState.PreGame || game.getState() == GameState.EndGame){
                        practicePlayer.sendMessage(ChatColor.RED + "Failed to spectate.");
                        return true;
                    }
                    game.addSpectator(practicePlayer);
                }else{
                    practicePlayer.sendMessage(ChatColor.RED + targetName + " isn't in a game");
                }
            }else{
                practicePlayer.sendMessage(ChatColor.RED + "/spectate <player>");
            }
        }
        return false;
    }
}