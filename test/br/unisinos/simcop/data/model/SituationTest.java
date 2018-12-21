/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unisinos.simcop.data.model;

import br.unisinos.simcop.core.dataType.SimcopCategories;
import br.unisinos.simcop.core.dataType.SimcopInterval;
import br.unisinos.simcop.tests.BaseUnitTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SituationTest  extends BaseUnitTest  {

    public SituationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEquals() {
        showBeginTest();
        Situation instance = new Situation();
        Situation other = new Situation();
        
        instance.setPredicate("test predicate");
        other.setPredicate("test predicate");
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
        
        other.setAuxiliary("test aux");
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));

        instance.setAuxiliary("test aux");
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));


        showEndTest();
    }

    @Test
    public void testHashCode() {
        showBeginTest();

        Situation instance = new Situation();
        Situation other = new Situation();

        instance.setPredicate("test predicate");
        other.setPredicate("test predicate");

        assertTrue(instance.equals(other));
        assertEquals(instance.hashCode(), other.hashCode());

        showEndTest();
    }


    @Test
    public void testIsOverRange() {
        showBeginTest();
        Situation instance = new Situation();
        instance.set(1000); //test value
        
        System.out.print("no range (always false): ");
        assertFalse(instance.isOverRange());
        System.out.println("ok");

        System.out.print("value greater than max range (true): ");
        instance.setRange( new SimcopInterval(100, 999) );
        assertTrue(instance.isOverRange());
        System.out.println("ok");

        System.out.print("value lesser than max range (false): ");
        instance.setRange( new SimcopInterval(100, 1001) );
        assertFalse(instance.isOverRange());
        System.out.println("ok");

        System.out.print("value lesser than min range (false): ");
        instance.setRange( new SimcopInterval(1001, 2000) );
        assertFalse(instance.isOverRange());
        System.out.println("ok");

        System.out.print("value is in categories: ");
        instance.setRange( new SimcopCategories( new String[] {"shoes", "shirt", "pants"}  ) );
        instance.set("shoes");
        assertFalse(instance.isOverRange());
        instance.set("other");
        assertTrue(instance.isOverRange());
        System.out.println("ok");

        showEndTest();
    }

    @Test
    public void testIsBelowRange() {
        showBeginTest();
        Situation instance = new Situation();
        instance.set(10);

        System.out.print("no range (always false): ");
        assertFalse(instance.isBelowRange());
        System.out.println("ok");

        System.out.print("value lesser than min range (true): ");
        instance.setRange( new SimcopInterval(11, 20) );
        assertTrue(instance.isBelowRange());
        System.out.println("ok");

        System.out.print("value greater than min range (false): ");
        instance.setRange( new SimcopInterval(9, 100) );
        assertFalse(instance.isBelowRange());
        System.out.println("ok");

        System.out.print("value greater than max range (false): ");
        instance.setRange( new SimcopInterval(0, 9) );
        assertFalse(instance.isBelowRange());
        System.out.println("ok");

        System.out.print("value is in categories: ");
        instance.setRange( new SimcopCategories( new String[] {"shoes", "shirt", "pants"}  ) );
        instance.set("shoes");
        assertFalse(instance.isBelowRange());
        instance.set("other");
        assertTrue(instance.isBelowRange());
        System.out.println("ok");

        showEndTest();
    }


    @Test
    public void testFindSituation_Equals_HashCode() {
        showBeginTest();
        Situation instanceA = new Situation("testX", "123");
        Situation instanceB = new Situation("testX", "123");
        Situation instanceC = new Situation("x", "y");

        System.out.println("A: " +instanceA.hashCode());
        System.out.println("B: " +instanceB.hashCode());
        System.out.println("C: " +instanceC.hashCode());

        //--
        assertTrue(instanceA.equals(instanceB));
        assertEquals(instanceA.hashCode(), instanceB.hashCode());
        assertFalse(instanceA.equals(instanceC));
        assertNotSame(instanceA.hashCode(), instanceC.hashCode());
        //--


        showEndTest();
    }

}