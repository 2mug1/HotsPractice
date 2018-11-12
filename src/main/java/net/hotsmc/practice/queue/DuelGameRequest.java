package net.hotsmc.practice.queue;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.game.RankedType;
import net.hotsmc.practice.game.task.DuelGame;
import net.hotsmc.practice.kit.KitType;
import org.bukkit.ChatColor;

@Getter
public class DuelGameRequest extends DuelRequest {

    private PracticePlayer sender;
    private PracticePlayer me;

    public DuelGameRequest(KitType kitType, PracticePlayer sender, PracticePlayer me) {
        super(kitType);
        this.sender = sender;
        this.me = me;
    }

    @Override
    public void accept() {
        me.getDuelGameRequests().remove(me.getDuelGameRequestBySender(sender));
        sender.getInventory().clear();
        me.getInventory().clear();
        sender.sendMessage(HotsCore.getHotsPlayer(sender.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(me.getPlayer()).getColorName());
        me.sendMessage(HotsCore.getHotsPlayer(me.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(sender.getPlayer()).getColorName());
        DuelGame duelGame = new DuelGame(sender, me, RankedType.UNRANKED, getKitType(), HotsPractice.getArenaFactory().create(getKitType()));
        duelGame.start();
    }
}
