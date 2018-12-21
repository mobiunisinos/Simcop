/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocationCategoryTest extends BaseUnitTest {

    public LocationCategoryTest() {

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
        LocationCategory instance = new LocationCategory();
        System.out.println(" as Distance Function");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_SAME_CATEGORY, "0");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_OTHER_CATEGORY, "100");
        try {
            instance.validateParameters();
        } catch (Exception ex) {
            Logger.getLogger(LocationCategoryTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        assertTrue( instance.isDistanceFunction() );
        
        System.out.println(" as Similarity Function");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_SAME_CATEGORY, "100");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_OTHER_CATEGORY, "0");
        try {
            instance.validateParameters();
        } catch (Exception ex) {
            Logger.getLogger(LocationCategoryTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        assertFalse( instance.isDistanceFunction() );

        showEndTest();
    }

    @Test
    public void testGetSimilarity_CaseSensitive() throws Exception {
        showBeginTest();
        double result;
        LocationDescription testA = location("Av. Unisinos, 777", "Home");
        LocationDescription testB = location("BR-116", "home");

        LocationCategory instance = new LocationCategory();
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_SAME_CATEGORY, "1");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_OTHER_CATEGORY, "0.5");
        instance.setParameter(LocationCategory.PAR_CASE_SENSITIVE, "true");

        result = instance.getSimilarity(testA, testB);
        assertEquals(0.5, result, 0.0);

        showEndTest();
    }

    @Test
    public void testGetSimilarity_CaseInsensitive() throws Exception{
        showBeginTest();
        double result;
        LocationDescription testA = location("Av. Unisinos, 777", "Home");
        LocationDescription testB = location("BR-116", "home");

        LocationCategory instance = new LocationCategory();
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_SAME_CATEGORY, "1");
        instance.setParameter(LocationCategory.PAR_VALUE_FOR_OTHER_CATEGORY, "0.5");
        instance.setParameter(LocationCategory.PAR_CASE_SENSITIVE, "false");
        result = instance.getSimilarity(testA, testB);
        assertEquals(1, result, 0.0);

        showEndTest();
    }

}