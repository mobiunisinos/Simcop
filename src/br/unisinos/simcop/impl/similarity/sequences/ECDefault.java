package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.impl.similarity.base.EachContext;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;

/**
 * Compares contexts in sequences A and B, following respectives indexes
 * and returning pairs {Ca, Cb} of contexts that have reached the minimum
 * similarity value, calculated by the object {@link #contextSimilarity}.
 * @author tiago
 */
public class ECDefault extends EachContext {

    public static final String ACCEPT_ALL = "acceptAll";
    public static final String MAX_DISTANCE = "maxDistance";
    public static final String MIN_SIMILARITY = "minSimilarity";
    public static final String ACCEPT_GAPS_SEQUENCE_A = "acceptGapsSequenceA";
    public static final String ACCEPT_GAPS_SEQUENCE_B = "acceptGapsSequenceB";
    private boolean acceptAll;
    private boolean acceptGapsA;
    private boolean acceptGapsB;
    private double maxDistance;
    private double minSimilarity;
    private double similaritySum;

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        similaritySum = 0;
        return super.internalGetSimilarity(s1, s2);
    }

    @Override
    protected void afterEvaluate(Context ctxA, Context ctxB, IContextSelector contextSelector, SimilarityResult result, double similarityOrDistance, boolean contextAccepted) {
        similaritySum += similarityOrDistance;
    }

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(ACCEPT_ALL, "false");
        pars.addParameter(MAX_DISTANCE, "1");
        pars.addParameter(MIN_SIMILARITY, "0.01");
        pars.addParameter(ACCEPT_GAPS_SEQUENCE_A, "false");
        pars.addParameter(ACCEPT_GAPS_SEQUENCE_B, "false");
        return pars;
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        acceptAll = "true".equalsIgnoreCase(getSimpleParameter(ACCEPT_ALL));
        acceptGapsA = "true".equalsIgnoreCase(getSimpleParameter(ACCEPT_GAPS_SEQUENCE_A));
        acceptGapsB = "true".equalsIgnoreCase(getSimpleParameter(ACCEPT_GAPS_SEQUENCE_B));
        maxDistance = Double.parseDouble(getSimpleParameter(MAX_DISTANCE).trim());
        minSimilarity = Double.parseDouble(getSimpleParameter(MIN_SIMILARITY).trim());
    }

    @Override
    protected IContextSelector createContextSelector() {
        if (contextSelector == null) {
            DefaultContextSelector result = new DefaultContextSelector();
            result.setAcceptAll(acceptAll);
            result.setAcceptGapsA(acceptGapsA);
            result.setAcceptGapsB(acceptGapsB);
            result.setMaxDistance(maxDistance);
            result.setMinSimilarity(minSimilarity);
            return result;
        } else {
            return contextSelector;
        }
    }

    @Override
    protected void setSequenceSimilarityValue(ContextSequence s1, ContextSequence s2, SimilarityResult result) {
        double maxCtx = getContextSimilarity().getLargestPossibleValue();
        double maxSimilarity = Math.max(s1.size() * maxCtx, s2.size() * maxCtx);
        if (maxSimilarity != 0) {
            result.setCalculatedValue(similaritySum / maxSimilarity);
        } else {
            result.setCalculatedValue(similaritySum);
        }
    }

    public boolean isDistanceFunction() {
        if (contextSimilarity == null) {
            return false;
        } else {
            return contextSimilarity.isDistanceFunction();
        }
    }
}
