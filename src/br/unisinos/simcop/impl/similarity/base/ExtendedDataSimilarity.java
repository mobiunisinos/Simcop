package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.data.model.ExtendedData;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;

/**
 *
 * @author tiago
 */
public abstract class ExtendedDataSimilarity extends AttributeSimilarity implements IExtendedDataSimilarity {
    public static final String KEY="ED";
    
    public double getSimilarity( ExtendedData edA,  ExtendedData edB) {
        if (edA == null || edB == null) {
           return missingValue;
       } else {
            return internalGetSimilarity(edA, edB);
        }
    }

    protected abstract double internalGetSimilarity(ExtendedData dA, ExtendedData dB);

}
