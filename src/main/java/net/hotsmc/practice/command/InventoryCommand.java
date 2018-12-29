package net.hotsmc.practice.command;

import net.hotsmc.practice.gui.match.MatchDetailsMenu;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 1){
                String uuid = args[0];
                MatchDetailsMenu menu = MatchDetailsMenu.getMenus().get(UUID.fromString(uuid));
                if(menu == null){
                    ChatUtility.sendMessage(player, ChatColor.RED + "Not found data");
                }else{
                    menu.openMenu(player, 45);
                }
            }
        }
        return true;
    }
}
