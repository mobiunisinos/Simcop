package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.ContextPair;
import java.util.Date;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.contexts.SimpleAttributes;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.unisinos.simcop.impl.similarity.thirdParty.adapters.FastDTWAdapter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class WSDynamicTimeWarpTest extends BaseUnitTest {

    public WSDynamicTimeWarpTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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
        WSDynamicTimeWarp instance = new WSDynamicTimeWarp();
        instance.setContextSimilarity(new SimpleAttributes());
        instance.setParameter(FastDTWAdapter.PAR_SEARCH_RADIUS, "1");
        //---

        try {
            Date d1 = new Date();
            SimilarityResult sr = instance.getSimilarity(s1, s2);
            Date d2 = new Date();

            System.out.println("   TIME: " + (d2.getTime() - d1.getTime()) + "ms");
            assertNotNull("result null", sr);
            for (int i = 0; i < sr.size(); i++) {
                System.out.println("---[" + i + "]---------------------------------------------------------------");
                ContextPair cp = sr.getContextPairs().get(i);
                if (cp != null) {
                    System.out.println(cp.toString());
                } else {
                    System.out.println("null");
                }
                System.out.println(" ");
            }
            System.out.println("=========================");
            System.out.println("DTW Result: " + sr.getCalculatedValue());

        } catch (Exception ex) {
            Logger.getLogger(ECDefaultTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        showEndTest();

    }
}
