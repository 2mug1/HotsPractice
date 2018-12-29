package net.hotsmc.practice.handler;

import lombok.Getter;
import net.hotsmc.practice.event.EventManager;
import net.hotsmc.practice.gui.event.EventMenu;
import net.hotsmc.practice.gui.event.parkour.ParkourEventListMenu;
import net.hotsmc.practice.gui.event.parkour.ParkourEventMenu;
import net.hotsmc.practice.gui.event.sumo.SumoEventListMenu;
import net.hotsmc.practice.gui.event.sumo.SumoEventMenu;
import net.hotsmc.practice.gui.kit.KitEditSelectMenu;
import net.hotsmc.practice.gui.party.PartyCreateMenu;
import net.hotsmc.practice.gui.party.PartyFightMenu;
import net.hotsmc.practice.gui.party.PublicPartyListMenu;
import net.hotsmc.practice.gui.queue.RankedMenu;
import net.hotsmc.practice.gui.queue.UnrankedMenu;

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
