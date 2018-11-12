package net.hotsmc.practice.database;

import lombok.Data;
import net.hotsmc.practice.HotsPractice;
import net.hotsmc.practice.config.ConfigCursor;
import net.hotsmc.practice.config.FileConfig;
import net.hotsmc.practice.utility.ConsoleUtility;
@Data
public class MongoConfig {

    private String host;
    private int port;
    private String databaseName;
    private boolean authEnabled;
    private String username, password;

    public MongoConfig load() {
        ConfigCursor cursor = new ConfigCursor(new FileConfig(HotsPractice.getInstance(), "MongoConfig.yml"), "mongo");
        if (!cursor.exists("host")
                || !cursor.exists("port")
                || !cursor.exists("databaseName")
                || !cursor.exists("authEnabled")
                || !cursor.exists("username")
                || !cursor.exists("password")){
            throw new RuntimeException("Failed to load mongoConfig.yml");
        }
        setHost(cursor.getString("host"));
        setPort(cursor.getInt("port"));
        setDatabaseName(cursor.getString("databaseName"));
        setAuthEnabled(cursor.getBoolean("authEnabled"));
        if(cursor.getBoolean("authEnabled")) {
            setUsername(cursor.getString("username"));
            setPassword(cursor.getString("password"));
        }
        ConsoleUtility.sendMessage("MongoConfig.yml has loaded");
        return this;
    }
}
