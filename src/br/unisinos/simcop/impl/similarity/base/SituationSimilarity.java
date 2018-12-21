package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;

public abstract class SituationSimilarity extends AttributeSimilarity implements ISituationSimilarity {
    public static final String KEY =  "SD";
    public static final String DEFAULT_KEY = ".default";

    public double getSimilarity( Situation sitA, Situation sitB) throws Exception {
        validateParameters();
        if (sitA == null || sitB == null) {
           return missingValue;
       } else {
            return internalGetSimilarity(sitA, sitB);
        }
    }

    protected abstract double internalGetSimilarity(Situation sitA, Situation sitB) throws Exception;


}
