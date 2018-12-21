package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WholeSequences extends SequenceSimilarity {

    protected Double[][] buildMatrix(ContextSequence s1, ContextSequence s2, ContextSimilarity similarityFunction) throws Exception {
        Double[][] result = new Double[s1.size()][s2.size()];
        similarityFunction.validateParameters();
        
        for (int i = 0; i < s1.size(); i++) {
            for (int j = 0; j < s2.size(); j++) {
                Context c1 = s1.get(i);
                Context c2 = s2.get(j);
                result[i][j] = similarityFunction.getSimilarity(c1, c2);
            }
        }
        return result;
    }

    protected Boolean[][] buildEqualsMatrix(ContextSequence s1, ContextSequence s2, ContextSimilarity similarityFunction) {
        Boolean[][] result = new Boolean[s1.size()][s2.size()];
        try {
            similarityFunction.validateParameters();
        } catch (Exception ex) {
            Utils.log(ex);
        }
        
        for (int i = 0; i < s1.size(); i++) {
            for (int j = 0; j < s2.size(); j++) {
                Context c1 = s1.get(i);
                Context c2 = s2.get(j);
                result[i][j] = c1 == null ? c2 == null : c1.equals(c2);
            }
        }
        return result;
    }


}
 
