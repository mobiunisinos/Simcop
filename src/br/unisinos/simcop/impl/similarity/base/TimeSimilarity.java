package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;

public abstract class TimeSimilarity extends AttributeSimilarity implements ITimeSimilarity {
    public static final String KEY="TD";

    public double getSimilarity(TimeDescription tdA, TimeDescription tdB) throws Exception {
        validateParameters();
        if (tdA == null || tdB == null) {
           return missingValue;
       } else {
            return internalGetSimilarity(tdA, tdB);
        }
    }

    protected abstract double internalGetSimilarity(TimeDescription tdA, TimeDescription tdB);

}
 
