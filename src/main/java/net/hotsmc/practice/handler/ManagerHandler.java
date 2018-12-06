package net.hotsmc.practice.handler;

import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.ArenaManager;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.event.EventManager;
import net.hotsmc.practice.match.MatchInventoryManager;
import net.hotsmc.practice.knockback.KnockbackManager;
import net.hotsmc.practice.ladder.LadderEditManager;
import net.hotsmc.practice.match.MatchManager;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.queue.QueueManager;

@Getter
public class ManagerHandler {

    private ArenaManager arenaManager;
    private QueueManager queueManager;
    private MatchManager matchManager;
    private EventManager eventManager;
    private LadderEditManager ladderEditManager;
    private PartyManager partyManager;
    private MatchInventoryManager matchInventoryManager;
    private KnockbackManager knockbackManager;

    public ManagerHandler load(HotsPractice instance){
        arenaManager = new ArenaManager(instance);
        ladderEditManager = new LadderEditManager(new ConfigCursor(new FileConfig(instance, "KitEditLocations.yml"), "Locations"));
        queueManager = new QueueManager();
        matchManager = new MatchManager();
        partyManager = new PartyManager();
        eventManager = new EventManager();
        matchInventoryManager = new MatchInventoryManager();
        knockbackManager = new KnockbackManager();

        knockbackManager.load();
        ladderEditManager.load();
        arenaManager.load();
        return this;
    }
}
