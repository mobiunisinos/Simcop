package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.LocationDescription;

/**
 *
 * @author tiago
 */
public interface ILocationSimilarity extends IAttributeSimilarity {
    public double getSimilarity( LocationDescription ldA, LocationDescription ldB ) throws Exception;
}
