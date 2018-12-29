package net.hotsmc.practice.match.impl;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

@Getter
public class PartyDuelMatch extends Match {

    private Party[] parties;

    public PartyDuelMatch(LadderType ladderType, Arena arena, Party party1, Party party2) {
        super(ladderType, arena);
        parties = new Party[]{party1, party2};
    }

    @Override
    protected void onStart() {
        HotsPractice.getInstance().getManagerHandler().getMatchManager().addGame(this);
        for (Party party : parties) {
            party.getDuelPartyRequests().clear();
            party.setInGame(true);
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.getDuelMatchRequests().clear();
                addPlayer(practicePlayer);
                practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
            }
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
            }else{
                practicePlayer.setMaximumNoDamageTicks(19);
            }
            parties[0].setPrefix(ChatColor.GREEN);
            parties[1].setPrefix(ChatColor.RED);
            if (parties[0].getPlayers().contains(practicePlayer)) {
                practicePlayer.setPrefix(ChatColor.GREEN + "");
                practicePlayer.teleport(arena.getSpawn1());
            }
            if (parties[1].getPlayers().contains(practicePlayer)) {
                practicePlayer.setPrefix(ChatColor.RED + "");
                practicePlayer.teleport(arena.getSpawn2());
            }
            practicePlayer.heal();
            practicePlayer.setKitHotbar();
        }
    }

    @Override
    protected void tickPreGame() {
        if (getTime() <= 5 && time != 0) {
            for (Party party : parties) {
                for (PracticePlayer practicePlayer : party.getPlayers()) {
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 2F);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
                }
            }
        }
    }

    @Override
    protected void onPlaying() {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Match has begun!");
            }
        }
    }

    @Override
    protected void sendWinner(String winner) {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.sendMessage(ChatColor.YELLOW + "Match has finished!");
                practicePlayer.sendMessage(ChatColor.GRAY + "Match Winner: " + ChatColor.WHITE + winner + "'s party");
            }
        }
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "Match has finished!");
            practicePlayer.sendMessage(ChatColor.GRAY + "Match Winner: " + ChatColor.WHITE + winner + "'s party");
        }
    }

    @Override
    protected void onEnd() {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.resetPlayer();
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.LEVEL_UP, 0.5F, 1);
                if (ladderType == LadderType.Combo) {
                    practicePlayer.setMaximumNoDamageTicks(20);
                }
            }
        }
    }

    @Override
    protected void onFinish() {
        for (Party party : parties) {
            party.setInGame(false);
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                if (practicePlayer.isEnableSpectate()) {
                    practicePlayer.disableSpectate();
                }
                practicePlayer.resetPlayer();
                practicePlayer.teleportToLobby();
                practicePlayer.setHotbar(PlayerHotbar.PARTY);
                practicePlayer.setAlive(true);
                NametagAPI.resetNametag(practicePlayer.getName());
            }
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

    public Party getOpponent(Party myParty) {
        for (Party party : parties) {
            if (!party.getPartyName().equals(myParty.getPartyName())) {
                return party;
            }
        }
        return null;
    }
}
