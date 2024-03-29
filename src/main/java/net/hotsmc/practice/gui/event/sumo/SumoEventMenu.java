package net.hotsmc.practice.gui.event.sumo;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.core.player.PlayerRank;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SumoEventMenu extends Menu {


    public SumoEventMenu() {
        super(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Sumo Event";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Create Sumo Event", Material.LEASH, false);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                HotsPlayer hotsPlayer = HotsCore.getHotsPlayer(player);
                if (practicePlayer == null || hotsPlayer == null) return;
                if (hotsPlayer.getPlayerRank().getPermissionLevel() < PlayerRank.Gold.getPermissionLevel()) {
                    practicePlayer.sendMessage(ChatColor.RED + "Requires you have" + ChatColor.GOLD + " Gold Rank "  + ChatColor.RED  + "to create event.");
                    return;
                }
                if(HotsPractice.getInstance().getManagerHandler().getEventManager().getEvents().size() >= 1){
                    practicePlayer.sendMessage(ChatColor.RED + "Currently the other Event is running.");
                    return;
                }
                if(!HotsPractice.getInstance().getManagerHandler().getEventManager().getEventCooldown().hasExpired()){
                    practicePlayer.sendMessage(ChatColor.RED + "Currently event cooldown time is active.");
                    return;
                }
                if(practicePlayer.hasHoldingEventGame()){
                    practicePlayer.sendMessage(ChatColor.RED + "You have already been hosting event.");
                    return;
                }
                practicePlayer.clearInventory();
                SumoEvent sumoEventGame = new SumoEvent(HotsPractice.getInstance().getArenaFactory().create(LadderType.Sumo), practicePlayer);
                sumoEventGame.init(practicePlayer);
            }
        });
        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Sumo Events", Material.EYE_OF_ENDER, false);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                HotsPractice.getInstance().getMenuHandler().getSumoEventListMenu().openMenu(player, 9);
            }
        });
        return buttons;
    }
}
