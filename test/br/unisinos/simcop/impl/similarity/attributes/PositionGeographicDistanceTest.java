package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.core.GeoPoint;
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
public class PositionGeographicDistanceTest extends BaseUnitTest {

    public PositionGeographicDistanceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Test
    public void testIsDistanceFunction() {
        showBeginTest();
        double result;
        LocationDescription testA = new LocationDescription( -31.0665, -51.166);
        LocationDescription testB = new LocationDescription( -31.087, -51.445);

        PositionGeographicDistance instance = new PositionGeographicDistance();
        instance.setParameter(PositionGeographicDistance.PAR_LATITUDE_INDEX, "0");
        instance.setParameter(PositionGeographicDistance.PAR_LONTITUDE_INDEX, "1");

        System.out.println(" from " + testA.toString() + " to " + testB.toString() + ":");
        try {
            //
            instance.setParameter(PositionGeographicDistance.PAR_METRIC, "m");
            result = instance.getSimilarity(testA, testB);            
            System.out.println(result + " m");
            //
            instance.setParameter(PositionGeographicDistance.PAR_METRIC, "km");
            result = instance.getSimilarity(testA, testB);
            System.out.println(result + " km");
            //
            instance.setParameter(PositionGeographicDistance.PAR_METRIC, "ft");
            result = instance.getSimilarity(testA, testB);
            System.out.println(result + " ft");
        } catch (Exception ex) {
            Logger.getLogger(PositionGeographicDistanceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
            return;
        }

        showEndTest();
    }

}