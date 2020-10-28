package redis.datatypes;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RHashes {

    public Long addKey(Jedis conn, String hashKey, String subKey, String value) {

        return conn.hset(hashKey, subKey, value);
    }

    public String getKey(Jedis conn, String hashKey, String subKey) {
        return conn.hget(hashKey, subKey);
    }

    public Map<String, String> getMap(Jedis conn, String hashKey) {
        return conn.hgetAll(hashKey);
    }

    public Long removeKey(Jedis conn, String hashkey, String subKey) {
        return conn.hdel(hashkey, subKey);
    }

}
