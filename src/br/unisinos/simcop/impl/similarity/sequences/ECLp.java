package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.EachContext;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Lp Distance Family to calculate the distance between two sequences.<br/>
 * Each context are considered as a coordinate in a n-dimensional space where n = max(seqA.size, seqB.size).</br>
 * The distances of each context/coordinate are calculated by the 
 * {@link br.unisinos.simcop.interfaces.similarity.IContextSimilarity Context Distance Function} choosed.<br/>
 *
 * @author tiago
 * @see br.unisinos.simcop.core.CommonFunctions#lpDistance(java.util.List, double)

 */
public class ECLp extends EachContext {
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
    private List<Double> differences;


    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_P, "2");
        return pars;
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        p = Double.parseDouble(getSimpleParameter(PAR_P).trim());
        if (p <= 0) {
            throw new IllegalArgumentException("Invalid value for " + PAR_P + " parameter: must be > 0 ");
        }
    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected IContextSelector createContextSelector() {
        if (contextSelector == null) {
            DefaultContextSelector result = new DefaultContextSelector();
            result.setAcceptAll(true);
            result.setAcceptGapsA(true);
            result.setAcceptGapsB(true);
            result.setMaxDistance(Double.MAX_VALUE);
            result.setMinSimilarity(Double.MIN_VALUE);
            return result;
        } else {
            return contextSelector;
        }
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        differences = new ArrayList<Double>();
        return super.internalGetSimilarity(s1, s2);
    }

    @Override
    protected void afterEvaluate(Context ctxA, Context ctxB, IContextSelector contextSelector, SimilarityResult result, double similarityOrDistance, boolean contextAccepted) {
        differences.add(similarityOrDistance);
    }

    @Override
    protected void setSequenceSimilarityValue(ContextSequence s1, ContextSequence s2, SimilarityResult result) {
        double lp = CommonFunctions.lpDistance(differences, p);
        result.setCalculatedValue(lp);
    }


}

