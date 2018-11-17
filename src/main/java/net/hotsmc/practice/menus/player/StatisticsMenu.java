package net.hotsmc.practice.menus.player;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsMenu extends Menu {

    private PlayerData playerData;

    public StatisticsMenu(PlayerData playerData){
        super(false);
        this.playerData = playerData;
    }

    @Override
    public String getTitle(Player player) {
        return "Stats - " + player.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Total Won: " + ChatColor.WHITE + playerData.getRankedWin());
                lore.add(ChatColor.GRAY + "Total Played: " + ChatColor.WHITE + playerData.getRankedPlayed());
                return ItemUtility.createItemStack("" +  ChatColor.YELLOW + ChatColor.BOLD + "Ranked Total", Material.PAPER, false, 1, lore);
            }
        });

        buttons.put(1, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GRAY + "NoDebuff: " + ChatColor.WHITE +playerData.getNodebuffRankedWin() + "/" + playerData.getNodebuffRankedPlayed());
                        lore.add(ChatColor.GRAY + "Debuff: " + ChatColor.WHITE +playerData.getDebuffRankedWin() + "/" + playerData.getDebuffRankedPlayed());
                        lore.add(ChatColor.GRAY + "MCSG: " + ChatColor.WHITE +playerData.getMcsgRankedWin() + "/" + playerData.getMcsgRankedPlayed());
                        lore.add(ChatColor.GRAY + "OCTC: " +ChatColor.WHITE + playerData.getOctcRankedWin() + "/" + playerData.getOctcRankedPlayed());
                        lore.add(ChatColor.GRAY + "Gapple: " +ChatColor.WHITE + playerData.getGappleRankedWin() + "/" + playerData.getGappleRankedPlayed());
                        lore.add(ChatColor.GRAY + "Archer: " + ChatColor.WHITE +playerData.getArcherRankedWin() + "/" + playerData.getArcherRankedPlayed());
                        lore.add(ChatColor.GRAY + "Combo: " + ChatColor.WHITE +playerData.getComboRankedWin() + "/" + playerData.getComboRankedPlayed());
                        lore.add(ChatColor.GRAY + "Soup: " + ChatColor.WHITE +playerData.getSoupRankedWin() + "/" + playerData.getSoupRankedPlayed());
                        lore.add(ChatColor.GRAY + "BuildUHC: " +ChatColor.WHITE + playerData.getBuilduhcRankedWin() + "/" + playerData.getBuilduhcRankedPlayed());
                        lore.add(ChatColor.GRAY + "Sumo: " + ChatColor.WHITE +playerData.getSumoRankedWin() + "/" + playerData.getSumoRankedPlayed());
                        lore.add(ChatColor.GRAY + "Axe: " + ChatColor.WHITE +playerData.getAxeRankedWin() + "/" + playerData.getAxeRankedPlayed());
                        lore.add(ChatColor.GRAY + "Spleef: " +ChatColor.WHITE + playerData.getSpleefRankedWin() + "/" + playerData.getSpleefRankedPlayed());
                        lore.add(ChatColor.GRAY + "GappleSG: " +ChatColor.WHITE + playerData.getGapplesgRankedWin() + "/" + playerData.getGapplesgRankedPlayed());

                        return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Ranked Win Ratio (Win/Total)", Material.PAPER, false, 1, lore);
                    }
                });

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "NoDebuff: " +ChatColor.WHITE + playerData.getNodebuffElo());
                lore.add(ChatColor.GRAY + "Debuff: " +ChatColor.WHITE + playerData.getDebuffElo());
                lore.add(ChatColor.GRAY + "MCSG: " +ChatColor.WHITE + playerData.getMcsgElo());
                lore.add(ChatColor.GRAY + "OCTC: " + ChatColor.WHITE +playerData.getOctcElo());
                lore.add(ChatColor.GRAY + "Gapple: " +ChatColor.WHITE + playerData.getGappleElo());
                lore.add(ChatColor.GRAY + "Archer: " +ChatColor.WHITE + playerData.getArcherElo());
                lore.add(ChatColor.GRAY + "Combo: " + ChatColor.WHITE +playerData.getComboElo());
                lore.add(ChatColor.GRAY + "Soup: " +ChatColor.WHITE + playerData.getSoupElo());
                lore.add(ChatColor.GRAY + "BuildUHC: " +ChatColor.WHITE + playerData.getBuilduhcElo());
                lore.add(ChatColor.GRAY + "Sumo: " + ChatColor.WHITE +playerData.getSumoElo());
                lore.add(ChatColor.GRAY + "Axe: " + ChatColor.WHITE +playerData.getAxeElo());
                lore.add(ChatColor.GRAY + "Spleef: " +ChatColor.WHITE + playerData.getSpleefElo());
                lore.add(ChatColor.GRAY + "GappleSG: " +ChatColor.WHITE + playerData.getGapplesgElo());

                return ItemUtility.createItemStack("" +  ChatColor.YELLOW + ChatColor.BOLD + "Ranked Elo", Material.PAPER, false, 1, lore);
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Total Won: " + ChatColor.WHITE + playerData.getUnraknedWin());
                lore.add(ChatColor.GRAY + "Total Played: " +ChatColor.WHITE + playerData.getUnrankedPlayed());
                return ItemUtility.createItemStack("" + ChatColor.WHITE + ChatColor.YELLOW + ChatColor.BOLD + "Unranked Total", Material.PAPER, false, 1, lore);
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "NoDebuff: " + ChatColor.WHITE +playerData.getNodebuffUnankedWin() + "/" + playerData.getNodebuffUnankedPlayed());
                lore.add(ChatColor.GRAY + "Debuff: " +ChatColor.WHITE + playerData.getDebuffUnankedWin() + "/" + playerData.getDebuffUnankedPlayed());
                lore.add(ChatColor.GRAY + "MCSG: " + ChatColor.WHITE +playerData.getMcsgUnankedWin() + "/" + playerData.getMcsgUnankedPlayed());
                lore.add(ChatColor.GRAY + "OCTC: " + ChatColor.WHITE +playerData.getOctcUnankedWin() + "/" + playerData.getOctcUnankedPlayed());
                lore.add(ChatColor.GRAY + "Gapple: " +ChatColor.WHITE + playerData.getGappleUnankedWin() + "/" + playerData.getGappleUnankedPlayed());
                lore.add(ChatColor.GRAY + "Archer: " +ChatColor.WHITE + playerData.getArcherUnankedWin() + "/" + playerData.getArcherUnankedPlayed());
                lore.add(ChatColor.GRAY + "Combo: " + ChatColor.WHITE +playerData.getComboUnankedWin() + "/" + playerData.getComboUnankedPlayed());
                lore.add(ChatColor.GRAY + "Soup: " + ChatColor.WHITE +playerData.getSoupUnankedWin() + "/" + playerData.getSoupUnankedPlayed());
                lore.add(ChatColor.GRAY + "BuildUHC: " + ChatColor.WHITE +playerData.getBuilduhcUnankedWin() + "/" + playerData.getBuilduhcUnankedPlayed());
                lore.add(ChatColor.GRAY + "Sumo: " + ChatColor.WHITE +playerData.getSumoUnankedWin() + "/" + playerData.getSumoUnankedPlayed());
                lore.add(ChatColor.GRAY + "Axe: " + ChatColor.WHITE +playerData.getAxeUnankedWin() + "/" + playerData.getAxeUnankedPlayed());
                lore.add(ChatColor.GRAY + "Spleef: " +ChatColor.WHITE + playerData.getSpleefUnankedWin() + "/" + playerData.getSpleefUnankedPlayed());
                lore.add(ChatColor.GRAY + "GappleSG: " + ChatColor.WHITE +playerData.getGapplesgUnankedWin() + "/" + playerData.getGapplesgUnankedPlayed());

                return ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Unranked Win Ratio (Win/Total)", Material.PAPER, false, 1, lore);
            }
        });


        return buttons;
    }
}
