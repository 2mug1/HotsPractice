package net.hotsmc.practice.menus.stats;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends Menu {

    public SettingsMenu() {
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "My Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        return buttons;
    }
}
