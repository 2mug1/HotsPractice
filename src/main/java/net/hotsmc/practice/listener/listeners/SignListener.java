package net.hotsmc.practice.listener.listeners;

import net.hotsmc.practice.HotsPractice;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class SignListener implements Listener {

    @EventHandler
    public void onChange(SignChangeEvent event){
        if(event.getLine(0).equalsIgnoreCase("return")){
            event.setLine(0, null);
            event.setLine(1, "Click");
            event.setLine(2, "Return to spawn");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK))return;
        if(event.getClickedBlock().getState() instanceof Sign){
            Sign sign = (Sign) event.getClickedBlock().getState();
            if(sign.getLine(2).contains("Return")){
                Objects.requireNonNull(HotsPractice.getDuelPlayer(event.getPlayer())).disableKitEdit();
            }
        }
    }
}
