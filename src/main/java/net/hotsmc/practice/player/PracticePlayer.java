package net.hotsmc.practice.player;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import lombok.Setter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.core.hotbar.HotbarAdapter;
import net.hotsmc.core.other.Cooldown;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.ladder.PlayerLadder;
import net.hotsmc.practice.menus.kit.KitChestInventory;
import net.hotsmc.practice.menus.kit.KitLoadoutMenu;
import net.hotsmc.practice.other.BukkitReflection;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.queue.DuelGameRequest;
import net.hotsmc.practice.queue.Queue;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PracticePlayer {

    private Player player;
    private PlayerData playerData;
    private List<PlayerLadder> playerKits;
    private boolean enableKitEdit = false;
    private LadderType editLadderType;
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

    public HotsPlayer getHotsPlayer(){
        return HotsCore.getHotsPlayer(player);
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

    public boolean isInLobby(){
        return !isInMatch() && !isInEvent();
    }

    public boolean isInMatch() {
        return  HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(this) != null;
    }

    public DuelGameRequest getDuelGameRequestBySender(PracticePlayer sender) {
        for (DuelGameRequest duelGameRequest : duelGameRequests) {
            if (duelGameRequest.getSender().getName().equals(sender.getName())) {
                return duelGameRequest;
            }
        }
        return null;
    }

    public void addDuelGameRequest(PracticePlayer sender, LadderType ladderType) {
        DuelGameRequest duelRequest = getDuelGameRequestBySender(sender);
        if (duelRequest == null) {
            duelGameRequests.add(new DuelGameRequest(ladderType, sender, this));
        } else {
            duelRequest.setLadderType(ladderType);
        }
        sender.sendMessage(ChatColor.YELLOW + "You sent practice request to " + getName() + " / あなたは" + getName() + "にDuelリクエストを送りました");

        sendMessage(ChatColor.GOLD + "You have been received the practice request by " + sender.getName() + " / あなたは" + sender.getName() + "からDuelリクエストを受け取りました");
        ComponentBuilder msg = new ComponentBuilder(ChatUtility.PLUGIN_MESSAGE_PREFIX);
        msg.append("" + ChatColor.YELLOW + ChatColor.UNDERLINE + "Click to accept / クリックして開始 - " + ladderType.name());
        msg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + sender.getName()));
        msg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/duel accept " + sender.getName()).create()));
        player.spigot().sendMessage(msg.create());
    }

    public boolean isInParty() {
        return  HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(this) != null;
    }

    public void loadPlayerKits() {
        String uuid = player.getUniqueId().toString();
        for (LadderType ladderType : LadderType.values()) {
            playerKits.add(new PlayerLadder(ladderType, uuid).load());
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

    public PlayerLadder getPlayerKitData(LadderType ladderType) {
        for (PlayerLadder kitData : playerKits) {
            if (kitData.getLadderType() == ladderType) {
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


    public void setHotbar(PlayerHotbar hotbar) {
        clearInventory();
        HotbarAdapter adapter = hotbar.getAdapter();
        ClickActionItem[] items = adapter.getItems();
        for(int i = 0; i < items.length; i++){
            if(items[i] != null) {
                setItem(i, items[i].getItemStack());
            }
        }
        player.updateInventory();
    }

    public void teleportToLobby() {
        player.teleport( HotsPractice.getInstance().getMatchConfig().getLobbyLocation());
    }

    public void resetPlayer() {
        clearInventory();
        clearArmors();
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setHealth(20D);
        PlayerUtility.clearEffects(player);
    }

    public void setKitHotbar() {
        getInventory().clear();

        ClickActionItem[] clickItems = PlayerHotbar.KIT.getAdapter().getItems();

        setItem(0, clickItems[0].getItemStack());

        int slot = 2;
        for (int i = 0; i < 7; i++) {
            if (getPlayerKitData( HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(this).getLadderType()).getLadderList().get(i) != null) {
                setItem(slot, clickItems[slot].getItemStack());
            }
            slot++;
        }
    }

    public void onQueue() {
        player.closeInventory();
        setHotbar(PlayerHotbar.QUEUE);
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

    public void setDefaultKit(LadderType type) {
        getInventory().clear();
        if (type == LadderType.Sumo) {
            sendMessage(ChatColor.YELLOW + "You have loaded Default kit");
            return;
        }
        if (type == LadderType.Spleef) {
            setItem(0, ItemUtility.addEnchant(ItemUtility.createItemStack(ChatColor.YELLOW + "Spleef Spade", Material.DIAMOND_SPADE, true), Enchantment.DIG_SPEED, 5));
            sendMessage(ChatColor.YELLOW + "You have loaded Default kit");
            return;
        }
        setItems(HotsPractice.getInstance().getDefaultLadder().getKitData(type).getItems());
        setArmors(HotsPractice.getInstance().getDefaultLadder().getKitData(type).getArmors());
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


    public void enableKitEdit(LadderType ladderType) {
        setEnableKitEdit(true);
        this.setEditLadderType(ladderType);
        HotsPractice.getInstance().getManagerHandler().getLadderEditManager().teleport(player, ladderType);
        setDefaultKit(ladderType);
    }

    public void clearEffects() {
        PlayerUtility.clearEffects(player);
    }

    public void disableKitEdit() {
        setEnableKitEdit(false);
        this.setEditLadderType(null);
        clearArmors();
        clearEffects();
        teleportToLobby();
        setHotbar(PlayerHotbar.LOBBY);
    }

    public void openKitLoadoutMenu() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitLoadoutMenu(getPlayerKitData(editLadderType)).openMenu(player, 45);
    }

    public void openKitChest() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitChestInventory(editLadderType).open(player);
    }

    public void sendMessage(String message) {
        if (player.isOnline()) {
            ChatUtility.sendMessage(player, message);
        }
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
        enderpearlCooldown = new Cooldown(HotsPractice.getInstance().getMatchConfig().getEnderpearlCooldownTime() * 1000);
    }

    public void startGappleCooldown() {
        gappleCooldown = new Cooldown(2000);
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public void heal() {
        player.setHealth(20D);
        player.setFoodLevel(20);
    }

    public boolean isInEvent() {
        return HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(this) != null;
    }

    /**
     * イベントを主催中か
     *
     * @return
     */
    public boolean hasHoldingEventGame() {
        for (Event event : HotsPractice.getInstance().getManagerHandler().getEventManager().getEvents()) {
            if (event.getLEADER_UUID().equals(getUUID())) {
                return true;
            }
        }
        return false;
    }

    public Party getInParty() {
        return HotsPractice.getInstance().getManagerHandler().getPartyManager().getPlayerOfParty(this);
    }

    public Match getInMatch() {
        return HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(this);
    }

    public Event getInEventGame() {
        return HotsPractice.getInstance().getManagerHandler().getEventManager().getPlayerOfEvent(this);
    }

    public void addCurrentCps(int amount) {
        currentCps = amount + currentCps;
    }

    public boolean isInQueue() {
        return getInQueue() != null;
    }

    public Queue getInQueue(){
        return HotsPractice.getInstance().getManagerHandler().getQueueManager().getPlayerOfQueue(this);
    }
}