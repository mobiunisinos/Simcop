/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unisinos.simcop.core.config;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.core.process.SimcopProcess;
import br.unisinos.simcop.core.process.SimcopTask;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.contexts.WeightAttributes;
import br.unisinos.simcop.impl.similarity.sequences.ECDefault;
import br.unisinos.simcop.interfaces.filters.IFilterInput;
import br.unisinos.simcop.interfaces.filters.IFilterOutput;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.transformations.ITransformInput;
import br.unisinos.simcop.interfaces.transformations.ITransformOutput;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.io.File;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author tiago
 */
public class SimcopConfigXMLTest extends BaseUnitTest {

    public SimcopConfigXMLTest() { }

    private static File xmlFile;
    private static SimcopConfigXML instance;

    @BeforeClass
    public static void setUpClass() throws Exception {
        xmlFile = new File("/tmp/simcop.xml");
        instance = new SimcopConfigXML(xmlFile);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //Runtime.getRuntime().exec("exo-open " + xmlFile.getAbsolutePath());
    }

    @Test
    public void testCreateDocument() throws Exception {
        showBeginTest();
        Document result = instance.createDocument();
        assertNotNull("document not found", result);
        NodeList nl = result.getChildNodes();
        assertNotNull(nl);
        assertEquals(1, nl.getLength());
        assertEquals(SimcopConfigXML.TAG_ROOT, nl.item(0).getNodeName());
        Element root = instance.getRootTag(result);
        assertNotNull("root tag not found", root);
        showEndTest();
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        showBeginTest();
        SimcopProcess savedProcess = new SimcopProcess();
        savedProcess.addInputFilter(createInFilter());
        savedProcess.addInputTransformation(createInTransform());
        savedProcess.setSequenceSimilarity(createISimilarity());
        savedProcess.addOutputFilter(createOutFilter());
        savedProcess.addOutputTransformation(createOutTransform());
        //TODO ontologias e sources

        instance.loadFromProcess(savedProcess);
        instance.save();
        show("SAVE OK");
        instance = null;
        instance = new SimcopConfigXML(xmlFile);
        instance.load();

        //TEST
        assertEquals("Tasks Size", 5, instance.getProcessTasks().size());
        SimcopProcess loadedProcess = instance.createProcess();
        assertNotNull(loadedProcess);
        List<SimcopTask> savedTasks = savedProcess.getTasks();
        List<SimcopTask> loadedTasks = loadedProcess.getTasks();
        assertEquals("Process Tasks Size", savedTasks.size(), loadedTasks.size());
        for (int itsk = 0; itsk < savedTasks.size(); itsk++) {
            SimcopTask savedTask = savedTasks.get(itsk);
            SimcopTask loadedTask = loadedTasks.get(itsk);
            assertEquals("Parameters Not Equal: " + savedTask.getTypeName(), savedTask.getParameters(), loadedTask.getParameters());
            assertEquals("Not Equal: " + savedTask.getTypeName(), savedTask, loadedTask);
        }
      
        assertEquals(savedProcess, loadedProcess);



        showEndTest();
    }

    private IFilterInput createInFilter() {
       InFilter result = new InFilter();
       result.setParameter("maxDate", "2013-09-27", "yyyy-MM-dd");
       result.setParameter("minDate", "2013-08-27", "yyyy-MM-dd");
       return result;
    }

    private ITransformInput createInTransform() {
       InTransform result = new InTransform();
       result.setParameter("alignmentType", "date");
       return result;
    }
    private ISequenceSimilarity createISimilarity() {
        Similarity result = new Similarity();
        result.setParameter(WeightAttributes.SITUATION_WEIGHT_PREFIX + "?source={null}&aux={null}&predicate=sample", "1");
        return result;
    }
    private ITransformOutput createOutTransform() {
       OutTransform result = new OutTransform();
       result.setParameter("logFile", "./log/simcop.log");
       return result;
    }
    private IFilterOutput createOutFilter() {
       OutFilter result = new OutFilter();
       result.setParameter("ranking", "10", "###,##0");
       return result;
    }




    /* *************************************************************************
     * MOCK CLASS
     * *************************************************************************/
    class BaseSimcop implements ISimcopClass{
        protected Parameters parameters;

        public boolean hasParameters() {
            return parameters != null && !parameters.isEmpty();
        }

        public Parameter setParameter(String key, String value) {
            return setParameter(key, value, null);
        }

        public Parameter setParameter(String key, String value, String pattern) {
            Parameter p = getParameters().addParameter(key, value, pattern);
            return p;
        }

        public Parameters getParameters() {
            if (parameters == null) {
                parameters = new Parameters();
            }
            return parameters;
        }

        public void setParameters(Parameters parameters) {
            this.parameters = parameters;
        }

        public Parameters getDefaultParameters() {
            return null;
        }

        public Parameter getParameter(String key) {
          return getParameters().get(key);
        }

        public String getSimpleParameter(String key) {
            Parameter p = getParameter(key);
            if (p != null) {
                return p.getValue();
            } else {
                return null;
            }
        }

    }

    class InFilter extends BaseSimcop implements IFilterInput {

        public boolean[] accept(int globalIndex,  Context ctx1, Context ctx2) {
            return new boolean[] {true, true};
        }
    }

    class InTransform extends BaseSimcop implements ITransformInput {

        public ContextSequence[] transform(ContextSequence contextSequence1, ContextSequence contextSequence2) {
            return new ContextSequence[] {contextSequence1, contextSequence2};
        }
    }

    class Similarity extends BaseSimcop implements ISequenceSimilarity {
        private IContextSelector selector;


        public SimilarityResult getSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
            return new SimilarityResult(s1,s2);
        }
                
        public void setContextSimilarity(IContextSimilarity contextSimilarity) {}

        public boolean isContextSimilarityNeeded() {
            return true;
        }

        public void setContextSelector(IContextSelector contextSelector) {
            this.selector = contextSelector;
        }

        public boolean isDistanceFunction() {
            return true;
        }

        public void validateParameters() throws Exception { }

        public IContextSimilarity getContextSimilarity() {
            return null;
        }

    }

    class OutFilter extends BaseSimcop implements IFilterOutput {

        public boolean accept(SimilarityResult result, ContextPair resultPair, int pairIndex) {
            return true;
        }
    }

    class OutTransform extends BaseSimcop implements ITransformOutput {

        public SimilarityResult transform(SimilarityResult similarityResult) {
            return similarityResult;
        }

    }
}