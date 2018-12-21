package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.ContextPair;
import java.util.Iterator;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.contexts.SimpleAttributes;
import br.unisinos.simcop.tests.BaseUnitTest;
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
public class ECDefaultTest extends BaseUnitTest {

    public ECDefaultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * TEST DESCRIPTION:
     *
     * <tt>
     * SEQUENCE 1                                                  || SEQUENCE 2                                                            || Result                        | <br/>
     * ------------------------------------------------------------++-----------------------------------------------------------------------++---------+--------+------------+ <br/>
     * Uid | Time     | Location | Situation 1 | Situation 2       || Uid | Time     | Location | Situation 1 	       | Situation 2        || Uniques | Equals | Similarity | <br/>
     * ----+----------+----------+-------------+-------------------++-----+----------+----------+----------------------+--------------------++---------+--------+------------+ <br/>
     * 101 | 08:00:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' || 201 | 08:00:00 | Home     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      4 |       1,00 | <br/>
     * 102 | 09:00:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' || 202 | 09:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='123'|| 	     4 |      3 |       0,75 | <br/>
     * 103 | 13:00:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' || 203 | 13:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      4 |       1,00 | <br/>
     * 104 | 19:00:00 | Home     | 'testA'='X' |                   || 204 | 19:00:00 | Home     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     3 |      3 |       1,00 | <br/>
     * 105 | 08:00:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' || 205 | 08:00:00 | Home     | 'otherPredicate'='X' | 'aux'.'testB'='Y'  || 	     5 |      3 |       0,60 | <br/>
     * 106 | 09:00:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' || 206 | 09:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      4 |       1,00 | <br/>
     * 107 | 13:00:00 | Work     | 'testA'='X' | 'aux'.'testB'='Y' || 207 | 13:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      4 |       1,00 | <br/>
     * 108 | 19:00:00 | School   | 'testA'='X' | 'aux'.'testB'='Y' || 208 | 19:00:00 | Home     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      3 |       0,75 | <br/>
     * 109 | 22:00:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' || 209 | 09:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      2 |       0,50 | <br/>
     * 110 | 08:00:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' || 210 | 13:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      2 |       0,50 | <br/>
     * ----+----------+----------+-------------+-------------------++-----+----------+----------+----------------------+--------------------++---------+--------+------------+ <br/>
     *                                                                                                Global Value for minSimilarity = 0.51 ||      10 |      8 |        0,8 | <br/>
     * ----+----------+----------+-------------+-------------------++-----+----------+----------+----------------------+--------------------++---------+--------+------------+ <br/>
     * </tt>
     *
     *
     * Similar:
     *
     * Parameter: Min Similarity Value = 0.51
     * ----+-----+---------+--------+------------+ <br/>
     * Uid | Uid | Uniques | Equals | Similarity | <br/>
     * ----+-----+---------+--------+------------+ <br/>
     * 101 | 201 |       4 |      4 |       1,00 | <br/>
     * 102 | 202 |       4 |      3 |       0,75 | <br/>
     * 103 | 203 |       4 |      4 |       1,00 | <br/>
     * 104 | 204 |       3 |      3 |       1,00 | <br/>
     * 105 | 205 |       5 |      3 |       0,60 | <br/>
     * 106 | 206 |       4 |      4 |       1,00 | <br/>
     * 107 | 207 |       4 |      4 |       1,00 | <br/>
     * 108 | 208 |       4 |      3 |       0,75 | <br/>
     * ----+-----+---------+--------+------------+ <br/>
     *                        total |       7,85 | <br/>
     * ----+-----+---------+--------+------------+ <br/>
     *
     * Expected Sequence Similarity Value:   sum(similarity) / count(contexts) =>  7.85 / 10 = 0.785
     *     obs: max similarity between two contexts = 1.00
     */
    @Test
    public void testGetSimilarity() {
        showBeginTest();
        /*
         * Sequence 1:
         */
        ContextSequence s1 = new ContextSequence(null);
        s1.addContext(createContext(101, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(102, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(103, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s1.addContext(createContext(104, time(19, 0), location("Home"), sit("testA", "X")));
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
        s2.addContext(createContext(202, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "123")));
        s2.addContext(createContext(203, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(204, time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(205, time(8, 0), location("Home"), sit("otherPredicate", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(206, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(207, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(208, time(19, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(209, time(9, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        s2.addContext(createContext(210, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));

        //---
        ECDefault instance = new ECDefault();
        instance.setContextSimilarity( new SimpleAttributes() );
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.51");
        //---

        try {
            SimilarityResult sr = instance.getSimilarity(s1, s2);
            assertNotNull("result null", sr);
            assertEquals("invalid result size", 8, sr.size());

            test(sr, 0, 101, 201, 1);
            test(sr, 1, 102, 202, 0.75);
            test(sr, 2, 103, 203, 1);
            test(sr, 3, 104, 204, 0.75);
            test(sr, 4, 105, 205, 0.6);
            test(sr, 5, 106, 206, 1);
            test(sr, 6, 107, 207, 1);
            test(sr, 7, 108, 208, 0.75);

            assertEquals("invalid sequence similarity value", 0.785, sr.getCalculatedValue(), 0.001);

        } catch (Exception ex) {
            Logger.getLogger(ECDefaultTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        showEndTest();
    }

    /**
     * TEST DESCRIPTION:
     *
     * <tt>
     * SEQUENCE 1     || SEQUENCE 2     |      | Accept                 | <br/>
     * ---------------++----------------+      +------+--------+--------+ <br/>
     * Uid | Time     || Uid | Time     | Gap  | ALL  | GAPS_A | GAPS_B | <br/>
     * ----+----------++-----+----------+------+------+--------+--------+ <br/>
     * 101 | 08:00:00 || 201 | 08:00:00 | none | true | TRUE   | TRUE   | <br/>
     *     |          || 202 | 09:00:00 | C1   | true | false  | TRUE   | <br/>
     * 103 | 13:00:00 ||     |          | C2   | true | TRUE   | false  | <br/>
     *     |          ||     |          | both | true | false  | false  | <br/>
     * ----+----------++-----+----------+------+------+--------+--------+ <br/>
     *                     Sequence Similarity | 0.50 | 0.50   | 0.50   | <br/>
     * ----+----------++-----+----------+------+------+--------+--------+ <br/>
     * </tt>
     */
    @Test
    public void testGetSimilarity_withGaps() {
        showBeginTest();
        /*
         * Sequence 1:
         */
        ContextSequence s1 = new ContextSequence(null);
        s1.addContext(createContext(101, time(8, 0), null, null, null));
        s1.addContext(null);
        s1.addContext(createContext(103, time(13, 0), null, null, null));
        s1.addContext(null);


        /*
         * Sequence 2:
         */
        ContextSequence s2 = new ContextSequence(null);
        s2.addContext(createContext(201, time(8, 0), null, null, null));
        s2.addContext(createContext(202, time(9, 0), null, null, null));
        s2.addContext(null);
        s2.addContext(null);


        ECDefault instance;
        /*
         * ACCEPT ALL
         */
        System.out.println("\n 1. Accept all contexts");
        instance = new ECDefault();
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.00");
        instance.setContextSimilarity( new SimpleAttributes() );
        instance.setParameter(ECDefault.ACCEPT_ALL, "true");
        testGap(instance, s1, s2, true, false, false);

        /*
         * ACCEPT GAPS IN C1
         */
        System.out.println("\n 2. Accept Gaps in Sequence A");
        instance = new ECDefault();
        instance.setContextSimilarity( new SimpleAttributes() );
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.00");
        instance.setParameter(ECDefault.ACCEPT_GAPS_SEQUENCE_A, "true");
        testGap(instance, s1, s2, false, true, false);

        /*
         * ACCEPT GAPS IN C2
         */
        System.out.println("\n 3. Accept Gaps in Sequence B");
        instance = new ECDefault();
        instance.setContextSimilarity( new SimpleAttributes() );
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.00");
        instance.setParameter(ECDefault.ACCEPT_GAPS_SEQUENCE_B, "true");
        testGap(instance, s1, s2, false, false, true);

        /*
         * ACCEPT NO GAPS
         */
        System.out.println("\n 4. Accept no gaps");
        instance = new ECDefault();
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.00");
        instance.setContextSimilarity( new SimpleAttributes() );
        testGap(instance, s1, s2, false, false, false);

        showEndTest();
    }

    /*
     * AUXILIARY
     */
    private void test(SimilarityResult sr, int index, int indexA, int indexB, double expected) {
        test(sr, index,  indexA, indexB, expected, false, false);
    }

    private void test(SimilarityResult sr, int index, int indexA, int indexB, double expected, boolean expGapInA, boolean expGapInB) {
        System.out.print("   ContextPair[" + index + "] " + (expGapInA ? "null" : indexA) + "/" + (expGapInB ? "null" : indexB )+ ": ");
        assertTrue("invalid index: " + index, sr.getContextPairs().size() > index);

        ContextPair cp = sr.getContextPairs().get(index);
        assertNotNull("no context pair", cp);
        if (expGapInA) {
            assertNull("no gap in context 1 slot", cp.getC1());
        } else {
            assertNotNull("no context 1", cp.getC1());
            assertEquals("invalid C1 Index", indexA, cp.getC1().getIndex(), 0);
        }
        if (expGapInB) {
            assertNull("no gap in context 2 slot", cp.getC2());
        } else {
            assertNotNull("no context 2", cp.getC2());
            assertEquals("invalid C2 Index", indexB, cp.getC2().getIndex(), 0);
        }
        assertEquals("invalid similarity value", expected, cp.getCalculatedValue(), 0.001);

        System.out.println(cp.getCalculatedValue() + " (ok)");
    }

    private void testGap(ECDefault instance, ContextSequence s1, ContextSequence s2, boolean all, boolean gapsA, boolean gapsB) {
        try {
            SimilarityResult sr = instance.getSimilarity(s1, s2);
            assertNotNull("result null", sr);
            if (all) {
                assertEquals("invalid result size", 4, sr.size());
                test(sr, 0, 101, 201, 1,false, false);
                test(sr, 1, 102, 202, 0,true, false);
                test(sr, 2, 103, 203, 0,false, true);
                test(sr, 3, 104, 204, 1,true, true);
                assertEquals("invalid sequence similarity value", 0.5, sr.getCalculatedValue(), 0.0);
            } else if (gapsA) {
                assertEquals("invalid result size", 2, sr.size());
                test(sr, 0, 101, 201, 1,false, false);
                test(sr, 1, 102, 202, 0,true, false);
                assertEquals("invalid sequence similarity value", 0.5, sr.getCalculatedValue(), 0.001);
            } else if (gapsB) {
                assertEquals("invalid result size", 2, sr.size());
                test(sr, 0, 101, 201, 1,false, false);
                test(sr, 1, 103, 203, 0,false, true);
                assertEquals("invalid sequence similarity value", 0.5, sr.getCalculatedValue(), 0.001);
            } else {
                assertEquals("invalid result size", 1, sr.size());
                test(sr, 0, 101, 201, 1,false, false);
                assertEquals("invalid sequence similarity value", 0.5, sr.getCalculatedValue(), 0.001);
            }

        } catch (Exception ex) {
            Logger.getLogger(ECDefaultTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

    }
}
