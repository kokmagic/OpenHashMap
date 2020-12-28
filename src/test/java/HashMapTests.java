import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HashMapTests {
    OpenHashMap<Integer, String> testMap = new OpenHashMap<Integer, String>();

    @Test
    public void containsTest(){
        testMap.put(13, "круто");
        assertTrue(testMap.containsKey(13));
    }

    @Test
    public void getTest() {
        testMap.put(13, "круто");
        assertEquals("круто", testMap.get(13));
    }

    @Test
    public void putTest() {
        testMap.put(13, "круто");
        testMap.put(13, "круто");
        assertEquals("круто", testMap.get(13));
    }

    @Test
    public void removeTest() {
        testMap.put(13, "круто");
        assertEquals("круто", testMap.remove(13));
    }

    @Test
    public void hashCodeTest() {
        OpenHashMap<Integer, String> testMap1 = new OpenHashMap<Integer, String>();
        OpenHashMap<Integer, String> testMap2 = new OpenHashMap<Integer, String>();
        for (int i = 0; i < 1202; i++){
            testMap1.put(i, "круто" + i);
            testMap2.put(i, "круто" + i);
        }
        assertEquals(testMap1.hashCode(), testMap2.hashCode());
    }

}
