package net.hotsmc.practice.menus.event.sumo;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SumoEventListMenu extends Menu {


    public SumoEventListMenu() {
        super(true);
    }

    @Override
    public String getTitle(Player player) {
        return "Sumo Events";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        List<SumoEvent> sumoEventGames = new ArrayList<>();
        for(Event event : HotsPractice.getInstance().getMenuHandler().getEventManager().getEvents()){
            if(event instanceof SumoEvent){
                sumoEventGames.add((SumoEvent) event);
            }
        }
        for(int i = 0; i < sumoEventGames.size(); i++){
            SumoEvent sumoEvent = sumoEventGames.get(i);
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createSumoEventInfoItem(sumoEvent);
                }
                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    sumoEvent.addPlayer(HotsPractice.getPracticePlayer(player));
                }
            });
        }
        return buttons;
    }
}
