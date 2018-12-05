package net.hotsmc.practice.menus.party;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.menus.ladder.DuelPartyLadderMenu;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartyOtherFightMenu extends Menu {

    private String myPartyName;

    public PartyOtherFightMenu(String myPartyName) {
        super(false);
        this.myPartyName = myPartyName;
    }

    @Override
    public String getTitle(Player player) {
        return "Other Parties";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
        List<Party> newParties = new ArrayList<>();
        for (int i = 0; i < partyManager.getParties().size(); i++) {
            Party party = partyManager.getParties().get(i);
            if (!party.getPartyName().equals(myPartyName)) {
                if (!party.isInGame()) {
                    newParties.add(party);
                }
            }
        }
        for (int i = 0; i < newParties.size(); i++) {
            Party party = newParties.get(i);
            buttons.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return ItemUtility.createOtherFightPartyItem(party);
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    player.closeInventory();

                    if (!partyManager.exsitsParty(party.getPartyName())) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "Not found the party / そのパーティは存在しません");
                        return;
                    }

                    if (partyManager.getPartyByName(party.getPartyName()).isInGame()) {
                        ChatUtility.sendMessage(player, ChatColor.RED + "This party is fighting / そのパーティは現在戦っています");
                        return;
                    }

                    new DuelPartyLadderMenu(party).openMenu(player, 18);
                }
            });
        }
        return buttons;
    }
}

