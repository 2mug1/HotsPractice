package net.hotsmc.practice.utility;

import net.hotsmc.practice.HotsPractice;
import org.bson.Document;

public class NameUtility {

    public static String getNameByUUID(String uuid){
        Document document = HotsPractice.getInstance().getMongoConnection().getPlayers().find(MongoUtility.find("UUID", uuid)).first();
        if(document == null){
            return null;
        }
        return document.getString("NAME");
    }

    public static String getUUIDByName(String name){
        Document document = HotsPractice.getInstance().getMongoConnection().getPlayers().find(MongoUtility.find("NAME", name)).first();
        if(document == null){
            return null;
        }
        return document.getString("UUID");
    }
}
