package net.hotsmc.practice.match;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.impl.DuelMatch;

import java.util.List;

public class MatchManager {

    @Getter
    private List<Match> matches;

    public MatchManager() {
        matches = Lists.newArrayList();
    }

    public void addGame(Match match) {
        matches.add(match);
    }

    public void removeGame(Match match) {
        matches.remove(match);
    }

    public int countDuelGame(RankedType rankedType, LadderType ladderType) {
        int count = 0;
        for (Match match : matches) {
            if (match instanceof DuelMatch) {
                DuelMatch duelGame = (DuelMatch) match;
                if (rankedType == duelGame.getRankedType() && duelGame.getLadderType() == ladderType) {
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
    public Match getPlayerOfGame(PracticePlayer practicePlayer) {
        for (Match match : matches) {
            for (PracticePlayer player : match.getGamePlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return match;
                }
            }
        }
        return null;
    }

    public Match getPlayerOfSpectatingGame(PracticePlayer practicePlayer){
        for (Match match : matches) {
            for (PracticePlayer player : match.getSpectatePlayers()) {
                if (player.getName().equals(practicePlayer.getName())) {
                    return match;
                }
            }
        }
        return null;
    }
}
