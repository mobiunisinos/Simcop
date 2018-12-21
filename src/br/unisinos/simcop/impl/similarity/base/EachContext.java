package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;   
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;

public abstract class EachContext extends SequenceSimilarity {

    public boolean isContextSimilarityNeeded() {
        return true;
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        SimilarityResult result = new SimilarityResult(s1,s2);
        if (s1 != null && s2 != null) {
            IContextSelector ctxSelector = createContextSelector();
            if (contextSimilarity != null) {
               contextSimilarity.validateParameters();
            }
            for (int index = 0; index < Math.max(s1.size(), s2.size()); index++) {
                Context ctxA = index < s1.size() ? s1.get(index) : null;
                Context ctxB = index < s2.size() ? s2.get(index) : null;
                evaluate(ctxA, ctxB, ctxSelector, result);
            }
            setSequenceSimilarityValue(s1, s2, result);
        }
        return result;
    }

    protected void evaluate(Context ctxA, Context ctxB, IContextSelector contextSelector, SimilarityResult result) throws Exception {
        double similarityOrDistance = contextSimilarity.getSimilarity(ctxA, ctxB);

        boolean accepted;
        if (contextSelector.isSelectContext(similarityOrDistance, contextSimilarity, ctxA, ctxB)) {
            createPair(ctxA, ctxB, similarityOrDistance, result);
            accepted = true;
        } else {
            accepted = false;
        }
        afterEvaluate(ctxA, ctxB, contextSelector, result, similarityOrDistance, accepted);
    }

    protected void createPair(Context ctxA, Context ctxB, double similarityOrDistance, SimilarityResult result) {
        result.add(ctxA, ctxB, similarityOrDistance);
    }

    protected void afterEvaluate(Context ctxA, 
                                   Context ctxB, 
                                   IContextSelector contextSelector, 
                                   SimilarityResult result, 
                                   double similarityOrDistance, 
                                   boolean contextAccepted) {
    }

    protected abstract void setSequenceSimilarityValue(ContextSequence s1, 
                                                          ContextSequence s2,
                                                          SimilarityResult result);

    protected abstract IContextSelector createContextSelector();
}
