package net.hotsmc.practice.game.events;

import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.other.Cooldown;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;

public class SumoEventGame extends EventGame {

    public PracticePlayer[] fightingPlayers = new PracticePlayer[2];

    public SumoEventGame(Arena arena, PracticePlayer leader) {
        super(KitType.Sumo, arena, leader, 50, 3,10);
    }

    @Override
    protected void onInit(PracticePlayer leader) {
        HotsPractice.getEventGameManager().addEventGame(this);
        leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Please wait while 3 seconds for preparing the event arena...");
        leader.startSumoEventWaitingScoreboard();
        new BukkitRunnable() {
            @Override
            public void run() {
                leader.teleport(arena.getDefaultSpawn());
                leader.setEventLeaderItems();
                state = EventGameState.WAITING_FOR_PLAYERS;
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
            practicePlayer.startSumoEventCountdownScoreboard();
        }
    }

    @Override
    protected void onStopCountdown(PracticePlayer leader) {
        for(PracticePlayer practicePlayer : eventPlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "Countdown has stopped");
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
            practicePlayer.startSumoEventWaitingScoreboard();
        }
    }

    @Override
    protected void onAddPlayer(PracticePlayer player) {
        if(state == EventGameState.WAITING_FOR_PLAYERS){
            player.startSumoEventWaitingScoreboard();
        }
        if(state == EventGameState.COUNTDOWN){
            player.startSumoEventCountdownScoreboard();
        }
        if(state != EventGameState.WAITING_FOR_PLAYERS && state != EventGameState.COUNTDOWN) {
            player.startSumoEventFightingScoreboard();
        }
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
        broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Random selecting players...");
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
        broadcast(ChatColor.YELLOW + "(Event) " + HotsCore.getHotsPlayer(fightingPlayers[0].getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(fightingPlayers[1].getPlayer()).getColorName());
        for(PracticePlayer practicePlayer : fightingPlayers){
            practicePlayer.teleport(getPlayerSpawnLocation(practicePlayer));
            practicePlayer.clearInventory();
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            practicePlayer.setFrozen(true);
            time = 4;
            startCooldown = new Cooldown(time*1000);
            state = EventGameState.PREPARING;
        }
    }

    @Override
    protected void tick() {
        if(time >= 0) {
            if(state != EventGameState.WAITING_FOR_PLAYERS && state != EventGameState.FIGHTING){
                time--;
            }
        }

        if(state == EventGameState.COUNTDOWN){
            if (getTime() <= 5 && time != 0) {
                for (PracticePlayer practicePlayer : eventPlayers) {
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
                }
            }
        }

        if(state == EventGameState.PREPARING){
            if (getTime() <= 3 && time != 0) {
                for(PracticePlayer practicePlayer : fightingPlayers){
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 0.5F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Starting in " + ChatColor.WHITE + time + "s");
                }
            }
        }

        if(state == EventGameState.FIGHTING){
            time++;
        }

        //0になったとき
        if (time <= 0) {
            if (state == EventGameState.COUNTDOWN) {
                randomSelectPlayer();
                for(PracticePlayer practicePlayer : eventPlayers){
                    practicePlayer.startSumoEventFightingScoreboard();
                    if(practicePlayer != fightingPlayers[0] && practicePlayer != fightingPlayers[1]){
                        practicePlayer.teleport(arena.getDefaultSpawn());
                    }
                }
                return;
            }
            if (state == EventGameState.PREPARING) {
                for(PracticePlayer practicePlayer : fightingPlayers){
                    state = EventGameState.FIGHTING;
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
