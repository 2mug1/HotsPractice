package net.hotsmc.practice.listener.listeners;

import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.events.EventGameState;
import net.hotsmc.practice.game.events.SumoEventGame;
import net.hotsmc.practice.game.games.*;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.inventory.InventoryDataManager;
import net.hotsmc.practice.inventory.PlayerInventory;
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

import javax.naming.SizeLimitExceededException;

import static net.hotsmc.practice.HotsPractice.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
        if (practicePlayer != null) {
            ItemStack itemStack = event.getItem();
            if(practicePlayer.isEnableSpectate()){
                ItemStack item = event.getItem();
                if(item == null)return;
                if(item.getType() != Material.SLIME_BALL) {
                    event.setCancelled(true);
                }
            }
            if (itemStack == null || itemStack.getType() == Material.AIR) return;
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                for (ClickActionItem clickActionItem : getDuelClickItems()) {
                    if (clickActionItem.equals(itemStack)) {
                        clickActionItem.clickAction(player);
                        event.setCancelled(true);
                    }
                }
            }
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                practicePlayer.addCurrentCps(1);
            }
            if (practicePlayer.isInGame()) {
                if(event.getItem() == null)return;
                Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
                if (game.getKitType() == KitType.Soup) {
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
                        if (!game.getStartCooldown().hasExpired()) {
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
            if (player.getGameMode() != GameMode.CREATIVE) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Material type = event.getClickedBlock().getType();
                    if (type == Material.WORKBENCH || type == Material.FURNACE || type == Material.BURNING_FURNACE || type == Material.ANVIL || type == Material.CHEST) {
                        event.setCancelled(true);
                        return;
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
        practicePlayer.startLobbyScoreboard();
        practicePlayer.startTask();
        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
            if (all.isInGame()) {
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
        DuelQueueManager queueManager = getQueueManager();
        DuelQueue duelQueue = getQueueManager().getPlayerOfQueue(practicePlayer);
        Party party = HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer);
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        EventGame eventGame = HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
        InventoryDataManager inventoryDataManager = HotsPractice.getInventoryDataManager();
        PlayerInventory playerInventory = inventoryDataManager.getPlayerInventoryByUUID(player.getUniqueId().toString());

        if (practicePlayer == null) return;

        if (game != null) {
            if (game instanceof DuelGame) {
                DuelGame duelGame = (DuelGame) game;
                PracticePlayer opponent = duelGame.getOpponent(practicePlayer);
                GameState state = duelGame.getState();
                if (state == GameState.Teleporting) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.setClickItems();
                    opponent.startLobbyScoreboard();
                    duelGame.getArena().unload();
                    HotsPractice.getGameManager().removeGame(duelGame);
                }
                if (state == GameState.PreGame) {
                    duelGame.cancel();
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    opponent.teleportToLobby();
                    opponent.setClickItems();
                    opponent.startLobbyScoreboard();
                    duelGame.getArena().unload();
                    HotsPractice.getGameManager().removeGame(duelGame);
                    for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                        if (!all.isInGame()) {
                            practicePlayer.showPlayer(all);
                            all.showPlayer(practicePlayer);
                        }
                    }
                }
                if (state == GameState.Playing) {
                    opponent.sendMessage(ChatColor.RED + "Your opponent has abandoned this match / 対戦相手はマッチを放棄しました");
                    String duration_time = TimeUtility.timeFormat(duelGame.getTime());
                    inventoryDataManager.addPlayerInventory(practicePlayer.getPlayer(), duration_time);
                    inventoryDataManager.addPlayerInventory(opponent.getPlayer(), duration_time);
                    practicePlayer.getPlayerData().withdrawPoint();
                    opponent.getPlayerData().addPoint();
                    opponent.getPlayerData().addWinCount(duelGame.getRankedType(), duelGame.getKitType());
                    if(duelGame.getRankedType() == RankedType.RANKED){
                        KitType ladder = game.getKitType();
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

            if(game instanceof PartyFFAGame) {
                PartyFFAGame partyFFAGame = (PartyFFAGame) game;
                GameState state = partyFFAGame.getState();
                partyFFAGame.getGamePlayers().remove(practicePlayer);
                if (state == GameState.Teleporting) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setClickItems();
                            partyPlayer.startPartyScoreboard();
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getGameManager().removeGame(partyFFAGame);
                    }
                }
                if (state == GameState.PreGame) {
                    if (partyFFAGame.getGamePlayers().size() < 3) {
                        partyFFAGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                            partyPlayer.startPartyScoreboard();
                        }
                        partyFFAGame.getArena().unload();
                        HotsPractice.getGameManager().removeGame(partyFFAGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInGame()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
                if (state == GameState.Playing) {
                    if (partyFFAGame.getGamePlayers().size() <= 1) {
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                            partyPlayer.startPartyScoreboard();
                        }
                        partyFFAGame.removeAllSpectatePlayers();
                        partyFFAGame.getArena().unload();
                        HotsPractice.getGameManager().removeGame(partyFFAGame);
                        for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                            if (!all.isInGame()) {
                                practicePlayer.showPlayer(all);
                                all.showPlayer(practicePlayer);
                            }
                        }
                    }
                }
            }

            if(game instanceof PartyTeamGame){
                PartyTeamGame partyTeamGame = (PartyTeamGame) game;
                GameState state = partyTeamGame.getState();
                partyTeamGame.getGamePlayers().remove(practicePlayer);
                if (state == GameState.Teleporting) {
                    if(partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.setClickItems();
                            partyPlayer.startPartyScoreboard();
                        }
                        partyTeamGame.getArena().unload();
                        HotsPractice.getGameManager().removeGame(partyTeamGame);
                    }
                }
                if (state == GameState.PreGame) {
                    partyTeamGame.getMyTeam(practicePlayer).getPlayers().remove(practicePlayer);
                    if (partyTeamGame.getGamePlayers().size() < 3) {
                        partyTeamGame.cancel();
                        for (PracticePlayer partyPlayer : party.getPlayers()) {
                            partyPlayer.sendMessage(ChatColor.RED + "Party Event has cancelled.");
                            partyPlayer.teleportToLobby();
                            partyPlayer.setClickItems();
                            partyPlayer.startPartyScoreboard();
                        }
                    }
                    partyTeamGame.getArena().unload();
                    HotsPractice.getGameManager().removeGame(partyTeamGame);
                    for (PracticePlayer all : HotsPractice.getPracticePlayers()) {
                        if (!all.isInGame()) {
                            practicePlayer.showPlayer(all);
                            all.showPlayer(practicePlayer);
                        }
                    }
                }

                if (state == GameState.Playing) {
                    Team myTeam = partyTeamGame.getMyTeam(practicePlayer);
                    myTeam.getPlayers().remove(practicePlayer);
                    if(myTeam.getAlivePlayers().size() <= 0) {
                        Team opponent = partyTeamGame.getOpponentTeam(myTeam);
                        partyTeamGame.end(opponent.getPrefix() + opponent.getTeamName());
                    }
                }
            }
        }

        Game spectatingGame = HotsPractice.getGameManager().getPlayerOfSpectatingGame(practicePlayer);
        if(spectatingGame != null){
            spectatingGame.getSpectatePlayers().remove(practicePlayer);
            for(PracticePlayer a : spectatingGame.getGamePlayers()){
                a.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
            }
            for(PracticePlayer b : spectatingGame.getSpectatePlayers()){
                b.sendMessage(ChatColor.YELLOW + "(Spectate) " + HotsCore.getHotsPlayer(practicePlayer.getPlayer()).getColorName() + ChatColor.GRAY + " has left.");
            }
        }

        if (duelQueue != null) {
            queueManager.removeQueue(duelQueue);
        }

        if (party != null) {
            party.removePlayer(practicePlayer);
        }

        if (playerInventory != null) {
            inventoryDataManager.removePlayerInventory(playerInventory);
        }

        if(eventGame != null) {
            if (practicePlayer.hasHoldingEventGame()) {
                if (eventGame.getState() == EventGameState.ARENA_PREPARING) {
                    eventGame.delete();
                    return;
                }
            }
            if(eventGame instanceof SumoEventGame) {
                SumoEventGame sumoEventGame = (SumoEventGame) eventGame;
                if (eventGame.getState() != EventGameState.WAITING_FOR_PLAYERS && eventGame.getState() != EventGameState.COUNTDOWN) {
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

        if (deadPlayer.isInGame()) {

            Game game = HotsPractice.getGameManager().getPlayerOfGame(deadPlayer);
            deadPlayer.setRespawnLocation(location);

            if (game instanceof DuelGame) {
                DuelGame duelGame = (DuelGame) game;
                PracticePlayer opponent = duelGame.getOpponent(deadPlayer);
                InventoryDataManager inventoryDataManager = HotsPractice.getInventoryDataManager();
                String duration_time = TimeUtility.timeFormat(duelGame.getTime());

                inventoryDataManager.addPlayerInventory(dead, duration_time);
                inventoryDataManager.addPlayerInventory(opponent.getPlayer(), duration_time);

                opponent.getPlayerData().addPoint();
                deadPlayer.getPlayerData().withdrawPoint();
                opponent.getPlayerData().addWinCount(duelGame.getRankedType(), duelGame.getKitType());

                deadPlayer.respawn();

                if(duelGame.getRankedType() == RankedType.RANKED){
                    KitType ladder = game.getKitType();
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

                game.end(opponent.getName());
                return;
            }
            if (game instanceof PartyDuelGame) {
                PartyDuelGame partyDuelGame = (PartyDuelGame) game;
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
            if (game instanceof PartyFFAGame) {
                PartyFFAGame partyFFAGame = (PartyFFAGame) game;
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
            if (game instanceof PartyTeamGame) {
                PartyTeamGame partyTeamGame = (PartyTeamGame) game;
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
                EventGame eventGame = HotsPractice.getEventGameManager().getPlayerOfEventGame(practicePlayer);
                if(eventGame instanceof SumoEventGame) {
                    SumoEventGame sumoEventGame = (SumoEventGame) eventGame;
                    if (sumoEventGame.getState() == EventGameState.FIGHTING) {
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

            if (practicePlayer.isInGame()) {
                Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
                KitType kitType = game.getKitType();
                if (kitType == KitType.Sumo) {
                    if (game.getState() != GameState.EndGame) {
                        if (event.getTo().getBlock().isLiquid()) {
                            if (game instanceof DuelGame) {
                                DuelGame duelGame = (DuelGame) game;
                                PracticePlayer opponent = duelGame.getOpponent(practicePlayer);
                                opponent.getPlayerData().addWinCount(duelGame.getRankedType(), kitType);
                                opponent.getPlayerData().addTotalWinCount(duelGame.getRankedType());
                                opponent.getPlayerData().addPoint();
                                practicePlayer.getPlayerData().withdrawPoint();
                                duelGame.end(opponent.getName());
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
            if (!practicePlayer.isInGame()) {
                e.setCancelled(true);
                return;
            }
            if(practicePlayer.isEnableSpectate()){
                e.setCancelled(true);
                return;
            }
            Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
            if (game.getKitType() == KitType.Sumo || game.getKitType() == KitType.Spleef) {
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
        if (!practicePlayer.isInGame()) {
            e.setCancelled(true);
        }else{
            Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
            Item item = e.getItemDrop();
            if(item.getItemStack().getType() == Material.BOWL) {
                if (game.getKitType() == KitType.Soup) {
                    item.remove();
                }
                return;
            }
            if(item.getItemStack().getType() == Material.GLASS_BOTTLE) {
                if (game.getKitType() == KitType.Debuff || game.getKitType() == KitType.NoDebuff || game.getKitType() == KitType.OCTC ||
                        game.getKitType() == KitType.Gapple || game.getKitType() == KitType.Combo || game.getKitType() == KitType.Axe ||
                        game.getKitType() == KitType.Soup) {
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
        if(practicePlayer.isInGame()){
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
        if (!practicePlayer.isInGame()) {
            event.setCancelled(true);
            return;
        }
        if(practicePlayer.isEnableSpectate()){
            event.setCancelled(true);
            return;
        }
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        if (game.getState() != GameState.Playing) {
            event.setCancelled(true);
            return;
        }
        if (game.getKitType() == KitType.Spleef) {
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.SNOW_BLOCK) {
                event.setCancelled(true);
                return;
            }
        }
        if (game.getKitType() == KitType.BuildUHC) {
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
        if (!practicePlayer.isInGame()) {
            event.setCancelled(true);
            return;
        }
        if(practicePlayer.isEnableSpectate()){
            event.setCancelled(true);
            return;
        }
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
        if (game.getKitType() == KitType.BuildUHC) {
            if (game.getState() != GameState.Playing) {
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlock();
            Material type = block.getType();
            if (type != Material.COBBLESTONE && type != Material.WOOD) {
                event.setCancelled(true);
                return;
            }
            int kiten = game.getArena().getSpawn1().getBlockY() - 1;
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
                EventGame eventGame = practicePlayer.getInEventGame();
                if(eventGame instanceof SumoEventGame){
                    SumoEventGame sumo = (SumoEventGame) eventGame;
                    if(!sumo.isFighting(practicePlayer)){
                        event.setCancelled(true);
                    }else{
                        event.setDamage(0D);
                    }
                }
                return;
            }
            if (!practicePlayer.isInGame()) {
                event.setCancelled(true);
                return;
            }
            if (practicePlayer.isInGame()) {
                Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
                if (game.getState() == GameState.Teleporting || game.getState() == GameState.PreGame || game.getState() == GameState.EndGame) {
                    event.setCancelled(true);
                    return;
                }
                if(game.getKitType() == KitType.Sumo) {
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
        if (practicePlayer.isInGame()) {
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




