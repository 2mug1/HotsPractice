package net.hotsmc.practice.game;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.game.task.DuelGame;
import net.hotsmc.practice.game.task.Game;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.kit.KitType;

import java.util.List;

public class GameManager {

    @Getter
    private List<Game> games;

    public GameManager() {
        games = Lists.newArrayList();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public int countDuelGame(RankedType rankedType, KitType kitType) {
        int count = 0;
        for (Game game : games) {
            if (game instanceof DuelGame) {
                DuelGame duelGame = (DuelGame) game;
                if (rankedType == duelGame.getRankedType() && duelGame.getKitType() == kitType) {
                    count++;
                }
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
    public Game getPlayerOfGame(PracticePlayer practicePlayer) {
        for (Game game : games) {
            for (PracticePlayer player : game.getGamePlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return game;
                }
            }
        }
        return null;
    }
}
