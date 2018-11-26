package net.hotsmc.practice;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.core.scoreboard.BoardManager;
import net.hotsmc.practice.arena.ArenaManager;
import net.hotsmc.practice.arena.ArenaFactory;
import net.hotsmc.practice.commands.*;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.database.MongoConfig;
import net.hotsmc.practice.database.MongoConnection;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.event.EventManager;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.inventory.InventoryDataManager;
import net.hotsmc.practice.knockback.KnockbackListener;
import net.hotsmc.practice.knockback.KnockbackManager;
import net.hotsmc.practice.match.MatchConfig;
import net.hotsmc.practice.menus.event.EventMenu;
import net.hotsmc.practice.menus.event.EventSettingsMenu;
import net.hotsmc.practice.menus.event.SumoEventListMenu;
import net.hotsmc.practice.menus.event.SumoEventMenu;
import net.hotsmc.practice.menus.player.LeaderboardMenu;
import net.hotsmc.practice.menus.player.StatisticsMenu;
import net.hotsmc.practice.menus.queue.RankedMenu;
import net.hotsmc.practice.menus.queue.UnrankedMenu;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.match.MatchManager;
import net.hotsmc.practice.ladder.DefaultLadder;
import net.hotsmc.practice.ladder.LadderEditManager;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.listener.ListenerHandler;
import net.hotsmc.practice.menus.kit.KitEditSelectMenu;
import net.hotsmc.practice.menus.party.PartyCreateMenu;
import net.hotsmc.practice.menus.party.PartyFightMenu;
import net.hotsmc.practice.menus.party.PartyOtherFightMenu;
import net.hotsmc.practice.menus.party.PublicPartyListMenu;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class HotsPractice extends JavaPlugin {

    @Getter private static HotsPractice instance;
    @Getter private static MongoConnection mongoConnection;
    @Getter private static MatchConfig matchConfig;
    @Getter private static List<PracticePlayer> practicePlayers;
    @Getter private static List<ClickActionItem> duelClickItems;
    @Getter private static ArenaManager arenaManager;
    @Getter private static QueueManager queueManager;
    @Getter private static MatchManager matchManager;
    @Getter private static LadderEditManager ladderEditManager;
    @Getter private static DefaultLadder defaultLadder;
    @Getter private static ArenaFactory arenaFactory;
    @Getter private static PartyManager partyManager;
    @Getter private static InventoryDataManager inventoryDataManager;
    @Getter private static PartyCreateMenu partyCreateMenu;
    @Getter private static PublicPartyListMenu publicPartyListMenu;
    @Getter private static LeaderboardMenu leaderboardMenu;
    @Getter private static KitEditSelectMenu kitEditSelectMenu;
    @Getter private static RankedMenu rankedMenu;
    @Getter private static UnrankedMenu unrankedMenu;
    @Getter private static PartyFightMenu partyFightMenu;
    @Getter private static EventManager eventManager;
    @Getter private static EventMenu eventMenu;
    @Getter private static SumoEventMenu sumoEventMenu;
    @Getter private static SumoEventListMenu sumoEventListMenu;
    @Getter private static KnockbackManager knockbackManager;

    @Override
    public void onEnable() {
        instance = this;
        mongoConnection = new MongoConnection(new MongoConfig().load());
        mongoConnection.open();
        matchConfig = new MatchConfig(new ConfigCursor(new FileConfig(this, "MatchConfig.yml"), "MatchConfig")).load();
        registerCommands();
        practicePlayers = Lists.newArrayList();
        arenaManager = new ArenaManager(this);
        arenaManager.load();
        ladderEditManager = new LadderEditManager(new ConfigCursor(new FileConfig(this, "KitEditLocations.yml"), "Locations"));
        ladderEditManager.load();
        queueManager = new QueueManager();
        matchManager = new MatchManager();
        rankedMenu = new RankedMenu();
        unrankedMenu = new UnrankedMenu();
        leaderboardMenu = new LeaderboardMenu();
        kitEditSelectMenu = new KitEditSelectMenu();
        partyCreateMenu = new PartyCreateMenu();
        initClickItems();
        defaultLadder = new DefaultLadder(this);
        defaultLadder.loadLadders();
        arenaFactory = new ArenaFactory();
        partyManager = new PartyManager();
        publicPartyListMenu = new PublicPartyListMenu();
        partyFightMenu = new PartyFightMenu();
        inventoryDataManager = new InventoryDataManager();
        eventManager = new EventManager();
        eventMenu = new EventMenu();
        sumoEventListMenu = new SumoEventListMenu();
        sumoEventMenu = new SumoEventMenu();
        knockbackManager = new KnockbackManager();
        knockbackManager.loadKnockbacks();
        Bukkit.getPluginManager().registerEvents(new KnockbackListener(), this);
        ListenerHandler.loadListenersFromPackage(this, "net.hotsmc.practice.listener.listeners");
        HotsCore.getInstance().setBoardManager(new BoardManager(this, new PracticeScoreboardAdapter()));
    }

    private void registerCommands(){
        this.getCommand("practice").setExecutor(new SettingCommand());
        this.getCommand("party").setExecutor(new PartyCommand());
        this.getCommand("duel").setExecutor(new DuelCommand());
        this.getCommand("inv").setExecutor(new InventoryCommand());
        this.getCommand("event").setExecutor(new EventCommand());
        this.getCommand("spectate").setExecutor(new SpectateCommand());
        this.getCommand("gf").setExecutor(new GroundFixCommand());
    }

    public static PracticePlayer getPracticePlayer(Player player){
        for(PracticePlayer practicePlayer : practicePlayers){
            if(player.getName().equals(practicePlayer.getPlayer().getName())){
                return practicePlayer;
            }
        }
        return null;
    }

    public static void addDuelPlayer(PracticePlayer practicePlayer){
        practicePlayers.add(practicePlayer);
    }

    public static void removeDuelPlayer(PracticePlayer practicePlayer){
        practicePlayers.remove(practicePlayer);
    }

    private void initClickItems(){
        duelClickItems = Lists.newArrayList();
        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Ranked Queue", Material.DIAMOND_SWORD, true)) {
            @Override
            public void clickAction(Player player) {
                rankedMenu.openMenu(player);
            }
        });
        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Unranked Queue", Material.IRON_SWORD, true)) {
            @Override
            public void clickAction(Player player) {
                unrankedMenu.openMenu(player);
            }
        });
        duelClickItems.add(new ClickActionItem(ItemUtility.createPlayerSkull("" + ChatColor.YELLOW + ChatColor.BOLD + "Party")) {
            @Override
            public void clickAction(Player player) {
                partyCreateMenu.openMenu(player, 9);
            }
        });
        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Kit Editor", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                kitEditSelectMenu.openMenu(player, 18);
            }
        });
        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Statistics", Material.PAPER, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                if (practicePlayer != null) {
                    PlayerData playerData = practicePlayer.getPlayerData();
                    new StatisticsMenu(playerData).openMenu(player, 9);
                }
            }
        });
        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "LeaderBoard", Material.BLAZE_ROD, false)) {
            @Override
            public void clickAction(Player player) {
                leaderboardMenu.openMenu(player, 36);
            }
        });
        duelClickItems.add(new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Queue", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                if(practicePlayer == null)return;
                Queue queue = queueManager.getPlayerOfQueue(practicePlayer);
                String kit = queue.getLadderType().name();
                String type = queue.getRankedType().name();
                queueManager.removeQueue(queue);
                practicePlayer.setClickItems();
                player.updateInventory();
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "You have removed from " + ChatColor.YELLOW + kit + "(" + type + ") "+ ChatColor.GRAY + " of queue.");
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Load Default ladder", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                if(practicePlayer == null)return;
                Match match = matchManager.getPlayerOfGame(practicePlayer);
                if(match == null)return;
                LadderType ladderType = match.getLadderType();
                practicePlayer.setDefaultKit(ladderType);
            }
        });

        duelClickItems.add(new ClickActionItem(net.hotsmc.core.utility.ItemUtility.createDye( "" + ChatColor.YELLOW + ChatColor.BOLD +  "Leave Party", 1, 14)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                partyManager.getPlayerOfParty(practicePlayer).removePlayer(practicePlayer);
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack( "" + ChatColor.YELLOW + ChatColor.BOLD + "Fight Other Party", Material.BOOK, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                Party party = partyManager.getPlayerOfParty(practicePlayer);
                if (party != null) {
                    if (!party.isLeader(practicePlayer)) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "You aren't leader / あなたはリーダーではありません");
                        return;
                    }
                    new PartyOtherFightMenu(party.getPartyName()).openMenu(player, 54);
                }
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack( "" + ChatColor.YELLOW + ChatColor.BOLD +  "View Members", Material.PAPER, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                Party party = partyManager.getPlayerOfParty(practicePlayer);
                if (party != null) {
                    ChatUtility.sendMessage(player, ChatColor.YELLOW + party.getPartyName() + "'s party members");
                    for (PracticePlayer partyPlayer : party.getPlayers()) {
                        ChatUtility.sendMessage(player,  ChatColor.GRAY + "- " + ChatColor.WHITE + partyPlayer.getPlayer().getName());
                    }
                }
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Party Fight", Material.STONE_SWORD, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                Party party = partyManager.getPlayerOfParty(practicePlayer);
                if (party != null) {
                    if (!party.isLeader(practicePlayer)) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "You aren't leader / あなたはリーダーではありません");
                        return;
                    }
                    partyFightMenu.openMenu(player, 9);
                }
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Event", Material.NETHER_STAR, false)) {
            @Override
            public void clickAction(Player player) {
                eventMenu.openMenu(player, 9);
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Event", Material.SLIME_BALL, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                if (practicePlayer == null) return;
                Event event = eventManager.getPlayerOfEventGame(practicePlayer);
                if(event != null){
                    event.removePlayer(practicePlayer);
                }
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Event Settings", Material.NAME_TAG, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                if (practicePlayer == null) return;
                Event event = eventManager.getPlayerOfEventGame(practicePlayer);
                if(event != null){
                    new EventSettingsMenu(event, practicePlayer).openMenu(player, 9);
                }
            }
        });

        duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Leave Spectate", Material.SLIME_BALL, false)) {
            @Override
            public void clickAction(Player player) {
                PracticePlayer practicePlayer = getPracticePlayer(player);
                matchManager.getPlayerOfSpectatingGame(practicePlayer).removeSpectator(practicePlayer);
            }
        });

        for(int i = 0; i < 7; i++) {
            int finalI = i;
            duelClickItems.add(new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Load Kit #" + finalI, Material.BOOK, false)) {
                @Override
                public void clickAction(Player player) {
                    PracticePlayer practicePlayer = getPracticePlayer(player);
                    if (practicePlayer == null) return;
                    Match match = matchManager.getPlayerOfGame(practicePlayer);
                    if (match == null) return;
                    LadderType ladderType = match.getLadderType();
                    practicePlayer.getPlayerKitData(ladderType).setKit(finalI,  player);
                }
            });
        }
    }

    public static int getCountQueue(){
       return queueManager.getQueues().size();
    }

    public static int getCountFight(){
        int i = 0;
        for(PracticePlayer practicePlayer : practicePlayers){
            if(practicePlayer.isInMatch()){
                i++;
            }
        }
        return i;
    }

    public static void broadcast(String message){
        for(PracticePlayer practicePlayer : practicePlayers){
            practicePlayer.sendMessage(message);
        }
    }

    public static void broadcast(BaseComponent[] baseComponent){
        for(PracticePlayer practicePlayer : practicePlayers){
            practicePlayer.getPlayer().spigot().sendMessage(baseComponent);
        }
    }

    public static int countOnline() {
        return practicePlayers.size();
    }

    public void reload(CommandSender sender) {
        knockbackManager.loadKnockbacks();
        defaultLadder.loadLadders();
        ChatUtility.sendMessage(sender, ChatColor.YELLOW + "Reload completed: Knockbacks, Default ladders");
    }
}
