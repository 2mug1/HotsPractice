package net.hotsmc.practice;

import net.hotsmc.core.other.Cooldown;
import net.hotsmc.core.other.Style;
import net.hotsmc.core.scoreboard.Board;
import net.hotsmc.core.scoreboard.BoardAdapter;
import net.hotsmc.practice.player.PlayerData;
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
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.queue.Queue;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class PracticeScoreboardAdapter implements BoardAdapter {

    private static final String DISPLAY_NAME = "" + Style.YELLOW + ChatColor.BOLD + "Hots Practice";
    private static final String SERVER_IP = Style.GRAY + "HotsMC.net";
    private static final String MAIN = Style.YELLOW;
    private static final String SUB = Style.WHITE;

    @Override
    public String getTitle(Player player) {
        return DISPLAY_NAME;
    }

    @Override
    public List<String> getScoreboard(Player player, Board board) {
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        final List<String> toReturn = new ArrayList<>();
        if (practicePlayer == null) return null;
        PlayerData playerData = practicePlayer.getPlayerData();
        if(!playerData.isSidebarVisibility()) return null;

        if (practicePlayer.isInLobby()) {
            toReturn.add(MAIN + "Online: " + SUB + HotsPractice.countOnline());
            toReturn.add(MAIN + "Queuing: " + SUB + HotsPractice.getCountQueue());
            toReturn.add(MAIN + "Fighting: " + SUB + HotsPractice.getCountFight());
            Cooldown eventCooldown = HotsPractice.getInstance().getManagerHandler().getEventManager().getEventCooldown();
            if(!eventCooldown.hasExpired()){
                toReturn.add(MAIN + "Event Cooldown: " + SUB + TimeUtility.formatTime(eventCooldown.getRemaining()));
            }

            if (practicePlayer.isInQueue()) {
                final Queue queue = practicePlayer.getInQueue();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(MAIN + "Queue:");
                toReturn.add(" " + SUB + queue.getRankedType().getName() + " " + queue.getLadderType());
                toReturn.add(MAIN + "Time: ");
                toReturn.add(" " + TimeUtility.formatTime(queue.getPassed()));
            }

            if (practicePlayer.isInParty()) {
                final Party party = practicePlayer.getInParty();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(MAIN + "Leader: " + SUB + party.getPartyName());
                toReturn.add(MAIN + "Type: " + SUB + party.getType().name());
                toReturn.add(MAIN + "Players: " + SUB + party.getPlayers().size() + "/" + Party.MAX_PLAYER);
            }

            if (practicePlayer.isEnableKitEdit()) {
                final LadderType type = practicePlayer.getEditLadderType();
                toReturn.add(Style.SCOREBAORD_SEPARATOR);
                toReturn.add(Style.GRAY + Style.BOLD + "Kit Editor");
                toReturn.add(MAIN + " Ladder: " + SUB + type.name());
            }
        }
        else if (practicePlayer.isInMatch()) {
            final Match match = practicePlayer.getInMatch();
            final MatchState state = match.getState();

            toReturn.add(MAIN + "Ladder: " + SUB+ match.getLadderType());

            if (state == MatchState.PreGame) {
                toReturn.add(MAIN + "Starting: " + SUB + match.getStartCooldown().getTimeLeft() + "s");
            } else {
                toReturn.add(MAIN + "Duration: " + SUB + TimeUtility.timeFormat(match.getTime()));
            }

            if (match instanceof DuelMatch) {
                final DuelMatch duelMatch = (DuelMatch) match;
                toReturn.add(MAIN + "Opponent: " + SUB + duelMatch.getOpponent(practicePlayer).getName());
                if (playerData.isSidebarPingVisibility()) {
                    toReturn.add("");
                    toReturn.add(MAIN + "Your Ping: " + SUB + practicePlayer.getPing() + "ms");
                    toReturn.add(MAIN + "Their Ping: " + SUB + duelMatch.getOpponent(practicePlayer).getPing() + "ms");
                }
            }
            if (match instanceof PartyDuelMatch) {
                final PartyDuelMatch partyDuelMatch = (PartyDuelMatch) match;
                final Party party = practicePlayer.getInParty();
                toReturn.add(MAIN + "Your Party: " + party.getPrefix() + party.getPartyName());
                toReturn.add(partyDuelMatch.getParties()[0].getPrefix() + partyDuelMatch.getParties()[0].getPartyName() + ": " + SUB + partyDuelMatch.getParties()[0].getAlivePlayers().size());
                toReturn.add(partyDuelMatch.getParties()[1].getPrefix() + partyDuelMatch.getParties()[1].getPartyName() + ": " + SUB + partyDuelMatch.getParties()[1].getAlivePlayers().size());
                if (playerData.isSidebarPingVisibility()) {
                    toReturn.add("");
                    toReturn.add(MAIN + "Your Ping: " + SUB + practicePlayer.getPing() + "ms");
                }
            }
            if (match instanceof PartyFFAMatch) {
                final PartyFFAMatch partyFFAMatch = (PartyFFAMatch) match;
                toReturn.add(MAIN + "Party Fight: " + SUB + "FFA");
                toReturn.add(MAIN + "Alive: " + SUB + partyFFAMatch.getParty().getAlivePlayers().size());
                if (playerData.isSidebarPingVisibility()) {
                    toReturn.add("");
                    toReturn.add(MAIN + "Your Ping: " + SUB + practicePlayer.getPing() + "ms");
                }
            }
            if (match instanceof PartyTeamMatch) {
                final PartyTeamMatch partyTeamMatch = (PartyTeamMatch) match;
                toReturn.add(MAIN + "Party Fight: " + SUB + "Team");
                net.hotsmc.practice.other.Team myTeam = partyTeamMatch.getMyTeam(practicePlayer);
                if (myTeam != null) {
                    toReturn.add(MAIN + "Your Team: " + myTeam.getPrefix() + myTeam.getTeamName());
                }
                net.hotsmc.practice.other.Team teamA = partyTeamMatch.getTeams()[0];
                if (teamA != null) {
                    toReturn.add(teamA.getPrefix() + teamA.getTeamName() + ": " + SUB + teamA.getAlivePlayers().size());
                }
                net.hotsmc.practice.other.Team teamB = partyTeamMatch.getTeams()[1];
                if (teamB != null) {
                    toReturn.add(teamB.getPrefix() + teamB.getTeamName() + ": " + SUB + teamB.getAlivePlayers().size());
                }

                if (playerData.isSidebarPingVisibility()) {
                    toReturn.add("");
                    toReturn.add(MAIN + "Your Ping: " + SUB + practicePlayer.getPing() + "ms");
                }
            }

            toReturn.add(MAIN + "Spectators: " + SUB + match.getSpectatePlayers().size());

            if (!practicePlayer.getEnderpearlCooldown().hasExpired()) {
                toReturn.add(MAIN + "Enderpearl: " + SUB + practicePlayer.getEnderpearlCooldown().getTimeLeft() + "s");
            }
        } else if (practicePlayer.isInEvent()) {
            final Event event = practicePlayer.getInEventGame();
            final EventState state = event.getState();
            if (event instanceof SumoEvent) {
                toReturn.add(MAIN + "Event: " + SUB + "Sumo");
                toReturn.add(MAIN + "Host: " + SUB + event.getHost());
                toReturn.add(MAIN + "State: " + SUB + event.getState().getName());
                toReturn.add(MAIN + "Players: " + SUB + event.getWinningPlayers().size() + "/" + event.getMaxPlayers());

                if (state == EventState.COUNTDOWN) {
                    toReturn.add(MAIN + "Countdown: " + SUB + event.getStartCooldown().getTimeLeft() + "s");
                }
                if (state == EventState.PREPARING) {
                    toReturn.add(MAIN + "Starting: " + SUB + event.getStartCooldown().getTimeLeft() + "s");
                }
                if (state == EventState.FIGHTING) {
                    toReturn.add(MAIN + "Duration: " + SUB +  TimeUtility.timeFormat(event.getTime()));
                    final SumoEvent sumoEvent = (SumoEvent) event;
                    toReturn.add(Style.SCOREBAORD_SEPARATOR);

                    PracticePlayer playerA = sumoEvent.getFightingPlayers()[0];
                    PracticePlayer playerB = sumoEvent.getFightingPlayers()[1];

                    toReturn.add(MAIN + playerA.getName() + Style.GRAY + " vs " + MAIN + playerB.getName());
                    toReturn.add(Style.GRAY + "(" + MAIN + playerA.getPing() + "ms" + Style.GRAY + ") | (" + MAIN + playerB.getPing() + "ms" + Style.GRAY + ")");
                    toReturn.add(Style.GRAY + "(" + MAIN + playerA.getCps() + "CPS" + Style.GRAY + ") | (" + MAIN + playerB.getCps() + "CPS" + Style.GRAY + ")");
                }
            }
        }

        toReturn.add(0, Style.SCOREBAORD_SEPARATOR);
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
