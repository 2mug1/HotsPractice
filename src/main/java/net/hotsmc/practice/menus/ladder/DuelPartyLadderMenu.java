package net.hotsmc.practice.menus.ladder;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class DuelPartyLadderMenu extends Menu {

    private Party opponentParty;

    public DuelPartyLadderMenu(Party opponentParty){
        super(false);
        this.opponentParty = opponentParty;
    }


    @Override
    public String getTitle(Player player) {
        return "Party Duel Ladder";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff",
                        1, PotionType.INSTANT_HEAL);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.NoDebuff);
                    }
                }
            }
        });

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff",
                        1, PotionType.POISON);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Debuff);
                    }
                }
            }
        });


        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false,
                        1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.MCSG);
                    }
                }
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.OCTC);
                    }
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",1);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Gapple);
                    }
                }
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Archer);
                    }
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo",1, 3);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Combo);
                    }
                }
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Soup);
                    }
                }
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.BuildUHC);
                    }
                }
            }
        });

        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Axe);
                    }
                }
            }
        });

        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.Spleef);
                    }
                }
            }
        });

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,1);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()){
                        opponentParty.addDuelPartyRequest(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer), KitType.GappleSG);
                    }
                }
            }
        });
        return buttons;
    }
}
