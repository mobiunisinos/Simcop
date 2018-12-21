package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.impl.similarity.sequences.WSContextProbability;
import br.unisinos.simcop.data.model.Situation;
import java.util.Map;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.TimeDescription;
import java.util.Iterator;
import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.ArrayList;
import java.util.List;
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
public class WSContextProbabilityTest extends BaseUnitTest {

    public WSContextProbabilityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCalculateFrequency() {
        showBeginTest();
        ContextSequence sequence = new ContextSequence(null);
        //CTX1
        Context ctx1 = createContext(100, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context ctx1b = createContext(101, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context ctx1c = createContext(102, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context ctx1d = createContext(103, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context ctx1e = createContext(104, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        //CTX2
        Context ctx2 = createContext(200, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"));
        Context ctx2b = createContext(201, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"));
        Context ctx2c = createContext(202, new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"));
        //CTX3
        Context ctx3 = createContext(300, new TimeDescription(7, 45, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context ctx3b = createContext(301, new TimeDescription(7, 45, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        //==========

        sequence.addContext(ctx1);
        sequence.addContext(ctx2);
        sequence.addContext(ctx1b);
        sequence.addContext(ctx1c);
        sequence.addContext(ctx2b);
        sequence.addContext(ctx3);
        sequence.addContext(ctx1d);
        sequence.addContext(ctx3b);
        sequence.addContext(ctx1e);
        sequence.addContext(ctx2c);


        //-----
        WSContextProbability instance = new WSContextProbability();
        for (Context ctx : sequence.getContexts()) {
            System.out.println("Context " + ctx.getIndex() + ": " + instance.getHashCode(ctx));
        }
        Map<Integer, Context> result = instance.calculateFrequency(sequence);
        //-----
        for (Integer hash : result.keySet()) {
            System.out.println("Group: Context[" + result.get(hash).getIndex() + "]");
        }

        assertEquals(3, result.size());
        assertNotNull("ctx1 not found", result.get(ctx1.hashCode()));
        assertNotNull("ctx2 not found", result.get(ctx2.hashCode()));
        assertNotNull("ctx3 not found", result.get(ctx3.hashCode()));
        assertEquals("ctx1 invalid frequency", 5, ctx1.getFrequency(), 0);
        assertEquals("ctx2 invalid frequency", 3, ctx2.getFrequency(), 0);
        assertEquals("ctx3 invalid frequency", 2, ctx3.getFrequency(), 0);

        System.out.println("CTX1: " + ctx1.getFrequency());
        System.out.println("CTX2: " + ctx2.getFrequency());
        System.out.println("CTX3: " + ctx3.getFrequency());


        showEndTest();
    }

    /**
     * TEST DESCRIPTION:
     *
     * Sequence 1:
     * Uid |  Time   | Location | Situation 1 | Situation 2       | Group
     * ----+---------+----------+-------------+-------------------+--------
     * 101 |   08:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | A
     * 102 |   09:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | B
     * 103 |   13:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | C
     * 104 |   19:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | D
     * 105 |   08:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | A
     * 106 |   09:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | B
     * 107 |   13:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | C
     * 108 |   19:00 | School   | 'testA'='X' | 'aux'.'testB'='Y' | E
     * 109 |   22:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | F
     * 110 |   08:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | A
     *
     * Sequence 2:
     * Uid |  Time   | Location | Situation 1 | Situation 2       | Group
     * ----+---------+----------+-------------+-------------------+--------
     * 201 |   08:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | A
     * 202 |   09:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | B
     * 203 |   13:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | C
     * 204 |   19:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | D
     * 205 |   08:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | A
     * 206 |   09:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | B
     * 207 |   13:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | C
     * 208 |   19:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' | D
     * 209 |   09:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | B
     * 210 |   13:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' | C
     *
     * Frequencies:
     *
     * Group| S1 | S2 | Selected (Pb >= Pa)
     * -----+----+----+---------------------
     *    A | 3  | 2  | No 
     *    B | 2  | 3  | Yes   -> 09:00 | Work | 'testA'='X' | 'aux'.'testB'='Y' | B
     *    C | 2  | 3  | Yes   -> 13:00 | Work | 'testA'='X' | 'aux'.'testB'='Y' | C
     *    D | 1  | 2  | Yes   -> 19:00 | Home | 'testA'='X' | 'aux'.'testB'='Y' | D
     *    E | 1  | 0  | No
     *    F | 1  | 0  | No
     *
     * Expected Sequence Similarity Value:   result.size() / uniques contexts =>  3 / 6 = 0.5
     */
    @Test
    public void testGetSimilarity() {
        showBeginTest();

        /*
         * Expected result:
         */
        List<Context> expected = new ArrayList<Context>();
        expected.add(createContext(time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        expected.add(createContext(time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        expected.add(createContext(time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));

        /*
         * Sequence 1:
         */
        ContextSequence s1 = new ContextSequence(null);
        s1.addContext(createContext(101, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(102, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(103, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(104, time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(105, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(106, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(107, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(108, time(19, 0), location("School"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(109, time(22, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(110, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));


        /*
         * Sequence 2:
         */
        ContextSequence s2 = new ContextSequence(null);
        s2.addContext(createContext(201, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(202, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(203, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(204, time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(205, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(206, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(207, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(208, time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(209, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(210, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));

        /*
         * TEST
         */

        WSContextProbability instance = new WSContextProbability();
        try {
            System.out.println("TEST 1: time, location, situation");
            //-----
            SimilarityResult sr = instance.getSimilarity(s1, s2);
            //-----

            assertNotNull("result null", sr);
            assertEquals("invalid size", expected.size(), sr.size(), 0);
            for (ContextPair cp : sr.getContextPairs()) {
                long indexA = cp.getC1().getIndex();
                TimeDescription td = cp.getC1().getTime();
                LocationDescription ld = cp.getC1().getLocation();
                System.out.println(indexA + " | " + td.getHour() + ":00 | " + ld.getName() + " (" + cp.getC1().getProbability() + " | " + cp.getC2().getProbability() + ")");

                assertEquals("not equals", cp.getC1(), cp.getC2());
                assertTrue("invalid selected context", findContext(expected, cp.getC1()));
            }
            assertEquals("invalid sequence similarity value", 0.5, sr.getCalculatedValue(), 0);


            //------------------
            System.out.println("\nTEST 2: location, situation");
            instance.setParameter(WSContextProbability.GROUP_TIME, "false");
            sr = instance.getSimilarity(s1, s2);
            assertEquals("invalid size", 1, sr.size(), 0);
            for (ContextPair cp : sr.getContextPairs()) {
                long indexA = cp.getC1().getIndex();
                TimeDescription td = cp.getC1().getTime();
                LocationDescription ld = cp.getC1().getLocation();

                System.out.println(indexA + " | " + td.getHour() + ":00 | " + ld.getName() + " (" + cp.getC1().getProbability() + " | " + cp.getC2().getProbability() + ")");
                assertEquals("invalid context", 102, cp.getC1().getIndex());

            }



        } catch (Exception ex) {
            Logger.getLogger(WSContextProbabilityTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }


        showEndTest();
    }

    private boolean findContext(List<Context> expected, Context ctx) {
        boolean result = false;
        Iterator<Context> it = expected.iterator();
        while (it.hasNext() && !result) {
            Context exp = it.next();
            result = exp.getTime().getHour() == ctx.getTime().getHour()
                    && exp.getLocation().getName().equalsIgnoreCase(ctx.getLocation().getName());

        }

        return result;
    }
}
