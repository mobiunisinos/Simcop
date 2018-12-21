package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
public class DefaultFileSourceTest extends BaseUnitTest{

    private static File sample;
    private static final String[] subjects = new String[]{"EntityA", "EntityB", "EntityC", "EntityD"};
    private static final String[] times = new String[]{"00:00", "07:00", "12:30", "17:00"};
    private static final String[] locations = new String[]{"home", "work", "work", "school"};
    private static final String[] situations = new String[]{"sleeping", "working", "working", "learning"};

    public DefaultFileSourceTest() {
    }

    @AfterClass
    public static void tearDownClass() {
        if (!sample.delete()) {
            sample.deleteOnExit();
        }
    }

    @BeforeClass
    public static void setUpClass() {
        sample = new File("simcopSample.csv");
        try {
            FileWriter fw = new FileWriter(sample);
            for (int i = 0; i < subjects.length; i++) {
                String entityUid = "E" + i;
                String entityName = subjects[i];
                System.out.println("Creating: [" + entityUid + "]" + entityName);
                for (int j = 0; j < times.length; j++) {
                    System.out.println("\t"+times[j]);
                    fw.write(entityUid);//column 0
                    fw.write(";");
                    fw.write(entityName);//column 1
                    fw.write(";");
                    fw.write(times[j]);//column 2
                    fw.write(";");
                    fw.write(locations[j]);//column 3
                    fw.write(";");
                    fw.write("116.30368,39.97505");//column 4
                    fw.write(";");
                    fw.write(situations[j]);//column 5
                    fw.write("\n");
                }

            }
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(DefaultFileSourceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    private DefaultFileSource initInstance() {
        DefaultFileSource src = new DefaultFileSource();
        src.setParameter(DefaultFileSource.PAR_FILE_PATH, sample.getAbsolutePath());
        src.setParameter(DefaultFileSource.PAR_ENTITY_UID, "0");
        src.setParameter(DefaultFileSource.PAR_LOCATION_CATEGORY, "0"); //only for test        
        src.setParameter(DefaultFileSource.PAR_ENTITY_NAME, "1");
        src.setParameter(DefaultFileSource.PAR_TIME, "2");
        src.setParameter(DefaultFileSource.PAR_LOCATION_NAME, "3");
        src.setParameter(DefaultFileSource.PAR_LOCATION_POSITION, "4");
        src.setParameter(DefaultFileSource.PAR_SITUATION + "0", "5");
        src.setParameter(DefaultFileSource.PAR_PREDICATE_NAME + "0", "activity");
        src.setParameter(DefaultFileSource.PAR_TIME_FORMAT, "HH:mm");
        return src;
    }

    @Test
    public void testGetListEntities() throws Exception {
        showBeginTest();
        DefaultFileSource src = initInstance();
        List<Entity> entities = src.getListEntities(null);
        assertNotNull("no entity found", entities);
        assertEquals("invalid size", subjects.length, entities.size());
        for (int i = 0; i < subjects.length; i++) {
            assertEquals("invalid entity: " + i, subjects[i], entities.get(i).getName());
        }
    }

    @Test
    public void testGetListEntities_Filter() throws Exception {
        showBeginTest();
        DefaultFileSource src = initInstance();
        List<Entity> entities = src.getListEntities(subjects[0]);
        assertNotNull("no entity found", entities);
        assertEquals("invalid size", 1, entities.size());
        assertEquals("invalid entity: ", subjects[0], entities.get(0).getName());
        showEndTest();
    }

    @Test
    public void testLoadEntity() throws Exception {
        showBeginTest();
        DefaultFileSource src = initInstance();
        Entity entity = src.loadEntity("E1");
        assertNotNull("no entity found", entity);
        assertEquals("invalid entity: ", "E1", entity.getUid());
        assertEquals("invalid entity: ", subjects[1], entity.getName());
        showEndTest();
    }

    @Test
    public void testLoadIntoSequence() throws Exception {
        showBeginTest();
        DefaultFileSource src = initInstance();
        Entity entity = src.loadEntity("E2");
        assertNotNull("no entity found", entity);
        assertEquals("invalid entity: ", "E2", entity.getUid());
        assertEquals("invalid entity: ", subjects[2], entity.getName());

        src.loadIntoSequence(entity.getSequence());
        assertNotNull(entity.getSequence());
        assertEquals("invalid sequence size ", times.length, entity.getSequence().size());

        for (int i = 0; i < times.length; i++) {
            Context ctx = entity.getContext(i);            
            TimeDescription td = ctx.getTime();
            LocationDescription ld = ctx.getLocation();            
            String category = "E2";
            assertEquals("Invalid entity of context " + i, category, ld.getCategory());

            String time = times[i];
            String location = locations[i];
            
            assertEquals("Invalid at index: " + i, time, td.asString("HH:mm"));
            assertEquals("Invalid at index: " + i, location, ld.getName());
            double position1 = 116.30368;
            double position2 = 39.97505;
            assertNotNull("Invalid at index: " + i, ld.getPosition());
            assertEquals("Invalid at index: " + i, 2, ld.getPosition().length);
            assertEquals("Invalid at index: " + i, position1, ld.getPosition()[0], 0);
            assertEquals("Invalid at index: " + i, position2, ld.getPosition()[1], 0);
            
            List<Situation> ctxSits = ctx.getSituations();
            String situation = "activity";
            String situationValue = situations[i];
            assertNotNull("Invalid at index: " + i, ctxSits);
            assertEquals("Invalid at index: " + i, 1, ctxSits.size());
            assertEquals("Invalid at index: " + i, situation, ctxSits.get(0).getPredicate());
            assertEquals("Invalid at index: " + i, situationValue, ctxSits.get(0).getValue().printValue(false));
            
            
            
        }
        showEndTest();
    }
}