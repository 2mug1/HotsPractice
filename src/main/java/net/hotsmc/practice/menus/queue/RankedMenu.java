package net.hotsmc.practice.menus.queue;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.PracticePlayer;
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

public class RankedMenu extends Menu {

    QueueManager queueManager;
    MatchManager matchManager;

    public RankedMenu(){
        super(true);
        queueManager =  HotsPractice.getQueueManager();
        matchManager = HotsPractice.getMatchManager();
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
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.NoDebuff), PotionType.INSTANT_HEAL,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.NoDebuff),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.NoDebuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.NoDebuff);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.NoDebuff);
                }else{
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.NoDebuff, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
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
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Debuff), PotionType.POISON,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Debuff),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Debuff));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Debuff);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Debuff);
                }else{
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Debuff, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });


        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.MCSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.MCSG),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.MCSG));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.MCSG);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.MCSG);
                }else{
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.MCSG, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.OCTC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.OCTC),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.OCTC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.OCTC);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.OCTC);
                }else{
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.OCTC, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Gapple),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Gapple),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Gapple));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Gapple);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Gapple);
                }else{
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Gapple, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Archer),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Archer),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Archer));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Archer);
                if(queue != null){
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Archer);
                }else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Archer, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo", matchManager.countDuelGame(RankedType.RANKED, LadderType.Combo),
                        3,
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Combo),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Combo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Combo);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Combo);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Combo, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Soup),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Soup),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Soup));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Soup);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Soup);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Soup, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.BuildUHC),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.BuildUHC),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.BuildUHC));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.BuildUHC);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.BuildUHC);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.BuildUHC, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Sumo", Material.LEASH, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Sumo),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Sumo),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Sumo));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Sumo);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Sumo);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Sumo, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Axe),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Axe),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Axe));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Axe);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Axe);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Axe, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Spleef", Material.DIAMOND_SPADE, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.Spleef),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.Spleef),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.Spleef));
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.Spleef);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.Spleef);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.Spleef, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });

        buttons.put(12, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,
                        matchManager.countDuelGame(RankedType.RANKED, LadderType.GappleSG),
                        ChatColor.WHITE + "In Queue: " + ChatColor.YELLOW + queueManager.countQueue(RankedType.RANKED, LadderType.GappleSG),
                        ChatColor.WHITE + "In Match: " + ChatColor.YELLOW + matchManager.countDuelGame(RankedType.RANKED, LadderType.GappleSG));
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = queueManager.getQueue(RankedType.RANKED, LadderType.GappleSG);
                if (queue != null) {
                    queue.startGame(practicePlayer, RankedType.RANKED, LadderType.GappleSG);
                } else {
                    Queue newQueue = new Queue(RankedType.RANKED, LadderType.GappleSG, practicePlayer);
                    HotsPractice.getQueueManager().addQueue(newQueue);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have added to " + ChatColor.YELLOW + newQueue.getLadderType() + "(" + newQueue.getRankedType() + ")" + ChatColor.GRAY + " of queue.");
                    practicePlayer.onQueue();
                }
            }
        });
        return buttons;
    }
}
