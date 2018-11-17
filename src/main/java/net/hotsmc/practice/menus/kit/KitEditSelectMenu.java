package net.hotsmc.practice.menus.kit;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KitEditSelectMenu extends Menu {

    public KitEditSelectMenu() {
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Select a kit to edit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff", 1, PotionType.INSTANT_HEAL, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.NoDebuff);
            }
        });

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff", 1, PotionType.POISON, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Debuff);
            }
        });


        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false, 1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.MCSG);
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.OCTC);
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Gapple);
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Archer);
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", 1, 3, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Combo);
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Soup);
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.BuildUHC);
            }
        });


        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.Axe);
            }
        });


        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,1, ChatColor.YELLOW + "Click to edit the your kit");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Objects.requireNonNull(HotsPractice.getPracticePlayer(player)).enableKitEdit(KitType.GappleSG);
            }
        });
        return buttons;
    }
}
