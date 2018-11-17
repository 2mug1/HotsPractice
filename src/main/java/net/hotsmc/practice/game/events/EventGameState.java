package net.hotsmc.practice.game.events;

import lombok.Getter;

public enum EventGameState {

    ARENA_PREPARING("Arena Preparing"),
    WAITING_FOR_PLAYERS("Waiting Players"),
    COUNTDOWN("Countdown"),
    PREPARING("Preparing"),
    FIGHTING("Fighting");

    @Getter
    private final String name;

    EventGameState(String name){
        this.name = name;
    }
}
