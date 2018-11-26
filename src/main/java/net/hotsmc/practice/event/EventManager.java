package net.hotsmc.practice.event;

import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.ladder.LadderType;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    @Getter
    private List<Event> games;

    public EventManager(){
        games = new ArrayList<>();
    }

    public void addEventGame(Event game) {
        games.add(game);
    }

    public void removeEventGame(Event game) {
        games.remove(game);
    }

    public int countEventGame(LadderType ladderType) {
        int count = 0;
        for (Event game : games) {
            if (game.getLadderType() == ladderType) {
                count++;
            }
        }
        return count;
    }


    /**
     * プレイヤーが参加しているゲームを返す
     *
     * @param practicePlayer
     * @return
     */
    public Event getPlayerOfEventGame(PracticePlayer practicePlayer) {
        for (Event game : games) {
            for (PracticePlayer player : game.getEventPlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return game;
                }
            }
        }
        return null;
    }

    public Event getEventGameByEventName(String eventName) {
        for (Event game : games) {
            for (PracticePlayer player : game.getEventPlayers()) {
                if(game.getHost().equals(eventName)) {
                    return game;
                }
            }
        }
        return null;
    }
}
