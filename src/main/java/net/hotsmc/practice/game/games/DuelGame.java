package net.hotsmc.practice.game.games;

import lombok.Getter;
import net.hotsmc.core.utility.PlayerDataUtility;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

@Getter
public class DuelGame extends Game {

    private final PracticePlayer[] practicePlayers;
    private RankedType rankedType;

    public DuelGame(PracticePlayer practicePlayer1, PracticePlayer practicePlayer2, RankedType rankedType, KitType kitType, Arena arena) {
        super(kitType, arena);
        practicePlayers = new PracticePlayer[]{practicePlayer1, practicePlayer2};
        this.rankedType = rankedType;
    }

    @Override
    protected void onStart() {
        HotsPractice.getGameManager().addGame(this);
        for (PracticePlayer practicePlayer : practicePlayers) {
            addPlayer(practicePlayer);
            practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
        }
    }

    @Override
    protected void onTeleport() {
        if (kitType == KitType.Sumo) {
            for (PracticePlayer practicePlayer : practicePlayers) {
                for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                    if (all != practicePlayer && all != getOpponent(practicePlayer)) {
                        practicePlayer.hidePlayer(all);
                        all.hidePlayer(practicePlayer);
                    }
                }
            }
        } else {
            for (PracticePlayer practicePlayer : practicePlayers) {
                for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                    if (all != practicePlayer) {
                        practicePlayer.hidePlayer(all);
                        all.hidePlayer(practicePlayer);
                    }
                }
            }
        }

        for (PracticePlayer practicePlayer : practicePlayers) {
            if (kitType == KitType.Combo) {
                practicePlayer.setMaximumNoDamageTicks(0);
            }
            practicePlayer.teleport(getPlayerSpawnLocation(practicePlayer));
            if (kitType != KitType.Sumo) {
                practicePlayer.setKitItems();
            }
            if (kitType == KitType.Sumo) {
                practicePlayer.setFrozen(true);
            }
            if (kitType == KitType.NoDebuff || kitType == KitType.Debuff) {
                practicePlayer.startEnderpearlItemTask();
            }
            practicePlayer.heal();
        }
        practicePlayers[0].startDuelGameScoreboard(practicePlayers[1].getName());
        practicePlayers[1].startDuelGameScoreboard(practicePlayers[0].getName());
    }

    @Override
    protected void tickPreGame() {
        if (getTime() <= 5 && time != 0) {
            for (PracticePlayer practicePlayer : practicePlayers) {
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 2F);
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
            }
        }
    }

    @Override
    protected void onPlaying() {
        for (PracticePlayer practicePlayer : practicePlayers) {
            practicePlayer.getPlayerData().addPlayCount(rankedType, kitType);
            practicePlayer.getPlayerData().addTotalPlayCount(rankedType);
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
            ChatUtility.sendMessage(practicePlayer, ChatColor.WHITE + "Match has begun!");
            practicePlayer.showPlayer(getOpponent(practicePlayer));
            if (kitType == KitType.Sumo) {
                practicePlayer.setFrozen(false);
            }
        }
    }

    @Override
    protected void sendWinner(String winner) {
        for (PracticePlayer practicePlayer : practicePlayers) {
            practicePlayer.sendMessage(ChatColor.YELLOW + "Match has finished!");
            practicePlayer.sendMessage(ChatColor.YELLOW + "Match Winner: " + PlayerDataUtility.getColorName(winner));
        }
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "Match has finished!");
            practicePlayer.sendMessage(ChatColor.YELLOW + "Match Winner: " + PlayerDataUtility.getColorName(winner));
        }
    }

    @Override
    protected void onEnd() {
        if (kitType != KitType.Sumo && kitType != KitType.Spleef) {

            for (PracticePlayer practicePlayer : practicePlayers) {
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.LEVEL_UP, 0.5F, 1);
                if (kitType == KitType.Combo) {
                    practicePlayer.setMaximumNoDamageTicks(20);
                }
                if (kitType != KitType.Sumo && kitType != KitType.Spleef) {
                    ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.WHITE + "View Inventory: ");
                    msg.append("" + ChatColor.YELLOW + practicePlayers[0].getName());
                    msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inv " + practicePlayers[0].getUUID()));
                    msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.YELLOW + "Click to view " + practicePlayers[0].getName() + "'s inventory").create()));
                    msg.append(" ");
                    msg.append("" + ChatColor.YELLOW + practicePlayers[1].getName());
                    msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inv " + practicePlayers[1].getUUID()));
                    msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.YELLOW + "Click to view " + practicePlayers[1].getName() + "'s inventory").create()));
                    practicePlayer.getPlayer().spigot().sendMessage(msg.create());
                }
            }
            for (PracticePlayer practicePlayer : spectatePlayers) {
                if (kitType != KitType.Sumo && kitType != KitType.Spleef) {
                    ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX + ChatColor.WHITE + "View Inventory: ");
                    msg.append("" + ChatColor.YELLOW + practicePlayers[0].getName());
                    msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inv " + practicePlayers[0].getUUID()));
                    msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.YELLOW + "Click to view " + practicePlayers[0].getName() + "'s inventory").create()));
                    msg.append(" ");
                    msg.append("" + ChatColor.YELLOW + practicePlayers[1].getName());
                    msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inv " + practicePlayers[1].getUUID()));
                    msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.YELLOW + "Click to view " + practicePlayers[1].getName() + "'s inventory").create()));
                    practicePlayer.getPlayer().spigot().sendMessage(msg.create());
                }
            }
        }
    }

    @Override
    protected void onFinish() {
        for (PracticePlayer practicePlayer : practicePlayers) {
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!all.isInGame()) {
                    if (all != practicePlayer) {
                        practicePlayer.showPlayer(all);
                        all.showPlayer(practicePlayer);
                    }
                }
            }
            practicePlayer.resetPlayer();
            practicePlayer.teleportToLobby();
            practicePlayer.startLobbyScoreboard();
            practicePlayer.setClickItems();
        }
    }

    public PracticePlayer getOpponent(PracticePlayer me) {
        for (PracticePlayer practicePlayer : practicePlayers) {
            if (!practicePlayer.getName().equals(me.getName())) {
                return practicePlayer;
            }
        }
        return null;
    }

    public Location getPlayerSpawnLocation(PracticePlayer me) {
        if (practicePlayers[0].getName().equals(me.getName())) {
            return arena.getSpawn1();
        }
        if (practicePlayers[1].getName().equals(me.getName())) {
            return arena.getSpawn2();
        }
        return null;
    }
}
