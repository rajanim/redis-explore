package redis.datatypes;

import redis.clients.jedis.Jedis;


public class RStrings {

    public String setString(Jedis con, String key, String value) {
        return (con.set(key, value));
    }

    public String getString(Jedis con, String key) {
        return con.get(key);
    }

    public String delString(Jedis con, String key){
        return con.del(key).toString();
    }



}
