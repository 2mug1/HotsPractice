package net.hotsmc.practice.menus.kit;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.kit.PlayerKitData;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KitLoadoutMenu extends Menu {

    private PlayerKitData playerKitData;

    public KitLoadoutMenu(PlayerKitData playerKitData) {
        super(false);
        this.playerKitData = playerKitData;
    }

    @Override
    public String getTitle(Player player) {
        return playerKitData.getKitType().name() + " Loadouts";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        //メインボタン
        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.GREEN + ChatColor.BOLD + "Load", 1, 13);
            }
        });
        buttons.put(18, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.YELLOW + ChatColor.BOLD + "Save", 1, 4);
            }
        });
        buttons.put(27, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.RED + ChatColor.BOLD + "Delete", 1, 14);
            }
        });

        //デフォルトキットのボタン
        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GREEN + "Load kit", 1, 13, ChatColor.GRAY + "Default kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).setDefaultKit(playerKitData.getKitType());
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
            }
        });
        buttons.put(19, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GRAY + "This kit cannot be changed", 1, 7, ChatColor.GRAY + "Default kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.playSound(player.getLocation(), Sound.BLAZE_HIT, 0.5F, 0.7F);
            }
        });
        buttons.put(28, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GRAY + "This kit cannot be changed", 1, 7, ChatColor.GRAY + "Default kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.playSound(player.getLocation(), Sound.BLAZE_HIT, 0.5F, 0.7F);
            }
        });

        //保存ボタン
        int saveIndex = 0;
        for (int i = 20; i < 27; i++) {
            int finalSaveIndex = saveIndex;
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.YELLOW + "Save kit", 1, 4, ChatColor.GRAY + "Loadout #" + finalSaveIndex);
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    playerKitData.save(finalSaveIndex, player);
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                    player.closeInventory();
                    openMenu(player, 45);
                }
            });
            saveIndex++;
        }

        //読み込みと削除ボタン
        int loadSlot = 11;
        int deleteSlot = 29;
        for (int i = 0; i < 7; i++) {
            if (playerKitData.getKitDataList().get(i) != null) {
                int finalI = i;
                buttons.put(loadSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GREEN + "Load kit", 1, 13, ChatColor.GRAY + "Loadout #" + finalI);
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        playerKitData.setKit(finalI,  player);
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                    }
                });
                buttons.put(deleteSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.RED + "Delete kit", 1, 14, ChatColor.GRAY + "Loadout #" + finalI);
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        playerKitData.delete(finalI, player.getUniqueId().toString());
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                        player.closeInventory();
                        openMenu(player, 45);
                    }
                });
            } else {
                int finalI1 = i;
                buttons.put(loadSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GRAY + "No kit saved", 1, 7, ChatColor.GRAY + "Loadout #" + finalI1);
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.playSound(player.getLocation(), Sound.BLAZE_HIT, 0.5F, 0.7F);
                    }
                });
                buttons.put(deleteSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GRAY + "No kit saved", 1, 7, ChatColor.GRAY + "Loadout #" + finalI1);
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.playSound(player.getLocation(), Sound.BLAZE_HIT, 0.5F, 0.7F);
                    }
                });
            }
            loadSlot++;
            deleteSlot++;
        }
        return buttons;
    }
}