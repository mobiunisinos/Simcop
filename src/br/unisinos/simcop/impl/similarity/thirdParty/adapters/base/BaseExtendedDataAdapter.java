package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.data.model.ExtendedData;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseExtendedDataAdapter extends BaseAdapter implements IExtendedDataSimilarity{
    protected IExtendedDataSimilarity edFunction;

    public BaseExtendedDataAdapter(ISimilarity function) {
        super(function);
        this.edFunction = (IExtendedDataSimilarity) function;
    }

    public double getSimilarity(ExtendedData edA, ExtendedData edB) throws Exception {
        return edFunction.getSimilarity(edA, edB);
    }

    public void setCallerObject(IContextSimilarity callerObject) {
        edFunction.setCallerObject(callerObject);
    }

}
