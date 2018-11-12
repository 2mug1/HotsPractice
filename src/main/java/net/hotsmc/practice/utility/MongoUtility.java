package net.hotsmc.practice.utility;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

public class MongoUtility {

    public static Bson find(String key, Object value) {
        return Filters.eq(key, String.valueOf(value));
    }
}
