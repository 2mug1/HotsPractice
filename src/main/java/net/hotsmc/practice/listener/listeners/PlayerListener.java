package net.hotsmc.practice.listener.listeners;

import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.game.task.*;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.menus.InventoryDataManager;
import net.hotsmc.practice.menus.PlayerInventory;
import net.hotsmc.practice.queue.*;
import net.hotsmc.practice.game.*;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.utility.TimeUtility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
        if (event.getItem() == null) return;
        if (practicePlayer != null) {
            if (practicePlayer.isInGame()) {
                Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
                if (game.getKitType() == KitType.Soup) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
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
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Material type = event.getClickedBlock().getType();
                if (type == Material.ANVIL) {
                    player.closeInventory();
                    practicePlayer.openKitLoadoutMenu();
                }
                if (type == Material.CHEST) {
                    player.closeInventory();
                    practicePlayer.openKitChest();
                }
            }
            event.setCancelled(true);
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
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            for (ClickActionItem clickActionItem : getDuelClickItems()) {
                if (clickActionItem.equals(itemStack)) {
                    clickActionItem.clickAction(player);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
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
        PracticePlayer practicePlayer = getDuelPlayer(player);
        DuelQueueManager queueManager = getQueueManager();
        DuelQueue duelQueue = getQueueManager().getPlayerOfQueue(practicePlayer);
        Party party = HotsPractice.getPartyManager().getPlayerOfParty(practicePlayer);
        Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
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
                    duelGame.end(opponent.getName());
                }
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

        removeDuelPlayer(practicePlayer);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player dead = event.getEntity();
        PracticePlayer deadPlayer = HotsPractice.getDuelPlayer(dead);
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
                return;
            }
            if (game instanceof PartyTeamGame) {
                PartyTeamGame partyTeamGame = (PartyTeamGame) game;
                return;
            }
            if(game instanceof SumoEventGame){
                SumoEventGame sumoEventGame = (SumoEventGame) game;
                return;
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
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
                                return;
                            }
                            if (game instanceof SumoEventGame) {

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
            PracticePlayer practicePlayer = getDuelPlayer(player);
            if (practicePlayer == null) return;
            if (!practicePlayer.isInGame()) {
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
        PracticePlayer practicePlayer = getDuelPlayer(player);
        if (practicePlayer == null) return;
        if (!practicePlayer.isInGame()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        PracticePlayer practicePlayer = getDuelPlayer(player);
        if (practicePlayer == null) return;
        if (!practicePlayer.isInGame()) {
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
        PracticePlayer practicePlayer = getDuelPlayer(player);
        if (practicePlayer == null) return;
        if (!practicePlayer.isInGame()) {
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
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PracticePlayer practicePlayer = getDuelPlayer(player);
            if (practicePlayer == null) return;
            if (!practicePlayer.isInGame()) {
                event.setCancelled(true);
                return;
            }
            if (practicePlayer.isInGame()) {
                Game game = HotsPractice.getGameManager().getPlayerOfGame(practicePlayer);
                if (game.getState() == GameState.PreGame || game.getState() == GameState.EndGame) {
                    event.setCancelled(true);
                    return;
                }
                if(game.getKitType() == KitType.Sumo) {
                    player.setHealth(20D);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setFireTicks(0);
        PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
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
            PracticePlayer practicePlayer = HotsPractice.getDuelPlayer(player);
            if (practicePlayer == null) return;
            if (!practicePlayer.getGappleCooldown().hasExpired()) {
                event.setCancelled(true);
                return;
            }
            practicePlayer.startGappleCooldown();
            if (item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Head")) {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
            } else {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
            }
        }
    }
}




