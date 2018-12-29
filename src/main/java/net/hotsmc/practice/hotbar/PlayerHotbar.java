package net.hotsmc.practice.hotbar;

import lombok.Getter;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.core.hotbar.HotbarAdapter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.gui.event.EventSettingsMenu;
import net.hotsmc.practice.gui.party.PartyOtherFightMenu;
import net.hotsmc.practice.gui.player.SettingsMenu;
import net.hotsmc.practice.gui.player.StatisticsMenu;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.queue.Queue;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public enum PlayerHotbar {

    LOBBY(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[0] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Unranked Queue", Material.IRON_SWORD, true)) {
            @Override
            public void clickAction(Player player) {
                HotsPractice.getInstance().getMenuHandler().getUnrankedMenu().openMenu(player);
            }
        };
        items[1] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Ranked Queue", Material.DIAMOND_SWORD, true)) {
            @Override
            public void clickAction(Player player) {
                HotsPractice.getInstance().getMenuHandler().getRankedMenu().openMenu(player);
            }
        };
        items[2] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Party", Material.GOLD_SWORD, false)) {
            @Override
            public void clickAction(Player player) {
                HotsPractice.getInstance().getMenuHandler().getPartyCreateMenu().openMenu(player, 9);
            }
        };
        items[4] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Event", Material.NAME_TAG, false)) {
            @Override
            public void clickAction(Player player) {
                HotsPractice.getInstance().getMenuHandler().getEventMenu().openMenu(player, 27);
            }
        };
        items[6] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Settings", Material.WATCH, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                new SettingsMenu().openMenu(player, 9);
            }
        };
        items[7] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Statistics", Material.EMERALD, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                new StatisticsMenu(practicePlayer.getPlayerData()).openMenu(player, 54);
            }
        };
        items[8] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Kit Editor", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                HotsPractice.getInstance().getMenuHandler().getKitEditSelectMenu().openMenu(player, 18);
            }
        };
        return items;
    }),
    QUEUE(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[0] = new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Queue", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Queue queue = HotsPractice.getInstance().getManagerHandler().getQueueManager().getPlayerOfQueue(practicePlayer);
                String kit = queue.getLadderType().name();
                String type = queue.getRankedType().name();
                HotsPractice.getInstance().getManagerHandler().getQueueManager().removeQueue(queue);
                practicePlayer.setHotbar(PlayerHotbar.LOBBY);
                player.updateInventory();
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have removed from " + ChatColor.YELLOW + kit + "(" + type + ") " + ChatColor.GRAY + " of queue.");
            }
        };
        return items;
    }),
    PARTY(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[0] = new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Party", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).removePlayer(practicePlayer);
            }
        };
        items[2] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Fight Other Party", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                Party party = HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer);
                if (party != null) {
                    if (!party.isLeader(practicePlayer)) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "You aren't leader / あなたはリーダーではありません");
                        return;
                    }
                    new PartyOtherFightMenu(party.getPartyName()).openMenu(player, 54);
                }
            }
        };
        items[4] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "View Members", Material.PAPER, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                Party party = HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer);
                if (party != null) {
                    ChatUtility.sendMessage(player, ChatColor.YELLOW + party.getPartyName() + "'s party members");
                    for (PracticePlayer partyPlayer : party.getPlayers()) {
                        ChatUtility.sendMessage(player, ChatColor.GRAY + "- " + ChatColor.WHITE + partyPlayer.getPlayer().getName());
                    }
                }
            }
        };
        items[6] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Party Fight", Material.GOLD_AXE, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                Party party = HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer);
                if (party != null) {
                    if (!party.isLeader(practicePlayer)) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "You aren't leader / あなたはリーダーではありません");
                        return;
                    }
                    HotsPractice.getInstance().getMenuHandler().getPartyFightMenu().openMenu(player, 9);
                }
            }
        };
        items[8] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Settings", Material.WATCH, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                new SettingsMenu().openMenu(player, 9);
            }
        };
        return items;
    }),
    SPECTATE(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[4] = new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Spectate", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfSpectateMatch(practicePlayer).removeSpectator(practicePlayer);
            }
        };
        return items;
    }),

    EVENT_DEFAULT(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[4] = new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Event", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Event event = HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(practicePlayer);
                if (event != null) {
                    event.removePlayer(practicePlayer);
                }
            }
        };
        return items;
    }),

    EVENT_LEADER(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[3] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Event", Material.SLIME_BALL, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Event event = HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(practicePlayer);
                if (event != null) {
                    event.removePlayer(practicePlayer);
                }
            }
        };
        items[5] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Event Settings", Material.NAME_TAG, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Event event = HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(practicePlayer);
                if (event != null) {
                    new EventSettingsMenu(event, practicePlayer).openMenu(player, 9);
                }
            }
        };
        return items;
    }),

    KIT(() -> {
        ClickActionItem[] items = new ClickActionItem[9];
        Arrays.fill(items, null);
        items[0] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Load Default kit", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                if (match == null) return;
                LadderType ladderType = match.getLadderType();
                practicePlayer.setDefaultKit(ladderType);
            }
        };
        items[8] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Settings", Material.WATCH, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer == null) return;
                new SettingsMenu().openMenu(player, 9);
            }
        };
        return items;
    });

    @Getter
    private HotbarAdapter adapter;

    PlayerHotbar(HotbarAdapter adapter) {
        this.adapter = adapter;
    }

}
