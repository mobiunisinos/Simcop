package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseTimeAdapter extends BaseAdapter implements ITimeSimilarity {
    protected ITimeSimilarity timeFunction;

    public BaseTimeAdapter(ITimeSimilarity function) {
        super(function);
        this.timeFunction = (ITimeSimilarity) function;
    }

    public double getSimilarity(TimeDescription tdA, TimeDescription tdB) throws Exception {
        return timeFunction.getSimilarity(tdA, tdB);
    }

    public void setCallerObject(IContextSimilarity callerObject) {
        timeFunction.setCallerObject(callerObject);
    }

}
