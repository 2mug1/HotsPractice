package net.hotsmc.practice.handler;

import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.ArenaManager;
import net.hotsmc.practice.event.EventManager;
import net.hotsmc.practice.knockback.KnockbackManager;
import net.hotsmc.practice.match.MatchManager;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.queue.QueueManager;

@Getter
public class ManagerHandler {

    private ArenaManager arenaManager;
    private QueueManager queueManager;
    private MatchManager matchManager;
    private EventManager eventManager;
    private PartyManager partyManager;
    private KnockbackManager knockbackManager;

    public ManagerHandler load(HotsPractice instance){
        arenaManager = new ArenaManager(instance);
        queueManager = new QueueManager();
        matchManager = new MatchManager();
        partyManager = new PartyManager();
        eventManager = new EventManager();
        knockbackManager = new KnockbackManager();

        knockbackManager.load();
        arenaManager.load();
        return this;
    }
}
