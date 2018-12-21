package br.unisinos.simcop.impl.transformations;

import java.text.DecimalFormat;
import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SortResultTest extends BaseUnitTest {

    public SortResultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Context createContext(int sequence) {
        Context ctx = new Context();
        ctx.setIndex(sequence);
        return ctx;
    }

    private ContextPair add(SimilarityResult sr, int pairIndex, int c1Index, int c2Index, double value) {
        ContextPair cp = new ContextPair(pairIndex, createContext(c1Index), createContext(c2Index), value);
        sr.getContextPairs().add(cp);
        return cp;
    }

    private SimilarityResult createTestData(SimilarityResult expected, int sortBy) {
        SimilarityResult sr = new SimilarityResult(expected.getS1(), expected.getS2());
        ContextPair cp1 = add(sr, 1, 10, 50, 0);
        ContextPair cp2 = add(sr, 2, 20, 40, 0.2);
        ContextPair cp3 = add(sr, 3, 30, 30, 1);
        ContextPair cp4 = add(sr, 4, 40, 20, 0.5);
        ContextPair cp5 = add(sr, 5, 50, 10, 0.01);

        switch (sortBy) {
            case 0://value
                expected.getContextPairs().add( cp1 );
                expected.getContextPairs().add( cp5 );
                expected.getContextPairs().add( cp2 );
                expected.getContextPairs().add( cp4 );
                expected.getContextPairs().add( cp3 );
                break;

            case 1://c1
                expected.getContextPairs().add( cp1 );
                expected.getContextPairs().add( cp2 );
                expected.getContextPairs().add( cp3 );
                expected.getContextPairs().add( cp4 );
                expected.getContextPairs().add( cp5 );
                break;

            case 2://c2
                expected.getContextPairs().add( cp5 );
                expected.getContextPairs().add( cp4 );
                expected.getContextPairs().add( cp3 );
                expected.getContextPairs().add( cp2 );
                expected.getContextPairs().add( cp1 );
                break;
        }
        return sr;
    }

    private void test(String sortBy, int type) {
        SortResult instance = new SortResult();
        instance.setParameter("sortBy", sortBy);
        SimilarityResult expResult = new SimilarityResult(null, null);
        //--
        SimilarityResult testData = createTestData(expResult, type);
        SimilarityResult result = instance.transform( testData );
        //--
        assertEquals(expResult.getContextPairs().size(), result.getContextPairs().size(), 0);
        DecimalFormat df = new DecimalFormat("000");
            System.out.println("IDX | EXP | RST");
            System.out.println("----+-----+----");

        for (int i = 0; i < expResult.getContextPairs().size(); i++) {
            ContextPair expPair = expResult.getContextPairs().get(i);
            ContextPair resultPair = result.getContextPairs().get(i);
            System.out.println(df.format(i) + " | " + df.format(expPair.getIndex()) + " | " + df.format(resultPair.getIndex()));

            assertEquals("Invalid Sort at position [" + i + "]", expPair.getIndex(), resultPair.getIndex(), 0);
        }
    }

    @Test
    public void testTransform_value() {
        showBeginTest();
        test(SortResult.SORT_CALCULATED_VALUE, 0);
        showEndTest();
    }

    @Test
    public void testTransform_c1() {
        showBeginTest();
        test(SortResult.SORT_C1_INDEX, 1);
        showEndTest();
    }

    @Test
    public void testTransform_c2() {
        showBeginTest();
        test(SortResult.SORT_C2_INDEX, 2);
        showEndTest();
    }


}
