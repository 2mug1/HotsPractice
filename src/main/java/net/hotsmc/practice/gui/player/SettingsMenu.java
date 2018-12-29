package net.hotsmc.practice.gui.player;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.other.Style;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.core.player.PlayerTime;
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

            if(playerData.isGlobalChat()){
                buttons.put(0, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Global Chat: " + Style.GREEN + "Enabled", Material.PAPER, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateGlobalChat(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Global Chat" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(0, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Global Chat: " + Style.RED + "Disabled", Material.PAPER, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateGlobalChat(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Global Chat" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(playerData.isPrivateMessages()){
                buttons.put(1, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Private Messages: " + Style.GREEN + "Enabled", Material.BOOK_AND_QUILL, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updatePrivateMessages(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Private Messages" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(1, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Private Messages: " + Style.RED + "Disabled", Material.BOOK_AND_QUILL, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updatePrivateMessages(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Private Messages" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(playerData.isSidebarVisibility()){
                buttons.put(2, new Button() {
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
                buttons.put(2, new Button() {
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
                buttons.put(3, new Button() {
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
                buttons.put(3, new Button() {
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
                buttons.put(4, new Button() {
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
                buttons.put(4, new Button() {
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
                            practicePlayer.sendMessage(Style.YELLOW + "Allowing Duels" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(playerData.isAllowingSpectators()){
                buttons.put(5, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Allowing Spectators: " + Style.GREEN + "Enabled", Material.REDSTONE_TORCH_ON, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateAllowingSpectators(false);
                            practicePlayer.sendMessage(Style.YELLOW + "Allowing Spectators" + Style.GRAY + " has been " + Style.RED + "Disabled");
                        }
                    }
                });
            }else{
                buttons.put(5, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(Style.YELLOW + "Allowing Spectators: " + Style.RED + "Disabled", Material.REDSTONE_TORCH_ON, false);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer != null){
                            practicePlayer.getPlayerData().updateAllowingSpectators(true);
                            practicePlayer.sendMessage(Style.YELLOW + "Allowing Spectators" + Style.GRAY + " has been " + Style.GREEN + "Enabled");
                        }
                    }
                });
            }

            if(practicePlayer.getHotsPlayer().getPlayerTime() == PlayerTime.DAY){
                buttons.put(6, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlass(Style.YELLOW + "Time Type: " + Style.AQUA + "Day", 1, 4);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer == null)return;
                        HotsPlayer hotsPlayer = practicePlayer.getHotsPlayer();
                        if(hotsPlayer == null)return;
                        hotsPlayer.changePlayerTime(PlayerTime.SUNSET);
                    }
                });
            }

            if(practicePlayer.getHotsPlayer().getPlayerTime() == PlayerTime.SUNSET){
                buttons.put(6, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlass(Style.YELLOW + "Time Type: " + Style.AQUA + "Sunset", 1, 1);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer == null)return;
                        HotsPlayer hotsPlayer = practicePlayer.getHotsPlayer();
                        if(hotsPlayer == null)return;
                        hotsPlayer.changePlayerTime(PlayerTime.NIGHT);
                    }
                });
            }

            if(practicePlayer.getHotsPlayer().getPlayerTime() == PlayerTime.NIGHT) {
                buttons.put(6, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlass(Style.YELLOW + "Time Type: " + Style.AQUA + "Night", 1, 11);
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if (practicePlayer == null) return;
                        HotsPlayer hotsPlayer = practicePlayer.getHotsPlayer();
                        if (hotsPlayer == null) return;
                        hotsPlayer.changePlayerTime(PlayerTime.DAY);
                    }
                });
            }
        }
        return buttons;
    }
}
