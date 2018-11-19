package net.hotsmc.practice.utility;

import net.minecraft.util.org.apache.commons.io.FileDeleteStrategy;
import net.minecraft.util.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class WorldDataUtility {

    public static void copyWorld(File source, File target) {
        try {
            FileUtils.copyDirectory(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWorld(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}