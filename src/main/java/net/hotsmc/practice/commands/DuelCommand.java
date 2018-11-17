package net.hotsmc.practice.commands;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.queue.DuelGameRequest;
import net.hotsmc.practice.queue.DuelPartyRequest;
import net.hotsmc.practice.PracticePlayer;
import net.hotsmc.practice.menus.ladder.DuelPartyLadderMenu;
import net.hotsmc.practice.menus.ladder.DuelPlayerLadderMenu;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);

            if (practicePlayer == null) {
                return true;
            }

            if(practicePlayer.isInGame() || practicePlayer.isInEvent() || practicePlayer.isEnableSpectate() || practicePlayer.isEnableKitEdit()){
                practicePlayer.sendMessage("You can't use.");
                return true;
            }

            if(args.length == 0){
                practicePlayer.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Duel Commands");
                practicePlayer.sendMessage(ChatColor.YELLOW + "/duel <player/party>");
                practicePlayer.sendMessage(ChatColor.YELLOW + "/duel accept <player/party>");
            }

            if (args.length == 1) {

                String targetName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetName);

                if(targetName.equalsIgnoreCase(player.getName())){
                    practicePlayer.sendMessage("Can't duel yourself");
                    return true;
                }

                //プレイヤーが存在しないなら
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    practicePlayer.sendMessage("Can't find player / プレイヤーが見つかりませんでした");
                    return true;
                }
                PracticePlayer targetPracticePlayer = HotsPractice.getPracticePlayer(targetPlayer);
                if (targetPracticePlayer == null) {
                    practicePlayer.sendMessage("Can't find player / プレイヤーが見つかりませんでした");
                    return true;
                }

                PartyManager partyManager = HotsPractice.getPartyManager();

                //自分がパーティに属しているなら
                if (practicePlayer.isInParty()) {

                    //リーダーじゃないなら拒否
                    if (!partyManager.getPlayerOfParty(practicePlayer).isLeader(practicePlayer)) {
                        practicePlayer.sendMessage(ChatColor.RED + "You aren't leader / あなたはリーダーではありません");
                        return true;
                    }

                    //自分がリーダーなら
                    //相手がパーティに属しているなら
                    if (targetPracticePlayer.isInParty()) {

                        Party party = partyManager.getPlayerOfParty(targetPracticePlayer);

                        //試合に参加中なら
                        if(party.isInGame()){
                            practicePlayer.sendMessage(ChatColor.RED + targetName + " is fighting in match / " + targetName + "は試合中です");
                            return true;
                        }

                        //相手がリーダーじゃないなら属しているパーティ名を送る
                        if (!party.isLeader(targetPracticePlayer)) {
                            practicePlayer.sendMessage(ChatColor.RED + targetName + " isn't leader / " + targetName + "はパーティリーダーではありません");
                            practicePlayer.sendMessage(ChatColor.RED + targetName + " belong to party that name is " + party.getPartyName() + " / " +
                                    targetName + "はパーティ名" + party.getPartyName() + "に所属しています");
                        } else {
                            //相手がリーダーなら選択メニューを開く
                            new DuelPartyLadderMenu(party).openMenu(player, 18);
                        }
                    } else {
                        //相手がパーティに属していないなら拒否
                        practicePlayer.sendMessage(ChatColor.RED + targetName + "doesn't belong to any party / " + targetName + "は何処のパーティにも所属していません");
                        practicePlayer.sendMessage(ChatColor.RED + "If you want to send party duel to any player, then player is need belonging to any party / " +
                                "あなたがパーティーDuelを送りたい場合、相手はどこかのパーティーに属している必要があります");
                    }
                }else {
                    //自分がパーティに属していないのなら

                    //相手がパーティに属していたら
                    if (targetPracticePlayer.isInParty()) {
                        practicePlayer.sendMessage(ChatColor.RED + targetName + " have already been in the party / " + targetName + "はパーティに属していますがあなたはパーティに属していないため、Duelを送ることができません");
                        return true;
                    }
                    //試合に参加中なら
                    if (targetPracticePlayer.isInGame()) {
                        practicePlayer.sendMessage(ChatColor.RED + targetName + " is fighting in match / " + targetName + "は試合中です");
                    } else {
                        //相手がパーティに属していないならメニューを開く
                        new DuelPlayerLadderMenu(targetPracticePlayer).openMenu(player, 18);
                    }
                }
                return true;
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("accept")){
                    String targetName = args[1];

                    if(targetName.equalsIgnoreCase(player.getName())){
                        practicePlayer.sendMessage(ChatColor.RED + "Can't accept yourself");
                        return true;
                    }

                    PartyManager partyManager = HotsPractice.getPartyManager();

                    //自分がパーティに入っているなら
                    if(practicePlayer.isInParty()){
                        Party myParty = partyManager.getPlayerOfParty(practicePlayer);

                        if(myParty.isLeader(practicePlayer)) {

                            //相手のパーティが存在したら
                            if (partyManager.exsitsParty(targetName)) {

                                Party opponentParty = partyManager.getPartyByName(targetName);

                                //相手のパーティが試合中だったら
                                if (opponentParty.isInGame()) {
                                    practicePlayer.sendMessage(ChatColor.RED + targetName + "'s party is fighting /" + targetName + "のパーティは試合中です");
                                    return true;
                                }

                                DuelPartyRequest duelPartyRequest = myParty.getDuelPartyRequestBySendParty(opponentParty);

                                //そのパーティからDuelが送られていたら
                                if (duelPartyRequest != null) {
                                    duelPartyRequest.accept();
                                } else {
                                    practicePlayer.sendMessage(ChatColor.RED + targetName + " hasn't send party duel to yours party yet / " + targetName + "はあなたのパーティにPartyDuelを送っていません");
                                }
                            } else {
                                practicePlayer.sendMessage(ChatColor.RED + "Can't find party / パーティが見つかりません");
                            }
                        }else{
                            practicePlayer.sendMessage(ChatColor.RED + "You aren't party leader / あなたはパーティリーダーではありません");
                        }
                    }else{
                        //パーティに入ってないなら
                        Player targetPlayer = Bukkit.getPlayer(targetName);
                        //プレイヤーが存在しないなら
                        if (targetPlayer == null || !targetPlayer.isOnline()) {
                            practicePlayer.sendMessage("Can't find player / プレイヤーが見つかりませんでした");
                            return true;
                        }
                        PracticePlayer targetPracticePlayer = HotsPractice.getPracticePlayer(targetPlayer);
                        if (targetPracticePlayer == null) {
                            practicePlayer.sendMessage("Can't find player / プレイヤーが見つかりませんでした");
                            return true;
                        }

                        DuelGameRequest duelGameRequest = practicePlayer.getDuelGameRequestBySender(targetPracticePlayer);

                        //Duelが送られていたら
                        if(duelGameRequest != null){
                            //相手がパーティに属していたら
                            if (targetPracticePlayer.isInParty()) {
                                practicePlayer.sendMessage(ChatColor.RED + targetName + " have been in the party / " + targetName + "はパーティに属しているためDuelを開始できません");
                                return true;
                            }
                            //試合に参加中なら
                            if (targetPracticePlayer.isInGame()) {
                                practicePlayer.sendMessage(ChatColor.RED + targetName + " is fighting in match / " + targetName + "は試合中です");
                                return true;
                            }
                            duelGameRequest.accept();
                        }else{
                            practicePlayer.sendMessage(ChatColor.RED + targetName + " hasn't send duel to you yet / " + targetName + "はあなたにDuelを送っていません");
                        }
                    }
                }
            }
        }
        return false;
    }
}
