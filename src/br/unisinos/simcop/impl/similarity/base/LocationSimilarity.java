package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;

public abstract class LocationSimilarity extends AttributeSimilarity implements ILocationSimilarity {
    public static final String KEY="LD";

    public double getSimilarity(LocationDescription ldA, LocationDescription ldB) throws Exception {
        validateParameters();
        if (ldA == null || ldB == null) {
            return missingValue;
        } else {
            return internalGetSimilarity(ldA, ldB);
        }
    }

    protected abstract double internalGetSimilarity(LocationDescription ldA, LocationDescription ldB) throws Exception;
}
