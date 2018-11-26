package net.hotsmc.practice.listener.listeners;

import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.match.*;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.event.EventState;
import net.hotsmc.practice.event.impl.SumoEvent;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.inventory.InventoryDataManager;
import net.hotsmc.practice.inventory.PlayerInventory;
import net.hotsmc.practice.match.impl.DuelMatch;
import net.hotsmc.practice.match.impl.PartyDuelMatch;
import net.hotsmc.practice.match.impl.PartyFFAMatch;
import net.hotsmc.practice.match.impl.PartyTeamMatch;
import net.hotsmc.practice.other.Team;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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
                        practicePlayer.openKitChest();
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
                Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
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
        practicePlayer.setClickItems();
        practicePlayer.teleportToLobby();
        practicePlayer.startTask();
        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
            if (all.isInMatch()) {
                practicePlayer.hidePlayer(all);
                all.hidePlayer(practicePlayer);
            }
        }
        addDuelPlayer(practicePlayer);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = getPracticePlayer(player);
        QueueManager queueManager = getQueueManager();
        Queue queue = getQueueManager().getPlayerOfQueue(practicePlayer);
        Party party = HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer);
        Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
        Event eventGame = HotsPractice.getEventManager().getPlayerOfEventGame(practicePlayer);
        InventoryDataManager inventoryDataManager = HotsPractice.getInventoryDataManager();
        PlayerInventory playerInventory = inventoryDataManager.getPlayerInventoryByUUID(player.getUniqueId().toString());

        if (practicePlayer == null) return;

        if (match != null) {
            if (match instanceof DuelMatch) {
                DuelMatch duelGame = (DuelMatch) match;
                PracticePlayer opponent = duelGame.getOpponent(practicePlayer);
                MatchState state = duelGame.getState();
                if (state == MatchState.Teleporting) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.setClickItems();
                    duelGame.getArena().unload();
                    HotsPractice.getMatchManager().removeGame(duelGame);
                }
                if (state == MatchState.PreGame) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.teleportToLobby();
                    opponent.setClickItems();
                    duelGame.getArena().unload();
                    HotsPractice.getMatchManager().removeGame(duelGame);
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
                    inventoryDataManager.addPlayerInventory(practicePlayer.getPlayer(), duration_time);
                    inventoryDataManager.addPlayerInventory(opponent.getPlayer(), duration_time);
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

            if (match instanceof PartyDuelMatch) {
                PartyDuelMatch partyDuelGame = (PartyDuelMatch) match;
                MatchState state = partyDuelGame.getState();
                partyDuelGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if (partyDuelGame.getGamePlayers().size() < 2) {
                        partyDuelGame.cancel();
                        for (PracticePlayer partyPlayer : partyDuelGame.getGamePlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Fight has cancelled.");
                            partyPlayer.setClickItems();
                        }
                        partyDuelGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyDuelGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    if (partyDuelGame.getGamePlayers().size() < 2) {
                        partyDuelGame.cancel();
                        for (PracticePlayer partyPlayer : partyDuelGame.getGamePlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Fight has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                        }
                        partyDuelGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyDuelGame);
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

            if(match instanceof PartyFFAMatch) {
                PartyFFAMatch partyFFAGame = (PartyFFAMatch) match;
                MatchState state = partyFFAGame.getState();
                partyFFAGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setClickItems();
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyFFAGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyFFAGame);
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
                            partyPlayer.setClickItems();
                        }
                        partyFFAGame.removeAllSpectatePlayers();
                        partyFFAGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyFFAGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInMatch()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
            }

            if(match instanceof PartyTeamMatch){
                PartyTeamMatch partyTeamGame = (PartyTeamMatch) match;
                MatchState state = partyTeamGame.getState();
                partyTeamGame.getGamePlayers().remove(practicePlayer);
                if (state == MatchState.Teleporting) {
                    if(partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setClickItems();
                        }
                        partyTeamGame.getArena().unload();
                        HotsPractice.getMatchManager().removeGame(partyTeamGame);
                    }
                }
                if (state == MatchState.PreGame) {
                    partyTeamGame.getMyTeam(practicePlayer).getPlayers().remove(practicePlayer);
                    if (partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                        }
                    }
                    partyTeamGame.getArena().unload();
                    HotsPractice.getMatchManager().removeGame(partyTeamGame);
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

        Match spectatingMatch = HotsPractice.getMatchManager().getPlayerOfSpectatingGame(practicePlayer);
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

        if (playerInventory != null) {
            inventoryDataManager.removePlayerInventory(playerInventory);
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
                        String meName = HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName();
                        String opponentName = HotsCore.getHotsPlayer(opponent.getPlayer()).getColorName();
                        sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + meName + ChatColor.GRAY + " has been eliminated by " + opponentName);
                        if (sumoEventGame.getWinningPlayers().size() == 2) {
                            sumoEventGame.broadcast(ChatColor.YELLOW + "(Event) " + ChatColor.WHITE + "Event Winner: " + opponentName);
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
        removeDuelPlayer(practicePlayer);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player dead = event.getEntity();
        PracticePlayer deadPlayer = HotsPractice.getPracticePlayer(dead);
        if (deadPlayer == null) return;
        Location location = dead.getLocation();

        if (deadPlayer.isInMatch()) {

            Match match = HotsPractice.getMatchManager().getPlayerOfGame(deadPlayer);
            deadPlayer.setRespawnLocation(location);

            if (match instanceof DuelMatch) {
                DuelMatch duelGame = (DuelMatch) match;
                PracticePlayer opponent = duelGame.getOpponent(deadPlayer);
                InventoryDataManager inventoryDataManager = HotsPractice.getInventoryDataManager();
                String duration_time = TimeUtility.timeFormat(duelGame.getTime());

                inventoryDataManager.addPlayerInventory(dead, duration_time);
                inventoryDataManager.addPlayerInventory(opponent.getPlayer(), duration_time);

                opponent.getPlayerData().addPoint();
                deadPlayer.getPlayerData().withdrawPoint();
                opponent.getPlayerData().addWinCount(duelGame.getRankedType(), duelGame.getLadderType());

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
                return;
            }
            if (match instanceof PartyDuelMatch) {
                PartyDuelMatch partyDuelGame = (PartyDuelMatch) match;
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
                deadPlayer.respawn();
                deadPlayer.enableSpectate();
                PartyManager partyManager = HotsPractice.getPartyManager();
                Party deadPlayerParty = partyManager.getPlayerOfParty(deadPlayer);
                if (deadPlayerParty.getAlivePlayers().size() <= 0) {
                    Party opponentParty = partyDuelGame.getOpponent(deadPlayerParty);
                    partyDuelGame.end(opponentParty.getPartyName());
                }
                return;
            }
            if (match instanceof PartyFFAMatch) {
                PartyFFAMatch partyFFAGame = (PartyFFAMatch) match;
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
                deadPlayer.respawn();
                deadPlayer.enableSpectate();
                PartyManager partyManager = HotsPractice.getPartyManager();
                Party deadPlayerParty = partyManager.getPlayerOfParty(deadPlayer);
                if (deadPlayerParty.getAlivePlayers().size() <= 1) {
                    partyFFAGame.end(dead.getKiller().getName());
                }
                return;
            }
            if (match instanceof PartyTeamMatch) {
                PartyTeamMatch partyTeamGame = (PartyTeamMatch) match;
                Team myTeam = partyTeamGame.getMyTeam(deadPlayer);
                deadPlayer.setRespawnLocation(location);
                deadPlayer.setAlive(false);
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
                Event eventGame = HotsPractice.getEventManager().getPlayerOfEventGame(practicePlayer);
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
                                        practicePlayer.setEventLeaderItems();
                                    }else{
                                        practicePlayer.setEventItems();
                                    }
                                    sumoEventGame.randomSelectPlayer();
                                }
                            }
                        }
                    }
                }
            }

            if (practicePlayer.isInMatch()) {
                Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
                LadderType ladderType = match.getLadderType();
                if (ladderType == LadderType.Sumo) {
                    if (match.getState() != MatchState.EndGame) {
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
            Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
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
            Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
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
        Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
        if (match.getLadderType() != LadderType.BuildUHC) {
            event.setCancelled(true);
            return;
        }
        if (match.getLadderType() == LadderType.Spleef) {
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.SNOW_BLOCK) {
                event.setCancelled(true);
                return;
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
        Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
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
                Match match = HotsPractice.getMatchManager().getPlayerOfGame(practicePlayer);
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
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        if (item.getType() == Material.GOLDEN_APPLE) {
            Player player = event.getPlayer();
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
    }
}




