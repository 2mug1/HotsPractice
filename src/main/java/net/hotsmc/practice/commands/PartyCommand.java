package net.hotsmc.practice.commands;

import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.player.PracticePlayer;
import net.hotsmc.practice.party.Party;
import net.hotsmc.practice.party.PartyManager;
import net.hotsmc.practice.utility.ChatUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = HotsPractice.getPracticePlayer(player);
            PartyManager partyManager = HotsPractice.getInstance().getManagerHandler().getPartyManager();
            if (practicePlayer == null) {
                return true;
            }
            if(args.length == 0){
                if (practicePlayer.isInParty()) {
                    practicePlayer.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Party Commands");
                    practicePlayer.sendMessage("" + ChatColor.YELLOW + "/party disband - Disband the party");
                    practicePlayer.sendMessage("" + ChatColor.YELLOW + "/party invite <stats> - Send invitation to stats");
                    practicePlayer.sendMessage("" + ChatColor.YELLOW + "/party kick <stats> - Kick party from party");
                    practicePlayer.sendMessage("" + ChatColor.YELLOW + "/party join <party_name> - Join the party");
                }else{
                    practicePlayer.sendMessage(ChatColor.RED + "You aren't in party / あなたはパーティに属していません");
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("disband")) {
                    if (practicePlayer.isInParty()) {
                        Party party = partyManager.getPlayerOfParty(practicePlayer);
                        if (!party.isLeader(practicePlayer)) {
                            practicePlayer.sendMessage(ChatColor.RED + "You aren't leader of the party / あなたはリーダーではありません");
                        } else {
                            party.disband();
                            partyManager.removeParty(partyManager.getPartyByName(party.getPartyName()));
                        }
                    } else {
                        practicePlayer.sendMessage(ChatColor.RED + "You aren't in party / あなたはパーティに属していません");
                    }
                }
                return true;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("invite")) {
                    String targetName = args[1];
                    if(targetName.equalsIgnoreCase(player.getName())){
                        practicePlayer.sendMessage(ChatColor.RED + "Can't send party invitation yourself / パーティ招待を自分に送ることはできません");
                        return true;
                    }
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null || !target.isOnline()) {
                        practicePlayer.sendMessage(ChatColor.RED + "Can't find stats / プレイヤーが見つかりません");
                        return true;
                    }
                    PracticePlayer targetPracticePlayer = HotsPractice.getPracticePlayer(target);
                    if (targetPracticePlayer == null) {
                        practicePlayer.sendMessage(ChatColor.RED + "Can't find stats / プレイヤーが見つかりません");
                        return true;
                    }
                    if (practicePlayer.isInParty()) {
                        Party party = partyManager.getPlayerOfParty(practicePlayer);
                        if (targetPracticePlayer.isInParty()) {
                            ChatUtility.sendMessage(player, "" + ChatColor.RED + target + " has already been in party / " + targetName + "は既にパーティに属しています");
                            return true;
                        }
                        if (targetPracticePlayer.isInMatch()) {
                            ChatUtility.sendMessage(player, "" + ChatColor.RED + target + " is fighting in the queue / " + targetName + "は戦っている最中です");
                            return true;
                        }
                        if (!party.isLeader(practicePlayer)) {
                            practicePlayer.sendMessage(ChatColor.RED + "You aren't leader of the party / あなたはリーダーではありません");
                        }else{
                            party.invite(practicePlayer, targetPracticePlayer);
                        }
                    } else {
                        practicePlayer.sendMessage(ChatColor.RED + "You aren't in party / あなたはパーティに属していません");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("join")) {
                    String partyName = args[1];
                    if(partyManager.exsitsParty(partyName)){
                        Party party = partyManager.getPartyByName(partyName);
                        party.addPlayer(practicePlayer);
                    }else{
                        practicePlayer.sendMessage(ChatColor.RED + "Party doesn't exist / パーティが存在しません");
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("kick")){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null || !target.isOnline()) {
                        practicePlayer.sendMessage(ChatColor.RED + "Can't find stats / プレイヤーが見つかりません");
                        return true;
                    }
                    PracticePlayer targetPracticePlayer = HotsPractice.getPracticePlayer(target);
                    if (targetPracticePlayer == null) {
                        practicePlayer.sendMessage(ChatColor.RED + "Can't find stats / プレイヤーが見つかりません");
                        return true;
                    }
                    if (practicePlayer.isInParty()) {
                        Party party = partyManager.getPlayerOfParty(practicePlayer);
                        if(party.isLeader(practicePlayer)){
                            if(targetName.equalsIgnoreCase(player.getName())){
                                practicePlayer.sendMessage(ChatColor.RED + "Can't kick yourself / 自分をキックすることはできません");
                            }else{
                                party.kickPlayer(practicePlayer, targetPracticePlayer);
                            }
                        }else{
                            practicePlayer.sendMessage(ChatColor.RED + "You aren't leader of the party / あなたはリーダーではありません");
                        }
                    } else {
                        practicePlayer.sendMessage(ChatColor.RED + "You aren't in a party / あなたはパーティに属していません");
                    }
                }
            }
        }
        return true;
    }
}