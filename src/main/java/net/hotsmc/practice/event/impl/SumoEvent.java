package net.hotsmc.practice.event.impl;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.other.Cooldown;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

@Getter
public class SumoEvent extends Event {

    private PracticePlayer[] fightingPlayers = new PracticePlayer[2];

    public SumoEvent(Arena arena, PracticePlayer leader) {
        super(LadderType.Sumo, arena, leader, 50, 5,40);
    }

    @Override
    protected void onInit(PracticePlayer leader) {
        HotsPractice.getInstance().getManagerHandler().getEventManager().addEventGame(this);
        leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Please wait while 3 seconds for preparing the event arena...");
        new BukkitRunnable() {
            @Override
            public void run() {
                leader.teleport(arena.getDefaultSpawn());
                leader.setHotbar(PlayerHotbar.EVENT_LEADER);
                state = EventState.WAITING_FOR_PLAYERS;
                leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "You have create a new " + ChatColor.YELLOW + ChatColor.BOLD + "Sumo Event");
                cancel();
            }
        }.runTaskLater(HotsPractice.getInstance(), 20*3);
    }

    @Override
    protected void onStartCountdown(PracticePlayer leader) {
        startCooldown = new Cooldown(time*1000);
        for(PracticePlayer practicePlayer : eventPlayers){
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
        }
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.YELLOW + "(Event) ");
        msg.append("" + ChatColor.GRAY + "Starting Soon: ");
        msg.append(ChatColor.WHITE + getHost() + "'s " + ladderType.name() + " Event ");
        msg.append(""+ ChatColor.AQUA + ChatColor.BOLD + "Click to Join");
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/event join " + getHost()));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/event join " + getHost()).create()));
        HotsPractice.broadcast(msg.create());
    }

    @Override
    protected void onStopCountdown(PracticePlayer leader) {
        for(PracticePlayer practicePlayer : eventPlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "Countdown has stopped");
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
        }
    }

    @Override
    protected void onAddPlayer(PracticePlayer player) {
    }

    @Override
    protected void onRemovePlayer(PracticePlayer player) {

    }

    public boolean isFighting(PracticePlayer player){
        if(fightingPlayers[0] == null || fightingPlayers[1] == null)return false;
        for(PracticePlayer fightingPlayer : fightingPlayers) {
            if (fightingPlayer.getUUID().equals(player.getUUID())) {
                return true;
            }
        }
        return false;
    }

    public void randomSelectPlayer(){
        broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Selecting random players...");
        List<PracticePlayer> players = getWinningPlayers();
        if(players.size() == 2){
            fightingPlayers[0] = players.get(0);
            fightingPlayers[1] = players.get(1);
        }else {
            Collections.shuffle(players);
            fightingPlayers[0] = players.get(0);
            players.remove(players.get(0));
            Collections.shuffle(players);
            fightingPlayers[1] = players.get(0);
        }
        broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.BLUE + "Round" + round + ": " + ChatColor.GRAY + "Starting event match (" + HotsCore.getHotsPlayer(fightingPlayers[0].getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(fightingPlayers[1].getPlayer()).getColorName() + ChatColor.GRAY + ")");
        for(PracticePlayer practicePlayer : fightingPlayers) {
            practicePlayer.teleport(getPlayerSpawnLocation(practicePlayer));
            practicePlayer.clearInventory();
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            practicePlayer.setFrozen(true);
        }
        time = 4;
        startCooldown = new Cooldown(time*1000);
        state = EventState.PREPARING;
        round++;
    }

    @Override
    protected void tick() {
        if(time >= 0) {
            if(state != EventState.WAITING_FOR_PLAYERS && state != EventState.FIGHTING){
                time--;
            }
        }

        if(state == EventState.COUNTDOWN){
            if(getTime() == 40 || getTime() == 30 || getTime() == 20 || getTime() == 10){
                ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.YELLOW + "(Event) ");
                msg.append("" + ChatColor.GRAY + "Starting Soon: ");
                msg.append(ChatColor.WHITE + getHost() + "'s " + ladderType.name() + " Event ");
                msg.append(""+ ChatColor.AQUA + ChatColor.BOLD + "Click to Join");
                msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/event join " + getHost()));
                msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/event join " + getHost()).create()));
                HotsPractice.broadcast(msg.create());
                for(PracticePlayer practicePlayer : eventPlayers){
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
                }
            }
            if (getTime() <= 5 && time != 0) {
                for (PracticePlayer practicePlayer : eventPlayers) {
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
                }
            }
        }

        if(state == EventState.PREPARING){
            if (getTime() <= 3 && time != 0) {
                for(PracticePlayer practicePlayer : fightingPlayers){
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 0.5F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Starting in " + ChatColor.WHITE + time + "s");
                }
            }
        }

        if(state == EventState.FIGHTING){
            time++;
        }

        //0になったとき
        if (time <= 0) {
            if (state == EventState.COUNTDOWN) {
                randomSelectPlayer();
                for(PracticePlayer practicePlayer : eventPlayers){
                    if(practicePlayer != fightingPlayers[0] && practicePlayer != fightingPlayers[1]){
                        practicePlayer.teleport(arena.getDefaultSpawn());
                    }
                }
                return;
            }
            if (state == EventState.PREPARING) {
                for(PracticePlayer practicePlayer : fightingPlayers){
                    state = EventState.FIGHTING;
                    practicePlayer.setFrozen(false);
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1.0F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Event match has begun!");
                }
            }
        }
    }

    public Location getPlayerSpawnLocation(PracticePlayer me) {
        if (fightingPlayers[0].getName().equals(me.getName())) {
            return arena.getSpawn1();
        }
        if (fightingPlayers[1].getName().equals(me.getName())) {
            return arena.getSpawn2();
        }
        return null;
    }

    public PracticePlayer getOpponent(PracticePlayer me) {
        for (PracticePlayer practicePlayer : fightingPlayers) {
            if (!practicePlayer.getName().equals(me.getName())) {
                return practicePlayer;
            }
        }
        return null;
    }
}
