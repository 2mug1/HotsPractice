package net.hotsmc.practice.queue;

import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.games.PartyDuelGame;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.party.Party;
import org.bukkit.ChatColor;

@Getter
public class DuelPartyRequest extends DuelRequest {

    private Party sendParty;
    private Party myParty;

    public DuelPartyRequest(KitType kitType, Party sendParty, Party myParty) {
        super(kitType);
        this.sendParty = sendParty;
        this.myParty = myParty;
    }

    @Override
    public void accept() {
        myParty.getDuelPartyRequests().remove(myParty.getDuelPartyRequestBySendParty(sendParty));
        for(PracticePlayer practicePlayer : sendParty.getPlayers()){
            practicePlayer.clearInventory();
            practicePlayer.sendMessage(ChatColor.YELLOW + sendParty.getPartyName() + "'s party" + ChatColor.GRAY + " vs " + ChatColor.YELLOW + myParty.getPartyName() + "'s party");
        }
        for(PracticePlayer practicePlayer : myParty.getPlayers()){
            practicePlayer.clearInventory();
            practicePlayer.sendMessage(ChatColor.YELLOW + myParty.getPartyName() + "'s party" + ChatColor.GRAY + " vs " + ChatColor.YELLOW + sendParty.getPartyName() + "'s party");
        }
        PartyDuelGame partyDuelGame = new PartyDuelGame(getKitType(), HotsPractice.getArenaFactory().create(getKitType()), sendParty, myParty);
        partyDuelGame.start();
    }
}
