package br.unisinos.simcop.impl.similarity.sequences;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.WholeSequences;
import java.util.HashMap;
import java.util.Map;

/**
 * Measures the similarity between two contexts sequences by calculate the
 * probability of ocorrence of each context <b>Ca</b> in sequence <b>A</b>.<br/>
 * Then the class locates contexts <b>Cb</b> in sequence <b>B</b> with same
 * atributes (<i>time</i>, <i>location</i> and <i>situations</i>
 * descriptors).<br/>
 * Finally pairs of <b>Ca</b> and <b>Cb</b> are selected where <tt><i> P(Cb)
 * &gt;= P(Ca) and the global similarity value are the number of pairs selected
 * divides by the count of uniques contexts
 *
 * @see br.unisinos.simcop.impl.transformations.SortResultProbability
 * @author tiago
 */
public class WSContextProbability extends WholeSequences {

    public static final String GROUP_TIME = "groupTime";
    public static final String GROUP_LOCATION = "groupLocation";
    public static final String GROUP_SITUATION = "groupSituation";
    public static final String GROUP_EXTENDED_DATA = "groupExtendedData";
    private boolean groupTime = true;
    private boolean groupLocation = true;
    private boolean groupSituation = true;
    private boolean groupExtendedData = true;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(GROUP_TIME, "true");
        pars.addParameter(GROUP_LOCATION, "true");
        pars.addParameter(GROUP_SITUATION, "true");
        pars.addParameter(GROUP_EXTENDED_DATA, "true");
        return pars;
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        groupTime = "true".equalsIgnoreCase(getSimpleParameter(GROUP_TIME));
        groupLocation = "true".equalsIgnoreCase(getSimpleParameter(GROUP_LOCATION));
        groupSituation = "true".equalsIgnoreCase(getSimpleParameter(GROUP_SITUATION));
        groupExtendedData = "true".equalsIgnoreCase(getSimpleParameter(GROUP_EXTENDED_DATA));
    }

    public int getHashCode(Context ctx) {
        if (ctx == null) {
            return 1;
        } else {
            int hash = 7;
            if (groupTime) {                
                hash = 3 * hash + (ctx.getTime() != null ? ctx.getTime().hashCode() : 0);
            }
            if (groupLocation) {
                hash = 3 * hash + (ctx.getLocation() != null ? ctx.getLocation().hashCode() : 0);
            }
            if (groupSituation) {
                for (Situation sit : ctx.getSituations()) {
                    hash = 3 * hash + (sit != null ? sit.hashCode() : 0);
                }
            }
            if (groupExtendedData) {
                hash = 3 * hash + (ctx.getExtendedData() != null ? ctx.getExtendedData().hashCode() : 0);
            }
            return hash;
        }
    }

    public Map<Integer, Context> calculateFrequency(ContextSequence cs) {
        Map<Integer, Context> result = new HashMap<Integer, Context>();
        for (Context ctx : cs.getContexts()) {
            if (ctx != null) {
                int hash = getHashCode(ctx);
                Context ctxGroup = result.get(hash);
                if (ctxGroup == null) {
                    ctx.setFrequency(1);
                    result.put(hash, ctx);
                    getUniqueContexts().add(ctx);
                } else {
                    ctxGroup.setFrequency(ctxGroup.getFrequency() + 1);
                }
            }
        }
        return result;
    }

    @Override
    protected SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) {
        SimilarityResult result = new SimilarityResult(s1, s2);
        result.setS1(s1);
        result.setS2(s2);
        if (s1.size() > 0 && s2.size() > 0) {
            resetUniqueContexts();

            //calculate frequencies
            Map<Integer, Context> freqsA = calculateFrequency(s1);
            Map<Integer, Context> freqsB = calculateFrequency(s2);


            for (Integer ctxAHash : freqsA.keySet()) {
                //get frequency/probability of A
                Context ctxA = freqsA.get(ctxAHash);
                ctxA.setProbability(((double) ctxA.getFrequency()) / s1.size());

                //get frequency/probability of B
                Context ctxB = freqsB.get(ctxAHash);
                if (ctxB != null) {
                    ctxB.setProbability(((double) ctxB.getFrequency()) / s2.size());

                    //compares probabilities and store result
                    if (ctxB.getProbability() >= ctxA.getProbability()) {
                        result.add(ctxA, ctxB, 1.0);
                    }

                }

            }

            double uccount = getUniqueContexts().size();
            if (uccount != 0) {
                result.setCalculatedValue(result.size() / uccount);
            }
        }

        return result;
    }

    @Override
    public boolean isContextSimilarityNeeded() {
        return false;
    }

    public boolean isDistanceFunction() {
        return false; //the global value is a intersection function
    }
}
