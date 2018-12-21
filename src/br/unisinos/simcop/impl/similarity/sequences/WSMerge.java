package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.WholeSequences;
import java.util.HashSet;
import java.util.Set;

/**
 * Compares all contexts in Sequence A with all contexts in sequence B and
 * selects those who haves reached minimum similarity or maximum distance
 *
 * @author tiago
 */
public class WSMerge extends WholeSequences {

    public static final String MAX_DISTANCE = "maxDistance";
    public static final String MIN_SIMILARITY = "minSimilarity";
    private double maxDistance;
    private double minSimilarity;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(MAX_DISTANCE, "1");
        pars.addParameter(MIN_SIMILARITY, "0.01");
        return pars;
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        maxDistance = Double.parseDouble(getSimpleParameter(MAX_DISTANCE).trim());
        minSimilarity = Double.parseDouble(getSimpleParameter(MIN_SIMILARITY).trim());
    }

    public boolean isContextSimilarityNeeded() {
        return true;
    }

    public boolean isDistanceFunction() {
        if (contextSimilarity == null) {
            return false;
        } else {
            return contextSimilarity.isDistanceFunction();
        }
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        SimilarityResult result = new SimilarityResult(s1, s2);
        if (s1 != null && !s1.isEmpty() && s2 != null && !s2.isEmpty()) {
            if (contextSimilarity != null) {
                contextSimilarity.validateParameters();
            }

            boolean isDistance = isDistanceFunction();
            Set<Context> ctxCache = new HashSet<Context>();
            for (Context ctxA : s1.getContexts()) {
                if (ctxA != null) {
                    ctxCache.add(ctxA);
                    for (Context ctxB : s2.getContexts()) {
                        if (ctxB != null) {
                            ctxCache.add(ctxB);
                            double sdValue = contextSimilarity.getSimilarity(ctxA, ctxB);
                            if (isDistance) {
                                if (sdValue <= maxDistance) {
                                    result.add(ctxA, ctxB, sdValue);
                                }
                            } else {
                                if (sdValue >= minSimilarity) {
                                    result.add(ctxA, ctxB, sdValue);
                                }
                            }
                        }
                    }
                }
            }
            if (ctxCache.size() > 0) {
                result.setCalculatedValue((double) result.size() / ctxCache.size());
            } else {
                result.setCalculatedValue(new Double(0));
            }
        }
        return result;
    }
}
