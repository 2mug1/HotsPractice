package net.hotsmc.practice.commands;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.events.EventGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);

            if (practicePlayer == null) {
                return true;
            }

            //event join <event>
            if(args.length == 2){
                if (args[0].equalsIgnoreCase("join")) {
                    String eventName = args[1];
                    EventGame eventGame = HotsPractice.getEventGameManager().getEventGameByEventName(eventName);
                    if(eventGame == null){
                        practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "Not found event.");
                        return true;
                    }
                    if(practicePlayer.isInGame()){
                        practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You can't join the event.");
                        return true;
                    }
                    if(practicePlayer.isInParty()){
                        practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You can't join the event.");
                        return true;
                    }
                    if(practicePlayer.isEnableSpectate()){
                        practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You can't join the event.");
                        return true;
                    }
                    if(practicePlayer.isEnableKitEdit()){
                        practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You can't join the event.");
                        return true;
                    }
                    eventGame.addPlayer(practicePlayer);
                }
            }
        }
        return true;
    }
}
