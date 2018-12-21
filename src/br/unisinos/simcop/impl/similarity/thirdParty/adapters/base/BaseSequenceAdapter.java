package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseSequenceAdapter extends BaseAdapter implements ISequenceSimilarity{
    protected ISequenceSimilarity sequenceFunction;

    public BaseSequenceAdapter(ISimilarity function) {
        super(function);
        this.sequenceFunction = (ISequenceSimilarity) function;
    }


    public SimilarityResult getSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        return sequenceFunction.getSimilarity(s1, s2);
    }

    public void setContextSimilarity(IContextSimilarity contextSimilarity) {
       sequenceFunction.setContextSimilarity(contextSimilarity);
    }

    public IContextSimilarity getContextSimilarity() {
        return sequenceFunction.getContextSimilarity();
    }

    public boolean isContextSimilarityNeeded() {
        return sequenceFunction.isContextSimilarityNeeded();
    }

    public void setContextSelector(IContextSelector contextSelector) {
        sequenceFunction.setContextSelector(contextSelector);
    }


}
