package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.data.model.LocationDescription;
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
public class LocationNameCategoryTest extends BaseUnitTest {
    public static final double CAT_WEIGHT = 100;
    public static final double NAME_WEIGHT = 50;

    public LocationNameCategoryTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testIsDistanceFunction() {
        showBeginTest();
        assertFalse( new LocationNameCategory().isDistanceFunction() );
        showEndTest();
    }

    @Test
    public void testGetSimilarity_CaseSensitive() throws Exception {
        showBeginTest();
        test(true, "A", "B", "X", "Y", 0);
        test(true, "A", "B", "X", "B", 0);
        test(true, "A", "B", "A", "X", 0.66667);
        test(true, "A", "B", "A", "B", 1);
        test(true, "A", "B", "a", "x", 0);
        test(true, "A", "B", "a", "b", 0);

        showEndTest();
    }

    @Test
    public void testGetSimilarity_CaseInsensitive() throws Exception {
        showBeginTest();
        test(false, "A", "B", "X", "Y", 0);
        test(false, "A", "B", "X", "B", 0);
        test(false, "A", "B", "A", "X", 0.66667);
        test(false, "A", "B", "A", "B", 1);
        test(false, "A", "B", "a", "x", 0.66667);
        test(false, "A", "B", "a", "b", 1);
        showEndTest();
    }


    private void test(boolean caseSensitive, String catA, String nameA, String catB, String nameB, double expected) {
        System.out.print("   {" + caseSensitive + "} " + catA + "."+ nameA +" = "+ catB + "."+nameB + ": ");
        LocationDescription testA = location(nameA, catA);
        LocationDescription testB = location(nameB, catB);
        LocationNameCategory instance = new LocationNameCategory();
        instance.setParameter(LocationNameCategory.PAR_CATEGORY_WEIGHT, Double.toString(CAT_WEIGHT));
        instance.setParameter(LocationNameCategory.PAR_NAME_WEIGHT, Double.toString(NAME_WEIGHT));
        instance.setParameter(LocationNameCategory.PAR_CASE_SENSITIVE, caseSensitive ? "true" : "false");

        try {
            double result = instance.getSimilarity(testA, testB);
            assertEquals(expected, result, 0.00001);
        } catch (Exception ex) {
            Logger.getLogger(LocationNameCategoryTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        System.out.println("[OK]");

    }
}
