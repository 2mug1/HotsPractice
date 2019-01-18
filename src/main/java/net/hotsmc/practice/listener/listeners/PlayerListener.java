package net.hotsmc.practice.listener.listeners;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.menu.Button;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.core.other.Style;
import net.hotsmc.core.utility.InventoryUtility;
import net.hotsmc.practice.gui.match.MatchDetailsMenu;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PlayerData;
import net.hotsmc.practice.match.*;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.match.impl.DuelMatch;
import net.hotsmc.practice.match.impl.PartyDuelMatch;
import net.hotsmc.practice.match.impl.PartyFFAMatch;
import net.hotsmc.practice.match.impl.PartyTeamMatch;
import net.hotsmc.practice.other.Team;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.queue.Queue;
import net.hotsmc.practice.utility.ItemUtility;
import net.hotsmc.practice.utility.NumberUtility;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.util.*;

import static net.hotsmc.practice.HotsPractice.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if (practicePlayer != null) {

            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                practicePlayer.addCurrentCps(1);
            }

            if (practicePlayer.isEnableKitEdit()) {
                event.setCancelled(true);
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Material type = event.getClickedBlock().getType();
                    event.setCancelled(true);
                    if (type == Material.ANVIL) {
                        practicePlayer.openKitLoadoutMenu();
                    }
                    if (type == Material.CHEST) {
                        if (practicePlayer.getEditLadderType() == LadderType.NoDebuff || practicePlayer.getEditLadderType() == LadderType.Debuff || practicePlayer.getEditLadderType() == LadderType.Soup) {
                            practicePlayer.openKitChest();
                        }
                    }
                }
            }

            if (practicePlayer.isEnableSpectate()) {
                ItemStack item = event.getItem();
                if (item == null) return;
                if (item.getType() != Material.SLIME_BALL) {
                    event.setCancelled(true);
                }
            }

            if (practicePlayer.isInMatch()) {

                if (event.getItem() == null) return;
                Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                if (match.getLadderType() == LadderType.Soup) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                        if (event.getItem() == null) return;
                        if (event.getItem().getType() == Material.MUSHROOM_SOUP) {
                            double health = player.getHealth();
                            if (health != player.getMaxHealth()) {
                                player.getItemInHand().setType(Material.BOWL);
                                double soup = +7.0;
                                player.setFoodLevel(20);
                                player.setHealth(player.getHealth() + soup > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + soup);
                            }
                        }
                    }
                }

                if (event.getItem().getType() == Material.ENDER_PEARL) {
                    if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (!match.getStartCooldown().hasExpired()) {
                            event.setCancelled(true);
                            return;
                        }
                        if (!practicePlayer.getEnderpearlCooldown().hasExpired()) {
                            event.setCancelled(true);
                        } else {
                            practicePlayer.startEnderpearlCooldown();
                        }
                    }
                }

            } else {

                if (player.getGameMode() != GameMode.CREATIVE) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        Material type = event.getClickedBlock().getType();
                        if (type == Material.WORKBENCH || type == Material.FURNACE || type == Material.BURNING_FURNACE || type == Material.ANVIL || type == Material.CHEST) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if(practicePlayer == null)return;
        if(practicePlayer.isEnableSpectate()) {
            if (event.getRightClicked() instanceof Player) {
                Player target = (Player) event.getRightClicked();
                PracticePlayer targetPracticePlayer = HotsPractice.getPracticePlayer(target);
                if (targetPracticePlayer == null) return;
                if (!targetPracticePlayer.isEnableSpectate()) {
                    new Menu(false) {
                        Player targetPlayer = target;
                        @Override
                        public String getTitle(Player player) {
                            return target.getName();
                        }

                        @Override
                        public Map<Integer, Button> getButtons(Player player) {
                            Map<Integer, Button> buttons = new HashMap<>();

                            if(targetPlayer == null)return buttons;

                            ItemStack[] armors = targetPlayer.getInventory().getArmorContents();
                            Collection<PotionEffect> potionEffects = targetPlayer.getActivePotionEffects();
                            buttons.put(0, new Button() {
                                @Override
                                public ItemStack getButtonItem(Player player) {
                                    return armors[3];
                                }
                            });
                            buttons.put(1, new Button() {
                                @Override
                                public ItemStack getButtonItem(Player player) {
                                    return armors[2];
                                }
                            });
                            buttons.put(2, new Button() {
                                @Override
                                public ItemStack getButtonItem(Player player) {
                                    return armors[1];
                                }
                            });
                            buttons.put(3, new Button() {
                                @Override
                                public ItemStack getButtonItem(Player player) {
                                    return armors[0];
                                }
                            });

                            if(potionEffects.size() > 0){
                                buttons.put(7, new Button() {
                                    @Override
                                    public ItemStack getButtonItem(Player player) {
                                        List<String> lore = new ArrayList<>();
                                        for(PotionEffect potionEffect : potionEffects){
                                            int duration = potionEffect.getDuration() / 20;
                                            lore.add(ChatColor.GRAY + potionEffect.getType().getName() + " " + NumberUtility.RomanNumerals(potionEffect.getAmplifier()+1) + " - " + TimeUtility.timeFormat(duration));
                                        }
                                        return ItemUtility.createItemStack("" + ChatColor.AQUA + ChatColor.BOLD + "Potion Effects", Material.BREWING_STAND_ITEM, false, lore);
                                    }
                                });
                            }
                            BigDecimal bd;
                            bd = new BigDecimal(target.getHealth());
                            BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_DOWN);
                            bd = new BigDecimal(target.getFoodLevel());
                            BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_DOWN);
                            buttons.put(8, new Button() {
                                @Override
                                public ItemStack getButtonItem(Player player) {
                                    return ItemUtility.createPlayerNameSkull(targetPlayer.getName(), "" + ChatColor.AQUA + ChatColor.BOLD + "Player Gauges",
                                            ChatColor.YELLOW + "Health: " + ChatColor.WHITE + bd1.doubleValue(),
                                            ChatColor.YELLOW + "Food: " + ChatColor.WHITE + bd2.doubleValue());
                                }
                            });
                            ItemStack[] items = InventoryUtility.fixInventoryOrder(targetPlayer.getInventory().getContents());
                            int slot = 9;
                            for (ItemStack item : items) {
                                if (item != null) {
                                    buttons.put(slot, new Button() {
                                        @Override
                                        public ItemStack getButtonItem(Player player) {
                                            return item;
                                        }
                                    });
                                }
                                slot++;
                            }
                            return buttons;
                        }
                    }.openMenu(player, 45);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        player.setAllowFlight(false);
        player.setFlying(false);
        PracticePlayer practicePlayer = new PracticePlayer(player);
        PlayerData playerData = new PlayerData(player.getUniqueId().toString());
        playerData.setName(player.getName());
        playerData.loadData();
        practicePlayer.setPlayerData(playerData);
        practicePlayer.heal();
        practicePlayer.clearArmors();
        practicePlayer.clearEffects();
        practicePlayer.loadPlayerKits();
        practicePlayer.setHotbar(PlayerHotbar.LOBBY);
        practicePlayer.teleportToLobby();
        practicePlayer.startTask();
        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
            if (all.isInMatch()) {
                practicePlayer.hidePlayer(all);
                all.hidePlayer(practicePlayer);
            }
        }
        addPracticePlayer(practicePlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = getPracticePlayer(player);
        QueueManager queueManager = HotsPractice.getInstance().getManagerHandler().getQueueManager();
        Queue queue = HotsPractice.getInstance().getManagerHandler().getQueueManager().getPlayerOfQueue(practicePlayer);
        Party party = HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(practicePlayer);
        Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
        Event eventGame = HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(practicePlayer);

        if (practicePlayer == null) return;

        if (match != null) {
            if (match instanceof DuelMatch) {
                DuelMatch duelGame = (DuelMatch) match;
                PracticePlayer opponent = duelGame.getOpponent(practicePlayer);
                MatchState state = duelGame.getState();
                if (state == MatchState.Teleporting) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.setHotbar(PlayerHotbar.LOBBY);
                    duelGame.getArena().unload();
                    HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(duelGame);
                }
                if (state == MatchState.PreGame) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.teleportToLobby();
                    opponent.setHotbar(PlayerHotbar.LOBBY);
                    duelGame.getArena().unload();
                    HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(duelGame);
                    for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                        if (!all.isInMatch()) {
                            practicePlayer.showPlayer(all);
                            all.showPlayer(practicePlayer);
                        }
                    }
                }
                if (state == MatchState.Playing) {
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    String duration_time = TimeUtility.timeFormat(duelGame.getTime());
                    MatchDetailsMenu.put(practicePlayer.getPlayer(), duration_time);
                    MatchDetailsMenu.put(opponent.getPlayer(), duration_time);
                    practicePlayer.getPlayerData().withdrawPoint();
                    opponent.getPlayerData().addPoint();
                    opponent.getPlayerData().addWinCount(duelGame.getRankedType(), duelGame.getLadderType());
                    if(duelGame.getRankedType() == RankedType.RANKED){
                        LadderType ladder = match.getLadderType();
                        if(practicePlayer.getPlayerData().getElo(ladder) > 0) {
                            int passed = practicePlayer.getPlayerData().calculatedElo(ladder);

                            int oldElo = practicePlayer.getPlayerData().getElo(ladder);
                            int newElo = oldElo - passed;

                            practicePlayer.getPlayerData().updateElo(ladder, newElo);

                            int oldElo2 = opponent.getPlayerData().getElo(ladder);
                            int newElo2 = oldElo2 + passed;

                            opponent.getPlayerData().updateElo(ladder, newElo2);
                            opponent.sendMessage(ChatColor.YELLOW + "(Ranked Elo) " + ChatColor.BLUE + ladder.name() + ": " + ChatColor.WHITE + oldElo2 + ChatColor.GRAY + "→" +
                                    ChatColor.WHITE + newElo2 + ChatColor.GRAY + "(" + ChatColor.GREEN + "+" + passed + ChatColor.GRAY + ")");
                        }
                    }
                    duelGame.end(opponent.getName());
                }
            }

            else if (match instanceof PartyDuelMatch) {
                PartyDuelMatch partyDuelGame = (PartyDuelMatch) match;
                MatchState state = partyDuelGame.getState();
                partyDuelGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if (partyDuelGame.getGamePlayers().size() < 2) {
                        partyDuelGame.cancel();
                        for (PracticePlayer partyPlayer : partyDuelGame.getGamePlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Fight has cancelled.");
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyDuelGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyDuelGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    if (partyDuelGame.getGamePlayers().size() < 2) {
                        partyDuelGame.cancel();
                        for (PracticePlayer partyPlayer : partyDuelGame.getGamePlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Fight has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyDuelGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyDuelGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInMatch()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
                if (state == MatchState.Playing) {
                    party.getPlayers().remove(practicePlayer);
                    if(party.getAlivePlayers().size() <= 0) {
                        Party opponent = partyDuelGame.getOpponent(party);
                        partyDuelGame.end(opponent.getPartyName());
                    }
                }
            }

            else if(match instanceof PartyFFAMatch) {
                PartyFFAMatch partyFFAGame = (PartyFFAMatch) match;
                MatchState state = partyFFAGame.getState();
                partyFFAGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyFFAGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyFFAGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInMatch()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
                if (state == MatchState.Playing) {
                    if (partyFFAGame.getGamePlayers().size() <= 1) {
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyFFAGame.removeAllSpectatePlayers();
                        partyFFAGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyFFAGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInMatch()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
            }

            else if(match instanceof PartyTeamMatch){
                PartyTeamMatch partyTeamGame = (PartyTeamMatch) match;
                MatchState state = partyTeamGame.getState();
                partyTeamGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if(partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                        partyTeamGame.getArena().unload();
                        HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyTeamGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    partyTeamGame.getMyTeam(practicePlayer).getPlayers().remove(practicePlayer);
                    if (partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setHotbar(PlayerHotbar.LOBBY);
                        }
                    }
                    partyTeamGame.getArena().unload();
                    HotsPractice.getInstance().getManagerHandler().getMatchManager().removeGame(partyTeamGame);
                    for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                        if (!all.isInMatch()) {
                            practicePlayer.showPlayer(all);
                            all.showPlayer(practicePlayer);
                        }
                    }
                }

                if (state == MatchState.Playing) {
                    Team myTeam = partyTeamGame.getMyTeam(practicePlayer);
                    myTeam.getPlayers().remove(practicePlayer);
                    if(myTeam.getAlivePlayers().size() <= 0) {
                        Team opponent = partyTeamGame.getOpponentTeam(myTeam);
                        partyTeamGame.end(opponent.getPrefix() + opponent.getTeamName());
                    }
                }
            }
        }

        Match spectatingMatch = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfSpectateMatch(practicePlayer);
        if(spectatingMatch != null){
            spectatingMatch.getSpectatePlayers().remove(practicePlayer);
            for(PracticePlayer a : spectatingMatch.getGamePlayers()){
                a.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
            }
            for(PracticePlayer b : spectatingMatch.getSpectatePlayers()){
                b.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
            }
        }

        if (queue != null) {
            queueManager.removeQueue(queue);
        }

        if (party != null) {
            party.removePlayer(practicePlayer);
        }

        if(eventGame != null) {
            if (practicePlayer.hasHoldingEventGame()) {
                if (eventGame.getState() == EventState.ARENA_PREPARING) {
                    eventGame.delete();
                    return;
                }
            }
            if(eventGame instanceof SumoEvent) {
                SumoEvent sumoEventGame = (SumoEvent) eventGame;
                if (eventGame.getState() != EventState.WAITING_FOR_PLAYERS && eventGame.getState() != EventState.COUNTDOWN) {
                    if (sumoEventGame.isFighting(practicePlayer)){
                        PracticePlayer opponent = sumoEventGame.getOpponent(practicePlayer);
                        sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + Style.RED + practicePlayer.getName() + ChatColor.GRAY + " has been eliminated by " + Style.GREEN + opponent.getName());
                        if (sumoEventGame.getWinningPlayers().size() == 2) {
                            sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.WHITE + "Event Winner: " + opponent.getHotsPlayer().getColorName());
                            sumoEventGame.end();
                        } else {
                            sumoEventGame.removePlayer(practicePlayer);
                            opponent.teleport(sumoEventGame.getArena().getDefaultSpawn());
                            sumoEventGame.randomSelectPlayer();
                        }
                    }
                }
                eventGame.removePlayer(practicePlayer);
            }
        }
        removePracticePlayer(practicePlayer);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player dead = event.getEntity();
        Player killer = dead.getKiller();
        PracticePlayer deadPlayer = HotsPractice.getPracticePlayer(dead);
        if (deadPlayer == null) return;
        Location location = dead.getLocation();

        if (deadPlayer.isInMatch()) {

            Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(deadPlayer);
            deadPlayer.setRespawnLocation(location);

            if (match instanceof DuelMatch) {
                DuelMatch duelGame = (DuelMatch) match;
                PracticePlayer opponent = duelGame.getOpponent(deadPlayer);
                String duration_time = TimeUtility.timeFormat(duelGame.getTime());
                MatchDetailsMenu.put(dead, duration_time);
                MatchDetailsMenu.put(opponent.getPlayer(), duration_time);

                opponent.getPlayerData().addPoint();
                deadPlayer.getPlayerData().withdrawPoint();
                opponent.getPlayerData().addWinCount(duelGame.getRankedType(), duelGame.getLadderType());

                double health = opponent.getPlayer().getHealth();

                match.broadcast(deadPlayer.getHotsPlayer().getColorName() + ChatColor.GRAY + " has been eliminated by " + opponent.getHotsPlayer().getColorName()
                        + ChatColor.GRAY + "(" + ChatColor.RED + Style.UNICODE_HEART + health + ChatColor.GRAY + ")");

                deadPlayer.respawn();

                if(duelGame.getRankedType() == RankedType.RANKED){
                    LadderType ladder = match.getLadderType();
                    if(deadPlayer.getPlayerData().getElo(ladder) > 0) {
                        int passed = deadPlayer.getPlayerData().calculatedElo(ladder);

                        int oldElo = deadPlayer.getPlayerData().getElo(ladder);
                        int newElo = oldElo - passed;

                        deadPlayer.getPlayerData().updateElo(ladder, newElo);
                        deadPlayer.sendMessage(ChatColor.YELLOW + "(Ranked Elo) " + ChatColor.BLUE + ladder.name() + ": " +ChatColor.WHITE + oldElo + ChatColor.GRAY + "→" +
                                ChatColor.WHITE + newElo + ChatColor.GRAY + "(" + ChatColor.RED + "-" + passed + ChatColor.GRAY + ")");
                        deadPlayer.getPlayerData().updateElo(ladder, newElo);
                        int oldElo2 = opponent.getPlayerData().getElo(ladder);
                        int newElo2 = oldElo2 + passed;
                        opponent.getPlayerData().updateElo(ladder, newElo2);
                        opponent.sendMessage(ChatColor.YELLOW + "(Ranked Elo) " + ChatColor.BLUE + ladder.name() + ": " + ChatColor.WHITE + oldElo2 + ChatColor.GRAY + "→" +
                                ChatColor.WHITE + newElo2 + ChatColor.GRAY + "(" + ChatColor.GREEN + "+" + passed + ChatColor.GRAY + ")");
                    }
                }
                match.end(opponent.getName());
            }
            else if (match instanceof PartyDuelMatch) {
                PartyDuelMatch partyDuelGame = (PartyDuelMatch) match;
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
                location.getWorld().strikeLightningEffect(location.add(0, 3, 0));

                if(killer == null){
                    match.broadcast(deadPlayer.getInParty().getPrefix() + deadPlayer.getName() + ChatColor.GRAY + " has dead.");
                }else{
                    PracticePlayer killerPlayer = HotsPractice.getPracticePlayer(killer);
                    double health = killer.getHealth();
                    match.broadcast(deadPlayer.getInParty().getPrefix() + deadPlayer.getName() + ChatColor.GRAY  + " has been eliminated by " + killerPlayer.getInParty().getPrefix() +
                            killerPlayer.getName() + ChatColor.GRAY + "(" + ChatColor.RED + Style.UNICODE_HEART + health + ChatColor.GRAY + ")");
                }

                deadPlayer.respawn();
                deadPlayer.enableSpectate();
                PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
                Party deadPlayerParty = partyManager.getPlayerOfParty(deadPlayer);
                if (deadPlayerParty.getAlivePlayers().size() <= 0) {
                    Party opponentParty = partyDuelGame.getOpponent(deadPlayerParty);
                    partyDuelGame.end(opponentParty.getPartyName());
                }
            }
            else if (match instanceof PartyFFAMatch) {
                PartyFFAMatch partyFFAGame = (PartyFFAMatch) match;
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
                location.getWorld().strikeLightningEffect(location.add(0, 3, 0));

                if(killer == null){
                    match.broadcast(deadPlayer.getHotsPlayer().getColorName() + ChatColor.GRAY + " has dead.");
                }else{
                    PracticePlayer killerPlayer = HotsPractice.getPracticePlayer(killer);
                    double health = killer.getHealth();
                    match.broadcast(deadPlayer.getHotsPlayer().getColorName() + ChatColor.GRAY  + " has been eliminated by " + killerPlayer.getHotsPlayer().getColorName() + ChatColor.GRAY + "(" + ChatColor.RED + Style.UNICODE_HEART + health + ChatColor.GRAY + ")");
                }

                deadPlayer.respawn();
                deadPlayer.enableSpectate();
                PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
                Party deadPlayerParty = partyManager.getPlayerOfParty(deadPlayer);
                if (deadPlayerParty.getAlivePlayers().size() <= 1) {
                    if(dead.getKiller().getName() == null) {
                        partyFFAGame.end(deadPlayerParty.getAlivePlayers().get(0).getName());
                    }else{
                        partyFFAGame.end(dead.getKiller().getName());
                    }
                }
            }
            else if (match instanceof PartyTeamMatch) {
                PartyTeamMatch partyTeamGame = (PartyTeamMatch) match;
                Team myTeam = partyTeamGame.getMyTeam(deadPlayer);
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
                location.getWorld().strikeLightningEffect(location.add(0, 3, 0));

                if(killer == null){
                    match.broadcast(myTeam.getPrefix() + deadPlayer.getName() + ChatColor.GRAY + " has dead.");
                }else{
                    PracticePlayer killerPlayer = HotsPractice.getPracticePlayer(killer);
                    double health = killer.getHealth();
                    match.broadcast(myTeam.getPrefix() + deadPlayer.getName() + ChatColor.GRAY  + " has been eliminated by " + partyTeamGame.getMyTeam(killerPlayer).getPrefix() +
                            killerPlayer.getName() + ChatColor.GRAY + "(" + ChatColor.RED + Style.UNICODE_HEART + health + ChatColor.GRAY + ")");
                }

                deadPlayer.respawn();
                deadPlayer.enableSpectate();
                if (myTeam.getAlivePlayers().size() < 1) {
                    Team opponent = partyTeamGame.getOpponentTeam(myTeam);
                    partyTeamGame.end(opponent.getPrefix() + opponent.getTeamName());
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if (practicePlayer != null) {

            if (practicePlayer.isFrozen()) {
                Location from = event.getFrom();
                double xfrom = event.getFrom().getX();
                double yfrom = event.getFrom().getY();
                double zfrom = event.getFrom().getZ();
                double xto = event.getTo().getX();
                double yto = event.getTo().getY();
                double zto = event.getTo().getZ();
                if (!(xfrom == xto && yfrom == yto && zfrom == zto)) {
                    player.teleport(from);
                }
            }

            if(practicePlayer.isInEvent()){
                Event eventGame = HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(practicePlayer);
                if(eventGame instanceof SumoEvent) {
                    SumoEvent sumoEventGame = (SumoEvent) eventGame;
                    if (sumoEventGame.getState() == EventState.FIGHTING) {
                        if (sumoEventGame.isFighting(practicePlayer)) {
                            if (event.getTo().getBlock().isLiquid()) {
                                PracticePlayer opponent = sumoEventGame.getOpponent(practicePlayer);
                                String meName = HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName();
                                String opponentName = HotsCore.getHotsPlayer(opponent.getPlayer()).getColorName();
                                sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + meName + ChatColor.GRAY + " was eliminated by " + opponentName);
                                practicePlayer.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.RED + "You have lost the round.");
                                opponent.sendMessage(ChatColor.YELLOW + "(Event) " + ChatColor.GREEN + "You have won the round.");
                                if (sumoEventGame.getWinningPlayers().size() == 2) {
                                    sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.WHITE + "Event Winner: " + opponentName);
                                    sumoEventGame.end();
                                } else {
                                    practicePlayer.setEventLost(true);
                                    Location arena = sumoEventGame.getArena().getDefaultSpawn();
                                    practicePlayer.teleport(arena);
                                    opponent.teleport(arena);
                                    if(sumoEventGame.isLeader(practicePlayer)){
                                        practicePlayer.setHotbar(PlayerHotbar.EVENT_LEADER);
                                    }else{
                                        practicePlayer.setHotbar(PlayerHotbar.EVENT_DEFAULT);
                                    }
                                    if(sumoEventGame.isLeader(opponent)){
                                        opponent.setHotbar(PlayerHotbar.EVENT_LEADER);
                                    }else{
                                        opponent.setHotbar(PlayerHotbar.EVENT_DEFAULT);
                                    }
                                    sumoEventGame.randomSelectPlayer();
                                }
                            }
                        }
                    }
                }
            }

            if (practicePlayer.isInMatch()) {
                Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                LadderType ladderType = match.getLadderType();
                if (ladderType == LadderType.Sumo) {
                    if (match.getState() != MatchState.Teleporting && match.getState() != MatchState.EndGame) {
                        if (event.getTo().getBlock().isLiquid()) {
                            if (match instanceof DuelMatch) {
                                DuelMatch duelGame = (DuelMatch) match;
                                practicePlayer.setRespawnLocation(duelGame.getPlayerSpawnLocation(practicePlayer));
                                player.setHealth(0.0D);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            PracticePlayer practicePlayer = getPracticePlayer(player);
            if (practicePlayer == null) return;
            if (!practicePlayer.isInMatch()) {
                e.setCancelled(true);
                return;
            }
            if(practicePlayer.isEnableSpectate()){
                e.setCancelled(true);
                return;
            }
            Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
            if (match.getLadderType() == LadderType.Sumo || match.getLadderType() == LadderType.Spleef) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        PracticePlayer practicePlayer = getPracticePlayer(player);
        if (practicePlayer == null) return;
        if(practicePlayer.isEnableSpectate()){
            e.setCancelled(true);
            return;
        }
        if (!practicePlayer.isInMatch()) {
            e.setCancelled(true);
        }else{
            Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
            Item item = e.getItemDrop();
            if(item.getItemStack().getType() == Material.BOWL) {
                if (match.getLadderType() == LadderType.Soup) {
                    item.remove();
                }
                return;
            }
            if(item.getItemStack().getType() == Material.GLASS_BOTTLE) {
                if (match.getLadderType() == LadderType.Debuff || match.getLadderType() == LadderType.NoDebuff || match.getLadderType() == LadderType.OCTC ||
                        match.getLadderType() == LadderType.Gapple || match.getLadderType() == LadderType.Combo || match.getLadderType() == LadderType.Axe ||
                        match.getLadderType() == LadderType.Soup) {
                    item.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        Player player = e.getPlayer();
        PracticePlayer practicePlayer = getPracticePlayer(player);
        if (practicePlayer == null) return;
        if(practicePlayer.isEnableSpectate()){
            e.setCancelled(true);
            return;
        }
        if(practicePlayer.isInMatch()){
            if(e.getItem().getItemStack().getType() == Material.BOOK){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PracticePlayer practicePlayer = getPracticePlayer(player);
        if (practicePlayer == null) return;
        if (!practicePlayer.isInMatch()) {
            event.setCancelled(true);
            return;
        }
        if(practicePlayer.isEnableSpectate()){
            event.setCancelled(true);
            return;
        }
        Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
        if (match.getLadderType() != LadderType.BuildUHC && match.getLadderType() != LadderType.Spleef) {
            event.setCancelled(true);
        }
        if (match.getLadderType() == LadderType.Spleef) {
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.SNOW_BLOCK) {
                event.setCancelled(true);
            }
        }
        if (match.getLadderType() == LadderType.BuildUHC) {
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.COBBLESTONE && type != Material.WOOD) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PracticePlayer practicePlayer = getPracticePlayer(player);
        if (practicePlayer == null) return;
        if (!practicePlayer.isInMatch()) {
            event.setCancelled(true);
            return;
        }
        if(practicePlayer.isEnableSpectate()){
            event.setCancelled(true);
            return;
        }
        Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
        if (match.getLadderType() == LadderType.BuildUHC) {
            if (match.getState() != MatchState.Playing) {
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.COBBLESTONE && type != Material.WOOD) {
                event.setCancelled(true);
                return;
            }
            int kiten = match.getArena().getSpawn1().getBlockY() - 1;
            int maxY = kiten + 4;
            int blockY = block.getY();
            if (blockY > maxY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if(player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PracticePlayer practicePlayer = getPracticePlayer(player);
            if (practicePlayer == null) return;
            if(practicePlayer.isEnableSpectate()){
                event.setCancelled(true);
                return;
            }
            if(practicePlayer.isInEvent()){
                Event eventGame = practicePlayer.getInEventGame();
                if(eventGame instanceof SumoEvent){
                    SumoEvent sumo = (SumoEvent) eventGame;
                    if(!sumo.isFighting(practicePlayer)){
                        event.setCancelled(true);
                    }else{
                        event.setDamage(0D);
                    }
                }
                return;
            }
            if (!practicePlayer.isInMatch()) {
                event.setCancelled(true);
                return;
            }
            if (practicePlayer.isInMatch()) {
                Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                if (match.getState() == MatchState.Teleporting || match.getState() == MatchState.PreGame || match.getState() == MatchState.EndGame) {
                    event.setCancelled(true);
                    return;
                }
                if(match.getLadderType() == LadderType.Sumo) {
                    event.setDamage(0D);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setFireTicks(0);
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if (practicePlayer == null) return;
        if (practicePlayer.isInMatch()) {
            event.setRespawnLocation(practicePlayer.getRespawnLocation());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        if (item.getType() == Material.GOLDEN_APPLE) {

            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
            if (practicePlayer == null) return;
            if (!practicePlayer.getGappleCooldown().hasExpired()) {
                event.setCancelled(true);
                return;
            }
            practicePlayer.startGappleCooldown();
            player.removePotionEffect(PotionEffectType.REGENERATION);
            if (item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Head")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
            }
        }

        else if (item.getTypeId() == 373) {
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(HotsPractice.getInstance(), () -> player.setItemInHand(new ItemStack(Material.AIR)), 1L);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            PracticePlayer player = HotsPractice.getPracticePlayer(damaged);
            if(player == null)return;
            if (player.isEnableSpectate()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            PracticePlayer damagerPlayer = HotsPractice.getPracticePlayer(damager);
            PracticePlayer damagedPlayer = HotsPractice.getPracticePlayer(damaged);
            if (damagedPlayer == null) return;
            if (damagerPlayer == null) return;
            if (damagerPlayer.isEnableSpectate()) {
                event.setCancelled(true);
            }
            if (damagedPlayer.isEnableSpectate()) {
                event.setCancelled(true);
            }
        }
        else if(event.getDamager() instanceof Projectile && event.getEntity() instanceof Player) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player && projectile instanceof Arrow) {
                Player damaged = (Player) event.getEntity();
                PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(damaged);
                if (practicePlayer == null) return;
                if(practicePlayer.isEnableSpectate()){
                    event.setCancelled(true);
                    return;
                }
                Player shooter = (Player) projectile.getShooter();
                Player hit = (Player) event.getEntity();
                double health = hit.getHealth();
                double damage = event.getFinalDamage();
                double resultHealth = health - damage;
                if(resultHealth > 0) {
                    shooter.sendMessage(ChatColor.GRAY + hit.getName() + " (" + resultHealth + ")");
                }
            }
        }
        else if (event.getDamager() instanceof FishHook && ((FishHook) event.getDamager()).getShooter() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(damaged);
            if (practicePlayer == null) return;
            if(practicePlayer.isEnableSpectate()){
                event.setCancelled(true);
            }
        }
    }
}



