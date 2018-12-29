package net.hotsmc.practice.gui.event;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EventSettingsMenu extends Menu {

    private Event event;
    private PracticePlayer leader;

    public EventSettingsMenu(Event event, PracticePlayer leader) {
        super(false);
        this.event = event;
        this.leader = leader;
    }

    @Override
    public String getTitle(Player player) {
        return "Event Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if(event.getState() == EventState.WAITING_FOR_PLAYERS || event.getState() == EventState.COUNTDOWN) {
            buttons.put(2, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Broadcast Event", Material.BEACON, false, ChatColor.GRAY + "Click to broadcast on server for this event.");
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    event.broadcastEvent(leader);
                }
            });
        }
        if(event.getState() == EventState.WAITING_FOR_PLAYERS) {
            buttons.put(6, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Start Event", Material.EMERALD_BLOCK, false, ChatColor.GRAY + "Click to start this event.");
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    event.startCountdown(leader);
                }
            });
        }
        if(event.getState() == EventState.COUNTDOWN) {
            buttons.put(6, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.RED + ChatColor.BOLD + "Stop Countdown", Material.GOLD_BLOCK, false, ChatColor.GRAY + "Click to stop countdown.");
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    event.stopCountdown(leader);
                }
            });
        }
        return buttons;
    }
}
