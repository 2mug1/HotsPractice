package net.hotsmc.practice.game;

import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.games.DuelGame;
import net.hotsmc.practice.game.games.Game;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.menus.event.EventMenu;

import java.util.ArrayList;
import java.util.List;

public class EventGameManager {

    @Getter
    private List<EventGame> games;

    public EventGameManager(){
        games = new ArrayList<>();
    }

    public void addEventGame(EventGame game) {
        games.add(game);
    }

    public void removeEventGame(EventGame game) {
        games.remove(game);
    }

    public int countEventGame(KitType kitType) {
        int count = 0;
        for (EventGame game : games) {
            if (game.getKitType() == kitType) {
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
    public EventGame getPlayerOfEventGame(PracticePlayer practicePlayer) {
        for (EventGame game : games) {
            for (PracticePlayer player : game.getEventPlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return game;
                }
            }
        }
        return null;
    }

    public EventGame getEventGameByEventName(String eventName) {
        for (EventGame game : games) {
            for (PracticePlayer player : game.getEventPlayers()) {
                if(game.getEventName().equals(eventName)) {
                    return game;
                }
            }
        }
        return null;
    }
}
