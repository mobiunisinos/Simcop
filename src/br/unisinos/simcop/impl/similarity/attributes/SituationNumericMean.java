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
public class SituationNumericMean extends SituationSimilarity {


    @Override
    protected void addDefaultParameters(Parameters internalParameters) {}

    @Override
    protected void internalValidateParameters() throws Exception {}

    public boolean isDistanceFunction() {
        return false;
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

        return (a.doubleValue() + b.doubleValue()) / 2;


    }

}
