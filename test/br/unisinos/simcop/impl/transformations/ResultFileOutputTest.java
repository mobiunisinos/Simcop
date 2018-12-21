package br.unisinos.simcop.impl.transformations;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.core.ContextPair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.LocationDescription;
import java.io.IOException;
import java.io.Writer;
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
public class ResultFileOutputTest extends BaseUnitTest {

    private String type;

    public ResultFileOutputTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /* *********************************************************************************
     * ENTITY PART
     * *********************************************************************************/
    private Entity createTestEntity(String uid, String name) {
        Entity result = new Entity(uid, name);
        result.addAttribute("atr1", "Test Attribute 1", "testA");
        result.addAttribute("atr2", "Test Attribute 2", 128);
        try {
            result.addAttribute("atr3", "Test Attribute 3", new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2013"));
        } catch (ParseException ex) {
            Logger.getLogger(ResultFileOutputTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void expectEntity(List<String> expected, String uid, String name) {
        expected.add("      <entityUID>" + uid + "</entityUID>");
        expected.add("      <entityName>" + name + "</entityName>");
        expected.add("      <entityAttributes>");
        expected.add("         <attr uid='atr1' label='Test Attribute 1'>testA</attr>");
        expected.add("         <attr uid='atr2' label='Test Attribute 2'>128</attr>");
        expected.add("         <attr uid='atr3' label='Test Attribute 3'>2013-01-01 00:00:00.0</attr>");
        expected.add("      </entityAttributes>");
    }

    /* *********************************************************************************
     * TIME PART
     * *********************************************************************************/
    private TimeDescription getTimeDescription() {
        TimeDescription td = new TimeDescription();
        td.setHour(1);
        td.setMinute(23);
        td.setSecond(45);
        td.setMillisecond(128);
        td.setDay(4);
        td.setMonth(5);
        td.setYear(2013);
        return td;
    }

    private void expectTime(List<String> expected) {
        expected.add("         <time>");
        expected.add("            <day>4</day>");
        expected.add("            <month>5</month>");
        expected.add("            <year>2013</year>");
        expected.add("            <hour>1</hour>");
        expected.add("            <minute>23</minute>");
        expected.add("            <second>45</second>");
        expected.add("            <millisecond>128</millisecond>");
        expected.add("         </time>");
    }

    /* *********************************************************************************
     * LOCATION PART
     * *********************************************************************************/
    private LocationDescription getLocationDescription() {
        LocationDescription ld = new LocationDescription();
        ld.setUid(1);
        ld.setCategory("Home");
        ld.setName("Entity's Home");
        ld.setPosition(new double[]{-29.793357, -51.155398});
        ld.setIndex(1);
        ld.setOntologyClass("HOUSE");
        ld.setOntologyUID("BuildingsOntology");
        return ld;
    }

    private void expectLocation(List<String> expected) {
        expected.add("         <location>");
        expected.add("            <uid>1</uid>");
        expected.add("            <index>1</index>");
        expected.add("            <position>-29.793357,-51.155398</position>");
        expected.add("            <category>Home</category>");
        expected.add("            <name>Entity's Home</name>");
        expected.add("            <ontologyUID>BuildingsOntology</ontologyUID>");
        expected.add("            <ontologyClass>HOUSE</ontologyClass>");
        expected.add("         </location>");
    }

    /* *********************************************************************************
     * SITUATIONS PART
     * *********************************************************************************/
    private String[][] getSituationTest() {
        String[][] result = new String[5][];
        result[0] = new String[]{"predicateA", "valueA"};
        result[1] = new String[]{"predicateB", "valueB"};
        result[2] = new String[]{"predicateC", "valueC"};
        result[3] = new String[]{"predicateD", "valueD"};
        result[4] = new String[]{"predicateE", "valueE"};
        return result;
    }

    private void expectSituations(List<String> expected) {
        expected.add("         <situation predicate='predicateA'>valueA</situation>");
        expected.add("         <situation predicate='predicateB'>valueB</situation>");
        expected.add("         <situation predicate='predicateC'>valueC</situation>");
        expected.add("         <situation predicate='predicateD'>valueD</situation>");
        expected.add("         <situation predicate='predicateE'>valueE</situation>");
    }

    /* *********************************************************************************
     * CONTEXT PART
     * *********************************************************************************/
    private Context createContext(ContextSequence seq) {
        Context ctx = new Context();
        ctx.setTime(getTimeDescription());
        ctx.setLocation(getLocationDescription());
        for (String[] situation : getSituationTest()) {
            ctx.addSituation(situation[0], situation[1]);
        }
        ctx.setSequence(seq);
        seq.addContext(ctx);
        ctx.setIndex(seq.size() * 10);
        return ctx;
    }

    private void expectContext(List<String> expected, int index, int sequenceIndex) {
        expected.add("      <context" + index + " sequenceIndex='" + (sequenceIndex * 10) + "'>");
        expectTime(expected);
        expectLocation(expected);
        expectSituations(expected);
        expected.add("      </context" + index + ">");
    }

    /* *********************************************************************************
     * SEQUENCE PART
     * *********************************************************************************/
    private ContextSequence createSequence(String uid, String name) {
        ContextSequence result = new ContextSequence(createTestEntity(uid, name));
        result.setNotes("this is a test sequence!");
        return result;
    }

    private void expectSequence(List<String> expected, String seq, String uid, String name) {
        expected.add("   <sequence" + seq + ">");
        expectEntity(expected, uid, name);
        expected.add("      <sequenceSize>5</sequenceSize>");
        expected.add("      <notes><![CDATA[this is a test sequence!]]></notes>");
        expected.add("   </sequence" + seq + ">");
    }


    /* *********************************************************************************
     * CONTEXT-PAIR PART
     * *********************************************************************************/
    private SimilarityResult createTestData() {
        SimilarityResult result = new SimilarityResult(createSequence("001", "Entity A"), createSequence("002", "Entity B"));
        result.setCalculatedValue(0.512);

        for (int i = 0; i <= 4; i++) {
            Context ctx1 = createContext(result.getS1());
            Context ctx2 = createContext(result.getS2());
            double similarity = (i == 0 ? 0 : 1.0 / i);

            ContextPair cp = new ContextPair(i, ctx1, ctx2, similarity, "** additional information **");
            result.getContextPairs().add(cp);
        }
        return result;
    }

    private void expectPairs(List<String> expected) {
        for (int index = 0; index <= 4; index++) {
            double similarity = (index == 0 ? 0 : 1.0 / index);
            expected.add("   <contextPair index='" + index + "'>");
            expected.add("      <similarity>" + similarity + "</similarity>");
            expected.add("      <additionalInfo><![CDATA[** additional information **]]></additionalInfo>");
            expectContext(expected, 1, index+1);
            expectContext(expected, 2, index+1);
            expected.add("   </contextPair>");
        }
    }


    /* *********************************************************************************
     * TESTS
     * *********************************************************************************/
    @Test
    public void testSaveToCSV() throws Exception {
        showBeginTest();

        //----
        List<String> expected = new ArrayList<String>();
        expect(expected, ResultFileOutput.TYPE_CSV);

        //----
        List<String> result = new ArrayList<String>();
        SimilarityResult sr = createTestData();
        WriteToResult out = new WriteToResult(result);
        out.close();
        //----
        ResultFileOutput instance = new ResultFileOutput();
        instance.saveToCSV(sr, out);
        //----

        DecimalFormat df = new DecimalFormat("000");
        for (int iLine = 0; iLine < result.size(); iLine++) {
            System.out.print("   " + df.format(iLine + 1) + ": " + result.get(iLine));
        }

        //TODO contentTest(expected, result);
        showEndTest();
    }

    @Test
    public void testSaveToXML() throws Exception {
        showBeginTest();

        //----
        List<String> expected = new ArrayList<String>();
        expected.add("<?xml version='1.0' encoding='utf-8'?>");
        expected.add("<similarityResult>");
        expect(expected, ResultFileOutput.TYPE_XML);
        expected.add("</similarityResult>");

        //----
        List<String> result = new ArrayList<String>();
        SimilarityResult sr = createTestData();
        WriteToResult out = new WriteToResult(result);

        //----
        ResultFileOutput instance = new ResultFileOutput();
        instance.saveToXML(sr, out);
        out.close();
        //----

        contentTest(expected, result);
        showEndTest();
    }

    private void expect(List<String> expected, String type) {
        this.type = type;
        if (ResultFileOutput.TYPE_XML.equals(type)) {
            expected.add("   <similarityValue>0.512</similarityValue>");
            expectSequence(expected, "A", "001", "Entity A");
            expectSequence(expected, "B", "002", "Entity B");
            expectPairs(expected);
        } else {
            expected.add("similarityValue: 0.512");
            //TODO list pairs
        }
    }

    private void contentTest(List<String> expected, List<String> result) {
        assertNotNull(expected);
        assertNotNull(result);
        DecimalFormat df = new DecimalFormat("000");
        for (int iLine = 0; iLine < result.size(); iLine++) {
            System.out.print("   " + df.format(iLine + 1) + ": " + result.get(iLine));
            String exp = expected.get(iLine);
            exp = exp != null ? exp.trim() : exp;
            String rsl = result.get(iLine);
            rsl = rsl != null ? rsl.trim() : rsl;

            assertEquals("Line [" + df.format(iLine + 1) + "]: ", exp, rsl);
        }
    }

    class WriteToResult extends Writer {

        private List<String> result;
        private StringBuilder buffer;

        public WriteToResult(List<String> result) {
            this.result = result;
            this.buffer = new StringBuilder();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {            
            for (int i = off; i < len; i++) {
                buffer.append(cbuf[i]);
                if (cbuf[i] == '\n') {
                    result.add(buffer.toString());
                    buffer = new StringBuilder();
                }
            }
        }



        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }

        public List<String> getResult() {
            return result;
        }
    }
}
