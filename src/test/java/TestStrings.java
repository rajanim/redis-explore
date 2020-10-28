import redis.datatypes.RStrings;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import static junit.framework.TestCase.assertEquals;

public class TestStrings {

    static Jedis conn;

    @BeforeClass
    public static void setupOnce() {
       conn = new Jedis("localhost");
    }

    @Test
    public void testString() {
        RStrings str = new RStrings();
        System.out.println(conn.echo("set"));
        String ret = str.setString(conn, "key-2", "value-2");
        assertEquals("OK", ret);
       assertEquals("value-2",  str.getString(conn, "key-2"));
       String delete = str.delString(conn,"key-2");
        assertEquals("1", delete);
    }
}
