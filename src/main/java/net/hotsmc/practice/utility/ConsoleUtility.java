package net.hotsmc.practice.utility;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleUtility {

    public static String CONSOLE_MESSAGE_PREFIX = "[HotsPractice] ";

    private static ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

    private static String format(String message){
        return CONSOLE_MESSAGE_PREFIX + message;
    }

    public static void sendMessage(String message){
        consoleSender.sendMessage(format(message));
    }
}
