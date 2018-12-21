package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SimpleAttributesTest extends BaseUnitTest {

    public SimpleAttributesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testIsDistanceFunction() {
        showBeginTest();
        SimpleAttributes instance = new SimpleAttributes();
        boolean expResult = false;
        boolean result = instance.isDistanceFunction();
        assertEquals(expResult, result);
        showEndTest();
    }


    //---
    private void test(String label, Context c1, Context c2, double expResult) {
        System.out.print("   " +label + ": ");
        SimpleAttributes instance = new SimpleAttributes();
        double result = 0;
        try {
            instance.validateParameters();
            result = instance.getSimilarity(c1, c2);
        } catch (Exception ex) {
            failException(ex);
        }
        assertEquals(expResult, result, 0.0);
        System.out.println(expResult + " [ok]");
    }
    //----

    @Test
    public void testGetSimilarity_Time() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(time(9, 10), null, null, null);
        c2 = createContext(time(9, 10), null, null, null);        
        test("sameTime", c1, c2, 1.0);
        //2
        c1 = createContext(time(9, 10), null, null, null);
        c2 = createContext(time(9, 11), null, null, null);
        test("other time", c1, c2, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Location() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(null, location("work"), null, null);
        c2 = createContext(null, location("work"), null, null);
        test("same location", c1, c2, 1.0);
        //2
        c1 = createContext(null, location("work"), null, null);
        c2 = createContext(null, location("home"), null, null);
        test("other location", c1, c2, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Situation_1() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(null, null, sit("testA", "xyz"));
        c2 = createContext(null, null, sit("testA", "xyz"));
        test("same situation", c1, c2, 1.0);
        //2
        c1 = createContext(null, null, sit("testA", "123"));
        c2 = createContext(null, null, sit("testA", "456"));
        test("other situation", c1, c2, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Situation_2() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("testB", "xyz"));
        c2 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("testB", "xyz"));
        test("same situations", c1, c2, 1.0);
        //2
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("testB", "abc"), sit("testC", "123"));
        c2 = createContext(time(9, 11), null, sit("testA", "xyz"), sit("testB", "def"), sit("testC", "123"));
        test("other time and 1 situation", c1, c2, 0.5);
        //2
        c1 = createContext(time(9, 10), null, sit("testA", "123"), sit("testB", "456"));
        c2 = createContext(time(9, 11), null, sit("testA", "xyz"), sit("testB", "abc"));
        test("others situations", c1, c2, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Time_Location() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(9, 10), location("work"), null, null);
        test("same time same location", c1, c2, 1.0);
        //2
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(9, 10), location("home"), null, null);
        test("same time other location", c1, c2, 0.5);
        //3
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(8, 10), location("work"), null, null);
        test("other time same location ", c1, c2, 0.5);
        //4
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(7, 37), location("home"), null, null);
        test("other time other location", c1, c2, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Time_Situation() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(time(9, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(time(9, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        test("same time same situation", c1, c2, 1.0);
        //2
        c1 = createContext(time(9, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(time(9, 10), null, sit("testA", "ccc"));
        test("same time other situation", c1, c2, 0.25); //1 time value equal / 1 time + 3 unique situations
        //3
        c1 = createContext(time(9, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(time(8, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        test("other time same situation", c1, c2, 0.75); //3 situation values equal / 1 time + 3 unique situations
        //4
        c1 = createContext(time(9, 10), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(time(7, 37), null, sit("testX", "abc"), sit("testY", "123"), sit("testZ", "xyz"));
        test("other time other situation", c1, c2, 0.0);
        //--
        showEndTest();
    }


    @Test
    public void testGetSimilarity_Location_Situation() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(null, location("work"), sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(null, location("work"), sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        test("same location same situation", c1, c2, 1.0);
        //2
        c1 = createContext(null, location("work"), sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(null, location("work"), null, sit("testA", "ccc"));
        test("same location other situation", c1, c2, 0.25); //1 location name equal / 1 location + 3 unique situations
        //3
        c1 = createContext(null, location("work"), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(null, location("home"), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        test("other location same situation", c1, c2, 0.75); //3 situation values equal / 1 location + 3 unique situations
        //4
        c1 = createContext(null, location("work"), null, sit("testA", "abc"), sit("testB", "123"), sit("testC", "xyz"));
        c2 = createContext(null, location("home"), null, sit("testX", "abc"), sit("testY", "123"), sit("testZ", "xyz"));
        test("other location other situation", c1, c2, 0.0);
        //--
        showEndTest();
    }


}