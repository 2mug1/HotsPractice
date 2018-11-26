package net.hotsmc.practice;

import net.hotsmc.core.other.Style;
import net.hotsmc.core.scoreboard.Board;
import net.hotsmc.core.scoreboard.BoardAdapter;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.*;
import net.hotsmc.practice.match.impl.DuelMatch;
import net.hotsmc.practice.match.impl.PartyDuelMatch;
import net.hotsmc.practice.match.impl.PartyFFAMatch;
import net.hotsmc.practice.match.impl.PartyTeamMatch;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.queue.Queue;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class PracticeScoreboardAdapter implements BoardAdapter {

    private static final String DISPLAY_NAME = "" + Style.YELLOW + ChatColor.BOLD + "Practice";

    @Override
    public String getTitle(Player player) {
        return DISPLAY_NAME;
    }

    @Override
    public List<String> getScoreboard(Player player, Board board) {
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        final List<String> toReturn = new ArrayList<>();
        if (practicePlayer == null) return null;

        if (practicePlayer.isInLobby()) {
            toReturn.add(Style.YELLOW + "Online: " + Style.WHITE + HotsPractice.countOnline());
            toReturn.add(Style.YELLOW + "Queuing: " + Style.WHITE + HotsPractice.getCountQueue());
            toReturn.add(Style.YELLOW + "Fighting: " + Style.WHITE + HotsPractice.getCountFight());
            toReturn.add(Style.YELLOW + "Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");

            if (practicePlayer.isInQueue()) {
                final Queue queue = practicePlayer.getInQueue();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(Style.YELLOW + "Queue:");
                toReturn.add(" " + Style.WHITE + queue.getRankedType().getName() + " " + queue.getLadderType());
                toReturn.add(Style.YELLOW + "Time: ");
                toReturn.add(" " + TimeUtility.formatTime(queue.getPassed()));
            }

            if (practicePlayer.isInParty()) {
                final Party party = practicePlayer.getInParty();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(Style.YELLOW + "Party: " + Style.WHITE + party.getPartyName());
                toReturn.add(Style.YELLOW + "Type: " + Style.WHITE + party.getType().name());
                toReturn.add(Style.YELLOW + "Players: " + Style.WHITE + party.getPlayers().size() + "/" + Party.MAX_PLAYER);
            }

            if (practicePlayer.isEnableKitEdit()) {
                final LadderType type = practicePlayer.getEditLadderType();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(Style.GRAY + Style.BOLD + "Kit Editor");
                toReturn.add(Style.YELLOW + "Ladder: " + ChatColor.WHITE + type.name());
            }

        } else if (practicePlayer.isInMatch()) {
            final Match match = practicePlayer.getInMatch();
            final MatchState state = match.getState();

            toReturn.add(Style.YELLOW + "Ladder: " + Style.WHITE + match.getLadderType());

            if (state == MatchState.PreGame) {
                toReturn.add(Style.YELLOW + "Starting: " + Style.WHITE + match.getStartCooldown().getTimeLeft() + "s");
            } else {
                toReturn.add(Style.YELLOW + "Duration: " + Style.WHITE + TimeUtility.timeFormat(match.getTime()));
            }

            if (match instanceof DuelMatch) {
                final DuelMatch duelMatch = (DuelMatch) match;
                toReturn.add(Style.YELLOW + "Opponent: " + Style.WHITE + duelMatch.getOpponent(practicePlayer).getName());
                toReturn.add("");
                toReturn.add(Style.YELLOW + "Your Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");
                toReturn.add(Style.YELLOW + "Their Ping: " + Style.WHITE + duelMatch.getOpponent(practicePlayer).getPing() + "ms");
            }
            if (match instanceof PartyDuelMatch) {
                final PartyDuelMatch partyDuelMatch = (PartyDuelMatch) match;
                final Party party = practicePlayer.getInParty();
                toReturn.add(Style.YELLOW + "Your Party: " + party.getPrefix() + party.getPartyName());
                toReturn.add(partyDuelMatch.getParties()[0].getPrefix() + partyDuelMatch.getParties()[0].getPartyName() + ": " + Style.WHITE + partyDuelMatch.getParties()[0].getAlivePlayers().size());
                toReturn.add(partyDuelMatch.getParties()[1].getPrefix() + partyDuelMatch.getParties()[1].getPartyName() + ": " + Style.WHITE + partyDuelMatch.getParties()[1].getAlivePlayers().size());
                toReturn.add("");
                toReturn.add(Style.YELLOW + "Your Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");
            }
            if (match instanceof PartyFFAMatch) {
                final PartyFFAMatch partyFFAMatch = (PartyFFAMatch) match;
                toReturn.add(Style.YELLOW + "Party Fight: " + Style.WHITE + "FFA");
                toReturn.add(Style.YELLOW + "Alive: " + Style.WHITE + partyFFAMatch.getParty().getAlivePlayers().size());
                toReturn.add("");
                toReturn.add(Style.YELLOW + "Your Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");
            }
            if (match instanceof PartyTeamMatch) {
                final PartyTeamMatch partyTeamMatch = (PartyTeamMatch) match;
                toReturn.add(Style.YELLOW + "Party Fight: " + Style.WHITE + "Team");
                net.hotsmc.practice.other.Team myTeam = partyTeamMatch.getMyTeam(practicePlayer);
                if(myTeam != null) {
                    toReturn.add(Style.YELLOW + "Your Team: " + myTeam.getPrefix() + myTeam.getTeamName());
                }
                net.hotsmc.practice.other.Team teamA = partyTeamMatch.getTeams()[0];
                if(teamA != null) {
                    toReturn.add(teamA.getPrefix() + teamA.getTeamName() + ": " + Style.WHITE + teamA.getAlivePlayers().size());
                }
                net.hotsmc.practice.other.Team teamB = partyTeamMatch.getTeams()[1];
                if(teamB != null) {
                    toReturn.add(teamB.getPrefix() + teamB.getTeamName() + ": " + Style.WHITE + teamB.getAlivePlayers().size());
                }
                toReturn.add("");
                toReturn.add(Style.YELLOW + "Your Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");
            }

            if (!practicePlayer.getEnderpearlCooldown().hasExpired()) {
                toReturn.add(Style.YELLOW + "Enderpearl: " + Style.WHITE + practicePlayer.getEnderpearlCooldown().getTimeLeft() + "s");
            }
        } else if (practicePlayer.isInEvent()) {
            final Event event = practicePlayer.getInEventGame();
            final EventState state = event.getState();
            if (event instanceof SumoEvent) {
                toReturn.add(Style.YELLOW + "Event: " + Style.WHITE + "Sumo");
                toReturn.add(Style.YELLOW + "Host: " + Style.WHITE + event.getHost());
                toReturn.add(Style.YELLOW + "State: " + Style.WHITE + event.getState().getName());
                toReturn.add(Style.YELLOW + "Players: " + Style.WHITE + event.getWinningPlayers().size() + "/" + event.getMaxPlayers());
                toReturn.add(Style.YELLOW + "Ping: " + Style.WHITE + practicePlayer.getPing() + "ms");

                if (state == EventState.COUNTDOWN) {
                    toReturn.add(Style.YELLOW + "Countdown: " + Style.WHITE + event.getStartCooldown().getTimeLeft() + "s");
                }
                if (state == EventState.PREPARING) {
                    toReturn.add(Style.YELLOW + "Starting: " + ChatColor.WHITE + event.getStartCooldown().getTimeLeft() + "s");
                }
                if (state == EventState.FIGHTING) {
                    toReturn.add(Style.YELLOW + "Duration: " + ChatColor.WHITE +  TimeUtility.timeFormat(event.getTime()));
                    final SumoEvent sumoEvent = (SumoEvent) event;
                    toReturn.add(Style.SCOREBAORD_SEPARATOR);

                    PracticePlayer playerA = sumoEvent.getFightingPlayers()[0];
                    PracticePlayer playerB = sumoEvent.getFightingPlayers()[1];

                    toReturn.add(Style.YELLOW + playerA.getName() + Style.GRAY + " vs " + Style.YELLOW + playerB.getName());
                    toReturn.add(Style.GRAY + "(" + Style.YELLOW + playerA.getPing() + "ms" + Style.GRAY + ") | (" + Style.YELLOW + playerB.getPing() + "ms" + Style.GRAY + ")");
                    toReturn.add(Style.GRAY + "(" + Style.YELLOW + playerA.getCps() + "CPS" + Style.GRAY + ") | (" + Style.YELLOW + playerB.getCps() + "CPS" + Style.GRAY + ")");
                }
            }
        }

        toReturn.add(0, Style.SCOREBAORD_SEPARATOR);
        toReturn.add("");
        toReturn.add(Style.YELLOW + "HotsMC.net");
        toReturn.add(Style.SCOREBAORD_SEPARATOR);

        return toReturn;
    }

    @Override
    public long getInterval() {
        return 2L;
    }

    @Override
    public void onScoreboardCreate(Player player, Scoreboard scoreboard) {

    }

    @Override
    public void preLoop() {

    }
}
