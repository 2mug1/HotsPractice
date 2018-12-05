package net.hotsmc.practice.queue;

import lombok.Getter;
import net.hotsmc.core.HotsCore;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.match.RankedType;
import net.hotsmc.practice.match.impl.DuelMatch;
import net.hotsmc.practice.ladder.LadderType;
import org.bukkit.ChatColor;

@Getter
public class DuelGameRequest extends DuelRequest {

    private PracticePlayer sender;
    private PracticePlayer me;

    public DuelGameRequest(LadderType ladderType, PracticePlayer sender, PracticePlayer me) {
        super(ladderType);
        this.sender = sender;
        this.me = me;
    }

    @Override
    public void accept() {
        me.getDuelGameRequests().remove(me.getDuelGameRequestBySender(sender));
        sender.getInventory().clear();
        me.getInventory().clear();
        sender.sendMessage(ChatColor.GRAY + "Starting match (" + HotsCore.getHotsPlayer(sender.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(me.getPlayer()).getColorName() + ChatColor.GRAY + ")");
        me.sendMessage(ChatColor.GRAY + "Starting match (" + HotsCore.getHotsPlayer(me.getPlayer()).getColorName() + ChatColor.GRAY + " vs " + HotsCore.getHotsPlayer(sender.getPlayer()).getColorName() + ChatColor.GRAY + ")");
        DuelMatch duelGame = new DuelMatch(sender, me, RankedType.UNRANKED, this.getLadderType(), HotsPractice.getInstance().getArenaFactory().create(this.getLadderType()));
        duelGame.start();
    }
}
