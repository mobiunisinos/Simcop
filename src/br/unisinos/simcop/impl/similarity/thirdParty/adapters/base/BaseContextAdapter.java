package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseContextAdapter extends BaseAdapter implements IContextSimilarity {

    protected IContextSimilarity contextFunction;

    public BaseContextAdapter(ISimilarity function) {
        super(function);
        this.contextFunction = (IContextSimilarity) function;
        if (contextFunction != null) {
            try {
                contextFunction.validateParameters();
            } catch (Exception ex) {                
                Utils.log(ex);
            }
        }

    }

    public double getSimilarity(Context c1, Context c2) throws Exception {
        return contextFunction.getSimilarity(c1, c2);
    }

    public void setCallerObject(ISequenceSimilarity callerObject) {
        contextFunction.setCallerObject(callerObject);
    }

    public ISequenceSimilarity getCallerObject() {
        return contextFunction.getCallerObject();
    }

    public double getLargestPossibleValue() {
        return contextFunction.getLargestPossibleValue();
    }

    public void setTDSimilarity(ITimeSimilarity TDFunction) {
        contextFunction.setTDSimilarity(TDFunction);
    }

    public void setLDSimilarity(ILocationSimilarity LDFunction) {
        contextFunction.setLDSimilarity(LDFunction);
    }

    public void setEDSimilarity(IExtendedDataSimilarity EDFunction) {
        contextFunction.setEDSimilarity(EDFunction);
    }

    public ITimeSimilarity getTDSimilarity() {
        return contextFunction.getTDSimilarity();
    }

    public ILocationSimilarity getLDSimilarity() {
        return contextFunction.getLDSimilarity();
    }

    public IExtendedDataSimilarity getEDSimilarity() {
        return contextFunction.getEDSimilarity();
    }

    public void clearSDSimilarityMap() {
        contextFunction.clearSDSimilarityMap();
    }

    public void addSDSimilarity(String sourceName, String auxName, String predicateName, ISituationSimilarity SDFunction) throws IllegalArgumentException {
        contextFunction.addSDSimilarity(sourceName, auxName, predicateName, SDFunction);
    }

    public ISituationSimilarity getSituationSimilarity(String sourceName, String auxName, String predicateName) {
        return contextFunction.getSituationSimilarity(sourceName, auxName, predicateName);
    }

    public boolean isSDKeyValid(String key) {
        return contextFunction.isSDKeyValid(key);
    }

    public boolean isAttributeSimilarityNeeded() {
        return contextFunction.isAttributeSimilarityNeeded();
    }
}
