package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorts the pairs in the result by the probability of ocurrence of each context.
 *
 * @see br.unisinos.simcop.impl.similarity.sequences.wholeSeq.WSContextProbability
 * @author tiago
 */
public class SortResultProbability extends AbstractOutputTransformation {

    public static final String SORT_SEQUENCE = "sortSequence";

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(SORT_SEQUENCE, "1");
        return pars;
    }

    public SimilarityResult transform(SimilarityResult similarityResult) {

        Comparator comparator = new CompareProbability( getSimpleParameter(SORT_SEQUENCE) );
        Collections.sort( similarityResult.getContextPairs(), comparator );

        return similarityResult;
    }

    private class CompareProbability implements Comparator {
        private String sequence;

        public CompareProbability(String sequence) {
            this.sequence = sequence;
        }

        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            if (o1 instanceof ContextPair && o2 instanceof ContextPair) {
                ContextPair pair1 = (ContextPair) o1;
                ContextPair pair2 = (ContextPair) o2;

                double value1;
                double value2;
                if ("2".equalsIgnoreCase(sequence)) {
                    value1 = pair1.getC2().getProbability();
                    value2 = pair2.getC2().getProbability();
                } else {
                    value1 = pair1.getC1().getProbability();
                    value2 = pair2.getC1().getProbability();
                }


                if (value1 > value2) {
                    return 1;
                } else if (value1 < value2) {
                    return -1;
                } else {
                    return 0;
                }

            } else {
                return 0;
            }

        }
    }


}
