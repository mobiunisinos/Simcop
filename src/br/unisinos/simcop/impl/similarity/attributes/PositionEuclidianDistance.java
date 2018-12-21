package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.impl.similarity.base.LocationSimilarity;

/**
 * Returns the distance between two points using euclidian distance:<br/>
 * http://en.wikipedia.org/wiki/Euclidean_distance
 * @author tiago
 */
public class PositionEuclidianDistance extends LocationSimilarity {

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {}

    @Override
    protected void internalValidateParameters() throws Exception {}

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected double internalGetSimilarity(LocationDescription ldA, LocationDescription ldB) {
        double[] coordA = ldA.getPosition();
        double[] coordB = ldB.getPosition();
        if (coordA == null || coordB == null) {
            return missingValue;
        }

        double squareSum = 0;
        for (int i = 0; i < Math.max(coordA.length, coordB.length); i++) {
            double a = (i < coordA.length ? coordA[i] : 0);
            double b = (i < coordB.length ? coordB[i] : 0);

            squareSum += Math.pow( Math.abs(a - b), 2 );
        }

        return Math.sqrt(squareSum);
    }




}
