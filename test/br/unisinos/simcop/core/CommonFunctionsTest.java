/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unisinos.simcop.core;

import java.util.ArrayList;
import java.util.List;
import br.unisinos.simcop.tests.BaseUnitTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class CommonFunctionsTest extends BaseUnitTest {

    public CommonFunctionsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testLevenshteinDistance() {
        showBeginTest();
        assertEquals(1, CommonFunctions.levenshteinDistance("kitten", "sitten"));
        assertEquals(1, CommonFunctions.levenshteinDistance("sittin", "sitting"));
        assertEquals(3, CommonFunctions.levenshteinDistance("123. abcdefgh", "123456 abcdefgh"));
        assertEquals(3, CommonFunctions.levenshteinDistance("123456 abcdefgh", "123. abcdefgh"));

        int comp1 = CommonFunctions.levenshteinDistance("the quick fox jumped", "the fox jumped");
        int comp2 = CommonFunctions.levenshteinDistance("the quick fox jumped", "the fox");
        assertTrue(comp1 < comp2);
        
        showEndTest();
    }

    @Test
    public void testHaversineDistance() {
        showBeginTest();
        GeoPoint p1 = new GeoPoint(41.7486, -72.9864);
        GeoPoint p2 = new GeoPoint(40.7486, -73.9864);
        double expResult = 139.1; //km
        double result = CommonFunctions.haversineDistance(p1, p2);
        assertEquals(expResult, result, 0.09);
        showEndTest();
    }

    /**
     * Values used in this test was obtained from: <br/>
     * www.cse.ust.hk/~qyang/337/slides/dist.ppt
     */
    @Test
    public void testLpDistance() {
        showBeginTest();
        double[] pointA;
        double[] pointB;
        /*
         * CITY BLOCK or MANHATAN DISTANCE (p = 1)
         */
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {2, 0};
        testLp(pointA, pointB, 1, 4);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {3, 1};
        testLp(pointA, pointB, 1, 4);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 1, 6);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {3, 1};
        testLp(pointA, pointB, 1, 2);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 1, 4);
        //--
        pointA = new double[] {3, 1};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 1, 2);
        //--


        /*
         * EUCLIDIAN DISTANCE (p = 2)
         */
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {2, 0};
        testLp(pointA, pointB, 2, 2.828);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {3, 1};
        testLp(pointA, pointB, 2, 3.162);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 2, 5.099);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {3, 1};
        testLp(pointA, pointB, 2, 1.414);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 2, 3.162);
        //--
        pointA = new double[] {3, 1};
        pointB = new double[] {5, 1};
        testLp(pointA, pointB, 2, 2);
        //--


        showEndTest();
    }

    private void testLp(double[] pointA, double[] pointB, double p, double expected) {
        List<Double> differences = new ArrayList<Double>();
        for (int i = 0; i < pointA.length; i++) {
            double dif = pointA[i] - pointB[i];
            differences.add( dif );
        }
        assertEquals(expected, CommonFunctions.lpDistance(differences, p), 0.0009);

    }


    /**
     * Values used in this test was obtained from: <br/>
     * www.cse.ust.hk/~qyang/337/slides/dist.ppt <br/>
     *<br/>
     * Chebyshev = Lp, where p goes to infinity.
     *
     */
    @Test
    public void testChebyshevDistance() {
        showBeginTest();
        double[] pointA;
        double[] pointB;
        /*
         * EUCLIDIAN DISTANCE (p = 2)
         */
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {2, 0};
        testChebyshev(pointA, pointB, 2);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {3, 1};
        testChebyshev(pointA, pointB, 3);
        //--
        pointA = new double[] {0, 2};
        pointB = new double[] {5, 1};
        testChebyshev(pointA, pointB, 5);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {3, 1};
        testChebyshev(pointA, pointB, 1);
        //--
        pointA = new double[] {2, 0};
        pointB = new double[] {5, 1};
        testChebyshev(pointA, pointB, 3);
        //--
        pointA = new double[] {3, 1};
        pointB = new double[] {5, 1};
        testChebyshev(pointA, pointB, 2);
        //--

        showEndTest();
    }

    private void testChebyshev(double[] pointA, double[] pointB, double expected) {
        List<Double> differences = new ArrayList<Double>();
        for (int i = 0; i < pointA.length; i++) {
            double dif =pointA[i] - pointB[i];
            differences.add( dif );            
        }
        assertEquals(expected, CommonFunctions.chebyshevDistance(differences), 0);

    }

}