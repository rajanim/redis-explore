package redis.datatypes;

import redis.clients.jedis.Jedis;

import java.util.List;

public class Rlists {


    public Long leftPush(Jedis conn, String key, List<String> values) {
        Long num = 0L;
        for (String s : values) {
            num = conn.lpush(key, s);
        }
        return num;
    }

    public Long rightPush(Jedis conn, String key, List<String> values) {
        Long num = 0L;
        for (String s : values) {
            num = conn.rpush(key, s);
        }
        return num;
    }

    public String leftPop(Jedis conn, String key) {
        Long num = 0L;
        return conn.lpop(key);
    }

    public String rightPop(Jedis conn, String key) {
        Long num = 0L;
        return conn.rpop(key);
    }

    public String getElementAt(Jedis conn, String key, int index) {
        return conn.lindex(key, index);
    }

    public List<String> getElementsInRange(Jedis conn, String key, int start, int end) {
        return conn.lrange(key, start, end);
    }
}
