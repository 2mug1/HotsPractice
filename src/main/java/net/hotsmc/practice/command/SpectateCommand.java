package net.hotsmc.practice.command;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.match.MatchState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class SpectateCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
            if (practicePlayer == null) return true;
            if (args.length == 1) {
                if (practicePlayer.isInMatch()) {
                    practicePlayer.sendMessage(ChatColor.RED + "You are playing the match.");
                    return true;
                }
                if (practicePlayer.isInParty()) {
                    practicePlayer.sendMessage(ChatColor.RED + "You are belong to the party.");
                    return true;
                }
                if (practicePlayer.isInEvent()) {
                    practicePlayer.sendMessage(ChatColor.RED + "You are playing the event.");
                    return true;
                }
                if (practicePlayer.isEnableKitEdit()) {
                    practicePlayer.sendMessage(ChatColor.RED + "You are editing a ladder.");
                    return true;
                }
                if (practicePlayer.isEnableSpectate()) {
                    practicePlayer.sendMessage(ChatColor.RED + "You are spectating.");
                    return true;
                }
                String targetName = args[0];
                if (targetName.equalsIgnoreCase(player.getName())) {
                    practicePlayer.sendMessage(ChatColor.RED + "Can't spectate yourself.");
                    return true;
                }
                Player target = Bukkit.getPlayer(targetName);
                if (target == null) {
                    practicePlayer.sendMessage(ChatColor.RED + "Can't find player.");
                    return true;
                }
                PracticePlayer targetPlayer = HotsPractice.getPracticePlayer(target);
                if (targetPlayer == null) {
                    practicePlayer.sendMessage(ChatColor.RED + "Can't find player.");
                    return true;
                }
                if (!targetPlayer.getPlayerData().isAllowingSpectators()) {
                    practicePlayer.sendMessage(ChatColor.RED + targetName + " isn't allowing spectators.");
                    return true;
                }
                if (targetPlayer.isInEvent()) {
                    player.sendMessage(ChatColor.RED + targetName + " is in " + targetPlayer.getInEventGame().getHost() + "'s event.");
                    return true;
                }
                if (targetPlayer.isInMatch()) {
                    Match match = targetPlayer.getInMatch();
                    if (match.getState() != MatchState.Playing) {
                        practicePlayer.sendMessage(ChatColor.RED + "Failed to spectate.");
                        return true;
                    }
                    match.addSpectator(practicePlayer);
                } else {
                    practicePlayer.sendMessage(ChatColor.RED + targetName + " isn't in a match");
                }
            } else {
                practicePlayer.sendMessage(ChatColor.RED + "/spectate <player>");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> toReturn = new LinkedList<>();
        for(PracticePlayer practicePlayer : HotsPractice.getPracticePlayers()){
            if(practicePlayer.isInMatch()){
                final Match match = practicePlayer.getInMatch();
                if(match.getState() == MatchState.Playing){
                    toReturn.add(practicePlayer.getName());
                }
            }
        }
        return toReturn;
    }
}
