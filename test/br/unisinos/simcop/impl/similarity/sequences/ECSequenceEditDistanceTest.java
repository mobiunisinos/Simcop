package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.contexts.SimpleAttributes;
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
public class ECSequenceEditDistanceTest extends BaseUnitTest {

    public ECSequenceEditDistanceTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * <b>TEST DESCRIPTION:</b><br/>
     *<br/>
     * <span style='font-family:monospaced'>
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
     * 111 | 08:00:00 | Home     | 'testA'='X' | 'aux'.'testB'='Y' ||                [GAP]                                                  || 	     4 |      0 |       0,00 | <br/>
     *     |               [END OF SEQUENCE]                       || 211 | 13:00:00 | Work     | 'testA'='X' 	       | 'aux'.'testB'='Y'  || 	     4 |      0 |       0,00 | <br/>
     * ----+----------+----------+-------------+-------------------++-----+----------+----------+----------------------+--------------------++---------+--------+------------+ <br/>
     *                                                                     <br/>
     *                                                                     <br/>
     * Similar:                                                            <br/>
     *                                                                     <br/>
     * Parameter: Min Similarity Value = 0.7                               <br/>
     * ----+-----+---------+--------+------------+------------+-----+----+ <br/>
     * Uid | Uid | Uniques | Equals | Similarity | Equivalent | S1  | S2 | <br/>
     * ----+-----+---------+--------+------------+------------+-----+----+ <br/>
     * 101 | 201 |       4 |      4 |       1,00 |    YES     | '0' |'0' | <br/>
     * 102 | 202 |       4 |      3 |       0,75 |    YES     | '0' |'0' | <br/>
     * 103 | 203 |  	 4 |      4 |       1,00 |    YES     | '0' |'0' | <br/>
     * 104 | 204 |  	 3 |      3 |       1,00 |    YES     | '0' |'0' | <br/>
     * 105 | 205 |  	 5 |      3 |       0,60 |    NO      | '1' |'2' | <br/>
     * 106 | 206 |  	 4 |      4 |       1,00 |    YES     | '0' |'0' | <br/>
     * 107 | 207 |  	 4 |      4 |       1,00 |    YES     | '0' |'0' | <br/>
     * 108 | 208 |  	 4 |      3 |       0,75 |    YES     | '0' |'0' | <br/>
     * 109 | 209 | 	 4 |      2 |       0,50 |    NO      | '1' |'2' | <br/>
     * 110 | 210 |  	 4 |      2 |       0,50 |    NO      | '1' |'2' | <br/>
     * 111 |     |  	 4 |      0 |       0,00 |    NO      | '1' |'_' | <br/>
     * \0  | 211 |  	 4 |      0 |       0,00 |    NO      | EOS |'2' | <br/>
     * ----+-----+---------+--------+------------+------------+-----+----+ <br/>
     *                                                                     <br/>
     * S1 = |00001000111|                                                  <br/>
     * S2 = |0000200022_2|                                                 <br/>
     *      --------------                                                 <br/>
     * OP =  ____C___CCCI  : 4 changes, 1 insert = 5 edit operators        <br/>
     * </span>
     *
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
        s1.addContext(createContext(111, time(8, 0), location("Home"), sit("testA", "X"), sit("aux", "testB", "Y")));
        assertEquals(11, s1.size(), 0);


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
        s2.addContext(null);
        s2.addContext(createContext(211, time(13, 0), location("Work"), sit("testA", "X"), sit("aux", "testB", "Y")));
        assertEquals(12, s2.size(), 0);

        //---
        ECSequenceEditDistance instance = new ECSequenceEditDistance();
        instance.setContextSimilarity( new SimpleAttributes() );
        instance.setParameter(ECDefault.MIN_SIMILARITY, "0.7");
        //---

        try {
            SimilarityResult sr = instance.getSimilarity(s1, s2);

            String[] cs = instance.getCompareStrings();
            System.out.println("   S1: |" + cs[0] + "|");
            System.out.println("   S2: |" + cs[1] + "|");
            assertNotNull("result null", sr);
            System.out.println("   Result: " + sr.getCalculatedValue());

            assertEquals("invalid sequence similarity value", 5, sr.getCalculatedValue(), 0);
            assertEquals("invalid compare string S1", "00001000111", cs[0]);
            assertEquals("invalid compare string S2", "0000200022_2", cs[1]);


        } catch (Exception ex) {
            Logger.getLogger(ECDefaultTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        showEndTest();
    }

    
}
