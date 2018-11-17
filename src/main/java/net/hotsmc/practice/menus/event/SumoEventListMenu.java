package net.hotsmc.practice.menus.event;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.events.SumoEventGame;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
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
        List<SumoEventGame> sumoEventGames = new ArrayList<>();
        for(EventGame eventGame : HotsPractice.getEventGameManager().getGames()){
            if(eventGame instanceof SumoEventGame){
                sumoEventGames.add((SumoEventGame)eventGame);
            }
        }
        for(int i = 0; i < sumoEventGames.size(); i++){
            SumoEventGame sumoEvent = sumoEventGames.get(i);
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
