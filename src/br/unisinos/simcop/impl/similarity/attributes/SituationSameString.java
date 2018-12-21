package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.SituationSimilarity;

/**
 *
 * @author tiago
 */
public class SituationSameString extends SituationSimilarity {
    public static final String PAR_VALUE_FOR_SAME_STRING = "valueForSameString";
    public static final String PAR_VALUE_FOR_OTHER_STRING = "valueForOtherString";
    public static final String PAR_CASE_SENSITIVE = "caseSensitive";

    private double valueForSameString = 0;
    private double valueForOtherString = 1;
    private boolean caseSensitive = true;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_VALUE_FOR_SAME_STRING, "0.0");
        internalParameters.addParameter(PAR_VALUE_FOR_OTHER_STRING, "1.0");
        internalParameters.addParameter(PAR_CASE_SENSITIVE, "true");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String sSame = getSimpleParameter(PAR_VALUE_FOR_SAME_STRING);
        String sOther = getSimpleParameter(PAR_VALUE_FOR_OTHER_STRING);
        String sCase = getSimpleParameter(PAR_CASE_SENSITIVE);

        valueForSameString = sSame != null ? Double.parseDouble(sSame.trim()) : 0;
        valueForOtherString = sOther != null ? Double.parseDouble(sOther.trim()) : 0;
        caseSensitive = sCase != null ? "true".equalsIgnoreCase(sCase) : true;
    }

    public boolean isDistanceFunction() {
        return valueForSameString < valueForOtherString;
    }

    @Override
    protected double internalGetSimilarity(Situation sitA, Situation sitB) {
        String strA = sitA.getValue() != null ? sitA.getValue().getStringValue() : null;
        String strB = sitB.getValue() != null ? sitB.getValue().getStringValue() : null;
        if (strA == null || strB == null) {
            return missingValue;
        } else {
            boolean same = caseSensitive ? strA.equals(strB) : strA.equalsIgnoreCase(strB);
            if (same) {
                return valueForSameString;
            } else {
                return valueForOtherString;
            }
        }
    }

}
