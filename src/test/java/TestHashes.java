import redis.datatypes.RHashes;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static junit.framework.TestCase.assertEquals;

public class TestHashes {

    static Jedis conn;

    @BeforeClass
    public static void setupOnce() {
        conn = new Jedis("localhost");
    }

    @Test
    public void testRHashes() {
        RHashes hashes = new RHashes();
        System.out.println(conn.echo("Testing " + this.getClass().toString()));
        Long ret = hashes.addKey(conn, "hashKey", "subkey1", "value1");
        assertEquals("1", ret.toString());
        assertEquals("value1", hashes.getKey(conn, "hashKey", "subkey1"));
        Long delete = hashes.removeKey(conn, "hashKey", "subkey1");
        assertEquals("1", delete.toString());
    }
}
