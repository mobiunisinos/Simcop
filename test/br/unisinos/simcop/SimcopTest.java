/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unisinos.simcop;

import org.junit.Test;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.SequenceSimilarity;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.core.process.SimcopProcess;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.tests.BaseUnitTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class SimcopTest extends BaseUnitTest {

    public SimcopTest() {  }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private SequenceSimilarity createSS() {
        return new SequenceSimilarity() {
            @Override
            public boolean isContextSimilarityNeeded() {
                return false;
            }

            @Override
            protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
                SimilarityResult result = new SimilarityResult(s1,s2);
                for (int i = 0; i < s1.size(); i++) {
                   Context c1 = s1.get(i);
                   Context c2 = s2.get(i);
                   if (c1.getTime().equals(c2.getTime())) {
                       result.add(c1, c2, 1.0);
                   }
                }
                result.setCalculatedValue( (double)result.size() / s1.size());
                return result;
            }

            public Parameters getDefaultParameters() {
                return null;
            }

            public boolean isDistanceFunction() {
                return false;
            }

            @Override
            protected void internalValidateParameters() throws Exception {
                
            }
        };
    }

    private ContextSequence loadSequence(int entityUID) {
        ContextSequence s = new ContextSequence( new Entity(entityUID) );
        s.addContext( createContext(time(9, 0), null, null, null) );
        s.addContext( createContext(time(9, 1), null, null, null) );
        s.addContext( createContext(time(9, 2), null, null, null) );
        s.addContext( createContext(time(9, 3), null, null, null) );
        s.addContext( createContext(time(9, entityUID == 1 ? 4 : 37), null, null, null) );
        s.addContext( createContext(time(9, 5), null, null, null) );
        s.addContext( createContext(time(9, 6), null, null, null) );
        s.addContext( createContext(time(9, entityUID == 1 ? 7 : 48), null, null, null) );
        s.addContext( createContext(time(9, 8), null, null, null) );
        s.addContext( createContext(time(9, 9), null, null, null) );
        return s;
    }

    @Test
    public void testAnalyze_readProcess() {
        showBeginTest();
        SimcopProcess process = new SimcopProcess();
        process.setSequenceSimilarity( createSS() );
        Simcop instance = new Simcop(process);

        SimilarityResult result = null;
        try {
            ContextSequence s1 = loadSequence(1);
            ContextSequence s2 = loadSequence(2);
            result = instance.analyze(s1, s2);
        } catch (Exception ex) {
            Logger.getLogger(SimcopTest.class.getName()).log(Level.SEVERE, null, ex);
            failException(ex);
        }

        assertNotNull("result null", result);
        assertEquals("invalid value", 0.8, result.getCalculatedValue(), 0);        
        showEndTest();
    }

}