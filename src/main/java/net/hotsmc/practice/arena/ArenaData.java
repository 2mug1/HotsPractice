package net.hotsmc.practice.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArenaData {

    private String arenaName;
    private IgnoreWorldLocation spawn1;
    private IgnoreWorldLocation spawn2;
    private IgnoreWorldLocation defaultSpawn;
}
