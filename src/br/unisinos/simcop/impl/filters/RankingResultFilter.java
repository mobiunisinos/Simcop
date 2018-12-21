package br.unisinos.simcop.impl.filters;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;

/**
 *
 * @author tiago
 */
public class RankingResultFilter extends AbstractOutputFilter {
    public static final String FIRST_PAIRS = "firstPairs";
    private Integer firstPairs;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(FIRST_PAIRS, "10");
        return pars;
    }

    public boolean accept(SimilarityResult result, ContextPair resultPair, int pairIndex) {
        if (firstPairs == null)  {
            firstPairs = Integer.parseInt(getSimpleParameter(FIRST_PAIRS));
        }
        return pairIndex < firstPairs;
    }

}
