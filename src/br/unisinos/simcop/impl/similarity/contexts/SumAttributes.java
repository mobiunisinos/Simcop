package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;
import java.util.Map;

/**
 * Sums the similarities and subtract the distances between each parameters of
 * two contexts
 *
 * @author tiago
 */
public class SumAttributes extends ContextSimilarity {

    public static final String DISTANCE = "distance";
    public static final String SIMILARITY = "similarity";
    public static final String BOTH = "both";
    private boolean distance;
    private boolean similarity;

    public Parameters getDefaultParameters() {
        Parameters par = new Parameters();
        par.addParameter("DistanceOrSimilarity", "both");
        return par;
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String distanceOrSimilarity = getSimpleParameter("DistanceOrSimilarity");
        if (BOTH.equalsIgnoreCase(distanceOrSimilarity)) {
            distance = true;
            similarity = true;
        } else {
            distance = DISTANCE.equalsIgnoreCase(distanceOrSimilarity);
            similarity = SIMILARITY.equalsIgnoreCase(distanceOrSimilarity);
        }
    }

    public boolean isAttributeSimilarityNeeded() {
        return true;
    }

    public boolean isDistanceFunction() {
        return distance && !similarity;
    }

    private double sumResult(double result, boolean attributeDistance, double value) {
        if (attributeDistance) {
            if (similarity && !distance) {
                result -= value;
            } else {
                result += value;                
            }
        } else {
            if (distance && !similarity) {
                result += value;
            } else {
                result -= value;
            }

        }
        return result;
    }

    @Override
    protected double internalGetSimilarity(Context c1, Context c2) throws Exception {
        if (c1 == null || c2 == null) {
            return 0;
        }
        double result = 0;

        if (TDFunction != null) {
            result = sumResult(result, TDFunction.isDistanceFunction(), TDFunction.getSimilarity(c1.getTime(), c2.getTime()));
        }
        if (LDFunction != null) {
            result = sumResult(result, LDFunction.isDistanceFunction(), LDFunction.getSimilarity(c1.getLocation(), c2.getLocation()));
        }
        if (SDFunctions != null && !SDFunctions.isEmpty()) {
            Map<String, Situation[]> allSituations = unionSituations(c1, c2);
            if (allSituations != null) {
                for (String sitKey : allSituations.keySet()) {
                        System.err.println(sitKey);
                    Situation[] pair = allSituations.get(sitKey);
                    if (pair != null && pair.length == 2) {
                        ISituationSimilarity SDFunction = getSituationSimilarity(sitKey);
                        if (SDFunction != null) {
                            result = sumResult(result, SDFunction.isDistanceFunction(), SDFunction.getSimilarity(pair[0], pair[1]));
                        }
                    }
                }
            }
        }
        if (EDFunction != null) {
            result = sumResult(result, EDFunction.isDistanceFunction(), EDFunction.getSimilarity(c1.getExtendedData(), c2.getExtendedData()));
        }
        return result;
    }

    public double getLargestPossibleValue() {
        return 0; //See ECDefault.java  -> setSequenceSimilarityValue()
    }
}
