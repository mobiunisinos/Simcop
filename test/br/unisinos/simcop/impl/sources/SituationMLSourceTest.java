package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SituationMLSourceTest extends BaseUnitTest {

    public SituationMLSourceTest() {
    }
    private SituationMLSource srcMax;
    private SituationMLSource srcMix;
    private SituationMLSource srcMin;
    private final String[] subjects = new String[]{"EntityA", "EntityB", "EntityC", "EntityD"};
    private final String[] times = new String[]{"00:00", "07:00", "12:00", "17:00"};
    private final String[] locations = new String[]{"home", "work", "work", "school"};

    @Before
    public void setUp() {
        StringBuilder xmlMax = new StringBuilder();
        StringBuilder xmlMix = new StringBuilder();
        StringBuilder xmlMin = new StringBuilder();
        xmlMax.append("<SituationReport>");
        xmlMin.append("<SituationReport>");
        xmlMix.append("<SituationReport>");
        for (String entity : subjects) {
            for (int i = 0; i < times.length; i++) {
                xmlMax.append(createXmlMax(entity, times[i], locations[i]));
                xmlMix.append(createXmlMix(entity, times[i], locations[i]));
                xmlMin.append(createXmlMin(entity, times[i], locations[i]));
            }
        }
        xmlMax.append("</SituationReport>");
        xmlMin.append("</SituationReport>");
        xmlMix.append("</SituationReport>");
        try {
            srcMax = createSrc(xmlMax.toString());
            srcMix = createSrc(xmlMix.toString());
            srcMin = createSrc(xmlMin.toString());
        } catch (Exception ex) {
            Logger.getLogger(SituationMLSourceTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
    }

    private SituationMLSource createSrc(String xml) throws Exception {
        InputSource in = new InputSource(new StringReader(xml));
        SituationMLSource src = new SituationMLSource(in);
        src.setParameter(SituationMLSource.PAR_TIME_FORMAT, "HH:mm");
        return src;
    }

    private String createXmlMax(String subject, String time, String location) {
        StringBuilder sb = new StringBuilder();
        sb.append("<statement>");
        //mainpart
        sb.append("   <mainpart>");
        sb.append("      <subject>").append(subject).append("</subject>");
        sb.append("      <auxiliary>").append("aux").append("</auxiliary>");
        sb.append("      <predicate>").append("pred").append("</predicate>");
        sb.append("      <range>").append("rng").append("</range>");
        sb.append("      <object>").append("obj").append("</object>");
        sb.append("   </mainpart>");
        //situation
        sb.append("   <situation>");
        sb.append("      <start>").append(time).append("</start>");
        sb.append("      <end>").append(time).append("</end>");
        sb.append("      <durability>").append("dur").append("</durability>");
        sb.append("      <location>").append(location).append("</location>");
        sb.append("      <position>").append("10,11,12").append("</position>");
        sb.append("   </situation>");
        //explanation
        sb.append("   <explanation>");
        sb.append("      <source>").append("src").append("</source>");
        sb.append("      <creator>").append("crt").append("</creator>");
        sb.append("      <method>").append("met").append("</method>");
        sb.append("      <evidence>").append("evi").append("</evidence>");
        sb.append("      <confidence>").append("con").append("</confidence>");
        sb.append("   </explanation>");
        //privacy
        sb.append("   <privacy>");
        sb.append("      <key>").append("key").append("</key>");
        sb.append("      <owner>").append("own").append("</owner>");
        sb.append("      <access>").append("acc").append("</access>");
        sb.append("      <purpouse>").append("pur").append("</purpouse>");
        sb.append("      <retention>").append("ret").append("</retention>");
        sb.append("   </privacy>");
        //administration
        sb.append("   <administration>");
        sb.append("      <id>").append("#id").append("</id>");
        sb.append("      <unique>").append("unq").append("</unique>");
        sb.append("      <replaces>").append("rep").append("</replaces>");
        sb.append("      <group>").append("grp").append("</group>");
        sb.append("      <notes>").append("nts").append("</notes>");
        sb.append("   </administration>");
        sb.append("</statement>");
        return sb.toString();
    }

    private String createXmlMix(String subject, String time, String location) {
        StringBuilder sb = new StringBuilder();
        sb.append("<statement>");
        //mainpart
        sb.append("   <mainpart");
        sb.append("      subject=\"").append(subject).append("\"");
        sb.append("      auxiliary=\"").append("aux").append("\"");
        sb.append("      predicate=\"").append("pred").append("\"");
        sb.append("      range=\"").append("rng").append("\"");
        sb.append("      object=\"").append("obj").append("\"");
        sb.append("   />");
        //situation
        sb.append("   <situation");
        sb.append("      start=\"").append(time).append("\"");
        sb.append("      end=\"").append(time).append("\"");
        sb.append("      durability=\"").append("dur").append("\"");
        sb.append("      location=\"").append(location).append("\"");
        sb.append("      position=\"").append("10,11,12").append("\"");
        sb.append("   />");
        //explanation
        sb.append("   <explanation");
        sb.append("      source=\"").append("src").append("\"");
        sb.append("      creator=\"").append("crt").append("\"");
        sb.append("      method=\"").append("met").append("\"");
        sb.append("      evidence=\"").append("evi").append("\"");
        sb.append("      confidence=\"").append("con").append("\"");
        sb.append("   />");
        //privacy
        sb.append("   <privacy");
        sb.append("      key=\"").append("key").append("\"");
        sb.append("      owner=\"").append("own").append("\"");
        sb.append("      access=\"").append("acc").append("\"");
        sb.append("      purpouse=\"").append("pur").append("\"");
        sb.append("      retention=\"").append("ret").append("\"");
        sb.append("   />");
        //administration
        sb.append("   <administration");
        sb.append("      id=\"").append("#id").append("\"");
        sb.append("      unique=\"").append("unq").append("\"");
        sb.append("      replaces=\"").append("rep").append("\"");
        sb.append("      group=\"").append("grp").append("\"");
        sb.append("      notes=\"").append("nts").append("\"");
        sb.append("   />");
        sb.append("</statement>");
        return sb.toString();
    }

    private String createXmlMin(String subject, String time, String location) {
        StringBuilder sb = new StringBuilder();
        sb.append("<statement");
        //mainpart
        sb.append("      subject=\"").append(subject).append("\"");
        sb.append("      auxiliary=\"").append("aux").append("\"");
        sb.append("      predicate=\"").append("pred").append("\"");
        sb.append("      range=\"").append("rng").append("\"");
        sb.append("      object=\"").append("obj").append("\"");
        //situation
        sb.append("      start=\"").append(time).append("\"");
        sb.append("      end=\"").append(time).append("\"");
        sb.append("      durability=\"").append("dur").append("\"");
        sb.append("      location=\"").append(location).append("\"");
        sb.append("      position=\"").append("10,11,12").append("\"");
        //explanation
        sb.append("      source=\"").append("src").append("\"");
        sb.append("      creator=\"").append("crt").append("\"");
        sb.append("      method=\"").append("met").append("\"");
        sb.append("      evidence=\"").append("evi").append("\"");
        sb.append("      confidence=\"").append("con").append("\"");
        //privacy
        sb.append("      key=\"").append("key").append("\"");
        sb.append("      owner=\"").append("own").append("\"");
        sb.append("      access=\"").append("acc").append("\"");
        sb.append("      purpouse=\"").append("pur").append("\"");
        sb.append("      retention=\"").append("ret").append("\"");
        //administration
        sb.append("      id=\"").append("#id").append("\"");
        sb.append("      unique=\"").append("unq").append("\"");
        sb.append("      replaces=\"").append("rep").append("\"");
        sb.append("      group=\"").append("grp").append("\"");
        sb.append("      notes=\"").append("nts").append("\"");
        sb.append("   />");
        return sb.toString();
    }

    @Test
    public void testConnectToSource() {
        showBeginTest();
        assertTrue("Max Cannot connect", srcMax.connectToSource());
        assertTrue("Min Cannot connect", srcMin.connectToSource());
        assertTrue("Mix Cannot connect", srcMix.connectToSource());
        assertTrue("Max is disconnected", srcMax.isConnected());
        assertTrue("Min is disconnected", srcMin.isConnected());
        assertTrue("Mix is disconnected", srcMix.isConnected());
        showEndTest();
    }

    @Test
    public void testGetListEntities() throws Exception {
        showBeginTest();
        srcMax.connectToSource();
        srcMin.connectToSource();
        srcMix.connectToSource();
        assertTrue("not connected: max", srcMax.isConnected());
        assertTrue("not connected: min", srcMin.isConnected());
        assertTrue("not connected: mix", srcMix.isConnected());
        if (!Utils.isEmpty(srcMax.getErrors())) {
            System.out.println("ERRORS in MAX: ");
            for (String error : srcMax.getErrors()) {
                System.err.println(error);
            }
            fail("Error at connect");
        }

        List<Entity> entitiesMax = srcMax.getListEntities(null);
        List<Entity> entitiesMin = srcMin.getListEntities(null);
        List<Entity> entitiesMix = srcMix.getListEntities(null);
        assertNotNull("no entities in Max", entitiesMax);
        assertNotNull("no entities in Min", entitiesMin);
        assertNotNull("no entities in Mix", entitiesMix);

        assertEquals(subjects.length, entitiesMax.size());
        assertEquals(entitiesMax, entitiesMin);
        assertEquals(entitiesMin, entitiesMix);
        for (int i = 0; i < subjects.length; i++) {
            assertEquals("Invalid subject at index " + i, subjects[i], entitiesMax.get(i).getName());

            List<Entity> entitiesFiltered = srcMax.getListEntities(subjects[i]);
            assertEquals(1, entitiesFiltered.size());
            assertEquals(subjects[i], entitiesFiltered.get(0).getUid());
        }
        showEndTest();

    }

    @Test
    public void testLoadEntity() throws Exception {
        srcMax.connectToSource();
        srcMin.connectToSource();
        srcMix.connectToSource();
        
        Entity eMax = srcMax.loadEntity(subjects[0]);
        Entity eMix = srcMix.loadEntity(subjects[0]);
        Entity eMin = srcMin.loadEntity(subjects[0]);
        assertNotNull("Not Found in Max: '" + subjects[0] + "'", eMax);
        assertNotNull("Not Found in Mix: '" + subjects[0] + "'", eMix);
        assertNotNull("Not Found in Min: '" + subjects[0] + "'", eMin);

        assertEquals(subjects[0], eMax.getUid());
        assertEquals(subjects[0], eMix.getUid());
        assertEquals(subjects[0], eMin.getUid());
    }

    @Test
    public void testLoadIntoSequenceMax() throws Exception {
        testLoad(srcMax);
    }

    @Test
    public void testLoadIntoSequenceMix() throws Exception {
        testLoad(srcMix);
    }

    @Test
    public void testLoadIntoSequenceMin() throws Exception {
        testLoad(srcMin);
    }

    private void testLoad(SituationMLSource src) throws Exception {        
        //------
        src.connectToSource();
        Entity entity = src.loadEntity(subjects[0]);
        assertNotNull(entity);
        src.loadIntoSequence(entity.getSequence());
        //-----


        assertEquals(times.length, entity.getSequence().size());
        for (int i = 0; i < times.length; i++) {
            Context ctx = entity.getContext(i);
            assertEquals(times[i], ctx.getTime().asString("HH:mm"));
            assertEquals("Invalid situations size at " + times[i], 1, ctx.getSituations().size());
            assertEquals("Invalid situation.auxiliary at " + times[i], "aux", ctx.getSituations().get(0).getAuxiliary());
            assertEquals("Invalid situation.predicate at " + times[i], "pred", ctx.getSituations().get(0).getPredicate());
            assertEquals("Invalid situation.value at " + times[i], "obj", ctx.getSituations().get(0).getValue().getStringValue());
            assertEquals("Invalid location at " + times[i], locations[i], ctx.getLocation().getName());
            double[] position = ctx.getLocation().getPosition();
            assertNotNull("invalid position at " + times[i], position);
            assertEquals("invalid position at " + times[i], 3, position.length);
            assertEquals("invalid position at " + times[i], 10, position[0], 0);
            assertEquals("invalid position at " + times[i], 11, position[1], 0);
            assertEquals("invalid position at " + times[i], 12, position[2], 0);

        }
    }
}