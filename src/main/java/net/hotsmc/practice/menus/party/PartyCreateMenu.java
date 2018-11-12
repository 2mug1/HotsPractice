package net.hotsmc.practice.menus.party;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyType;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartyCreateMenu extends Menu {

    public PartyCreateMenu() {
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Create Party";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < 9; i++){
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
                }
            });
        }
        for(int i = 18; i < 27; i++){
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
                }
            });
        }
        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
            }
        });
        buttons.put(17, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
            }
        });

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.BOLD + ChatColor.YELLOW + "Public Party", Material.WOOD_DOOR,
                        false, ChatColor.WHITE + "Not requires invitation", ChatColor.WHITE + "参加自由パーティを作成します");
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer leader = HotsPractice.getDuelPlayer(player);
                if (leader != null) {
                    HotsPractice.getPartyManager().addParty(new Party(PartyType.Public, leader));
                    leader.setPartyClickItems();
                    leader.startPartyScoreboard();
                    leader.sendMessage(ChatColor.GRAY + "You have created a new " + ChatColor.YELLOW + ChatColor.BOLD + "Public Party");
                }
            }
        });

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.BOLD + ChatColor.YELLOW + "Public Party List", Material.EYE_OF_ENDER,
                        false, ChatColor.WHITE + "Click to view list of public party", ChatColor.WHITE + "参加自由パーティを表示します");
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                HotsPractice.getPublicPartyListMenu().openMenu(player, 54);
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.BOLD + ChatColor.YELLOW + "Private Party", Material.IRON_DOOR,
                        false, ChatColor.WHITE + "Requires invitation", ChatColor.WHITE + "招待限定パーティを作成します");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer leader = HotsPractice.getDuelPlayer(player);
                if (leader != null) {
                    HotsPractice.getPartyManager().addParty(new Party(PartyType.Private, leader));
                    leader.setPartyClickItems();
                    leader.startPartyScoreboard();
                    leader.sendMessage(ChatColor.GRAY + "You have created a new " + ChatColor.YELLOW + ChatColor.BOLD + "Private Party");
                }
            }
        });

        return buttons;
    }
}
