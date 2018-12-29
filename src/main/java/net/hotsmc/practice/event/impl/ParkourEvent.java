package net.hotsmc.practice.event.impl;

import net.hotsmc.core.other.Cooldown;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class ParkourEvent extends Event {

    public ParkourEvent(Arena arena, PracticePlayer leader) {
        super(arena, leader, 50, 5, 40);
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
                leader.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "You have create a new " + ChatColor.YELLOW + ChatColor.BOLD + "Parkour Event");
                cancel();
            }
        }.runTaskLater(HotsPractice.getInstance(), 20 * 3);
    }

    @Override
    protected void onStartCountdown(PracticePlayer leader) {
        startCooldown = new Cooldown(time * 1000);
        for (PracticePlayer practicePlayer : eventPlayers) {
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.CLICK, 1F, 2F);
        }
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.YELLOW + "(Event) ");
        msg.append("" + ChatColor.GRAY + "Starting Soon: ");
        msg.append(ChatColor.WHITE + getHost() + "'s " + ladderType.name() + " Parkour Event ");
        msg.append("" + ChatColor.AQUA + ChatColor.BOLD + "Click to Join");
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/event join " + getHost()));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/event join " + getHost()).create()));
        HotsPractice.broadcast(msg.create());
    }

    @Override
    protected void onStopCountdown(PracticePlayer leader) {
        for (PracticePlayer practicePlayer : eventPlayers) {
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

    @Override
    protected void tick() {
        if (time >= 0) {
            if (state != EventState.WAITING_FOR_PLAYERS && state != EventState.FIGHTING) {
                time--;
            }
        }

        if (state == EventState.COUNTDOWN) {
            if (getTime() == 40 || getTime() == 30 || getTime() == 20 || getTime() == 10) {
                ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.YELLOW + "(Event) ");
                msg.append("" + ChatColor.GRAY + "Starting Soon: ");
                msg.append(ChatColor.WHITE + getHost() + "'s " + ladderType.name() + " Parkour Event ");
                msg.append("" + ChatColor.AQUA + ChatColor.BOLD + "Click to Join");
                msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/event join " + getHost()));
                msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/event join " + getHost()).create()));
                HotsPractice.broadcast(msg.create());
                for (PracticePlayer practicePlayer : eventPlayers) {
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

        if (state == EventState.PREPARING) {
            if (getTime() <= 3 && time != 0) {
                for (PracticePlayer practicePlayer : getWinningPlayers()) {
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 0.5F);
                    practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Parkour starts in " + ChatColor.WHITE + time + "s");
                }
            }
        }

        if (state == EventState.FIGHTING) {
            time++;
        }

        //0になったとき
        if (time <= 0) {
            if (state == EventState.COUNTDOWN) {
                for (PracticePlayer practicePlayer : getWinningPlayers()) {
                    practicePlayer.teleport(arena.getSpawn1());
                    practicePlayer.setFrozen(true);
                }
            }
            return;
        }
        if (state == EventState.PREPARING) {
            for (PracticePlayer practicePlayer : getWinningPlayers()) {
                state = EventState.FIGHTING;
                practicePlayer.setFrozen(false);
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1.0F);
                practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GRAY + "Parkour has begun!");
            }
        }




    }

    @Override
    public boolean isParkour() {
        return true;
    }
}
