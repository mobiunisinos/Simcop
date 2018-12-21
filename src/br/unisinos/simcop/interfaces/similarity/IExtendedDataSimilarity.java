package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.ExtendedData;

/**
 *
 * @author tiago
 */
public interface IExtendedDataSimilarity extends IAttributeSimilarity {
    public double getSimilarity(ExtendedData edA, ExtendedData edB) throws Exception;

}
