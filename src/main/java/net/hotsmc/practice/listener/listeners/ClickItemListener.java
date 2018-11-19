package net.hotsmc.practice.listener.listeners;

import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.PracticePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if (practicePlayer != null) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) return;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                for (ClickActionItem clickActionItem : HotsPractice.getDuelClickItems()) {
                    if (clickActionItem.equals(itemStack)) {
                        clickActionItem.clickAction(player);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
