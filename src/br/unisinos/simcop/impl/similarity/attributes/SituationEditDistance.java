package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.SituationSimilarity;

/**
 *
 * @author tiago
 */
public class SituationEditDistance extends SituationSimilarity {
    public static final String PAR_NORMALIZE_CASE = "normalizeCase";

    private boolean normalizeCase = true;

    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_NORMALIZE_CASE, "true");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String sCase = getSimpleParameter(PAR_NORMALIZE_CASE);

        normalizeCase = sCase != null ? "true".equalsIgnoreCase(sCase) : true;
    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected double internalGetSimilarity(Situation sitA, Situation sitB) {
        String strA = sitA.getValue() != null ? sitA.getValue().getStringValue() : null;
        String strB = sitB.getValue() != null ? sitB.getValue().getStringValue() : null;
        if (strA == null || strB == null) {
            return missingValue;
        } else {
            if (normalizeCase) {
                strA = strA.trim().toUpperCase();
                strB = strB.trim().toUpperCase();
            }
            //return CommonFunctions.levenshteinDistance(strA, strB);
            return CommonFunctions.levenshteinDistance(strA, strB);            
            
        }
    }

}
