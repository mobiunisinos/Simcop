package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;
import java.util.List;

/**
 * Implements the Cosine.<br/>
 *
 * @author tiago
 * @see br.unisinos.simcop.core.CommonFunctions#lpDistance(java.util.List, double)
 */
public class CosineSimilarity extends ContextSimilarity {

    public static final String PAR_EVAL_TIME = "evaluateTime";
    public static final String PAR_EVAL_POSITION_COORD = "evaluatePositionCoordinates";
    public static final String PAR_EVAL_NUMERIC_SITUATION = "evaluateNumericValueSituations";
    private boolean evalTime = false;
    private boolean evalPositionCoord = false;
    private boolean evalNumericSituations = true;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_EVAL_TIME, "false");
        pars.addParameter(PAR_EVAL_POSITION_COORD, "false");
        pars.addParameter(PAR_EVAL_NUMERIC_SITUATION, "true");
        return pars;
    }

    public boolean isDistanceFunction() {
        return false;
    }

    public boolean isAttributeSimilarityNeeded() {
        return false;
    }

    public double getLargestPossibleValue() {
        return 1;
    }

    protected void internalValidateParameters() throws Exception {
        String sTime = getSimpleParameter(PAR_EVAL_TIME);
        String sPos = getSimpleParameter(PAR_EVAL_POSITION_COORD);
        String sSit = getSimpleParameter(PAR_EVAL_NUMERIC_SITUATION);

        evalTime = "true".equalsIgnoreCase(sTime);
        evalPositionCoord = "true".equalsIgnoreCase(sPos);
        evalNumericSituations = "true".equalsIgnoreCase(sSit);

    }

    @Override
    protected double internalGetSimilarity(Context c1, Context c2) throws Exception {
        List<double[]> map = getAttributesAsCoord(c1, c2, evalTime, evalPositionCoord, evalNumericSituations);
        if (map != null && map.size() > 0) {
            double[] P = new double[map.size()];
            double[] Q = new double[map.size()];
            for (int i = 0; i < map.size(); i++) {
                double[] PQ = map.get(i);
                P[i] = PQ[0];
                Q[i] = PQ[1];
                
//                Utils.log(i + ": " + P[i] + ", " + Q[i]);
            }
            return CommonFunctions.cosineSimilarity(P, Q);
        } else {
            return 0;
        }
    }
}

