package net.hotsmc.practice.utility;

import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.party.Party;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtility {
    public static ItemStack createItemStack(String name, Material mat, boolean unbreakable, String... lore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        is.setItemMeta(meta);
        return is;
    }

    public static ItemStack createItemStack(String name, Material mat, boolean unbreakable, List<String> lore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(lore);
        }
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        is.setItemMeta(meta);
        return is;
    }

    public static ItemStack createItemStack(String name, Material mat, boolean unbreakable, int amount, String... lore) {
        ItemStack is = new ItemStack(mat);
        if (amount == 0) {
            amount = 1;
        }
        is.setAmount(amount);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        is.setItemMeta(meta);
        return is;
    }

    public static ItemStack createItemStack(String name, Material mat, boolean unbreakable, int amount, List<String> lore) {
        ItemStack is = new ItemStack(mat);
        if (amount == 0) {
            amount = 1;
        }
        is.setAmount(amount);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        is.setItemMeta(meta);
        return is;
    }

    public static ItemStack createFlintAndSteel() {
        ItemStack is = new ItemStack(Material.FLINT_AND_STEEL);
        is.setDurability((short) 61);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Flint and Steel");
        is.setItemMeta(meta);
        return is;
    }

    public static ItemStack createPlayerSkull(String displayName, String... lore) {
        ItemStack myAwesomeSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = myAwesomeSkull.getItemMeta();
        meta.setDisplayName(displayName);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        myAwesomeSkull.setItemMeta(meta);
        return myAwesomeSkull;
    }

    public static ItemStack createPlayerSkull(String displayName, List<String> lore) {
        ItemStack myAwesomeSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta meta = myAwesomeSkull.getItemMeta();
        meta.setDisplayName(displayName);
        if (lore != null) {
            meta.setLore(lore);
        }
        myAwesomeSkull.setItemMeta(meta);
        return myAwesomeSkull;
    }

    public static ItemStack addLore(ItemStack itemStack, String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack addEnchant(ItemStack itemStack, Enchantment enchant, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(enchant, level, true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack createFish(String name, int amount, int type, String... lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.RAW_FISH, amount, (short) type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createFish(String name, int amount, int type, List<String> lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.RAW_FISH, amount, (short) type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createEnchGapple(String name, int amount, String... lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, amount, (short) 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createEnchGapple(String name, int amount, List<String> lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, amount, (short) 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createSplashPotion(String name, int amount, PotionType type, String... lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack item = new ItemStack(Material.POTION, amount);
        Potion pot = new Potion(type);
        pot.setSplash(true);
        pot.apply(item);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createSplashPotion(String name, int amount, PotionType type, List<String> lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack item = new ItemStack(Material.POTION, amount);
        Potion pot = new Potion(type);
        pot.setSplash(true);
        pot.apply(item);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createWool(String name, int amount, int type, String... lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.WOOL, amount, (short) type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createWool(String name, int amount, int type, List<String> lore) {
        if (amount == 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(Material.WOOL, amount, (short) type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createPlayerNameSkull(String playerName, String displayName, String... lore) {
        ItemStack myAwesomeSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta myAwesomeSkullMeta = (SkullMeta) myAwesomeSkull.getItemMeta();
        myAwesomeSkullMeta.setOwner(playerName);
        myAwesomeSkull.setItemMeta(myAwesomeSkullMeta);
        ItemMeta meta = myAwesomeSkull.getItemMeta();
        meta.setDisplayName(displayName);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        myAwesomeSkull.setItemMeta(meta);
        return myAwesomeSkull;
    }

    public static ItemStack createPartyListItem(Party party){
        List<String> members = new ArrayList<>();
        for(PracticePlayer practicePlayer : party.getPlayers()){
            members.add("" + ChatColor.GRAY + "- " + ChatColor.YELLOW + practicePlayer.getName());
        }
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + party.getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Party.MAX_PLAYER);
        lore.addAll(members);
        lore.add("" + ChatColor.WHITE + ChatColor.BOLD + ChatColor.UNDERLINE + "Click to Join");
        return ItemUtility.createPlayerSkull("" + ChatColor.YELLOW + ChatColor.BOLD + "Party: " + ChatColor.WHITE + party.getPartyName(), lore);
    }

    public static ItemStack createOtherFightPartyItem(Party party){
        List<String> members = new ArrayList<>();
        for(PracticePlayer practicePlayer : party.getPlayers()){
            members.add("" + ChatColor.GRAY + "- " + ChatColor.YELLOW + practicePlayer.getName());
        }
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + party.getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Party.MAX_PLAYER);
        lore.addAll(members);
        lore.add("" + ChatColor.WHITE + ChatColor.BOLD + ChatColor.UNDERLINE + "Click to Fight");
        return ItemUtility.createPlayerSkull("" + ChatColor.YELLOW + ChatColor.BOLD + "Party: " + ChatColor.WHITE + party.getPartyName(), lore);
    }

    public static ItemStack createSumoEventInfoItem(SumoEvent sumoEventGame) {
        EventState state = sumoEventGame.getState();
        String name = sumoEventGame.getHost();
        int players = sumoEventGame.getWinningPlayers().size();
        int max = sumoEventGame.getMaxPlayers();
        if (state == EventState.ARENA_PREPARING) {
            return net.hotsmc.core.utility.ItemUtility.createClay(ChatColor.YELLOW + "Sumo Event: " + ChatColor.BLUE + name,
                    1, 14, ChatColor.GRAY + "State: " + ChatColor.WHITE + state.getName());
        }
        if (state == EventState.WAITING_FOR_PLAYERS) {
            return net.hotsmc.core.utility.ItemUtility.createClay(ChatColor.YELLOW + "Sumo Event: " + ChatColor.BLUE + name,
                    1, 5, ChatColor.GRAY + "State: " + ChatColor.WHITE + state.getName(),
                    ChatColor.GRAY + "Players: " + ChatColor.WHITE + players + "/" + max);
        }
        if (state == EventState.COUNTDOWN) {
            return net.hotsmc.core.utility.ItemUtility.createClay(ChatColor.YELLOW + "Sumo Event: " + ChatColor.BLUE + name,
                    1, 5, ChatColor.GRAY + "State: " + ChatColor.WHITE + state.getName(),
                    ChatColor.GRAY + "Players: " + ChatColor.WHITE + players + "/" + max,
                    ChatColor.GRAY + "Starting in " + ChatColor.WHITE + sumoEventGame.getStartCooldown().getTimeLeft() + "s");
        }
        return net.hotsmc.core.utility.ItemUtility.createClay(ChatColor.YELLOW + "Sumo Event: " + ChatColor.BLUE + name,
                1, 4, ChatColor.GRAY + "State: " + ChatColor.WHITE + state.getName(),
                ChatColor.GRAY + "Players: " + ChatColor.WHITE + players + "/" + max);
    }
}
