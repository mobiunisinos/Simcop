package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.dataType.SimcopValue;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.SituationSimilarity;

/**
 *
 * @author tiago
 */
public class SituationNumericDifference extends SituationSimilarity {
    public static final String PAR_ABSOLUTE_DIFERENCE = "absoluteDifference";

    private boolean absoluteDifference;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_ABSOLUTE_DIFERENCE, "false");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String abs = getSimpleParameter(PAR_ABSOLUTE_DIFERENCE);
        absoluteDifference = abs != null ? "true".equalsIgnoreCase(abs) : false;
    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected double internalGetSimilarity(Situation sitA, Situation sitB) throws Exception {
        SimcopValue valueA = sitA.getValue();
        SimcopValue valueB = sitB.getValue();

        Number a = Utils.getAsNumber(valueA);
        Number b = Utils.getAsNumber(valueB);
        if (a == null) {
            throw new NumberFormatException("Invalid value type (" + valueA.printValue(true) + ") for: " + Utils.getPathInfo(sitA) );
        }
        if (b == null) {
            throw new NumberFormatException("Invalid value type (" + valueB.printValue(true) + ") for: " + Utils.getPathInfo(sitB) );
        }

       double result = a.doubleValue() - b.doubleValue();
       if (absoluteDifference) {
           return Math.abs(result);
       } else {
           return result;
       }

    }

}
