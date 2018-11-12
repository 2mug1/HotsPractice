package net.hotsmc.practice.game.task;

import lombok.Getter;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.arena.Arena;
import net.hotsmc.practice.game.GameState;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.other.Cooldown;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Game extends BukkitRunnable {

    protected int time;
    protected KitType kitType;
    protected GameState state;
    protected Arena arena;
    protected List<PracticePlayer> gamePlayers;
    protected Cooldown startCooldown;

    Game(KitType kitType, Arena arena){
        this.kitType = kitType;
        this.arena = arena;
        this.time = 3;
        this.state = GameState.Teleporting;
        this.gamePlayers = new ArrayList<>();
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
    }


    private void tick(){
        if (state == GameState.Teleporting && time >= 0 ||
            state == GameState.PreGame && time >= 0 ||
            state == GameState.EndGame && time >= 0) {
            time--;
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
}
