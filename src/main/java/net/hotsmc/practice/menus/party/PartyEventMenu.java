package net.hotsmc.practice.menus.party;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.PartyGameType;
import net.hotsmc.practice.menus.ladder.PartyEventLadderMenu;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PartyEventMenu extends Menu {

    public PartyEventMenu(){
        super(false);
    }

    @Override
    public String getTitle(Player player) {
        return "Party Event";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(""+ ChatColor.YELLOW + ChatColor.BOLD + "Team Fight", Material.DIAMOND_SWORD, false);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);

                if(practicePlayer == null)return;

                if(practicePlayer.isInParty()){
                    if(HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer).getPlayers().size() < 3) {
                        practicePlayer.sendMessage(ChatColor.RED + "More than 3 players required to start the Team Fight / Team Fightを開始するには3名以上のプレイヤーが必要です");
                        return;
                    }
                    new PartyEventLadderMenu(PartyGameType.TEAM_FIGHT).openMenu(player, 18);
                }
            }
        });
        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(""+ ChatColor.YELLOW + ChatColor.BOLD + "FFA Fight", Material.IRON_SWORD, false);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();

                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);

                if(practicePlayer == null)return;

                if(practicePlayer.isInParty()) {
                    if (HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer).getPlayers().size() < 3) {
                        practicePlayer.sendMessage(ChatColor.RED + "More than 3 players required to start the FFA Fight / FFA Fightを開始するには3名以上のプレイヤーが必要です");
                        return;
                    }
                }

                new PartyEventLadderMenu(PartyGameType.FFA_FIGHT).openMenu(player, 18);
            }
        });
        return buttons;
    }
}
