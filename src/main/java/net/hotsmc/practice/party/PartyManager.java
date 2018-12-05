package net.hotsmc.practice.party;

import lombok.Getter;
import net.hotsmc.practice.player.PracticePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * パーティ管理
 */
@Getter
public class PartyManager {

    private List<Party> parties;

    public PartyManager(){
        this.parties = new ArrayList<>();
    }

    public boolean exsitsParty(String partyName){
        return getPartyByName(partyName) != null;
    }

    /**
     * プレイヤーが参加しているパーティを返す
     * @param player
     * @return
     */
    public Party getPlayerOfParty(PracticePlayer player){
        for(Party party : parties){
            for(PracticePlayer practicePlayer : party.getPlayers()){
                if(practicePlayer.getPlayer().getName().equals(player.getPlayer().getName())){
                    return party;
                }
            }
        }
        return null;
    }

    /**
     * パーティ名からパーティを返す
     * @param partyName
     * @return
     */
    public Party getPartyByName(String partyName){
        for(Party party : parties){
            if(party.getPartyName().equals(partyName)){
                return party;
            }
        }
        return null;
    }

    /**
     * 追加
     * @param party
     */
    public void addParty(Party party){
        parties.add(party);
    }

    /**
     * 削除
     * @param party
     */
    public void removeParty(Party party){
        parties.remove(party);
    }
}
