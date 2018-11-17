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

public class RankedMenu extends Menu {

    DuelQueueManager queueManager;
    GameManager gameManager;

    public RankedMenu(){
        super(true);
        queueManager =  HotsPractice.getQueueManager();
        gameManager = HotsPractice.getGameManager();
    }

    @Override
    public String getTitle(Player player) {
        return "Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff",
                        gameManager.countDuelGame(RankedType.RANKED, KitType.NoDebuff), PotionType.INSTANT_HEAL,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.NoDebuff),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.NoDebuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.NoDebuff);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.NoDebuff);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.NoDebuff, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Debuff), PotionType.POISON,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Debuff),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Debuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Debuff);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Debuff);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Debuff, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.MCSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.MCSG),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.MCSG));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.MCSG);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.MCSG);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.MCSG, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.OCTC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.OCTC),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.OCTC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.OCTC);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.OCTC);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.OCTC, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Gapple),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Gapple),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Gapple));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Gapple);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Gapple);
                }else{
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Gapple, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Archer),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Archer),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Archer));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Archer);
                if(duelQueue != null){
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Archer);
                }else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Archer, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", gameManager.countDuelGame(RankedType.RANKED, KitType.Combo),
                        3,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Combo),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Combo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Combo);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Combo);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Combo, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Soup),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Soup),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Soup));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Soup);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Soup);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Soup, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.BuildUHC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.BuildUHC),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.BuildUHC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.BuildUHC);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.BuildUHC);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.BuildUHC, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Sumo),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Sumo),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Sumo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Sumo);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Sumo);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Sumo, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Axe),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Axe),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Axe));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Axe);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Axe);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Axe, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.Spleef),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.Spleef),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.Spleef));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.Spleef);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.Spleef);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.Spleef, practicePlayer);
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
                        gameManager.countDuelGame(RankedType.RANKED, KitType.GappleSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, KitType.GappleSG),
                        ChatColor.WHITE + "In Game: " + ChatColor.YELLOW + gameManager.countDuelGame(RankedType.RANKED, KitType.GappleSG));
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                DuelQueue duelQueue = queueManager.getQueue(RankedType.RANKED, KitType.GappleSG);
                if (duelQueue != null) {
                    duelQueue.startGame(practicePlayer, RankedType.RANKED, KitType.GappleSG);
                } else {
                    DuelQueue newQueue = new DuelQueue(RankedType.RANKED, KitType.GappleSG, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getKitType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });
        return buttons;
    }
}
