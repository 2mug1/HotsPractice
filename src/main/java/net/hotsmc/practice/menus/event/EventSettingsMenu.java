package net.hotsmc.practice.menus.event;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.events.EventGameState;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EventSettingsMenu extends Menu {

    private EventGame eventGame;
    private PracticePlayer leader;

    public EventSettingsMenu(EventGame eventGame, PracticePlayer leader) {
        super(false);
        this.eventGame = eventGame;
        this.leader = leader;
    }

    @Override
    public String getTitle(Player player) {
        return "Event Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if(eventGame.getState() == EventGameState.WAITING_FOR_PLAYERS || eventGame.getState() == EventGameState.COUNTDOWN) {
            buttons.put(2, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Broadcast Event", Material.BEACON, false, ChatColor.GRAY + "Click to broadcast on server for this event.");
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    eventGame.broadcastEvent(leader);
                }
            });
        }
        if(eventGame.getState() == EventGameState.WAITING_FOR_PLAYERS) {
            buttons.put(6, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Start Event", Material.EMERALD_BLOCK, false, ChatColor.GRAY + "Click to start this event.");
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    eventGame.startCountdown(leader);
                }
            });
        }
        if(eventGame.getState() == EventGameState.COUNTDOWN) {
            buttons.put(6, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createItemStack("" + ChatColor.RED + ChatColor.BOLD + "Stop Countdown", Material.GOLD_BLOCK, false, ChatColor.GRAY + "Click to stop countdown.");
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();
                    eventGame.stopCountdown(leader);
                }
            });
        }
        return buttons;
    }
}
