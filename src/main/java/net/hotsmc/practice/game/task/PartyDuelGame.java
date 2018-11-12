package net.hotsmc.practice.game.task;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.game.GameState;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class PartyDuelGame extends Game {

    private Party[] parties;

    public PartyDuelGame(KitType kitType, Arena arena, Party party1, Party party2) {
        super(kitType, arena);
        parties = new Party[]{party1, party2};
    }

    @Override
    protected void onStart() {
        HotsPractice.getGameManager().addGame(this);
        for (Party party : parties) {
            party.setInGame(true);
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.sendMessage(ChatColor.YELLOW + "Please wait for loading arena...");
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Party party : parties) {
                    for (PracticePlayer practicePlayer : party.getPlayers()) {
                        addPlayer(practicePlayer);
                    }
                }
                for (Party party : parties) {
                    for (PracticePlayer practicePlayer : party.getPlayers()) {
                        practicePlayer.setKitItems();
                        if (parties[0].getPlayers().contains(practicePlayer)) {
                            practicePlayer.setPrefix(ChatColor.GREEN + "");
                            practicePlayer.teleport(arena.getSpawn1());
                            practicePlayer.startPartyDuelGameScoreboard(ChatColor.AQUA + "" + parties[1].getPartyName());
                        }
                        if (parties[1].getPlayers().contains(practicePlayer)) {
                            practicePlayer.setPrefix(ChatColor.AQUA + "");
                            practicePlayer.teleport(arena.getSpawn2());
                            practicePlayer.startPartyDuelGameScoreboard(ChatColor.GREEN + "" + parties[0].getPartyName());
                        }
                    }
                }
                time = HotsPractice.getGameConfig().getPregameTime();
                state = GameState.PreGame;
                this.cancel();
            }
        }.runTaskLater(HotsPractice.getInstance(), 60);
    }

    @Override
    protected void onTeleport() {

    }

    @Override
    protected void tickPreGame() {
        if (getTime() <= 5 && time != 0) {
            for (Party party : parties) {
                for (PracticePlayer practicePlayer : party.getPlayers()) {
                    practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 2F);
                    ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Starting in " + ChatColor.YELLOW + time + "s");
                }
            }
        }
    }

    @Override
    protected void onPlaying() {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.getPlayer().playSound(practicePlayer.getLocation(), Sound.NOTE_PIANO, 1F, 1F);
                ChatUtility.sendMessage(practicePlayer, ChatColor.GRAY + "Match has begun!");
            }
        }
    }

    @Override
    protected void sendWinner(String winner) {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.sendMessage(ChatColor.YELLOW + "Match has finished!");
                practicePlayer.sendMessage(ChatColor.YELLOW + "Winners: " + ChatColor.WHITE + winner + "'s party");
            }
        }
    }

    @Override
    protected void onEnd() {
        for (Party party : parties) {
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                practicePlayer.resetPlayer();
            }
        }
    }

    @Override
    protected void onFinish() {
        for (Party party : parties) {
            party.setInGame(false);
            for (PracticePlayer practicePlayer : party.getPlayers()) {
                if(practicePlayer.isEnableSpectate()) {
                    practicePlayer.disableSpectate();
                }
                practicePlayer.resetPlayer();
                practicePlayer.teleport(HotsPractice.getGameConfig().getLobbyLocation());
                practicePlayer.startPartyScoreboard();
                practicePlayer.setPartyClickItems();
                practicePlayer.setAlive(true);
                HotsCore.getHotsPlayer(practicePlayer.getPlayer()).updateNameTag();
            }
        }
    }

    public Party getOpponent(Party myParty) {
        for (Party party : parties) {
            if (!party.getPartyName().equals(myParty.getPartyName())) {
                return party;
            }
        }
        return null;
    }
}
