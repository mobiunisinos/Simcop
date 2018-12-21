package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.data.model.TimeDescription;
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
public class TimeDifferenceTest extends BaseUnitTest {

    public TimeDifferenceTest() {
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
        assertEquals("Time Difference is a Distance Function!", expResult, result);
        showEndTest();
    }

    @Test
    public void testGetSimilarity() {
        showBeginTest();
        test(false); //difference can be < 0
        test(true); // difference allways >= 0
        showEndTest();
    }

    private void test(boolean absoluteDifference) {
        if (absoluteDifference) {
            System.out.println("ABSOLUTE DIFFERENCE: allways positive");
        } else {
            System.out.println("NOT ABSOLUTE DIFFERENCE: can be negative");
        }
        try {
            testAgeScale(absoluteDifference);
            testSeasonScale(absoluteDifference);
            testTimeScales(absoluteDifference);
        } catch (Exception ex) {
            Logger.getLogger(TimeDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Exception{" + ex.getClass().getSimpleName() + "}:" + ex.getMessage());
        }
    }

    private TimeDifference createInstance(boolean absolute, String scale) {
        System.out.println(" SCALE: " + scale);
        TimeDifference instance = new TimeDifference();
        instance.getParameter(TimeDifference.PAR_MISSING_VALUE).setValue("-9999999.99");
        instance.getParameter(TimeDifference.PAR_ABSOLUTE_DIFERENCE).setValue(absolute ? "true" : "false");
        instance.getParameter(TimeDifference.PAR_SCALE).setValue(scale);
        return instance;
    }

    private void testAgeScale(boolean absolute) throws Exception {
        TimeDifference instance = createInstance(absolute, TimeDifference.SCALE_AGES);

        System.out.print("    Same Age: ");
        double result1 = instance.getSimilarity(createTimeAge(20), createTimeAge(20));
        assertEquals(0, result1, 0);
        System.out.print("[OK]  ");

        System.out.print("| Other Age: ");
        double result2 = instance.getSimilarity(createTimeAge(20), createTimeAge(27));
        if (absolute) {
            assertEquals(7, result2, 0);
        } else {
            assertEquals(-7, result2, 0);
        }
        System.out.println("[OK]");
    }

    private void testSeasonScale(boolean absolute) throws Exception {
        TimeDifference instance = createInstance(absolute, TimeDifference.SCALE_SEASONS);

        System.out.print("    Same Season: ");
        double result1 = instance.getSimilarity(createTimeSeason(7), createTimeSeason(7));
        assertEquals(0, result1, 0);
        System.out.print("[OK]  ");

        System.out.print("| Other Season: ");
        double result2 = instance.getSimilarity(createTimeSeason(3), createTimeSeason(7));
        if (absolute) {
            assertEquals(4, result2, 0);
        } else {
            assertEquals(-4, result2, 0);
        }
        System.out.println("[OK]");
    }

    private void testTimeScales(boolean absolute) throws Exception {
        TimeDescription tdA;
        TimeDescription tdB;
        //
        System.out.println("\n DATE/TIME:");
        System.out.println(" ----------");

        //--------------------------------------------------------------------------
        tdA = createTime(2013, 10, 8, 23, 45, 17, 897);
        tdB = createTime(2013, 10, 8, 23, 45, 17, 897);
        System.out.println(" * SAME TIMES: " + tdA.toString());
        test(absolute, tdA, tdB, TimeDifference.SCALE_MILLISECONDS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_SECONDS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_MINUTES, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_HOURS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_DAYS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_WEEKS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_MONTHS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_YEARS, 0);
        //--------------------------------------------------------------------------

        //--------------------------------------------------------------------------
        tdA = createTime(2013, 10, 8, 23, 45, 17, 897);
        tdB = createTime(2014, 10, 8, 23, 45, 17, 897);
        System.out.println("\n * OTHER TIMES: {" + tdA.toString() + " | " + tdB.toString() + "}");
        test(absolute, tdA, tdB, TimeDifference.SCALE_MILLISECONDS, -31536000000L);
        test(absolute, tdA, tdB, TimeDifference.SCALE_SECONDS, -31536000);
        test(absolute, tdA, tdB, TimeDifference.SCALE_MINUTES, -525600);
        test(absolute, tdA, tdB, TimeDifference.SCALE_HOURS, -8760);
        test(absolute, tdA, tdB, TimeDifference.SCALE_DAYS, -365);
        test(absolute, tdA, tdB, TimeDifference.SCALE_WEEKS, -52);
        test(absolute, tdA, tdB, TimeDifference.SCALE_MONTHS, -12);
        test(absolute, tdA, tdB, TimeDifference.SCALE_YEARS, -1);
        //--------------------------------------------------------------------------

        //
        System.out.println("\n TIME:");
        System.out.println(" ----------");

        //--------------------------------------------------------------------------
        tdA = createTime(0, 0, 0, 8, 45, 17, 897);
        tdB = createTime(0, 0, 0, 8, 45, 17, 897);
        System.out.println(" * SAME TIMES: " + tdA.toString());
        test(absolute, tdA, tdB, TimeDifference.SCALE_MILLISECONDS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_SECONDS, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_MINUTES, 0);
        test(absolute, tdA, tdB, TimeDifference.SCALE_HOURS, 0);
        //--------------------------------------------------------------------------

        //--------------------------------------------------------------------------
        tdA = createTime(0, 0, 0, 8, 45, 17, 897);
        System.out.println("\n * OTHER TIMES: {" + tdA.toString() + " | " + tdB.toString() + "}");
        test(absolute, tdA, createTime(0, 0, 0, 8, 45, 17, 898), TimeDifference.SCALE_MILLISECONDS, -1);
        test(absolute, tdA, createTime(0, 0, 0, 8, 45, 18, 897), TimeDifference.SCALE_MILLISECONDS, -1000);
        test(absolute, tdA, createTime(0, 0, 0, 8, 46, 17, 897), TimeDifference.SCALE_MILLISECONDS, -60000);
        test(absolute, tdA, createTime(0, 0, 0, 9, 45, 17, 897), TimeDifference.SCALE_MILLISECONDS, -3600000);

        test(absolute, tdA, createTime(0, 0, 0, 8, 45, 17, 898), TimeDifference.SCALE_MILLISECONDS, -1);
        test(absolute, tdA, createTime(0, 0, 0, 8, 45, 18, 897), TimeDifference.SCALE_SECONDS, -1);
        test(absolute, tdA, createTime(0, 0, 0, 8, 46, 17, 897), TimeDifference.SCALE_MINUTES, -1);
        test(absolute, tdA, createTime(0, 0, 0, 9, 45, 17, 897), TimeDifference.SCALE_HOURS, -1);
        //--------------------------------------------------------------------------

    }

    private void test(boolean absolute, TimeDescription tdA, TimeDescription tdB, String scale, double expDif) throws Exception {
        if (absolute) {
            expDif = Math.abs(expDif);
        }
        TimeDifference instance = createInstance(absolute, scale);
        double result = instance.getSimilarity(tdA, tdB);
        System.out.print("       " + scale + ": " + result);
        assertEquals("invalid value in scale: " + scale, expDif, result, 0);
        System.out.println("  [OK]");
    }
}
