package net.hotsmc.practice.event;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.other.Cooldown;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Event extends BukkitRunnable {

    private final int maxPlayers;
    private final int reqiurePlayers;
    private String host;
    protected int time;
    protected LadderType ladderType;
    protected EventState state;
    protected Arena arena;
    protected List<PracticePlayer> eventPlayers;
    protected Cooldown startCooldown;
    protected Cooldown broadcastCooldown = new Cooldown(0);
    protected final String LEADER_UUID;
    protected final int countdownSeconds;

    public Event(LadderType ladderType, Arena arena, PracticePlayer leader, int maxPlayers, int reqiurePlayers, int countdownSeconds){
        this.maxPlayers = maxPlayers;
        this.reqiurePlayers = reqiurePlayers;
        this.ladderType = ladderType;
        this.arena = arena;
        this.time = 0;
        this.state = EventState.ARENA_PREPARING;
        this.eventPlayers = new ArrayList<>();
        this.LEADER_UUID = leader.getUUID();
        this.host = leader.getName();
        this.countdownSeconds = countdownSeconds;
        eventPlayers.add(leader);
    }

    protected abstract void onInit(PracticePlayer leader);

    protected abstract void onStartCountdown(PracticePlayer leader);

    protected abstract void onStopCountdown(PracticePlayer leader);

    protected abstract void onAddPlayer(PracticePlayer player);

    protected abstract void onRemovePlayer(PracticePlayer player);

    protected abstract void tick();

    public void init(PracticePlayer leader) {
        onInit(leader);
        runTaskTimer(HotsPractice.getInstance(), 0, 20);
    }

    public void startCountdown(PracticePlayer leader){
        if(reqiurePlayers > eventPlayers.size()){
            leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "More than " + reqiurePlayers + " players required to start the event");
            return;
        }
        time = countdownSeconds;
        onStartCountdown(leader);
        state = EventState.COUNTDOWN;
    }

    public void stopCountdown(PracticePlayer leader){
        time = 0;
        state = EventState.WAITING_FOR_PLAYERS;
        onStopCountdown(leader);
    }

    public PracticePlayer getEventPlayer(Player player){
        for(PracticePlayer practicePlayer : eventPlayers){
            if(practicePlayer.getUUID().equals(player.getUniqueId().toString())){
                return practicePlayer;
            }
        }
        return null;
    }

    public boolean hasJoined(PracticePlayer player){
        return getEventPlayer(player.getPlayer()) != null;
    }

    public boolean isLeader(PracticePlayer player){
        return player.getUUID().equals(LEADER_UUID);
    }

    public void addPlayer(PracticePlayer player){
        if(player.isInEvent()){
            player.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You have already been in event.");
            return;
        }
        if(eventPlayers.size() >= maxPlayers) {
            player.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "This event is fully!");
            return;
        }
        if(state == EventState.ARENA_PREPARING){
            player.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "Preparing the arena...");
            return;
        }
        if(state != EventState.WAITING_FOR_PLAYERS && state != EventState.COUNTDOWN) {
            player.setEventLost(true);
        }
        eventPlayers.add(player);
        player.teleport(arena.getDefaultSpawn());
        if(!isLeader(player)) {
            player.setEventItems();
        }else{
            player.setEventLeaderItems();
        }
        if(state == EventState.WAITING_FOR_PLAYERS || state == EventState.COUNTDOWN){
            broadcast(ChatColor.YELLOW + "(Event) " + HotsCore.getHotsPlayer(player.getPlayer()).getColorName() +
                    ChatColor.GRAY + " has joined (" + eventPlayers.size() + "/" + maxPlayers + ")");
        }
        onAddPlayer(player);
    }

    public void removePlayer(PracticePlayer player) {
        if (!hasJoined(player)) return;
        eventPlayers.remove(player);
        player.setEventLost(false);
        player.resetPlayer();
        player.teleportToLobby();
        player.setClickItems();
        if(!player.isEventLost()) {
            broadcast(ChatColor.YELLOW + "(Event) " + HotsCore.getHotsPlayer(player.getPlayer()).getColorName() + ChatColor.GRAY + " has left (" + eventPlayers.size() + "/" + maxPlayers + ")");
        }
        onRemovePlayer(player);

        if (isLeader(player)) {
            if (state == EventState.WAITING_FOR_PLAYERS || state == EventState.COUNTDOWN) {
                if(player.isOnline()){
                    player.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "The event has aborted.");
                }
                broadcast(ChatColor.RED + "The event has aborted by host.");
                for (PracticePlayer practicePlayer : eventPlayers) {
                    if (practicePlayer != player) {
                        practicePlayer.setEventLost(false);
                        practicePlayer.teleportToLobby();
                        practicePlayer.setClickItems();
                    }
                }
                delete();
            }
        }
    }

    @Override
    public void run() {
        tick();
    }

    public void delete(){
        arena.unload();
        this.cancel();
        HotsPractice.getEventManager().removeEventGame(this);
    }

    public void broadcast(String message) {
        for(PracticePlayer practicePlayer : eventPlayers){
            practicePlayer.sendMessage(message);
        }
    }

    public List<PracticePlayer> getWinningPlayers(){
        List<PracticePlayer> winningPlayers = new ArrayList<>();
        for(PracticePlayer eventPlayer : eventPlayers){
            if(!eventPlayer.isEventLost()){
                winningPlayers.add(eventPlayer);
            }
        }
        return winningPlayers;
    }

    public void broadcastEvent(PracticePlayer leader){
        if(!broadcastCooldown.hasExpired()){
            leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "Can't spam broadcast.");
        }else{
            ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.YELLOW + "(Event) ");
            msg.append("" + ChatColor.GRAY + "Starting Soon: ");
            msg.append(ChatColor.WHITE + getHost() + "'s " + ladderType.name() + " Event ");
            msg.append(""+ ChatColor.AQUA + ChatColor.BOLD + "Click to Join");
            msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/event join " + getHost()));
            msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/event join " + getHost()).create()));
            HotsPractice.broadcast(msg.create());
            broadcastCooldown = new Cooldown(1000*10);
        }
    }

    public void end(){
        for(PracticePlayer practicePlayer : eventPlayers){
            practicePlayer.setEventLost(false);
            practicePlayer.teleportToLobby();
            practicePlayer.setClickItems();
        }
        delete();
    }
}
