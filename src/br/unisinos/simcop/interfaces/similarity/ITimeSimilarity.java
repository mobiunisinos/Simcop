package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.TimeDescription;

/**
 *
 * @author tiago
 */
public interface ITimeSimilarity extends IAttributeSimilarity {
    public double getSimilarity( TimeDescription tdA, TimeDescription tdB ) throws Exception;
}
