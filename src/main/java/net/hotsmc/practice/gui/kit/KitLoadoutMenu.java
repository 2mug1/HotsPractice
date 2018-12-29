package net.hotsmc.practice.gui.kit;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.ladder.PlayerLadder;
import net.hotsmc.practice.player.PracticePlayer;
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

    private PlayerLadder playerLadder;

    public KitLoadoutMenu(PlayerLadder playerLadder) {
        super(false);
        this.playerLadder = playerLadder;
    }

    @Override
    public String getTitle(Player player) {
        return playerLadder.getLadderType().name() + " Loadouts";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        //メインボタン
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.GREEN + ChatColor.BOLD + "Load", 1, 13);
            }
        });
        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.YELLOW + ChatColor.BOLD + "Save", 1, 4);
            }
        });
        buttons.put(18, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.AQUA + ChatColor.BOLD + "Rename", 1, 3);
            }
        });
        buttons.put(27, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createWool("" + ChatColor.RED + ChatColor.BOLD + "Delete", 1, 14);
            }
        });

        //デフォルトキットのボタン
        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GREEN + "Load kit", 1, 13, ChatColor.GRAY + "Default kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).setDefaultKit(playerLadder.getLadderType());
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
            }
        });
        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GRAY + "This kit cannot be changed", 1, 7, ChatColor.GRAY + "Default kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.playSound(player.getLocation(), Sound.BLAZE_HIT, 0.5F, 0.7F);
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
        for (int i = 11; i < 18; i++) {
            int finalSaveIndex = saveIndex;
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.YELLOW + "Save kit", 1, 4, ChatColor.GRAY + "Loadout #" + finalSaveIndex);
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    playerLadder.save(finalSaveIndex, player);
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                    player.closeInventory();
                    openMenu(player, 45);
                }
            });
            saveIndex++;
        }

        //読み込みと名前変更と削除ボタン
        int loadSlot = 2;
        int renameSlot = 20;
        int deleteSlot = 29;
        for (int i = 0; i < 7; i++) {
            if (playerLadder.getLadders()[i] != null) {
                int finalI = i;
                buttons.put(loadSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.GREEN + "Load kit", 1, 13, ChatColor.GRAY + "Loadout #" + finalI + (playerLadder.getLadders()[finalI].isRenamed() ? " (" + playerLadder.getLadders()[finalI].getName() + ")" : ""));
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        playerLadder.setKit(finalI,  player);
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                    }
                });
                buttons.put(renameSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.AQUA + "Rename kit", 1, 3, ChatColor.GRAY + "Loadout #" + finalI + (playerLadder.getLadders()[finalI].isRenamed() ? " (" + playerLadder.getLadders()[finalI].getName() + ")" : ""));
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();
                        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                        if(practicePlayer == null)return;
                        practicePlayer.setRenamingKitIndex(finalI);
                        practicePlayer.sendMessage(ChatColor.YELLOW + "Please enter a kit name for #" + finalI + "...");
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5F, 0.7F);
                    }
                });
                buttons.put(deleteSlot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return net.hotsmc.core.utility.ItemUtility.createGlassPane(ChatColor.RED + "Delete kit", 1, 14, ChatColor.GRAY + "Loadout #" + finalI + (playerLadder.getLadders()[finalI].isRenamed() ? " (" + playerLadder.getLadders()[finalI].getName() + ")" : ""));
                    }

                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        playerLadder.delete(finalI, player.getUniqueId().toString());
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
                buttons.put(renameSlot, new Button() {
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
            renameSlot++;
            deleteSlot++;
        }
        return buttons;
    }
}