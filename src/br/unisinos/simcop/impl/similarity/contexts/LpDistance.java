package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;
import java.util.List;

/**
 * Implements the Lp Distance Family to calculate the distance between two contexts.<br/>
 * The value of attributes must be numeric and comparable ie, the ratio between both attributes must have meaning.
 *
 * @author tiago
 * @see br.unisinos.simcop.core.CommonFunctions#lpDistance(java.util.List, double) 
 */
public class LpDistance extends ContextSimilarity {
    /**
     * Value of <i>p</i>: <br/>
     * 1 = City Block, or manhatan distance    <br/>
     * 2 = Euclidian distance                  <br/>
     * &gt;2 = Minkowsky distance                 <br/>
     * 0 &lt; p &lt; 1 = Fractional Lp  <br/>
     *<br/>
     * REFERENCES:<br/>
     * CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
     * Density Functions. International Journal of Mathematical Models and Methods in
    * Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     *
     */
    public static final String PAR_P = "p";

    private double p;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_P, "2");
        return pars;
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

    protected void internalValidateParameters() throws Exception {
        p = Double.parseDouble(getSimpleParameter(PAR_P).trim());
        if (p <= 0) {
            throw new IllegalArgumentException("Invalid value for " + PAR_P + " parameter: must be > 0 ");
        }        
    }

    @Override
    protected double internalGetSimilarity(Context c1, Context c2) throws Exception {
        List<Double> distances = getAttributesSimilarities(c1, c2);
        if (distances != null) {
            return CommonFunctions.lpDistance(distances, p);
        } else {
            return 0;
        }
    }


}
