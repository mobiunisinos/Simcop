package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;

/**
 * Decides if a pair of contexts will be included in the SimilarityResult.
 *
 * @see br.unisinos.simcop.impl.similarity.base.EachContext
 * @author tiago
 */
public class DefaultContextSelector implements IContextSelector {
    private boolean acceptAll;
    private double minSimilarity;
    private double maxDistance;
    private boolean acceptGapsA;
    private boolean acceptGapsB;



    public boolean isSelectContext(double value, IContextSimilarity similarityFunction, Context c1, Context c2) {
        if (acceptAll) {
            return true;
        } else {
            boolean result = true;
            if (c1 == null) {
                result = result && acceptGapsA;
            }
            if (c2 == null) {
                result = result && acceptGapsB;
            }
            if (similarityFunction.isDistanceFunction()) {
                result = result && value <= maxDistance;
            } else {
                result = result && value >= minSimilarity;
            }
            return result;

        }
    }

    public boolean isAcceptAll() {
        return acceptAll;
    }

    public void setAcceptAll(boolean acceptAll) {
        this.acceptAll = acceptAll;
    }

    public double getMinSimilarity() {
        return minSimilarity;
    }

    public void setMinSimilarity(double minSimilarity) {
        this.minSimilarity = minSimilarity;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isAcceptGapsA() {
        return acceptGapsA;
    }

    public void setAcceptGapsA(boolean acceptGapsA) {
        this.acceptGapsA = acceptGapsA;
    }

    public boolean isAcceptGapsB() {
        return acceptGapsB;
    }

    public void setAcceptGapsB(boolean acceptGapsB) {
        this.acceptGapsB = acceptGapsB;
    }


}
