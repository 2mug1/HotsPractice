package net.hotsmc.practice.party;

import lombok.Getter;
import lombok.Setter;
import net.hotsmc.practice.*;
import net.hotsmc.practice.match.impl.PartyFFAMatch;
import net.hotsmc.practice.match.impl.PartyTeamMatch;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.queue.DuelPartyRequest;
import net.hotsmc.practice.utility.ChatUtility;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Party {

    public static final int MAX_PLAYER = 10;

    private PartyType type;
    private String partyName;
    private List<PracticePlayer> players;
    private List<String> invitePlayers;
    private List<DuelPartyRequest> duelPartyRequests;
    private boolean inGame = false;
    private ChatColor prefix;

    public Party(PartyType type, PracticePlayer leader){
        this.type = type;
        this.players = new ArrayList<>();
        this.invitePlayers = new ArrayList<>();
        this.duelPartyRequests = new ArrayList<>();
        this.partyName = leader.getPlayer().getName();
        players.add(leader);
    }

    public PracticePlayer getLeader(){
        return players.get(0);
    }

    public PracticePlayer getPartyPlayer(Player player){
        for(PracticePlayer practicePlayer : players){
            if(practicePlayer.getPlayer().getName().equals(player.getName())){
                return practicePlayer;
            }
        }
        return null;
    }

    public DuelPartyRequest getDuelPartyRequestBySendParty(Party sendParty) {
        for (DuelPartyRequest duelPartyRequest : duelPartyRequests) {
            if (duelPartyRequest.getSendParty().getPartyName().equals(sendParty.getPartyName())) {
                return duelPartyRequest;
            }
        }
        return null;
    }

    public void addDuelPartyRequest(Party sendFromParty, LadderType ladderType){
        DuelPartyRequest duelPartyRequest = getDuelPartyRequestBySendParty(sendFromParty);
        if(duelPartyRequest == null){
            duelPartyRequests.add(new DuelPartyRequest(ladderType, sendFromParty, this));
        }else{
            duelPartyRequest.setLadderType(ladderType);
        }

        sendFromParty.getLeader().sendMessage(ChatColor.YELLOW + "You sent party duel to " + partyName + " / あなたは" + partyName + "にPartyDuelを送りました");
        getLeader().sendMessage(ChatColor.GOLD + "You have been received the party duel by " + sendFromParty.getPartyName() + " / あなたは" + sendFromParty.getPartyName() + "からPartyDuelを受け取りました");
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX);
        msg.append("" + ChatColor.YELLOW + ChatColor.UNDERLINE + "Click to Accept / クリックして開始 - " + ladderType.name());
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/duel accept " + sendFromParty.getPartyName()));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/duel accept " + sendFromParty.getPartyName()).create()));
        getLeader().getPlayer().spigot().sendMessage(msg.create());
    }

    public boolean isLeader(PracticePlayer practicePlayer){
        return partyName.equals(practicePlayer.getPlayer().getName());
    }

    private void broadcast(String message){
        for(PracticePlayer partyPlayer : players){
            partyPlayer.sendMessage(message);
        }
    }

    public void invite(PracticePlayer sender, PracticePlayer practicePlayer){
        if(!invitePlayers.contains(practicePlayer.getPlayer().getName())){
            invitePlayers.add(practicePlayer.getPlayer().getName());
        }
        sender.sendMessage(ChatColor.YELLOW + "You sent party invitation to " + practicePlayer.getPlayer().getName() + " / あなたは" + practicePlayer.getPlayer().getName() + "にパーティ招待を送りました");
        practicePlayer.sendMessage(ChatColor.YELLOW + "You were invited to the " + partyName + "'s party / あなたは" + partyName + "のパーティに招待されました");
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX);
        msg.append(""+ ChatColor.YELLOW + ChatColor.UNDERLINE +
                "Click to join the " + partyName + "'s party");
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/party join " + partyName));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/party join " + partyName).create()));
        practicePlayer.getPlayer().spigot().sendMessage(msg.create());
        practicePlayer.getPlayer().playSound(practicePlayer.getPlayer().getLocation(), Sound.LEVEL_UP, 0.5F, 1);
    }

    public void disband() {
        invitePlayers.clear();
        for(PracticePlayer practicePlayer : players){
            practicePlayer.setHotbar(PlayerHotbar.LOBBY);
            practicePlayer.sendMessage(ChatColor.YELLOW + "Party has been disbanded by leader / パーティは解散されました");
        }
        players.clear();
    }

    public boolean hasJoined(PracticePlayer practicePlayer){
        return getPartyPlayer(practicePlayer.getPlayer()) != null;
    }

    public void addPlayer(PracticePlayer practicePlayer) {
        if(hasJoined(practicePlayer)) {
            practicePlayer.sendMessage(ChatColor.RED + "You've already joined this party / あなたは既にこのパーティに所属しています");
            return;
        }
        if (practicePlayer.isInParty()) {
            practicePlayer.sendMessage(ChatColor.RED + "You've already joined other party / あなたは既に他のパーティに所属しています");
            return;
        }
        if (players.size() >= MAX_PLAYER) {
            practicePlayer.sendMessage(ChatColor.RED + "The party is currently full! / 現在パーティは満員です");
            practicePlayer.sendMessage(ChatColor.RED + "Currently party players / 現在のパーティ人数 : " + players.size() + "/" + MAX_PLAYER);
            return;
        }
        if(inGame){
            practicePlayer.sendMessage(ChatColor.RED + "The party players have already been playing / 現在そのパーティのプレイヤー達は戦っているためパーティに参加することができません");
            return;
        }
        if (type == PartyType.Private) {
            if (!invitePlayers.contains(practicePlayer.getPlayer().getName())) {
                practicePlayer.sendMessage(ChatColor.RED + "You weren't invited by " + partyName + " / " + partyName + "から招待を受けていません");
            } else {
                players.add(practicePlayer);
                invitePlayers.remove(practicePlayer.getPlayer().getName());
                practicePlayer.setHotbar(PlayerHotbar.PARTY);
                broadcast(ChatColor.YELLOW + practicePlayer.getPlayer().getName() + " has joined the party / " + practicePlayer.getPlayer().getName() + "がパーティに参加しました");
                practicePlayer.sendMessage(ChatColor.YELLOW + "You have joined the " + partyName + "'s party / " + partyName + "のパーティに参加しました");
            }
            return;
        }
        players.add(practicePlayer);
        invitePlayers.remove(practicePlayer.getPlayer().getName());
        practicePlayer.setHotbar(PlayerHotbar.PARTY);
        broadcast(ChatColor.YELLOW + practicePlayer.getPlayer().getName() + " has joined the party / " + practicePlayer.getPlayer().getName() + "がパーティに参加しました");
        practicePlayer.sendMessage(ChatColor.YELLOW + "You have joined the " + partyName + "'s party / " + partyName + "のパーティに参加しました");
    }


    public void removePlayer(PracticePlayer practicePlayer) {
        players.remove(getPartyPlayer(practicePlayer.getPlayer()));
        practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        broadcast(ChatColor.YELLOW + practicePlayer.getPlayer().getName() + " has left the party / " + practicePlayer.getPlayer().getName() + "がパーティから離脱しました");
        if (isLeader(practicePlayer)) {
            if (players.size() <= 0) {
                PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
                partyManager.removeParty(partyManager.getPartyByName(partyName));
                return;
            }
            PracticePlayer newLeader = players.get(0);
            broadcast(ChatColor.YELLOW + "Party leader has changed to " + newLeader.getPlayer().getName() + " / パーティリーダーが" + newLeader.getPlayer().getName() + "に変更されました");
            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPartyByName(partyName).setPartyName(newLeader.getPlayer().getName());
        }

        if (practicePlayer.isOnline()) {
            practicePlayer.sendMessage(ChatColor.YELLOW + "You have left " + partyName + "'s party / あなたは" + partyName + "のパーティから離脱しました");
        }
    }

    public void kickPlayer(PracticePlayer sender, PracticePlayer practicePlayer) {
        if(!hasJoined(practicePlayer)) {
            sender.sendMessage(ChatColor.RED + practicePlayer.getPlayer().getName() + " isn't in this party / " + practicePlayer.getPlayer().getName() + "はこのパーティに参加していません");
            return;
        }
        players.remove(getPartyPlayer(practicePlayer.getPlayer()));
        practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        broadcast(ChatColor.YELLOW + practicePlayer.getPlayer().getName() + " has kicked from the party / " + practicePlayer.getPlayer().getName() + "がパーティからキックされました");
        practicePlayer.sendMessage(ChatColor.YELLOW + "You have kicked from the " + partyName + "'s party / あなたは" + partyName + "のパーティからキックされました");
    }

    public void startPartyFFAfight(LadderType type){
        for(PracticePlayer practicePlayer : players){
            practicePlayer.clearInventory();
        }
        PartyFFAMatch partyFFAGame = new PartyFFAMatch(type, HotsPractice.getInstance().getArenaFactory().create(type), this);
        partyFFAGame.start();
    }

    public void startPartyTeamFight(LadderType type){
        for(PracticePlayer practicePlayer : players){
            practicePlayer.clearInventory();
        }
        PartyTeamMatch partyTeamGame = new PartyTeamMatch(type, HotsPractice.getInstance().getArenaFactory().create(type), this);
        partyTeamGame.start();
    }

    public List<PracticePlayer> getAlivePlayers(){
        List<PracticePlayer> alive = new ArrayList<>();
        for(PracticePlayer player : players){
            if(player.isAlive()){
                alive.add(player);
            }
        }
        return alive;
    }

    public List<PracticePlayer> getDeadPlayers(){
        List<PracticePlayer> dead = new ArrayList<>();
        for(PracticePlayer player : players){
            if(!player.isAlive()){
                dead.add(player);
            }
        }
        return dead;
    }
}
