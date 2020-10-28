package redis.datatypes;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class RSets {

    public Long addElement(Jedis conn, String key, String elem) {
        return conn.sadd(key, elem);
    }


    public Long addElements(Jedis conn, String key, List<String> elements) {
        Long finalnum = 0L;
        for (String e : elements)
            finalnum = conn.sadd(key, e);
        return finalnum;
    }

    public Set<String> getElements(Jedis conn, String key) {
        return conn.smembers(key);
    }

    public Long removeElement(Jedis conn, String key, String elem) {
        return conn.srem(key, elem);
    }
}
