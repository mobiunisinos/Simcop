package br.unisinos.simcop.impl.similarity.contexts;

import java.text.DecimalFormat;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.tests.BaseUnitTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class WeightAttributesTest extends BaseUnitTest {

    private static final double DEFAULT_WEIGHT = 0.03;
    private static final double TIME_WEIGHT = 0.4;
    private static final double LOCATION_WEIGHT = 0.5;
    private static final double TESTA_WEIGHT = 0.6;
    private static final double AUX_TESTB_WEIGHT = 0.7;
    private static final double SOURCE_AUX_TESTC_WEIGHT = 0.8;
    private static final String PAR_TEST_A = buildSituationParameter(null, null, "testA");
    private static final String PAR_TEST_B = buildSituationParameter(null, "aux", "testB");
    private static final String PAR_TEST_C = buildSituationParameter("sensor_1", "aux", "testC");

    private static String buildSituationParameter(String source, String aux, String predicate) {
        StringBuilder sb = new StringBuilder();
        sb.append(WeightAttributes.SITUATION_WEIGHT_PREFIX);
        sb.append("?source=").append(source != null ? source : "{null}");
        sb.append("&aux=").append(aux != null ? aux : "{null}");
        sb.append("&predicate=").append(predicate != null ? predicate : "{null}");
        return sb.toString();
    }

    public WeightAttributesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("/************************************************************************************");
        System.out.println(" * Class...: br.unisinos.simcop.impl.similarity.contexts.WeightAttributes");
        System.out.println(" *************************************************************************************/");
        System.out.println("WEIGHTS USED IN THIS TEST: ");
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        System.out.println("\t" + df.format(DEFAULT_WEIGHT) + "..: " + WeightAttributes.DEFAULT_WEIGHT);
        System.out.println("\t" + df.format(TIME_WEIGHT) + "..: " + WeightAttributes.TIME_WEIGHT);
        System.out.println("\t" + df.format(LOCATION_WEIGHT) + "..: " + WeightAttributes.LOCATION_WEIGHT);
        System.out.println("\t" + df.format(TESTA_WEIGHT) + "..: " + PAR_TEST_A);
        System.out.println("\t" + df.format(AUX_TESTB_WEIGHT) + "..: " + PAR_TEST_B);
        System.out.println("\t" + df.format(SOURCE_AUX_TESTC_WEIGHT) + "..: " + PAR_TEST_C);
        System.out.println("---------------------------------------------------------------------------\n");



    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testIsDistanceFunction() {
        showBeginTest();
        WeightAttributes instance = new WeightAttributes();
        boolean expResult = false;
        boolean result = instance.isDistanceFunction();
        assertEquals(expResult, result);
        showEndTest();
    }

    @Test
    public void testValidateParameters_valid() {
        showBeginTest();

        WeightAttributes instance = new WeightAttributes();
        instance.setParameter(WeightAttributes.DEFAULT_WEIGHT, "20");
        instance.setParameter(WeightAttributes.EXTENDED_WEIGHT, "1.2");
        instance.setParameter(WeightAttributes.LOCATION_WEIGHT, "3.4");
        instance.setParameter(WeightAttributes.TIME_WEIGHT, "5.6");
        instance.setParameter(buildSituationParameter("source", "aux", "predicate"), "7.8");
        instance.setParameter(buildSituationParameter(null, "aux", "predicate"), "9.10");
        instance.setParameter(buildSituationParameter(null, null, "predicate"), "10.11");
        try {
            instance.validateParameters();
        } catch (Exception ex) {
            failException(ex);
        }
        showEndTest();
    }

    @Test
    public void testValidateParameters_invalid() {
        showBeginTest();

        WeightAttributes instance = new WeightAttributes();
        instance.setParameter(WeightAttributes.DEFAULT_WEIGHT, "20");
        instance.setParameter(WeightAttributes.EXTENDED_WEIGHT, "1.2");
        instance.setParameter(WeightAttributes.LOCATION_WEIGHT, "3.4");
        instance.setParameter(WeightAttributes.TIME_WEIGHT, "5.6");
        instance.setParameter(buildSituationParameter("sourceTest", "aux", "predicateTest"), "7.8");
        instance.setParameter(buildSituationParameter(null, "auxTest", "predicateTest"), ""); //exception!
        instance.setParameter(buildSituationParameter(null, null, "predicateTest"), "10.11");
        try {
            instance.validateParameters();
        } catch (Exception ex) {
            show("OK: " + ex.getMessage());
            return;
        }
        fail("must throws a exception");
    }

    /**
    Dimension      | Weight | Context A | Context B | Similar | SimilarWeight</br>
    ---------------+--------+-----------+-----------+---------+--------------
    Time           |   0,50 | 08:00:00  | 08:00:00  |       1 |         0,50
    Location       |   1,00 |   Home    |   Home    |       1 |         1,00
    testA          |   1,00 |    X      |           |       0 |         0,00
    aux.testB      |   1,00 |    Y      |     Y     |       1 |         1,00
    otherPredicate |   1,00 |           |     X     |       0 |         0,00
    ---------------+--------+-----------+-----------+---------+--------------
    Max Similarity |   4,50 |               Weight Similarity |         2,50
    ---------------+--------+-----------+-----------+---------+--------------
    Context Similarity:  (2,50 / 4,50) = 55,56%
     */
    private void test(String label, Context c1, Context c2, double expTotalWeight, double expSimilarWeight) {
        System.out.print("   " + label + ": ");
        WeightAttributes instance = new WeightAttributes();
        instance.setParameter(WeightAttributes.DEFAULT_WEIGHT, Double.toString(DEFAULT_WEIGHT));
        instance.setParameter(WeightAttributes.TIME_WEIGHT, Double.toString(TIME_WEIGHT));
        instance.setParameter(WeightAttributes.LOCATION_WEIGHT, Double.toString(LOCATION_WEIGHT));
        instance.setParameter(PAR_TEST_A, Double.toString(TESTA_WEIGHT));
        instance.setParameter(PAR_TEST_B, Double.toString(AUX_TESTB_WEIGHT));
        instance.setParameter(PAR_TEST_C, Double.toString(SOURCE_AUX_TESTC_WEIGHT));
        //----
        double result = -1;
        try {
            instance.validateParameters();
            result = instance.getSimilarity(c1, c2);
        } catch (Exception ex) {
            failException(ex);
        }
        //----
        assertEquals("invalid totalWeight", expTotalWeight, instance.getTotalWeight(), 0.001);
        assertEquals("invalid similarWeight", expSimilarWeight, instance.getSimilarWeight(), 0.001);

        double expectedResult = expSimilarWeight / expTotalWeight;
        assertEquals("invalid result", expectedResult, result, 0.001);
        System.out.println(instance.getSimilarWeight() + " / " + instance.getTotalWeight() + " = " + result + " [ok]");
    }

    @Test
    public void testGetSimilarity_Time() {
        showBeginTest();
        Context c1, c2;
        //1
        c1 = createContext(time(9, 10), null, null, null);
        c2 = createContext(time(9, 10), null, null, null);
        test("same Time", c1, c2, TIME_WEIGHT, TIME_WEIGHT);
        //2
        c1 = createContext(time(9, 10), null, null, null);
        c2 = createContext(time(9, 11), null, null, null);
        test("other time", c1, c2, TIME_WEIGHT, 0);
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
        test("same location", c1, c2, LOCATION_WEIGHT, LOCATION_WEIGHT);
        //2
        c1 = createContext(null, location("work"), null, null);
        c2 = createContext(null, location("home"), null, null);
        test("other location", c1, c2, LOCATION_WEIGHT, 0);
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
        test("same situation", c1, c2, TESTA_WEIGHT, TESTA_WEIGHT);
        //2
        c1 = createContext(null, null, sit("testA", "123"));
        c2 = createContext(null, null, sit("testA", "456"));
        test("other situation", c1, c2, TESTA_WEIGHT, 0.0);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Situation_2() {
        showBeginTest();
        Context c1, c2;
        double expTotal, expSimilar;
        //1
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "xyz"));
        c2 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "xyz"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT;
        expSimilar = expTotal;
        test("same situations", c1, c2, expTotal, expSimilar);
        //2
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(9, 11), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("other time same situation", c1, c2, expTotal, expSimilar);
        //2
        c1 = createContext(time(9, 10), null, sit("testA", "123"), sit("aux", "testB", "456"));
        c2 = createContext(time(9, 11), null, sit("testA", "xyz"), sit("aux", "testB", "abc"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT;
        expSimilar = 0;
        test("other time other situation", c1, c2, expTotal, expSimilar);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Time_Location() {
        showBeginTest();
        Context c1, c2;
        double expTotal, expSimilar;
        //1
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(9, 10), location("work"), null, null);
        expTotal = TIME_WEIGHT + LOCATION_WEIGHT;
        expSimilar = expTotal;
        test("same time same location", c1, c2, expTotal, expSimilar);
        //2
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(9, 10), location("home"), null, null);
        expTotal = TIME_WEIGHT + LOCATION_WEIGHT;
        expSimilar = TIME_WEIGHT;
        test("same time other location", c1, c2, expTotal, expSimilar);
        //3
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(8, 10), location("work"), null, null);
        expTotal = TIME_WEIGHT + LOCATION_WEIGHT;
        expSimilar = LOCATION_WEIGHT;
        test("other time same location ", c1, c2, expTotal, expSimilar);
        //4
        c1 = createContext(time(9, 10), location("work"), null, null);
        c2 = createContext(time(7, 37), location("home"), null, null);
        expTotal = TIME_WEIGHT + LOCATION_WEIGHT;
        expSimilar = 0;
        test("other time other location", c1, c2, expTotal, expSimilar);
        //--
        showEndTest();
    }

    @Test
    public void testGetSimilarity_Time_Situation() {
        showBeginTest();
        Context c1, c2;
        double expTotal, expSimilar;
        //CONTEXTS EQUALS
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = expTotal;
        test("same time same situation", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //SAME TIMES
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(9, 10), null, sit("testA", "000"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = TIME_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("same time other situation [1]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(9, 10), null, sit("testA", "000"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = TIME_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("same time other situation [2]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(9, 10), null, sit("other", "xyz"), sit("aux", "other", "abc"), sit("sensor_2", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + (DEFAULT_WEIGHT * 3) + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = TIME_WEIGHT;
        test("same time other situation [3]", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //SIMILAR SITUATIONS
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(8, 00), null, sit("testA", "000"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("other time same situation [1]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(8, 00), null, sit("testA", "000"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = TIME_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = SOURCE_AUX_TESTC_WEIGHT;
        test("other time same situation [2]", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //CONTEXTS WITHOUT ANY SIMILARITY
        c1 = createContext(time(9, 10), null, sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(time(7, 37), null, sit("testA", "abc"), sit("zzz", "testB", "abc"), sit("aux", "testC", "123"));
        expTotal = TIME_WEIGHT + (DEFAULT_WEIGHT * 2) + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = 0;
        test("other time other situation", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        showEndTest();
    }

    @Test
    public void testGetSimilarity_Location_Situation() {
        showBeginTest();
        Context c1, c2;
        double expTotal, expSimilar;

        //CONTEXTS EQUALS
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = expTotal;
        test("same location same situation", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //SAME LOCATIOINS
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("home"), sit("testA", "000"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = LOCATION_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("same location other situation [1]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("home"), sit("testA", "000"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = LOCATION_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("same location other situation [2]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("home"), sit("other", "xyz"), sit("aux", "other", "abc"), sit("sensor_2", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + (DEFAULT_WEIGHT * 3) + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = LOCATION_WEIGHT;
        test("same location other situation [3]", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //SIMILAR SITUATIONS
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("work"), sit("testA", "abc"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        test("other location same situation [1]", c1, c2, expTotal, expSimilar);
        //
        c1 = createContext(null, location("home"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, null, sit("testA", "000"), null, sit("sensor_1", "aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = SOURCE_AUX_TESTC_WEIGHT;
        test("other location (null) same situation [2]", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        //CONTEXTS WITHOUT ANY SIMILARITY
        c1 = createContext(null, location("work"), sit("testA", "xyz"), sit("aux", "testB", "abc"), sit("sensor_1", "aux", "testC", "123"));
        c2 = createContext(null, location("home"), sit("testA", "abc"), sit("zzz", "testB", "abc"), sit("aux", "testC", "123"));
        expTotal = LOCATION_WEIGHT + (DEFAULT_WEIGHT * 2) + TESTA_WEIGHT + AUX_TESTB_WEIGHT + SOURCE_AUX_TESTC_WEIGHT;
        expSimilar = 0;
        test("other location other situation", c1, c2, expTotal, expSimilar);
        //-----------------------------------------------------------------------------------------------------------------------------

        showEndTest();
    }
}
