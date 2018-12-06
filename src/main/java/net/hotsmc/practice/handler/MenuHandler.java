package net.hotsmc.practice.handler;

import lombok.Getter;
import net.hotsmc.core.gui.menu.Menu;
import net.hotsmc.practice.event.EventManager;
import net.hotsmc.practice.menus.event.EventMenu;
import net.hotsmc.practice.menus.event.parkour.ParkourEventListMenu;
import net.hotsmc.practice.menus.event.parkour.ParkourEventMenu;
import net.hotsmc.practice.menus.event.sumo.SumoEventListMenu;
import net.hotsmc.practice.menus.event.sumo.SumoEventMenu;
import net.hotsmc.practice.menus.kit.KitEditSelectMenu;
import net.hotsmc.practice.menus.party.PartyCreateMenu;
import net.hotsmc.practice.menus.party.PartyFightMenu;
import net.hotsmc.practice.menus.party.PublicPartyListMenu;
import net.hotsmc.practice.menus.queue.RankedMenu;
import net.hotsmc.practice.menus.queue.UnrankedMenu;

@Getter
public class MenuHandler {

    private PartyCreateMenu partyCreateMenu;
    private PublicPartyListMenu publicPartyListMenu;
    private KitEditSelectMenu kitEditSelectMenu;
    private RankedMenu rankedMenu;
    private UnrankedMenu unrankedMenu;
    private PartyFightMenu partyFightMenu;
    private EventManager eventManager;
    private EventMenu eventMenu;
    private SumoEventMenu sumoEventMenu;
    private SumoEventListMenu sumoEventListMenu;
    private ParkourEventMenu parkourEventMenu;
    private ParkourEventListMenu parkourEventListMenu;

    public MenuHandler load(){
        rankedMenu = new RankedMenu();
        unrankedMenu = new UnrankedMenu();
        kitEditSelectMenu = new KitEditSelectMenu();
        partyCreateMenu = new PartyCreateMenu();
        publicPartyListMenu = new PublicPartyListMenu();
        partyFightMenu = new PartyFightMenu();
        eventManager = new EventManager();
        eventMenu = new EventMenu();
        sumoEventListMenu = new SumoEventListMenu();
        sumoEventMenu = new SumoEventMenu();
        parkourEventMenu = new ParkourEventMenu();
        parkourEventListMenu = new ParkourEventListMenu();
        return this;
    }

}
