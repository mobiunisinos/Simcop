package br.unisinos.simcop.core.process;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.tests.BaseUnitTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SimcopTaskTest extends BaseUnitTest {

    public SimcopTaskTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetTypeName_int() {
        showBeginTest();
        String expResult = "InputFilter";
        String result = SimcopTask.getTypeName(0);
        assertEquals(expResult, result);
        showEndTest();
    }

    @Test
    public void testGetTypeCode() {
        showBeginTest();
        int expResult = 0;
        int result = SimcopTask.getTypeCode("InputFilter");
        assertEquals(expResult, result);
        showEndTest();
    }

    @Test
    public void testCreateInstance() {
        showBeginTest();
        SimcopTask task = new SimcopTask(SimcopTask.ATTRIBUTE_SIMILARITY);
        task.setSequence(0);
        task.setClassName( "br.unisinos.simcop.impl.filters.FilterByPredicate" );
        ISimcopClass result = task.createInstance();
        assertNotNull(result);
        assertTrue( result instanceof br.unisinos.simcop.impl.filters.FilterByPredicate);
        showEndTest();
   }


    


}