package net.hotsmc.practice.match.impl;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.other.Team;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.Collections;
import java.util.List;

@Getter
public class PartyTeamMatch extends Match {

    private Party party;
    private Team[] teams = new Team[2];

    public PartyTeamMatch(LadderType ladderType, Arena arena, Party party) {
        super(ladderType, arena);
        this.party = party;
        teams[0] = new Team("Green", ChatColor.GREEN + "");
        teams[1] = new Team("Red", ChatColor.RED + "");
    }

    @Override
    protected void onStart() {
        party.setInGame(true);
        HotsPractice.getInstance().getManagerHandler().getMatchManager().addGame(this);
        for (PracticePlayer practicePlayer : party.getPlayers()) {
            practicePlayer.getDuelMatchRequests().clear();
            addPlayer(practicePlayer);
            practicePlayer.sendMessage(ChatColor.YELLOW + "Preparing the Team Fight");
            practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
        }
    }

    @Override
    protected void onTeleport() {
        Collections.shuffle(gamePlayers);
        List<PracticePlayer> green = gamePlayers.subList(0, gamePlayers.size()/2);
        List<PracticePlayer> red = gamePlayers.subList(gamePlayers.size()/2, gamePlayers.size());
        for(PracticePlayer player : green){
            teams[0].addPlayer(player);
            player.teleport(arena.getSpawn1());
        }
        for(PracticePlayer player : red){
            teams[1].addPlayer(player);
            player.teleport(arena.getSpawn2());
        }
        for (PracticePlayer practicePlayer : gamePlayers) {
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!containsGamePlayer(all)) {
                    practicePlayer.hidePlayer(all);
                    if (all != practicePlayer) {
                        all.hidePlayer(practicePlayer);
                    }
                }
            }
            if (ladderType == LadderType.Combo) {
                practicePlayer.setMaximumNoDamageTicks(0);
            }else{
                practicePlayer.setMaximumNoDamageTicks(19);
            }
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
            practicePlayer.sendMessage(ChatColor.YELLOW + "Team Fight has finished!");
            practicePlayer.sendMessage(ChatColor.GRAY + "Winner Team: " + winner);
        }
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.sendMessage(ChatColor.YELLOW + "Team Fight has finished!");
            practicePlayer.sendMessage(ChatColor.GRAY + "Winner Team: " + winner);
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
            NametagAPI.resetNametag(practicePlayer.getName());
        }
        for(Team team : teams){
            team.removeAllPlayer();
        }
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.setEnableSpectate(false);
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!all.isInMatch()) {
                    if (all != practicePlayer) {
                        practicePlayer.showPlayer(all);
                        all.showPlayer(practicePlayer);
                    }
                }
            }
        }
    }

    public Team getMyTeam(PracticePlayer me){
        if(teams[0].getPlayers().contains(me)){
            return teams[0];
        }
        if(teams[1].getPlayers().contains(me)){
            return teams[1];
        }
        return null;
    }

    public Team getOpponentTeam(Team team){
        for(Team ohterTeam : teams){
            if(!ohterTeam.getTeamName().equals(team.getTeamName())){
                return ohterTeam;
            }
        }
        return null;
    }
}