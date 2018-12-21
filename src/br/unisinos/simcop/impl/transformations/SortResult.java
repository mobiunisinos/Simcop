package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author tiago
 */
public class SortResult extends AbstractOutputTransformation {

    public static final String SORT_CALCULATED_VALUE = "calculatedValue";
    public static final String SORT_C1_INDEX = "c1Index";
    public static final String SORT_C2_INDEX = "c2Index";

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter("sortBy", SORT_CALCULATED_VALUE);
        return pars;
    }

    public SimilarityResult transform(SimilarityResult similarityResult) {
        String sortBy = getSimpleParameter("sortBy");
        Comparator comparator;
        if (SORT_C1_INDEX.equalsIgnoreCase(sortBy)) {
            comparator = new CompareCSquence(1);
        } else if (SORT_C2_INDEX.equalsIgnoreCase(sortBy)) {
            comparator = new CompareCSquence(2);
        } else {
            comparator = new CompareValue();
        }

        Collections.sort( similarityResult.getContextPairs(), comparator );

        return similarityResult;
    }

    private class CompareValue implements Comparator {

        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            if (o1 instanceof ContextPair && o2 instanceof ContextPair) {
                ContextPair cpA = (ContextPair) o1;
                ContextPair cpB = (ContextPair) o2;

                double value1 = cpA.getCalculatedValue() != null ? cpA.getCalculatedValue() : 0;
                double value2 = cpB.getCalculatedValue() != null ? cpB.getCalculatedValue() : 0;

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


    private class CompareCSquence implements Comparator {
        private int context;

        public CompareCSquence(int context) {
            this.context = context;
        }


        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            if (o1 instanceof ContextPair && o2 instanceof ContextPair) {
                ContextPair cpA = (ContextPair) o1;
                ContextPair cpB = (ContextPair) o2;

                long seq1, seq2;
                if (context == 1) {
                    seq1 = cpA.getC1() != null ? cpA.getC1().getIndex() : 0;
                    seq2 = cpB.getC1() != null ? cpB.getC1().getIndex() : 0;
                } else {
                    seq1 = cpA.getC2() != null ? cpA.getC2().getIndex() : 0;
                    seq2 = cpB.getC2() != null ? cpB.getC2().getIndex() : 0;
                }
                return (int) (seq1 - seq2);
            } else {
                return 0;
            }

        }
    }
}
