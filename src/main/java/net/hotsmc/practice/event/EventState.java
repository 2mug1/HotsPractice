package net.hotsmc.practice.event;

import lombok.Getter;

public enum EventState {

    ARENA_PREPARING("Arena Preparing"),
    WAITING_FOR_PLAYERS("Waiting For Players"),
    COUNTDOWN("Countdown"),
    PREPARING("Preparing"),
    FIGHTING("Fighting");

    @Getter
    private final String name;

    EventState(String name){
        this.name = name;
    }
}
