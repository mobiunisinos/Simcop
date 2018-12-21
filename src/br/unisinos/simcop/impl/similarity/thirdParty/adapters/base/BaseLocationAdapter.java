package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseLocationAdapter extends BaseAdapter implements ILocationSimilarity{
    protected ILocationSimilarity locationFunction;

    public BaseLocationAdapter(ILocationSimilarity function) {
        super(function);
        this.locationFunction = (ILocationSimilarity) function;
    }

    public double getSimilarity(LocationDescription ldA, LocationDescription ldB) throws Exception {
        return locationFunction.getSimilarity(ldA, ldB);
    }

    public void setCallerObject(IContextSimilarity callerObject) {
        locationFunction.setCallerObject(callerObject);
    }


}
