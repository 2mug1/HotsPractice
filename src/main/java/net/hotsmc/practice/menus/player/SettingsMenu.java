package net.hotsmc.practice.menus.player;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.other.Style;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PlayerData;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Menu {

    public SettingsMenu() {
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if(practicePlayer != null){
            PlayerData playerData = practicePlayer.getPlayerData();
            if(playerData.isSidebarVisibility()){
                buttons.put(0, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Sidebar Visibility: " + Style.GREEN + "Enabled", Material.EMPTY_MAP, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateSidebarVisibility(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Sidebar Visibility" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(0, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Sidebar Visibility: " + Style.RED + "Disabled", Material.EMPTY_MAP, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateSidebarVisibility(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Sidebar Visibility" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(playerData.isSidebarPingVisibility()){
                buttons.put(1, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Sidebar Ping Visibility: " + Style.GREEN + "Enabled", Material.NETHER_STAR, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateSidebarPingVisibility(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Sidebar Ping Visibility" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(1, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Sidebar Ping Visibility: " + Style.RED + "Disabled", Material.NETHER_STAR, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateSidebarPingVisibility(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Sidebar Ping Visibility" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(playerData.isAllowingDuels()){
                buttons.put(2, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Allowing Duels: " + Style.GREEN + "Enabled", Material.DIAMOND_SWORD, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateAllowingDuels(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Allowing Duels" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(2, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Allowing Duels: " + Style.RED + "Disabled", Material.DIAMOND_SWORD, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateAllowingDuels(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Allowing Duels " + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }
        }
        return buttons;
    }
}
