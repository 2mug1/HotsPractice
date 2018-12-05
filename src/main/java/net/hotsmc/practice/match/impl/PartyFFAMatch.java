package net.hotsmc.practice.match.impl;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@Getter
public class PartyFFAMatch extends Match {

    private Party party;

    public PartyFFAMatch(LadderType ladderType, Arena arena, Party party) {
        super(ladderType, arena);
        this.party = party;
    }

    @Override
    protected void onStart() {
        party.setInGame(true);
        HotsPractice.getInstance().getManagerHandler().getMatchManager().addGame(this);
        for (PracticePlayer practicePlayer : party.getPlayers()) {
            practicePlayer.getDuelGameRequests().clear();
            addPlayer(practicePlayer);
            practicePlayer.sendMessage(ChatColor.YELLOW + "Preparing the FFA Fight");
            practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
        }
    }

    @Override
    protected void onTeleport() {
        for (PracticePlayer practicePlayer : gamePlayers) {
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!containsGamePlayer(all)) {
                    practicePlayer.hidePlayer(all);
                    if (all != practicePlayer) {
                        all.hidePlayer(practicePlayer);
                    }
                }
            }
        }
        for (PracticePlayer practicePlayer : gamePlayers) {
            if (ladderType == LadderType.Combo) {
                practicePlayer.setMaximumNoDamageTicks(0);
            }
            practicePlayer.teleport(arena.getSpawn1());
            practicePlayer.heal();
            practicePlayer.setKitHotbar();
        }
    }

    @Override
    protected void tickPreGame() {
        if (getTime() <= 5 && time != 0) {
            for (PracticePlayer practicePlayer : gamePlayers) {
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 2F);
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
            }
        }
    }

    @Override
    protected void onPlaying() {
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
            ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Match has begun!");
        }
    }

    @Override
    protected void sendWinner(String winner) {
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.sendMessage(ChatColor.YELLOW + "FFA Fight has finished!");
            practicePlayer.sendMessage(ChatColor.GRAY + "Winner: " + ChatColor.WHITE + winner);
        }
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "FFA Fight has finished!");
            practicePlayer.sendMessage(ChatColor.GRAY + "Winner: " + ChatColor.WHITE + winner);
        }
    }

    @Override
    protected void onEnd() {
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.resetPlayer();
            practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.LEVEL_UP, 0.5F, 1);
            if (ladderType == LadderType.Combo) {
                practicePlayer.setMaximumNoDamageTicks(20);
            }
        }
    }

    @Override
    protected void onFinish() {
        party.setInGame(false);
        for (PracticePlayer practicePlayer : party.getPlayers()) {
            if (practicePlayer.isEnableSpectate()) {
                practicePlayer.disableSpectate();
            }
            practicePlayer.resetPlayer();
            practicePlayer.teleportToLobby();
            practicePlayer.setHotbar(PlayerHotbar.PARTY);
            practicePlayer.setAlive(true);
            HotsCore.getHotsPlayer(practicePlayer.getPlayer()).updateNameTag();
        }
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.setEnableSpectate(false);
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!all.isInMatch() && !HotsCore.getHotsPlayer(all.getPlayer()).isVanish()) {
                    if (all != practicePlayer) {
                        practicePlayer.showPlayer(all);
                        all.showPlayer(practicePlayer);
                    }
                }
            }
        }
    }
}
