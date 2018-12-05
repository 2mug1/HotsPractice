package net.hotsmc.practice.menus.event;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EventMenu extends Menu {

    public EventMenu() {
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Event";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Sumo Event", Material.LEASH, false);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                HotsPractice.getInstance().getMenuHandler().getSumoEventMenu().openMenu(player, 9);
            }
        });
        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        buttons.put(12, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        buttons.put(14, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        buttons.put(16, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "?", Material.LADDER, false);
            }
        });
        return buttons;
    }
}
