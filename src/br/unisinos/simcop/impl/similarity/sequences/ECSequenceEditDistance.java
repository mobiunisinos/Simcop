package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.EachContext;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;

/**
 * This class compares each pair of contexts in sequences using a Context
 * Similarity (or distance) function.<br/>
 *
 * As a result of the comparison of each pair, two strings are builded,
 * representing each of the sequences. The pairs who have reached the minimum
 * similarity (or maximum distance) are represented with the same letter in the
 * strings. Finally, the LevenshteinDistance technique is applied over the
 * strings, in order to calculating the minimum number of operations required to
 * transform one string into another.
 *
 * @author tiago
 */
public class ECSequenceEditDistance extends EachContext {

    public static final String MAX_DISTANCE = "maxDistance";
    public static final String MIN_SIMILARITY = "minSimilarity";
    private static final char EQUIVALENT_CONTEXT = '0';
    private static final char NOT_EQUIVALENT_CTX1 = '1';
    private static final char NOT_EQUIVALENT_CTX2 = '2';
    private static final char GAP = '_';
    private double maxDistance;
    private double minSimilarity;
    private StringBuilder sSequenceA;
    private StringBuilder sSequenceB;

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

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        //intialize compare strings
        sSequenceA = new StringBuilder();
        sSequenceB = new StringBuilder();
        if (contextSimilarity != null) {
            contextSimilarity.validateParameters();
        }


        //initialize result
        SimilarityResult result = new SimilarityResult(s1, s2);
        if (s1 != null && s2 != null) {
            //initialize context selector
            IContextSelector selector = createContextSelector();

            //iterates through the sequences
            for (int index = 0; index < Math.max(s1.size(), s2.size()); index++) {

                //checks if the end of sequences has been reached
                boolean endOfS1 = index >= s1.size();
                boolean endOfS2 = index >= s2.size();

                //get contexts at current position
                Context ctxA = endOfS1 ? null : s1.get(index);
                Context ctxB = endOfS2 ? null : s2.get(index);

                if (endOfS1) {
                    //end of sequence 1 reached.
                    sSequenceB.append(ctxB == null ? GAP : NOT_EQUIVALENT_CTX2);
                } else if (endOfS2) {
                    //end of sequence 2 reached. 
                    sSequenceA.append(ctxA == null ? GAP : NOT_EQUIVALENT_CTX1);
                } else {

                    //check for gaps in sequences
                    boolean gapAdded = false;
                    if (ctxA == null) {
                        sSequenceA.append(GAP);
                        gapAdded = true;
                    }
                    if (ctxB == null) {
                        sSequenceB.append(GAP);
                        if (ctxA != null) {
                            sSequenceA.append(NOT_EQUIVALENT_CTX1);
                        }
                        gapAdded = true;
                    }

                    if (!gapAdded) {
                        //calculates the similarity/distance between the two contexts
                        double similarityOrDistance = contextSimilarity.getSimilarity(ctxA, ctxB);

                        //check for minimum similarity or maximimum distance
                        if (selector.isSelectContext(similarityOrDistance, contextSimilarity, ctxA, ctxB)) {
                            createPair(ctxA, ctxB, similarityOrDistance, result);
                            sSequenceA.append(EQUIVALENT_CONTEXT);
                            sSequenceB.append(EQUIVALENT_CONTEXT);
                        } else {
                            sSequenceA.append(NOT_EQUIVALENT_CTX1);
                            sSequenceB.append(NOT_EQUIVALENT_CTX2);
                        }
                    }
                }
            }

            setSequenceSimilarityValue(s1, s2, result);
        }
        return result;
    }

    @Override
    protected IContextSelector createContextSelector() {
        if (contextSelector == null) {
            DefaultContextSelector result = new DefaultContextSelector();
            result.setAcceptAll(false);
            result.setAcceptGapsA(true);
            result.setAcceptGapsB(true);
            result.setMaxDistance(maxDistance);
            result.setMinSimilarity(minSimilarity);
            return result;
        } else {
            return contextSelector;
        }
    }

    @Override
    protected void afterEvaluate(Context ctxA, Context ctxB,
            IContextSelector contextSelector, SimilarityResult result,
            double similarityOrDistance,
            boolean contextAccepted) {
    }

    @Override
    protected void setSequenceSimilarityValue(ContextSequence s1, ContextSequence s2,
            SimilarityResult result) {
        double distance;
        if (sSequenceA != null && sSequenceB != null) {
            distance = CommonFunctions.levenshteinDistance(sSequenceA.toString(), sSequenceB.toString());
        } else {
            distance = 0;
        }
        result.setCalculatedValue(distance);
    }

    public String[] getCompareStrings() {
        String[] result = new String[2];
        result[0] = sSequenceA != null ? sSequenceA.toString() : null;
        result[1] = sSequenceB != null ? sSequenceB.toString() : null;
        return result;
    }
}
