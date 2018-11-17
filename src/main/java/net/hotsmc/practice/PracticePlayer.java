package net.hotsmc.practice;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import lombok.Setter;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.practice.database.PlayerData;
import net.hotsmc.practice.game.events.EventGame;
import net.hotsmc.practice.game.games.Game;
import net.hotsmc.practice.kit.KitType;
import net.hotsmc.practice.kit.PlayerKitData;
import net.hotsmc.practice.menus.kit.KitChestInventory;
import net.hotsmc.practice.menus.kit.KitLoadoutMenu;
import net.hotsmc.practice.other.BukkitReflection;
import net.hotsmc.practice.other.Cooldown;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.queue.DuelGameRequest;
import net.hotsmc.practice.scoreboard.*;
import net.hotsmc.practice.utility.ChatUtility;
import net.hotsmc.practice.utility.ItemUtility;
import net.hotsmc.practice.utility.PlayerUtility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PracticePlayer {

    private Player player;
    private PlayerData playerData;
    private PlayerScoreboard scoreboard;
    private String opponent;
    private List<PlayerKitData> playerKits;
    private boolean enableKitEdit = false;
    private KitType editKitType;
    private Location respawnLocation;
    private List<DuelGameRequest> duelGameRequests;
    private boolean alive = true;
    private boolean enableSpectate = false;
    private Cooldown enderpearlCooldown = new Cooldown(0);
    private Cooldown gappleCooldown = new Cooldown(0);
    private int ping = 0;
    private boolean frozen = false;
    private boolean eventLost = false;
    private int currentCps = 0;
    private int cps = 0;

    public PracticePlayer(Player player) {
        this.player = player;
        this.playerKits = new ArrayList<>();
        this.duelGameRequests = new ArrayList<>();
    }

    public void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                setCps(currentCps);
                setCurrentCps(0);
                setPing(BukkitReflection.getPing(player));
            }
        }.runTaskTimer(HotsPractice.getInstance(), 0, 20);
    }

    public boolean isInGame() {
        return HotsPractice.getGameManager().getPlayerOfGame(this) != null;
    }

    public DuelGameRequest getDuelGameRequestBySender(PracticePlayer sender) {
        for (DuelGameRequest duelGameRequest : duelGameRequests) {
            if (duelGameRequest.getSender().getName().equals(sender.getName())) {
                return duelGameRequest;
            }
        }
        return null;
    }

    public void addDuelGameRequest(PracticePlayer sender, KitType kitType) {
        DuelGameRequest duelRequest = getDuelGameRequestBySender(sender);
        if (duelRequest == null) {
            duelGameRequests.add(new DuelGameRequest(kitType, sender, this));
        } else {
            duelRequest.setKitType(kitType);
        }
        sender.sendMessage(ChatColor.YELLOW + "You sent practice request to " + getName() + " / あなたは" + getName() + "にDuelリクエストを送りました");

        sendMessage(ChatColor.GOLD + "You have been received the practice request by " + sender.getName() + " / あなたは" + sender.getName() + "からDuelリクエストを受け取りました");
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX);
        msg.append("" + ChatColor.YELLOW + ChatColor.UNDERLINE + "Click to accept / クリックして開始 - " + kitType.name());
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/practice accept " + sender.getName()));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/practice accept " + sender.getName()).create()));
        player.spigot().sendMessage(msg.create());
    }

    public boolean isInParty() {
        return HotsPractice.getPartyManager().getPlayerOfParty(this) != null;
    }

    public void loadPlayerKits() {
        String uuid = player.getUniqueId().toString();
        for (KitType kitType : KitType.values()) {
            playerKits.add(new PlayerKitData(kitType, uuid).load());
        }
    }

    public void setPrefix(String prefix) {
        NametagAPI.setPrefix(player.getName(), prefix);
    }

    public String getName() {
        return player.getName();
    }

    public String getUUID() {
        return player.getUniqueId().toString();
    }

    public PlayerKitData getPlayerKitData(KitType kitType) {
        for (PlayerKitData kitData : playerKits) {
            if (kitData.getKitType() == kitType) {
                return kitData;
            }
        }
        return null;
    }

    public void clearInventory() {
        getInventory().clear();
        player.updateInventory();
    }

    public Inventory getInventory() {
        return player.getInventory();
    }

    public Location getLocation() {
        return player.getLocation();
    }

    public void setItem(int slot, ItemStack itemStack) {
        getInventory().setItem(slot, itemStack);
    }

    public void clearArmors() {
        EntityEquipment equipment = player.getEquipment();
        equipment.setHelmet(null);
        equipment.setChestplate(null);
        equipment.setLeggings(null);
        equipment.setBoots(null);
        player.updateInventory();
    }

    public void setClickItems() {
        getInventory().clear();
        List<ClickActionItem> items = HotsPractice.getDuelClickItems();
        setItem(0, items.get(0).getItemStack());
        setItem(1, items.get(1).getItemStack());
        setItem(2, items.get(2).getItemStack());
        setItem(3, items.get(12).getItemStack());
        setItem(6, items.get(3).getItemStack());
        setItem(7, items.get(4).getItemStack());
        setItem(8, items.get(5).getItemStack());
        player.updateInventory();
    }

    public void teleportToLobby() {
        player.teleport(HotsPractice.getGameConfig().getLobbyLocation());
    }

    public void resetPlayer() {
        clearInventory();
        clearArmors();
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setHealth(20D);
        PlayerUtility.clearEffects(player);
    }

    public void startLobbyScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new LobbyScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startQueueScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new QueueScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startPartyScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new PartyScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startDuelGameScoreboard(String opponent) {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        this.opponent = opponent;
        scoreboard = new DuelGameScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startPartyDuelGameScoreboard(String opponentParty) {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        this.opponent = opponentParty;
        scoreboard = new PartyDuelGameScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startKitEditScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new KitEditScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startSumoEventWaitingScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new SumoEventWaitingScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startSumoEventCountdownScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new SumoEventCountdownScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startSumoEventFightingScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new SumoEventFightingScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startPartyFFAScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new PartyGameFFAScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void startPartyTeamScoreboard() {
        if (scoreboard != null) {
            scoreboard.stop();
        }
        scoreboard = new PartyGameTeamScoreboard(this);
        scoreboard.setup();
        scoreboard.start();
    }

    public void setQueueItem() {
        getInventory().clear();
        setItem(0, HotsPractice.getDuelClickItems().get(6).getItemStack());
        player.updateInventory();
    }

    public void setKitItems() {
        List<ClickActionItem> clickItems = HotsPractice.getDuelClickItems();

        getInventory().clear();
        setItem(0, clickItems.get(7).getItemStack());

        int slot = 2;
        int clickItemIndex = 16;
        for (int i = 0; i < 7; i++) {
            if (getPlayerKitData(HotsPractice.getGameManager().getPlayerOfGame(this).getKitType()).getKitDataList().get(i) != null) {
                setItem(slot, clickItems.get(clickItemIndex).getItemStack());
            }
            clickItemIndex++;
            slot++;
        }
    }

    public void onQueue() {
        player.closeInventory();
        setQueueItem();
        startQueueScoreboard();
    }

    public void setItems(List<ItemStack> items) {
        getInventory().clear();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            setItem(i, items.get(i));
        }
        player.updateInventory();
    }

    public void setArmors(List<ItemStack> armors) {
        EntityEquipment entityEquipment = player.getEquipment();
        entityEquipment.setHelmet(armors.get(3));
        entityEquipment.setChestplate(armors.get(2));
        entityEquipment.setLeggings(armors.get(1));
        entityEquipment.setBoots(armors.get(0));
        player.updateInventory();
    }

    public void setSpectateItems(){
        clearInventory();
        setItem(4, HotsPractice.getDuelClickItems().get(15).getItemStack());
    }

    public void setDefaultKit(KitType type) {
        getInventory().clear();
        if (type == KitType.Sumo) {
            sendMessage(ChatColor.YELLOW + "You have loaded Default kit");
            return;
        }
        if (type == KitType.Spleef) {
            setItem(0, ItemUtility.addEnchant(ItemUtility.createItemStack(ChatColor.YELLOW + "Spleef Spade", Material.DIAMOND_SPADE, true), Enchantment.DIG_SPEED, 5));
            sendMessage(ChatColor.YELLOW + "You have loaded Default kit");
            return;
        }
        setItems(HotsPractice.getDefaultKit().getKitData(type).getItems());
        setArmors(HotsPractice.getDefaultKit().getKitData(type).getArmors());
        sendMessage(ChatColor.YELLOW + "You have loaded Default kit");
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public void teleport(Location location) {
        if (player.isOnline()) {
            player.teleport(location);
        }
    }


    public void enableKitEdit(KitType kitType) {
        setEnableKitEdit(true);
        setEditKitType(kitType);
        HotsPractice.getKitEditManager().teleport(player, kitType);
        setDefaultKit(kitType);
        startKitEditScoreboard();
    }

    public void clearEffects() {
        PlayerUtility.clearEffects(player);
    }

    public void disableKitEdit() {
        setEnableKitEdit(false);
        setEditKitType(null);
        clearArmors();
        clearEffects();
        teleportToLobby();
        setClickItems();
        startLobbyScoreboard();
    }

    public void openKitLoadoutMenu() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitLoadoutMenu(getPlayerKitData(editKitType)).openMenu(player, 45);
    }

    public void openKitChest() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitChestInventory(editKitType).open(player);
    }

    public void sendMessage(String message) {
        if (player.isOnline()) {
            ChatUtility.sendMessage(player, message);
        }
    }

    public void setPartyClickItems() {
        getInventory().clear();
        List<ClickActionItem> clickActionItems = HotsPractice.getDuelClickItems();
        setItem(1, clickActionItems.get(8).getItemStack());
        setItem(3, clickActionItems.get(9).getItemStack());
        setItem(5, clickActionItems.get(10).getItemStack());
        setItem(7, clickActionItems.get(11).getItemStack());
    }

    public void respawn() {
        PlayerUtility.respawn(player, 10);
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }

    public void hidePlayer(PracticePlayer other) {
        if (isOnline()) {
            player.hidePlayer(other.getPlayer());
        }
    }

    public void showPlayer(PracticePlayer other) {
        if (isOnline()) {
            player.showPlayer(other.getPlayer());
        }
    }

    public void enableSpectate() {
        setEnableSpectate(true);
        for (PracticePlayer other : HotsPractice.getPracticePlayers()) {
            if (other != this) {
                other.hidePlayer(this);
            }
        }
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public void disableSpectate() {
        setEnableSpectate(false);
        for (PracticePlayer other : HotsPractice.getPracticePlayers()) {
            if (other != this) {
                other.showPlayer(this);
            }
        }
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public void startEnderpearlCooldown() {
        enderpearlCooldown = new Cooldown(HotsPractice.getGameConfig().getEnderpearlCooldownTime() * 1000);
    }

    public void startGappleCooldown() {
        gappleCooldown = new Cooldown(2000);
    }

    public void startEnderpearlItemTask() {
        PracticePlayer practicePlayer = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null || !isOnline() || HotsPractice.getGameManager().getPlayerOfGame(practicePlayer) == null) {
                    this.cancel();
                    return;
                }
                if (!enderpearlCooldown.hasExpired()) {
                    for (int i = 0; i < getInventory().getContents().length; i++) {
                        ItemStack itemStack = getInventory().getContents()[i];
                        if (itemStack == null) return;
                        if (itemStack.getType() == Material.ENDER_PEARL) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta == null) return;
                            itemMeta.setDisplayName(ChatColor.RESET + "Ender Pearl " + ChatColor.YELLOW + enderpearlCooldown.getTimeLeft() + "s");
                            itemStack.setItemMeta(itemMeta);
                            setItem(i, itemStack);
                        }
                    }
                } else {
                    for (int i = 0; i < getInventory().getContents().length; i++) {
                        ItemStack itemStack = getInventory().getContents()[i];
                        if (itemStack == null) return;
                        if (itemStack.getType() == Material.ENDER_PEARL) {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta == null) return;
                            String displayName = itemMeta.getDisplayName();
                            if (displayName == null) return;
                            if (!itemMeta.getDisplayName().equals("Ender Pearl")) {
                                itemMeta.setDisplayName(ChatColor.RESET + "Ender Pearl");
                                itemStack.setItemMeta(itemMeta);
                                setItem(i, itemStack);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(HotsPractice.getInstance(), 0, 1);
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public void heal() {
        player.setHealth(20D);
        player.setFoodLevel(20);
    }

    public boolean isInEvent() {
        return HotsPractice.getEventGameManager().getPlayerOfEventGame(this) != null;
    }

    public void setEventItems() {
        clearInventory();
        setItem(4, HotsPractice.getDuelClickItems().get(13).getItemStack());
    }

    public void setEventLeaderItems() {
        clearInventory();
        setItem(3, HotsPractice.getDuelClickItems().get(13).getItemStack());
        setItem(5, HotsPractice.getDuelClickItems().get(14).getItemStack());
    }

    /**
     * イベントを主催中か
     *
     * @return
     */
    public boolean hasHoldingEventGame() {
        for(EventGame eventGame : HotsPractice.getEventGameManager().getGames()){
            if(eventGame.getLEADER_UUID().equals(getUUID())){
                return true;
            }
        }
        return false;
    }

    public Party getInParty(){
        return HotsPractice.getPartyManager().getPlayerOfParty(this);
    }

    public Game getInGame(){
        return HotsPractice.getGameManager().getPlayerOfGame(this);
    }

    public EventGame getInEventGame(){
        return HotsPractice.getEventGameManager().getPlayerOfEventGame(this);
    }

    public void addCurrentCps(int amount) {
        currentCps = amount+currentCps;
    }
}