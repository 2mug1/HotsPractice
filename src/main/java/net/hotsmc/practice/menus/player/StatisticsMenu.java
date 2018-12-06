package net.hotsmc.practice.menus.player;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.other.Style;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.MongoConnection;
import net.hotsmc.practice.player.PlayerData;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.*;

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

        for(int i = 0; i < 54; i++){
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 15);
                }
            });
        }

        MongoConnection mongo = HotsPractice.getInstance().getMongoConnection();

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(Style.SCOREBAORD_SEPARATOR);
                lore.add(ChatColor.GRAY + "Total Won: " + ChatColor.WHITE + playerData.getUnraknedWin());
                lore.add(ChatColor.GRAY + "Total Played: " +ChatColor.WHITE + playerData.getUnrankedPlayed());
                lore.add(Style.SCOREBAORD_SEPARATOR);
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
                lore.add(Style.SCOREBAORD_SEPARATOR);
                return ItemUtility.createItemStack("" + ChatColor.WHITE + ChatColor.YELLOW + ChatColor.BOLD + "Unranked", Material.IRON_SWORD, false, 1, lore);
            }
        });

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(Style.SCOREBAORD_SEPARATOR);
                lore.add(ChatColor.GRAY + "Points: " + ChatColor.WHITE + playerData.getPoint());
                lore.add(Style.SCOREBAORD_SEPARATOR);
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
                lore.add(Style.SCOREBAORD_SEPARATOR);
                return ItemUtility.createPlayerNameSkull(player.getName(), HotsCore.getHotsPlayer(player).getColorName(), lore);
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(Style.SCOREBAORD_SEPARATOR);
                lore.add(ChatColor.GRAY + "Total Won: " + ChatColor.WHITE + playerData.getRankedWin());
                lore.add(ChatColor.GRAY + "Total Played: " + ChatColor.WHITE + playerData.getRankedPlayed());
                lore.add(Style.SCOREBAORD_SEPARATOR);
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
                lore.add(Style.SCOREBAORD_SEPARATOR);
                return ItemUtility.createItemStack("" +  ChatColor.YELLOW + ChatColor.BOLD + "Ranked", Material.DIAMOND_SWORD, false, 1, lore);
            }
        });

        buttons.put(28, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff",
                        1, PotionType.INSTANT_HEAL, mongo.getTop10("NODEBUFF_ELO"));
            }
        });


        buttons.put(29, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff",
                        1, PotionType.POISON, mongo.getTop10("DEBUFF_ELO"));
            }
        });

        buttons.put(30, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false, 1, mongo.getTop10("MCSG_ELO"));
            }
        });



        buttons.put(31, new Button() {
            @Override
            public ItemStack getButtonItem (Player player){
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false, 1, mongo.getTop10("OCTC_ELO"));
            }
        });


        buttons.put(32, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple", 1, mongo.getTop10("GAPPLE_ELO"));
            }
        });



        buttons.put(33, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false, 1, mongo.getTop10("ARCHER_ELO"));
            }
        });


        buttons.put(34, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", 1, 3, mongo.getTop10("COMBO_ELO"));
            }
        });



        buttons.put(37, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false, 1, mongo.getTop10("SOUP_ELO"));
            }
        });


        buttons.put(38, new Button() {
            @Override
            public ItemStack getButtonItem (Player player){
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false, 1, mongo.getTop10("BUILDUHC_ELO"));
            }
        });


        buttons.put(39, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Sumo", Material.LEASH, false, 1, mongo.getTop10("SUMO_ELO"));
            }
        });


        buttons.put(40, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false, 1, mongo.getTop10("AXE_ELO"));
            }
        });


        buttons.put(41, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false, 1, mongo.getTop10("SPLEEF_ELO"));
            }
        });



        buttons.put(42, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false, 1, mongo.getTop10("GAPPLESG_ELO"));
            }
        });

        buttons.put(43, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Point", Material.GOLD_INGOT, false, 1, mongo.getTop10("POINT"));
            }
        });


        return buttons;
    }
}
