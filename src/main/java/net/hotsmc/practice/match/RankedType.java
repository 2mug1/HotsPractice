package net.hotsmc.practice.match;

import lombok.Getter;

@Getter
public enum RankedType {

    RANKED("Ranked"),
    UNRANKED("Unranked");

    private String name;

    RankedType(String name){
        this.name = name;
    }
}
