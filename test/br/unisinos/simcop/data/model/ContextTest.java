/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.simcop.data.model;

import br.unisinos.simcop.core.dataType.SimcopInterval;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.core.dataType.SimcopRange;
import br.unisinos.simcop.core.dataType.SimcopValue;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class ContextTest extends BaseUnitTest {

    public ContextTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testResetSituations() {
        showBeginTest();
        Context instance = new Context();
        instance.resetSituations();
        assertEquals(0, instance.getSituations().size());
        showEndTest();
    }

    @Test
    public void testAddSituation_asObject() {
        showBeginTest();
        Context instance = new Context();
        //
        Situation added = instance.addSituation("activity", "walking");
        //
        assertEquals(1, instance.getSituations().size());
        assertNotNull(added);
        assertNotNull(added.getValue());
        assertEquals(SimcopValue.STRING, added.getValue().getType());
        assertEquals("walking", added.getValue().getStringValue());
        showEndTest();
    }

    @Test
    public void testAddSituation_5args() {
        showBeginTest();
        Context instance = new Context();
        //
        SimcopRange range = new SimcopInterval(30.0, 40.9);
        SimcopValue value = SimcopValue.createFromObject(37.5);
        Situation added = instance.addSituation("sensor 1", "celsius", "temperature", range, value);
        //
        assertEquals(1, instance.getSituations().size());
        assertNotNull(added);
        assertNotNull(added.getValue());
        assertEquals(SimcopValue.DOUBLE, added.getValue().getType());
        assertEquals(37.5, added.getValue().getDoubleValue(), 0);

        showEndTest();
    }

    @Test
    public void testGet() {
        showBeginTest();
        Context instance = new Context();
        //
        SimcopRange range = new SimcopInterval(30.0, 40.9);
        SimcopValue value = SimcopValue.createFromObject(37.5);
        Situation added1 = instance.addSituation("sensor 1", "celsius", "temperature", range, value);
        Situation added2 = instance.addSituation("sensor 2", "celsius", "temperature", range, value);
        assertEquals(2, instance.getSituations().size());
        //

        Situation obtained = instance.get(0);
        assertNotNull(obtained);
        assertEquals(added1, obtained);

        showEndTest();
    }

    @Test
    public void testFindSituation_Predicate() {
        showBeginTest();
        Context instance = new Context();
        //
        SimcopRange range = new SimcopInterval(30.0, 40.9);
        SimcopValue value = SimcopValue.createFromObject(37.5);
        Situation added1 = instance.addSituation("sensor 1", "celsius", "temperature", range, value);
        Situation added2 = instance.addSituation("sensor 2", "celsius", "temperature", range, value);
        //

        Situation obtained = instance.findSituation("temperature");
        assertNotNull(obtained);
        assertEquals(added1, obtained);

        showEndTest();
    }

    @Test
    public void testFindSituation_Aux_Predicate() {
        showBeginTest();
        Context instance = new Context();
        //
        SimcopRange range = new SimcopInterval(30.0, 40.9);
        SimcopValue value = SimcopValue.createFromObject(37.5);
        Situation added1 = instance.addSituation("sensor 1", "celsius", "temperature", range, value);
        Situation added2 = instance.addSituation("sensor 2", "fahrenheit", "temperature", range, value);
        //

        Situation obtained = instance.findSituation("fahrenheit", "temperature");
        assertNotNull(obtained);
        assertEquals(added2, obtained);

        showEndTest();
    }

    @Test
    public void testFindSituation_Source_Aux_Predicate() {
        showBeginTest();
        Context instance = new Context();
        //
        SimcopRange range = new SimcopInterval(30.0, 40.9);
        SimcopValue value = SimcopValue.createFromObject(37.5);
        Situation added1 = instance.addSituation("sensor 1", "celsius", "temperature", range, value);
        Situation added2 = instance.addSituation("sensor 2", "fahrenheit", "temperature", range, value);
        //

        Situation obtained = instance.findSituation("sensor 2", "fahrenheit", "temperature");
        assertNotNull(obtained);
        assertEquals(added2, obtained);

        showEndTest();
    }

    @Test
    public void testFindSituation_Equals_HashCode() {
        showBeginTest();
        Context instanceA = createContext(new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context instanceB = createContext(new TimeDescription(8, 49, 45, 333), new LocationDescription(5, 5), new Situation("test", "testA"), new Situation("testX", "123"));
        Context instanceC = createContext(new TimeDescription(7, 10, 6, 12), new LocationDescription(4, 4), new Situation("x", "y"));


        //--
        assertTrue(instanceA.getTime().equals(instanceB.getTime()));
        assertTrue(instanceA.getLocation().equals(instanceB.getLocation()));
        assertTrue(instanceA.getSituations().get(0).equals(instanceB.getSituations().get(0)));
        assertTrue(instanceA.getSituations().get(1).equals(instanceB.getSituations().get(1)));
        assertTrue(instanceA.getSituations().equals(instanceB.getSituations()));
        assertTrue(instanceA.equals(instanceB));
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        assertFalse(instanceA.equals(instanceC));
        assertNotSame(instanceA.hashCode(), instanceC.hashCode());
        //--

        showEndTest();
    }
}
