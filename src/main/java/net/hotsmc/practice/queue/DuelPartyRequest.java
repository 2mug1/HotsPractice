package net.hotsmc.practice.queue;

import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.match.impl.PartyDuelMatch;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.party.Party;
import org.bukkit.ChatColor;

@Getter
public class DuelPartyRequest extends DuelRequest {

    private Party sendParty;
    private Party myParty;

    public DuelPartyRequest(LadderType ladderType, Party sendParty, Party myParty) {
        super(ladderType);
        this.sendParty = sendParty;
        this.myParty = myParty;
    }

    @Override
    public void accept() {
        myParty.getDuelPartyRequests().remove(myParty.getDuelPartyRequestBySendParty(sendParty));
        for(PracticePlayer practicePlayer : sendParty.getPlayers()){
            practicePlayer.clearInventory();
            practicePlayer.sendMessage(ChatColor.GRAY + "Starting party match (" + ChatColor.YELLOW + sendParty.getPartyName() + "'s party" + ChatColor.GRAY + " vs " + ChatColor.YELLOW + myParty.getPartyName() + "'s party" + ChatColor.GRAY + ")");
        }
        for(PracticePlayer practicePlayer : myParty.getPlayers()){
            practicePlayer.clearInventory();
            practicePlayer.sendMessage(ChatColor.GRAY + "Starting party match (" + ChatColor.YELLOW + myParty.getPartyName() + "'s party" + ChatColor.GRAY + " vs " + ChatColor.YELLOW + sendParty.getPartyName() + "'s party" + ChatColor.GRAY + ")");
        }
        PartyDuelMatch partyDuelGame = new PartyDuelMatch(this.getLadderType(), HotsPractice.getArenaFactory().create(this.getLadderType()), sendParty, myParty);
        partyDuelGame.start();
    }
}
