package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;
import java.util.List;

/**
 * Implements the special case of Lp Distance Family, where p goes to infinite, for calculate the distance between two contexts.<br/>
 * The value of attributes must be numeric and comparable ie, the ratio between both attributes must have meaning.
 *
 * @author tiago
 * @see br.unisinos.simcop.core.CommonFunctions#chebyshevDistance(java.util.List)
 */
public class ChebyshevDistance extends ContextSimilarity {

    public Parameters getDefaultParameters() {
        return new Parameters();
    }

    public boolean isDistanceFunction() {
        return true;
    }

    public boolean isAttributeSimilarityNeeded() {
        return true;
    }

    public double getLargestPossibleValue() {
        return Double.POSITIVE_INFINITY;
    }

    protected void internalValidateParameters() throws Exception {}

    @Override
    protected double internalGetSimilarity(Context c1, Context c2) throws Exception {
        List<Double> distances = getAttributesSimilarities(c1, c2);
        if (distances != null) {
            return CommonFunctions.chebyshevDistance(distances);
        } else {
            return 0;
        }
    }


}

