package net.hotsmc.practice;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.scoreboard.BoardManager;
import net.hotsmc.practice.arena.ArenaFactory;
import net.hotsmc.practice.board.PracticeScoreboardAdapter;
import net.hotsmc.practice.command.*;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.database.MongoConfig;
import net.hotsmc.practice.database.MongoConnection;
import net.hotsmc.practice.handler.ManagerHandler;
import net.hotsmc.practice.handler.MenuHandler;
import net.hotsmc.practice.knockback.KnockbackListener;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.ladder.DefaultLadder;
import net.hotsmc.practice.listener.ListenerHandler;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

@Getter
public class HotsPractice extends JavaPlugin {

    @Getter private static HotsPractice instance;
    @Getter private static List<PracticePlayer> practicePlayers = new LinkedList<>();

    private MongoConnection mongoConnection;
    private PracticeConfig practiceConfig;
    private DefaultLadder defaultLadder;
    private ManagerHandler managerHandler;
    private MenuHandler menuHandler;
    private ArenaFactory arenaFactory;

    @Override
    public void onEnable() {
        init();
        registerListeners();
        registerCommands();
        registerAdapters();
    }

    private void init(){
        instance = this;
        mongoConnection = new MongoConnection(new MongoConfig().load());
        mongoConnection.open();
        practiceConfig = new PracticeConfig(new ConfigCursor(new FileConfig(this, "PracticeConfig.yml"), "PracticeConfig")).load();
        defaultLadder = new DefaultLadder(instance);
        defaultLadder.loadLadders();
        managerHandler = new ManagerHandler().load(this);
        menuHandler = new MenuHandler().load();
        arenaFactory = new ArenaFactory();
    }

    private void registerAdapters(){
        HotsCore.getInstance().setBoardManager(new BoardManager(this, new PracticeScoreboardAdapter()));
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new KnockbackListener(), this);
        ListenerHandler.loadListenersFromPackage(this, "net.hotsmc.practice.listener.listeners");
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

    public static void addPracticePlayer(PracticePlayer practicePlayer){
        practicePlayers.add(practicePlayer);
    }

    public static void removePracticePlayer(PracticePlayer practicePlayer){
        practicePlayers.remove(practicePlayer);
    }

    public static int getCountQueue(){
       return instance.getManagerHandler().getQueueManager().getQueues().size();
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
       return Bukkit.getServer().getOnlinePlayers().length;
    }

    public void reload(CommandSender sender) {
        managerHandler.getKnockbackManager().load();
        defaultLadder.loadLadders();
        ChatUtility.sendMessage(sender, ChatColor.YELLOW + "Reload completed: Knockbacks, Default ladders");
    }
}
