package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.Situation;

/**
 *
 * @author tiago
 */
public interface ISituationSimilarity extends IAttributeSimilarity {
    public double getSimilarity( Situation sitA, Situation sitB ) throws Exception;

}
