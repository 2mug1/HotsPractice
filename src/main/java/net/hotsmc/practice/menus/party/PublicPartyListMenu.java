package net.hotsmc.practice.menus.party;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.party.PartyType;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PublicPartyListMenu extends Menu {

    public PublicPartyListMenu(){
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Public Parties";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
        for(int i = 0; i < partyManager.getParties().size(); i++){
            Party party = partyManager.getParties().get(i);
            if(party.getType() == PartyType.Public && !party.isInGame()){
                buttons.put(i, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return ItemUtility.createPartyListItem(party);
                    }
                    @Override
                    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                        player.closeInventory();

                        if(!partyManager.exsitsParty(party.getPartyName())){
                            ChatUtility.sendMessage(player, ChatColor.RED + "Not found the party / そのパーティはありません");
                            return;
                        }

                        party.addPlayer(HotsPractice.getPracticePlayer(player));
                    }
                });
            }
        }
        return buttons;
    }
}
