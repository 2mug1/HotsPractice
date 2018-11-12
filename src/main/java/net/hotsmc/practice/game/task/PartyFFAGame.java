package net.hotsmc.practice.game.task;

import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.party.Party;
import org.bukkit.ChatColor;

@Getter
public class PartyFFAGame extends Game {

    private Party party;

    public PartyFFAGame(KitType kitType, Arena arena, Party party) {
        super(kitType, arena);
        this.party = party;
    }

    @Override
    protected void onStart() {
        HotsPractice.getGameManager().addGame(this);
        for (PracticePlayer practicePlayer : party.getPlayers()) {
            addPlayer(practicePlayer);
            practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
        }
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
