package net.hotsmc.practice.menus;

import lombok.Getter;
import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class PlayerInventory extends Menu {

    private String uuid;
    private String name;
    private List<ItemStack> items;
    private List<ItemStack> armors;
    private double health;
    private double food;
    private String game_duration_time;

    public PlayerInventory(String uuid, String name, List<ItemStack> items, List<ItemStack> armors, double health, double food, String game_duration_time) {
        super(false);
        this.uuid = uuid;
        this.name = name;
        this.items = items;
        this.armors = armors;
        this.health = health;
        this.food = food;
        this.game_duration_time = game_duration_time;
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
                return armors.get(3);
            }
        });
        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors.get(2);
            }
        });
        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors.get(1);
            }
        });
        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return armors.get(0);
            }
        });

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
}
