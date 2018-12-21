package br.unisinos.simcop.impl.similarity.attributes;

import java.text.DecimalFormat;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.core.dataType.SimcopValue;
import br.unisinos.simcop.data.model.Situation;
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
public class SituationNumericDifferenceTest extends BaseUnitTest {

    private static final double MISSING_VALUE = 0.5;

    public SituationNumericDifferenceTest() {
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
        TimeDifference instance = new TimeDifference();
        boolean expResult = true;
        boolean result = instance.isDistanceFunction();
        assertEquals("Numeric Difference is a Distance Function!", expResult, result);
        showEndTest();
    }

    @Test
    public void testGetSimilarity() {
        showBeginTest();

        //integers
        test(0, false, 1, 2, -1);
        test(1, true, 1, 2, 1);
        test(2, false, 1, 1, 0);
        test(3, false, 10, 1, 9);

        //longs
        test(4, false, 1L, 2L, -1L);
        test(5, true, 1L, 2L, 1L);
        test(6, false, 1, 1, 0);
        test(7, false, 10L, 1, 9); //compatible types: long and integer

        //doubles
        test(8, false, 0.1, 0.3333, -0.2333);
        test(9, false, 0.1, 0.2, -0.1);
        test(10, true, 0.1, 0.2, 0.1);
        test(11, false, 0.465, 0.465, 0);

        //times
        test(12, false, time(9, 59), time(10, 0), -60000);
        test(13, true, time(9, 59), time(10, 0), 60000);

        //arrays of integers
        test(14, false, new int[]{25, 50}, new int[]{50, 25, 25}, -25);
        test(15, true, new int[]{25, 50}, new int[]{50, 25, 25}, 25);
        test(16, false, new int[]{25, 25, 50}, new int[]{50, 25, 25}, 0);
        test(18, true, new int[]{60, 60, 80}, new int[]{10, 10, 10}, 170);

        //arrays of doubles
        test(19, false, new double[]{0.25, 0.50}, new double[]{0.50, 0.25, 0.25}, -0.25);
        test(20, true, new double[]{0.25, 0.50}, new double[]{0.50, 0.25, 0.25}, 0.25);
        test(21, false, new double[]{0.25, 0.25, 0.50}, new double[]{0.50, 0.25, 0.25}, 0);
        test(22, true, new double[]{0.60, 0.60, 0.80}, new double[]{0.10, 0.10, 0.10}, 1.70);
        test(23, true, new double[]{60, 60, 80}, new int[]{10, 10, 10}, 170); //compatible
        test(24, true, new double[]{60, 60, 80}, new int[]{60, 60, 80}, 0); //compatible

        //categories
        SimcopValue s1 = new SimcopValue();
        SimcopValue s2 = new SimcopValue();
        s1.setCategoryIndex(10);
        s2.setCategoryIndex(13);
        SituationNumericDifference inst = new SituationNumericDifference();
        try {
            double r = inst.getSimilarity(createSituation(s1), createSituation(s2));
            assertEquals(-3, r, 0);
        } catch (Exception ex) {
            Logger.getLogger(SituationNumericDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        //null
        test(28, true, null, null, 0);
        test(29, true, null, 10, 10);
        test(30, true, 10, null, 10);


        //invalid types (must throws a exception)
        try {
            SituationNumericDifference instance = new SituationNumericDifference();
            instance.getSimilarity(createSituation("String is not numeric!"), createSituation(10));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            showEndTest();
            return;
        }
        fail(" this values are not valid and must throws a exception");

    }

    private Situation createSituation(Object value) {
        Situation sit = new Situation();
        sit.setSource("sensor1");
        sit.setAuxiliary("test");
        sit.setPredicate("aValue");
        if (value instanceof SimcopValue) {
            sit.setValue((SimcopValue) value);
        } else {
            sit.setValue(SimcopValue.createFromObject(value));
        }
        return sit;
    }

    private void test(int idx, boolean absolute, Object v1, Object v2, double expected) {
        System.out.print(idx + ". " + (absolute ? "Absolute" : "Real") + " difference between " + v1 + " and " + v2 + ": Expected: " + expected + " Calculated: ");

        //----
        SituationNumericDifference instance = new SituationNumericDifference();
        instance.setParameter(SituationNumericDifference.PAR_ABSOLUTE_DIFERENCE, absolute ? "true" : "false");
        instance.setParameter(SituationNumericDifference.PAR_MISSING_VALUE, Double.toString(MISSING_VALUE));
        double result;
        try {
            result = instance.getSimilarity(createSituation(v1), createSituation(v2));
        } catch (Exception ex) {
            Logger.getLogger(SituationNumericDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }
        //---

        System.out.print(result);
        assertEquals(expected, result, 0.0001);
        System.out.println(" [OK]");
    }
}
