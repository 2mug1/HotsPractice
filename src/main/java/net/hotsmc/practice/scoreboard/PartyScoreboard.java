package net.hotsmc.practice.scoreboard;

import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.party.Party;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

public class PartyScoreboard extends PlayerScoreboard {

    public PartyScoreboard(PracticePlayer practicePlayer) {
        super(practicePlayer);
    }

    @Override
    void onSetup() {
        PlayerData playerData = practicePlayer.getPlayerData();
        if(playerData == null)return;

        Team line1 = scoreboard.registerNewTeam("Line1");
        line1.addEntry(ChatColor.WHITE.toString());
        line1.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line1.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.WHITE.toString()).setScore(11);

        Team online = scoreboard.registerNewTeam("Online");
        online.addEntry(ChatColor.DARK_BLUE.toString());
        online.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "Online: ");
        online.setSuffix("" + ChatColor.WHITE + HotsPractice.countOnline());
        obj.getScore(ChatColor.DARK_BLUE.toString()).setScore(10);

        Team inQueue = scoreboard.registerNewTeam("Inqueue");
        inQueue.addEntry(ChatColor.DARK_AQUA.toString());
        inQueue.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "In Queues: ");
        inQueue.setSuffix("" + ChatColor.WHITE + HotsPractice.countQueue());
        obj.getScore(ChatColor.DARK_AQUA.toString()).setScore(9);

        Team ingame = scoreboard.registerNewTeam("Ingame");
        ingame.addEntry(ChatColor.BLUE.toString());
        ingame.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "In Fights: ");
        ingame.setSuffix("" + ChatColor.WHITE + HotsPractice.countInGame());
        obj.getScore(ChatColor.BLUE.toString()).setScore(8);

        Team point = scoreboard.registerNewTeam("Point");
        point.addEntry(ChatColor.AQUA.toString());
        point.setPrefix(""+ ChatColor.YELLOW + ChatColor.BOLD + "Points: ");
        point.setSuffix("" + ChatColor.WHITE + playerData.getPoint());
        obj.getScore(ChatColor.AQUA.toString()).setScore(7);

        Team latency = scoreboard.registerNewTeam("Latency");
        latency.addEntry(ChatColor.MAGIC.toString());
        latency.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Latency: ");
        latency.setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");
        obj.getScore(ChatColor.MAGIC.toString()).setScore(6);

        Team line3 = scoreboard.registerNewTeam("Line3");
        line3.addEntry(ChatColor.YELLOW.toString());
        line3.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line3.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.YELLOW.toString()).setScore(5);

        Party party = HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer);
        if(party != null) {
            Team partyName = scoreboard.registerNewTeam("PartyName");
            partyName.addEntry(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Party: " + ChatColor.WHITE));
            partyName.setSuffix(getEntry(party.getPartyName()));
            obj.getScore(getEntry("" + ChatColor.YELLOW + ChatColor.BOLD + "Party: " + ChatColor.WHITE)).setScore(4);

            Team partysize = scoreboard.registerNewTeam("PartyPlayers");
            partysize.addEntry(ChatColor.BOLD.toString());
            partysize.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Players: ");
            partysize.setSuffix("" + ChatColor.WHITE + party.getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Party.MAX_PLAYER);
            obj.getScore(ChatColor.BOLD.toString()).setScore(3);

            Team partyType = scoreboard.registerNewTeam("PartyType");
            partyType.addEntry(ChatColor.RED.toString());
            partyType.setPrefix("" + ChatColor.YELLOW + ChatColor.BOLD + "Type: ");
            partyType.setSuffix(ChatColor.WHITE + party.getType().name());
            obj.getScore(ChatColor.RED.toString()).setScore(2);
        }
        Team line2 = scoreboard.registerNewTeam("Line2");
        line2.addEntry(ChatColor.STRIKETHROUGH.toString());
        line2.setPrefix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "------------");
        line2.setSuffix("" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-----------");
        obj.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(1);
    }

    @Override
    void onUpdate() {
        scoreboard.getTeam("Online").setSuffix("" + ChatColor.WHITE + HotsPractice.countOnline());
        scoreboard.getTeam("Inqueue").setSuffix("" + ChatColor.WHITE + HotsPractice.countQueue());
        scoreboard.getTeam("Ingame").setSuffix("" + ChatColor.WHITE + HotsPractice.countInGame());
        scoreboard.getTeam("Latency").setSuffix("" + ChatColor.WHITE + practicePlayer.getPing() + "ms");

        Party party = HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer);

        if (party != null) {
            scoreboard.getTeam("PartyName").setSuffix(getEntry(party.getPartyName()));
            scoreboard.getTeam("PartyPlayers").setSuffix("" + ChatColor.WHITE + party.getPlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Party.MAX_PLAYER);
            scoreboard.getTeam("PartyType").setSuffix(ChatColor.WHITE + party.getType().name());
        }
    }
}

