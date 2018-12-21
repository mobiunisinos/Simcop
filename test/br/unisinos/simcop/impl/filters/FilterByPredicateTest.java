/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.simcop.impl.filters;

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
public class FilterByPredicateTest extends BaseUnitTest {

    public FilterByPredicateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Context createContext(String... predicates) {
        Context ctx = new Context();
        for (String predicate : predicates) {
            ctx.addSituation(predicate, "testValue");
        }        
        return ctx;
    }


    @Test
    public void testAccept() {
        showBeginTest();
        Context ctx1 = createContext("test1", "test2", "test3");
        Context ctx2 = createContext("test1", "xyz", "abc", "def");


        FilterByPredicate filter = new FilterByPredicate();
        filter.getParameters().addParameter("predicate1", "test1");
        filter.getParameters().addParameter("predicate2", "test2");

        System.out.print("   filterType: AND  ");
        //---
        filter.getParameters().addParameter("filterType", "and");
        boolean[] result = filter.accept(0, ctx1, ctx2);
        assertEquals(2, result.length);
        assertTrue(result[0]); //CTX1 haves the predicates 'test1' and 'test2'
        assertFalse(result[1]); //CTX2 only haves the predicate 'test1'
        System.out.println(" [OK]");
        //---


        System.out.print("   filterType: OR   ");
        //---
        filter.getParameters().addParameter("filterType", "or");
        result = filter.accept(0, ctx1, ctx2);
        assertEquals(2, result.length);
        assertTrue(result[0]); //CTX1 haves the predicates 'test1' and 'test2'
        assertTrue(result[1]); //CTX2 haves the predicate 'test1'
        System.out.println(" [OK]");
        //---

        showEndTest();
    }
}
