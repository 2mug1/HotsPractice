package net.hotsmc.practice.menus.queue;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.game.GameManager;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class UnrankedMenu extends Menu {

    DuelQueueManager queueManager;
    GameManager gameManager;

    public UnrankedMenu(){
        super(true);
        queueManager =  HotsPractice.getQueueManager();
        gameManager = HotsPractice.getGameManager();
    }

    @Override
    public String getTitle(Player player) {
        return "Unranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff",
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.NoDebuff), PotionType.INSTANT_HEAL,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.NoDebuff),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.NoDebuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.NoDebuff);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.NoDebuff);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.NoDebuff, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff",
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Debuff), PotionType.POISON,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Debuff),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Debuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Debuff);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Debuff);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Debuff, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.MCSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.MCSG),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.MCSG));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.MCSG);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.MCSG);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.MCSG, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.OCTC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.OCTC),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.OCTC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.OCTC);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.OCTC);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.OCTC, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Gapple),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Gapple),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Gapple));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Gapple);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Gapple);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Gapple, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Archer),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Archer),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Archer));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Archer);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Archer);
                }else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Archer, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", gameManager.countDuelGame(RankedType.UNRANKED, KitType.Combo),
                        3,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Combo),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Combo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Combo);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Combo);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Combo, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Soup),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Soup),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Soup));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Soup);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Soup);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Soup, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.BuildUHC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.BuildUHC),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.BuildUHC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.BuildUHC);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.BuildUHC);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.BuildUHC, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Sumo", Material.LEASH, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Sumo),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Sumo),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Sumo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Sumo);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Sumo);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Sumo, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Axe),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Axe),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Axe));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Axe);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Axe);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Axe, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.Spleef),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.Spleef),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.Spleef));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.Spleef);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.Spleef);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.Spleef, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(12, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,
                        gameManager.countDuelGame(RankedType.UNRANKED, KitType.GappleSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, KitType.GappleSG),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.UNRANKED, KitType.GappleSG));
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.UNRANKED, KitType.GappleSG);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.UNRANKED, KitType.GappleSG);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.UNRANKED, KitType.GappleSG, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });
        return buttons;
    }
}