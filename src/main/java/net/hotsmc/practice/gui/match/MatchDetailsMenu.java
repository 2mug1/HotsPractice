package net.hotsmc.practice.gui.match;

import lombok.Getter;
import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.utility.InventoryUtility;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.utility.ItemUtility;
import net.hotsmc.practice.utility.NumberUtility;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.*;

@Getter
public class MatchDetailsMenu extends Menu {

    @Getter
    private static Map<UUID, MatchDetailsMenu> menus = new LinkedHashMap<>();

    private String name;
    private ItemStack[] items;
    private ItemStack[] armors;
    private double health;
    private double food;
    private String game_duration_time;
    private Collection<PotionEffect> potionEffects;

    public MatchDetailsMenu(String name, ItemStack[] items, ItemStack[] armors, double health, double food, String game_duration_time, Collection<PotionEffect> potionEffects) {
        super(false);
        this.name = name;
        this.items = items;
        this.armors = armors;
        this.health = health;
        this.food = food;
        this.game_duration_time = game_duration_time;
        this.potionEffects = potionEffects;
    }

    @Override
    public String getTitle(Player player) {
        return name;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors[3];
            }
        });
        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors[2];
            }
        });
        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors[1];
            }
        });
        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors[0];
            }
        });

        if(potionEffects.size() > 0){
            buttons.put(7, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    List<String> lore = new ArrayList<>();
                    for(PotionEffect potionEffect : potionEffects){
                        int duration = potionEffect.getDuration() / 20;
                        lore.add(ChatColor.GRAY + potionEffect.getType().getName() + " " + NumberUtility.RomanNumerals(potionEffect.getAmplifier()+1) + " - " + TimeUtility.timeFormat(duration));
                    }
                    return ItemUtility.createItemStack("" + ChatColor.AQUA + ChatColor.BOLD + "Potion Effects", Material.BREWING_STAND_ITEM, false, lore);
                }
            });
        }

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createPlayerNameSkull(name, "" + ChatColor.AQUA + ChatColor.BOLD + "Player Gauges",
                        ChatColor.YELLOW + "Health: " + ChatColor.WHITE + health,
                        ChatColor.YELLOW + "Food: " + ChatColor.WHITE + food,
                        ChatColor.YELLOW + "Duration: " + ChatColor.WHITE + game_duration_time);
            }
        });

        int slot = 9;
        for (ItemStack item : items) {
            if (item != null) {
                buttons.put(slot, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return item;
                    }
                });
            }
            slot++;
        }
        return buttons;
    }

    public static void put(Player player, String duration){
        BigDecimal bd;
        bd = new BigDecimal(player.getHealth());
        BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_DOWN);
        bd = new BigDecimal(player.getFoodLevel());
        BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_DOWN);
        PlayerInventory inv = player.getInventory();
        ItemStack[] items = InventoryUtility.fixInventoryOrder(inv.getContents());
        ItemStack[] armors = inv.getArmorContents();
        final UUID uuid = player.getUniqueId();
        menus.put(uuid, new MatchDetailsMenu(player.getName(), items, armors, bd1.doubleValue(), bd2.doubleValue(), duration, player.getActivePotionEffects()));
        new BukkitRunnable() {
            @Override
            public void run() {
                menus.remove(uuid);
                this.cancel();
            }
        }.runTaskLaterAsynchronously(HotsPractice.getInstance(), 20*180);
    }

}
