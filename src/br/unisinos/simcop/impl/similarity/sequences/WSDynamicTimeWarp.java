package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.WholeSequences;
import br.unisinos.simcop.impl.similarity.thirdParty.adapters.FastDTWAdapter;

/**
 * Uses the FastDTW to calculate the distance between two sequences<br/>
 *
 * REFERENCES:
 * FastDTW: Toward Accurate Dynamic Time Warping in Linear Time and Space. 
 * Stan Salvador & Philip Chan. KDD Workshop on Mining Temporal and Sequential Data,
 * pp. 70-80, 2004. (http://cs.fit.edu/~pkc/papers/tdm04.pdf)
 *
 * @author tiago
 *
 * @see https://code.google.com/p/fastdtw/
 */
public class WSDynamicTimeWarp extends WholeSequences {

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(FastDTWAdapter.PAR_SEARCH_RADIUS, "1");
        return pars;
    }

    @Override
    protected void internalValidateParameters() throws Exception {}

    @Override
    public boolean isContextSimilarityNeeded() {
        return true;
    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        FastDTWAdapter adapter = new FastDTWAdapter();
        return adapter.getSimilarityFor(s1, s2, this);
    }
}
