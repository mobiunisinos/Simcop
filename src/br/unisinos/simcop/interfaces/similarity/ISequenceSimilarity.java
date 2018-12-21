package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.ContextSequence;

public interface ISequenceSimilarity extends ISimilarity {
    public SimilarityResult getSimilarity(ContextSequence s1, ContextSequence s2) throws Exception;
    public boolean isContextSimilarityNeeded();
    public void setContextSimilarity(IContextSimilarity contextSimilarity);
    public IContextSimilarity getContextSimilarity();
    public void setContextSelector(IContextSelector contextSelector);
}
