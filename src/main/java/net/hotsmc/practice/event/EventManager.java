package net.hotsmc.practice.event;

import lombok.Getter;
import net.hotsmc.core.other.Cooldown;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.ladder.LadderType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EventManager {

    private List<Event> events;
    private Cooldown eventCooldown = new Cooldown(0);

    public EventManager(){
        events = new ArrayList<>();
    }

    public void addEventGame(Event game) {
        events.add(game);
    }

    public void removeEventGame(Event game) {
        events.remove(game);
    }

    public int countEventGame(LadderType ladderType) {
        int count = 0;
        for (Event game : events) {
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
    public Event getPlayerOfEvent(PracticePlayer practicePlayer) {
        for (Event event : events) {
            for (PracticePlayer player : event.getEventPlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return event;
                }
            }
        }
        return null;
    }

    public Event getEventByEventHost(String eventHost) {
        for (Event event : events) {
            if (event.getHost().equals(eventHost)) {
                return event;
            }
        }
        return null;
    }

    public void startEventCooldown(){
        eventCooldown = new Cooldown(600*1000);
    }
}
