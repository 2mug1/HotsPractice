package net.hotsmc.practice.other;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.practice.player.PracticePlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team {

    private String teamName;
    private String prefix;
    private List<PracticePlayer> players;

    public Team(String teamName, String prefix){
        this.teamName = teamName;
        this.prefix = prefix;
        this.players = new ArrayList<>();
    }

    public PracticePlayer getTeamPlayer(PracticePlayer practicePlayer){
        for(PracticePlayer teamPlayer : players){
            if(teamPlayer.getUUID().equals(practicePlayer.getUUID())){
                return teamPlayer;
            }
        }
        return null;
    }

    public void addPlayer(PracticePlayer teamPlayer){
        NametagAPI.setPrefix(teamPlayer.getName(), prefix);
        players.add(teamPlayer);
        teamPlayer.sendMessage(ChatColor.GRAY + "You have joined " + prefix + teamName);
    }

    public List<PracticePlayer> getAlivePlayers(){
        List<PracticePlayer> alive = new ArrayList<>();
        for(PracticePlayer teamPlayer : players){
            if(teamPlayer.isAlive()){
                alive.add(teamPlayer);
            }
        }
        return alive;
    }

    public List<PracticePlayer> getDeadPlayers(){
        List<PracticePlayer> dead = new ArrayList<>();
        for(PracticePlayer teamPlayer : players){
            if(!teamPlayer.isAlive()){
                dead.add(teamPlayer);
            }
        }
        return dead;
    }


    public void removeAllPlayer() {
        for(PracticePlayer teamPlayer : players){
            HotsPlayer hotsPlayer = HotsCore.getHotsPlayer(teamPlayer.getPlayer());
            if(hotsPlayer != null) {
                hotsPlayer.updateNameTag();
            }
        }
        players.clear();
    }
}
