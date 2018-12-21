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
public class SituationNumericRatio extends SituationSimilarity {

    public static final String PAR_RATIO_FORM = "ratioForm";
    private boolean ratioAB = true;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_RATIO_FORM, "A/B");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String form = getSimpleParameter(PAR_RATIO_FORM);
        ratioAB = form != null && form.replaceAll(" ", "").equalsIgnoreCase("A/B");
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
            throw new NumberFormatException("Invalid value type (" + valueA.printValue(true) + ") for: " + Utils.getPathInfo(sitA));
        }
        if (b == null) {
            throw new NumberFormatException("Invalid value type (" + valueB.printValue(true) + ") for: " + Utils.getPathInfo(sitB));
        }
        if (a.doubleValue() == 0 || b.doubleValue() == 0) {
            return 0;
        }

        if (ratioAB) {
            return a.doubleValue() / b.doubleValue();
        } else {
            return b.doubleValue() / a.doubleValue();
        }


    }
}
