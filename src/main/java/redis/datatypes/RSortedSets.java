package redis.datatypes;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;

public class  RSortedSets {

    public Long addScoreToSet(Jedis conn, String zsetKey, String member, float value){
        return conn.zadd(zsetKey, value, member);
    }

    public Long addScoresToSet(Jedis conn, String zsetKey, Map<Double, String> map){
        return conn.zadd(zsetKey, map);
    }

    public Set<Tuple> getScoresInRange(Jedis conn, String zsetKey, int min, int max){
        return conn.zrangeByScoreWithScores(zsetKey, min, max);
    }



}
