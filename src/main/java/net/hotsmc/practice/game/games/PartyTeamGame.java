package net.hotsmc.practice.game.games;

import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.other.Team;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyTeamGame extends Game {

    private Party party;
    public Team[] teams = new Team[2];

    public PartyTeamGame(KitType kitType, Arena arena, Party party) {
        super(kitType, arena);
        this.party = party;
        teams[0] = new Team("Green", ChatColor.GREEN + "");
        teams[1] = new Team("Red", ChatColor.RED + "");
    }

    @Override
    protected void onStart() {
        party.setInGame(true);
        HotsPractice.getGameManager().addGame(this);
        for (PracticePlayer practicePlayer : party.getPlayers()) {
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
            if (kitType == KitType.Combo) {
                practicePlayer.setMaximumNoDamageTicks(0);
            }
            if (kitType == KitType.NoDebuff || kitType == KitType.Debuff) {
                practicePlayer.startEnderpearlItemTask();
            }
            practicePlayer.startPartyTeamScoreboard();
            practicePlayer.heal();
            practicePlayer.setKitItems();
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
            if (kitType == KitType.Combo) {
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
            practicePlayer.startPartyScoreboard();
            practicePlayer.setPartyClickItems();
            practicePlayer.setAlive(true);
            HotsCore.getHotsPlayer(practicePlayer.getPlayer()).updateNameTag();
        }
        for(Team team : teams){
            team.removeAllPlayer();
        }
        for (PracticePlayer practicePlayer : gamePlayers) {
            practicePlayer.setEnableSpectate(false);
            for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                if (!all.isInGame()) {
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