package net.hotsmc.practice.game.task;

import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.party.Party;

public class PartyTeamGame extends Game {

    private Party party;

    PartyTeamGame(KitType kitType, Arena arena, Party party) {
        super(kitType, arena);
        this.party = party;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onTeleport() {

    }

    @Override
    protected void tickPreGame() {

    }

    @Override
    protected void onPlaying() {

    }

    @Override
    protected void sendWinner(String winner) {

    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void onFinish() {

    }
}
