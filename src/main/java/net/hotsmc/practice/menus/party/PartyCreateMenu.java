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

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Create Public Party", Material.WOOD_DOOR,
                        false, ChatColor.GRAY + "Not requires invitation.");
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer leader = HotsPractice.getPracticePlayer(player);
                if (leader != null) {
                    HotsPractice.getPartyManager().addParty(new Party(PartyType.Public, leader));
                    leader.setPartyClickItems();
                    leader.startPartyScoreboard();
                    leader.sendMessage(ChatColor.GRAY + "You have created a new " + ChatColor.YELLOW + ChatColor.BOLD + "Public Party");
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Public Parties", Material.EYE_OF_ENDER, false);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                HotsPractice.getPublicPartyListMenu().openMenu(player, 54);
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Create Private Party", Material.IRON_DOOR,
                        false, ChatColor.GRAY + "Requires invitation.");
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer leader = HotsPractice.getPracticePlayer(player);
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
