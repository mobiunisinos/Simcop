package br.unisinos.simcop.core.process;

import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.List;
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
public class SimcopProcessTest extends BaseUnitTest {

    public SimcopProcessTest() {
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
    public void testIsEmpty() {
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        assertEquals("A", true, instance.isEmpty());
        
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_FILTER) );
        assertEquals("B", false, instance.isEmpty());

        instance.clear();
        assertEquals("C", true, instance.isEmpty());

        showEndTest();
    }

    @Test
    public void testAddInputFilter() {
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        SimcopTask task = new SimcopTask( SimcopTask.INPUT_FILTER );
        task.setClassName( "br.unisinos.simcop.impl.filters.FilterByPredicate" );
        task.createInstance();
        instance.addTask(task, true);
        assertNotNull("task not found", task);
        assertEquals(SimcopTask.INPUT_FILTER, task.getType());
        assertEquals(SimcopTask.getTypeName(SimcopTask.INPUT_FILTER), SimcopTask.getTypeName(task.getType()));
        assertEquals(1, task.getSequence());
        assertNotNull("task instance null", task.getInstance());
        showEndTest();
    }

    @Test
    public void testAddInputTransformation() {
        //TODO create MOCK class for this test
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_TRANSFORMATION) );
        assertEquals(1, instance.getTasks().size() );
        SimcopTask task = instance.getTasks().get(0);
        assertNotNull("task not found", task);
        assertEquals(SimcopTask.INPUT_TRANSFORMATION, task.getType());
        assertEquals(SimcopTask.getTypeName(SimcopTask.INPUT_TRANSFORMATION), SimcopTask.getTypeName(task.getType()));
        showEndTest();
    }

    @Test
    public void testSetSequenceSimilarity() {
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        //TODO instance.setSequenceSimilarity( )
        System.out.println("TODO: instance.setSequenceSimilarity()");

        showEndTest();
    }

    @Test
    public void testAddOutputFilter() {
        //TODO create MOCK class for this test
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_FILTER) );
        assertEquals(1, instance.getTasks().size() );
        SimcopTask task = instance.getTasks().get(0);
        assertNotNull("task not found", task);
        assertEquals(SimcopTask.OUTPUT_FILTER, task.getType());
        assertEquals(SimcopTask.getTypeName(SimcopTask.OUTPUT_FILTER), SimcopTask.getTypeName(task.getType()));
        showEndTest();
    }

    @Test
    public void testAddOutputTransformation() {
        //TODO create MOCK class for this test
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_TRANSFORMATION) );
        assertEquals(1, instance.getTasks().size() );
        SimcopTask task = instance.getTasks().get(0);
        assertNotNull("task not found", task);
        assertEquals(SimcopTask.OUTPUT_TRANSFORMATION, task.getType());
        assertEquals(SimcopTask.getTypeName(SimcopTask.OUTPUT_TRANSFORMATION), SimcopTask.getTypeName(task.getType()));
        showEndTest();
    }

    @Test
    public void testClear() {
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_FILTER) );
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_TRANSFORMATION) );
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_FILTER) );
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_TRANSFORMATION) );
        assertEquals(4, instance.getTasks().size() );
        instance.clear();
        assertEquals(0, instance.getTasks().size() );
        showEndTest();
    }

    @Test
    public void testGetTasks() {
        showBeginTest();
        SimcopProcess instance = new SimcopProcess();
        List result = instance.getTasks();
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_FILTER) );
        instance.getTasks().add( new SimcopTask(SimcopTask.INPUT_TRANSFORMATION) );
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_FILTER) );
        instance.getTasks().add( new SimcopTask(SimcopTask.OUTPUT_TRANSFORMATION) );
        assertEquals(4, result.size() );
        showEndTest();
    }

}