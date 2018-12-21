package br.unisinos.simcop.interfaces.transformations;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.SimilarityResult;

public interface ITransformOutput extends ISimcopClass {
    public SimilarityResult transform(SimilarityResult similarityResult); 
}
 
