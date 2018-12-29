package net.hotsmc.practice.player;

import ca.wacos.nametagedit.NametagAPI;
import lombok.Getter;
import lombok.Setter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.core.gui.ClickActionItem;
import net.hotsmc.core.hotbar.HotbarAdapter;
import net.hotsmc.core.other.Cooldown;
import net.hotsmc.core.other.ExpbarTimer;
import net.hotsmc.core.player.HotsPlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.event.Event;
import net.hotsmc.practice.hotbar.PlayerHotbar;
import net.hotsmc.practice.ladder.Ladder;
import net.hotsmc.practice.match.Match;
import net.hotsmc.practice.ladder.LadderType;
import net.hotsmc.practice.ladder.PlayerLadder;
import net.hotsmc.practice.gui.kit.KitChestInventory;
import net.hotsmc.practice.gui.kit.KitLoadoutMenu;
import net.hotsmc.practice.other.BukkitReflection;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.queue.DuelMatchRequest;
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
import java.util.Arrays;
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
    private List<DuelMatchRequest> duelMatchRequests;
    private boolean alive = true;
    private boolean enableSpectate = false;
    private Cooldown enderpearlCooldown = new Cooldown(0);
    private Cooldown gappleCooldown = new Cooldown(0);
    private int ping = 0;
    private boolean frozen = false;
    private boolean eventLost = false;
    private int currentCps = 0;
    private int cps = 0;
    private ClickActionItem[] kitClickItems = new ClickActionItem[7];
    private int renamingKitIndex = -1;
    private PlayerHotbar hotbar;

    public PracticePlayer(Player player) {
        this.player = player;
        this.playerKits = new ArrayList<>();
        this.duelMatchRequests = new ArrayList<>();
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
        return HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(this) != null;
    }

    public DuelMatchRequest getDuelGameRequestBySender(PracticePlayer sender) {
        for (DuelMatchRequest duelMatchRequest : duelMatchRequests) {
            if (duelMatchRequest.getSender().getName().equals(sender.getName())) {
                return duelMatchRequest;
            }
        }
        return null;
    }

    public void addDuelGameRequest(PracticePlayer sender, LadderType ladderType) {
        DuelMatchRequest duelRequest = getDuelGameRequestBySender(sender);
        if (duelRequest == null) {
            duelMatchRequests.add(new DuelMatchRequest(ladderType, sender, this));
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
        this.hotbar = hotbar;
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
        player.teleport( HotsPractice.getInstance().getPracticeConfig().getLobbyLocation());
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
        Arrays.fill(kitClickItems, null);

        ClickActionItem[] clickItems = PlayerHotbar.KIT.getAdapter().getItems();

        setItem(0, clickItems[0].getItemStack());

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            Ladder ladder = getPlayerKitData(HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(this).getLadderType()).getLadders()[i];
            if (ladder != null) {
                if (ladder.isRenamed()) {
                    kitClickItems[i] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Load Kit " + ladder.getName(), Material.ENCHANTED_BOOK, false)) {
                        @Override
                        public void clickAction(Player player) {
                            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                            if (practicePlayer == null) return;
                            Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                            if (match == null) return;
                            LadderType ladderType = match.getLadderType();
                            practicePlayer.getPlayerKitData(ladderType).setKit(finalI, player);
                        }
                    };
                } else {
                    kitClickItems[i] = new ClickActionItem(ItemUtility.createItemStack("" + ChatColor.YELLOW + ChatColor.BOLD + "Load Kit #" + finalI, Material.ENCHANTED_BOOK, false)) {
                        @Override
                        public void clickAction(Player player) {
                            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
                            if (practicePlayer == null) return;
                            Match match = HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfMatch(practicePlayer);
                            if (match == null) return;
                            LadderType ladderType = match.getLadderType();
                            practicePlayer.getPlayerKitData(ladderType).setKit(finalI, player);
                        }
                    };
                }
            }
        }

        int slot = 1;
        for (int i = 0; i < kitClickItems.length; i++) {
            ClickActionItem clickActionItem = kitClickItems[i];
            if (clickActionItem != null) {
                setItem(slot, clickActionItem.getItemStack());
            }
            slot++;
        }

        setItem(8, clickItems[8].getItemStack());
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
        teleport(HotsPractice.getInstance().getPracticeConfig().getKitEditLocation());
        setDefaultKit(ladderType);
        renamingKitIndex = -1;
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
        renamingKitIndex = -1;
    }

    public void openKitLoadoutMenu() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitLoadoutMenu(getPlayerKitData(editLadderType)).openMenu(player, 36);
    }

    public void openKitChest() {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 0.3F, 0.5F);
        new KitChestInventory(editLadderType).open(player);
    }

    public void sendMessage(String message) {
        if (player.isOnline() && player != null) {
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
        sendMessage(ChatColor.YELLOW + "Interact to display player status.");
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
        enderpearlCooldown = new Cooldown(HotsPractice.getInstance().getPracticeConfig().getEnderpearlCooldownTime() * 1000);
        new ExpbarTimer(player, enderpearlCooldown).start();
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

    public Match getInSpectateMatch(){
        return HotsPractice.getInstance().getManagerHandler().getMatchManager().getPlayerOfSpectateMatch(this);
    }

    public boolean isKitRenaming(){
        return renamingKitIndex >= 0;
    }
}