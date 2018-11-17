package net.hotsmc.practice.game.games;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.other.Cooldown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.management.HotspotThreadMBean;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Game extends BukkitRunnable {

    protected int time;
    protected KitType kitType;
    protected GameState state;
    protected Arena arena;
    protected List<PracticePlayer> gamePlayers;
    protected List<PracticePlayer> spectatePlayers;
    protected Cooldown startCooldown;

    public Game(KitType kitType, Arena arena){
        this.kitType = kitType;
        this.arena = arena;
        this.time = 3;
        this.state = GameState.Teleporting;
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
        this.time = HotsPractice.getGameConfig().getEndgameTime();
        this.state = GameState.EndGame;
        sendWinner(winner);
        onEnd();
        for(PracticePlayer practicePlayer : spectatePlayers){
            practicePlayer.teleportToLobby();
            practicePlayer.disableSpectate();
            practicePlayer.setClickItems();
        }
        spectatePlayers.clear();
    }


    private void tick(){
        if(time >= 0) {
            if (state == GameState.Teleporting || state == GameState.PreGame || state == GameState.EndGame) {
                time--;
            }
        }

        if(state == GameState.PreGame){
            tickPreGame();
        }

        if(state == GameState.Playing){
            time++;
        }

        if (time <= 0) {
            if (state == GameState.Teleporting) {
                onTeleport();
                time = HotsPractice.getGameConfig().getPregameTime();
                startCooldown = new Cooldown(time*1000);
                state = GameState.PreGame;
                return;
            }
            if (state == GameState.PreGame) {
                onPlaying();
                state = GameState.Playing;
                return;
            }
            if (state == GameState.EndGame) {
                onFinish();
                delete();
            }
        }
    }

    void addPlayer(PracticePlayer practicePlayer){
        gamePlayers.add(practicePlayer);
    }

    @Override
    public void run() {
        tick();
    }

    private void delete(){
        arena.unload();
        HotsPractice.getGameManager().removeGame(this);
        this.cancel();
    }

    public boolean containsGamePlayer(PracticePlayer practicePlayer) {
        return gamePlayers.contains(practicePlayer);
    }

    public void addSpectator(PracticePlayer practicePlayer) {
        practicePlayer.enableSpectate();
        practicePlayer.teleport(arena.getDefaultSpawn());
        spectatePlayers.add(practicePlayer);
        practicePlayer.setSpectateItems();
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
        practicePlayer.setClickItems();
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
            practicePlayer.setClickItems();
        }
        for(PracticePlayer player : gamePlayers){
            for(PracticePlayer practicePlayer : spectatePlayers){
                player.showPlayer(practicePlayer);
            }
        }
        spectatePlayers.clear();
    }
}
