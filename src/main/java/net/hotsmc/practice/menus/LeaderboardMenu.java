package net.hotsmc.practice.menus;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.MongoConnection;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardMenu extends Menu {

    public LeaderboardMenu(){
        super(true);
        setUpdateInterval(20*60);
    }

    @Override
    public String getTitle(Player player) {
        return "LeaderBoard";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        MongoConnection mongo = HotsPractice.getMongoConnection();

        for(int i = 0; i < 9; i++){
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
                }
            });
        }
        for(int i = 27; i < 36; i++){
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return net.hotsmc.core.utility.ItemUtility.createGlassPane(" ", 1, 3);
                }
            });
        }
        buttons.put(10, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createSplashPotion(
                                ChatColor.GREEN + "NoDebuff",
                                1, PotionType.INSTANT_HEAL, mongo.getTop10("NODEBUFF_ELO"));
                    }
                });


        buttons.put(11, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createSplashPotion(
                                ChatColor.GREEN + "Debuff",
                                1, PotionType.POISON, mongo.getTop10("DEBUFF_ELO"));
                    }
                });

        buttons.put(12, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false, 1, mongo.getTop10("MCSG_ELO"));
                    }
                });



        buttons.put(13, new Button() {
                @Override
                public ItemStack getButtonItem (Player player){
                    return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false, 1, mongo.getTop10("OCTC_ELO"));
                }
            });


        buttons.put(14, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple", 1, mongo.getTop10("GAPPLE_ELO"));
                    }
                });



        buttons.put(15, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false, 1, mongo.getTop10("ARCHER_ELO"));
                    }
                });


        buttons.put(16, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createFish(ChatColor.GREEN + "Combo", 1, 3, mongo.getTop10("COMBO_ELO"));
                    }
                });



        buttons.put(19, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false, 1, mongo.getTop10("SOUP_ELO"));
                    }
                });


        buttons.put(20, new Button() {
                @Override
                public ItemStack getButtonItem (Player player){
                    return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false, 1, mongo.getTop10("BUILDUHC_ELO"));
                }
            });


        buttons.put(21, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "Sumo", Material.GRILLED_PORK, false, 1, mongo.getTop10("SUMO_ELO"));
                    }
                });


        buttons.put(22, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false, 1, mongo.getTop10("AXE_ELO"));
                    }
                });


        buttons.put(23, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false, 1, mongo.getTop10("SPLEEF_ELO"));
                    }
                });



        buttons.put(24, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false, 1, mongo.getTop10("GAPPLESG_ELO"));
            }
        });

        buttons.put(25, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Point", Material.GOLD_INGOT, false, 1, mongo.getTop10("POINT"));
            }
        });

        return buttons;
    }
}
