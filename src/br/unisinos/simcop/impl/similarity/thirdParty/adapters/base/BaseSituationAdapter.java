package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseSituationAdapter extends BaseAdapter implements ISituationSimilarity{
    protected ISituationSimilarity situationFunction;

    public BaseSituationAdapter(ISimilarity function) {
        super(function);
        this.situationFunction = (ISituationSimilarity) function;
    }

    public double getSimilarity(Situation sitA, Situation sitB) throws Exception {
        return situationFunction.getSimilarity(sitA, sitB);
    }

    public void setCallerObject(IContextSimilarity callerObject) {
        situationFunction.setCallerObject(callerObject);
    }



}
