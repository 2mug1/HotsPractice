package net.hotsmc.practice.gui.ladder;

import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.match.PartyMatchType;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.utility.ItemUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class PartyEventLadderMenu extends Menu {

    private PartyMatchType partyMatchType;

    public PartyEventLadderMenu(PartyMatchType partyMatchType) {
        super(false);
        this.partyMatchType = partyMatchType;
    }

    @Override
    public String getTitle(Player player) {
        return "Party Event Ladder";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "NoDebuff",
                        1, PotionType.INSTANT_HEAL);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null){
                    if(practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.NoDebuff);
                            return;
                        }
                        if(partyMatchType == PartyMatchType.TEAM_FIGHT){
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.NoDebuff);
                        }
                    }
                }
            }
        });

        buttons.put(1, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createSplashPotion(
                        ChatColor.GREEN + "Debuff",
                        1, PotionType.POISON);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Debuff);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Debuff);
                        }
                    }
                }
            }
        });


        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "MCSG", Material.FISHING_ROD, false,
                        1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.MCSG);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.MCSG);
                        }
                    }
                }
            }
        });

        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "OCTC", Material.IRON_SWORD, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.OCTC);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.OCTC);
                        }
                    }
                }
            }
        });

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createEnchGapple(ChatColor.GREEN + "Gapple",1);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Gapple);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Gapple);
                        }
                    }
                }
            }
        });

        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Archer", Material.BOW, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Archer);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Archer);
                        }
                    }
                }
            }
        });

        buttons.put(6, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createFish(ChatColor.GREEN + "Combo",1, 3);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Combo);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Combo);
                        }
                    }
                }
            }
        });

        buttons.put(7, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Soup", Material.MUSHROOM_SOUP, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Soup);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Soup);
                        }
                    }
                }
            }
        });

        buttons.put(8, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "BuildUHC", Material.LAVA_BUCKET, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if (practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.BuildUHC);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.BuildUHC);
                        }
                    }
                }
            }
        });

        buttons.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "Axe", Material.IRON_AXE, false,1);
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.Axe);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.Axe);
                        }
                    }
                }
            }
        });

        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return ItemUtility.createItemStack(ChatColor.GREEN + "GappleSG", Material.GOLDEN_APPLE, false,1);
            }
            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                player.closeInventory();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                if(practicePlayer != null) {
                    if (practicePlayer.isInParty()) {
                        if (partyMatchType == PartyMatchType.FFA_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyFFAfight(LadderType.GappleSG);
                            return;
                        }
                        if (partyMatchType == PartyMatchType.TEAM_FIGHT) {
                            HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer).startPartyTeamFight(LadderType.GappleSG);
                        }
                    }
                }
            }
        });
        return buttons;
    }
}
