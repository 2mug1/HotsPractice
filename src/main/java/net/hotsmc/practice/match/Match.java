package net.hotsmc.practice.match;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.other.Cooldown;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.ladder.LadderType;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Match extends BukkitRunnable {

    protected int time;
    protected LadderType ladderType;
    protected MatchState state;
    protected Arena arena;
    protected List<PracticePlayer> gamePlayers;
    protected List<PracticePlayer> spectatePlayers;
    protected Cooldown startCooldown;

    public Match(LadderType ladderType, Arena arena){
        this.ladderType = ladderType;
        this.arena = arena;
        this.time = 3;
        this.state = MatchState.Teleporting;
        this.gamePlayers = new ArrayList<>();
        this.spectatePlayers = new ArrayList<>();
    }

    protected abstract void onStart();

    protected abstract void onTeleport();

    protected abstract void tickPreGame();

    protected abstract void onPlaying();

    protected abstract void sendWinner(String winner);

    protected abstract void onEnd();

    protected abstract void onFinish();


    public void start(){
        onStart();
        this.runTaskTimer(HotsPractice.getInstance(), 0, 20);
    }

    public void end(String winner){
        this.time = HotsPractice.getInstance().getMatchConfig().getEndgameTime();
        this.state = MatchState.EndGame;
        sendWinner(winner);
        onEnd();
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.teleportToLobby();
            practicePlayer.disableSpectate();
            practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        }
        spectatePlayers.clear();
    }


    private void tick(){
        if(time >= 0) {
            if (state == MatchState.Teleporting || state == MatchState.PreGame || state == MatchState.EndGame) {
                time--;
            }
        }

        if(state == MatchState.PreGame){
            tickPreGame();
        }

        if(state == MatchState.Playing){
            time++;
        }

        if (time <= 0) {
            if (state == MatchState.Teleporting) {
                onTeleport();
                if(ladderType == LadderType.BuildUHC){
                    arena.getDefaultSpawn().getWorld().setGameRuleValue("naturalRegeneration", "false");
                }
                time = HotsPractice.getInstance().getMatchConfig().getPregameTime();
                startCooldown = new Cooldown(time*1000);
                state = MatchState.PreGame;
                return;
            }
            if (state == MatchState.PreGame) {
                onPlaying();
                state = MatchState.Playing;
                return;
            }
            if (state == MatchState.EndGame) {
                onFinish();
                delete();
            }
        }
    }

    protected void addPlayer(PracticePlayer practicePlayer){
        gamePlayers.add(practicePlayer);
    }

    @Override
    public void run() {
        tick();
    }

    private void delete(){
        arena.unload();
        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(this);
        this.cancel();
    }

    public boolean containsGamePlayer(PracticePlayer practicePlayer) {
        return gamePlayers.contains(practicePlayer);
    }

    public void addSpectator(PracticePlayer practicePlayer) {
        practicePlayer.enableSpectate();
        practicePlayer.teleport(arena.getDefaultSpawn());
        spectatePlayers.add(practicePlayer);
        practicePlayer.setHotbar(PlayerHotbar.SPECTATE);
        for(PracticePlayer player : gamePlayers){
            practicePlayer.showPlayer(player);
            player.sendMessage(ChatColor.YELLOW + "(Spectate) " +  HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " is watching.");
        }
        for(PracticePlayer player : spectatePlayers){
            player.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " is watching.");
        }
    }

    public void removeSpectator(PracticePlayer practicePlayer){
        practicePlayer.disableSpectate();
        practicePlayer.teleportToLobby();
        practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        spectatePlayers.remove(practicePlayer);
        for(PracticePlayer player : gamePlayers){
            practicePlayer.hidePlayer(player);
            player.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
        }
        for(PracticePlayer player : spectatePlayers){
            player.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
        }
    }

    public void removeAllSpectatePlayers(){
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.disableSpectate();
            practicePlayer.teleportToLobby();
            practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        }
        for(PracticePlayer player : gamePlayers){
            for(PracticePlayer practicePlayer : spectatePlayers){
                player.showPlayer(practicePlayer);
            }
        }
        spectatePlayers.clear();
    }

    public void broadcast(String message){
        for(PracticePlayer gamePlayer : gamePlayers){
            gamePlayer.getPlayer().sendMessage(message);
        }
        for(PracticePlayer spectatePlayer : spectatePlayers){
            spectatePlayer.getPlayer().sendMessage(message);
        }
    }
}
