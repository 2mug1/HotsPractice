package net.hotsmc.practice.menus.queue;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.match.RankedType;
import net.hotsmc.practice.match.MatchManager;
import net.hotsmc.practice.ladder.LadderType;
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

    QueueManager queueManager;
    MatchManager matchManager;

    public UnrankedMenu(){
        super(true);
        queueManager =  HotsPractice.getInstance().getManagerHandler().getQueueManager();
        matchManager = HotsPractice.getInstance().getManagerHandler().getMatchManager();
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
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.NoDebuff), PotionType.INSTANT_HEAL,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.NoDebuff),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.NoDebuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.NoDebuff);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.NoDebuff);
                }else{
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.NoDebuff, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff",
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Debuff), PotionType.POISON,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Debuff),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Debuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Debuff);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Debuff);
                }else{
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Debuff, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.MCSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.MCSG),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.MCSG));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.MCSG);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.MCSG);
                }else{
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.MCSG, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.OCTC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.OCTC),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.OCTC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.OCTC);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.OCTC);
                }else{
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.OCTC, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Gapple),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Gapple),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Gapple));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Gapple);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Gapple);
                }else{
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Gapple, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Archer),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Archer),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Archer));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Archer);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Archer);
                }else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Archer, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Combo),
                        3,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Combo),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Combo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Combo);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Combo);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Combo, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Soup),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Soup),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Soup));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Soup);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Soup);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Soup, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.BuildUHC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.BuildUHC),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.BuildUHC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.BuildUHC);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.BuildUHC);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.BuildUHC, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Sumo", Material.LEASH, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Sumo),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Sumo),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Sumo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Sumo);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Sumo);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Sumo, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Axe),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Axe),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Axe));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Axe);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Axe);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Axe, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Spleef),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.Spleef),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.Spleef));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.Spleef);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.Spleef);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.Spleef, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(12, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,
                        matchManager.countDuelGame(RankedType.UNRANKED, LadderType.GappleSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.UNRANKED, LadderType.GappleSG),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.UNRANKED, LadderType.GappleSG));
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.UNRANKED, LadderType.GappleSG);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.UNRANKED, LadderType.GappleSG);
                } else {
                    Queue newQueue = new Queue(RankedType.UNRANKED, LadderType.GappleSG, practicePlayer);
                    queueManager.addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });
        return buttons;
    }
}