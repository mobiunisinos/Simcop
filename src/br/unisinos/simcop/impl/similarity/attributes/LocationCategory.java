package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.impl.similarity.base.LocationSimilarity;

/**
 * Compares the location category of two location descriptors.
 * @author tiago
 */
public class LocationCategory extends LocationSimilarity {

    public static final String PAR_VALUE_FOR_SAME_CATEGORY = "valueForSameCategory";
    public static final String PAR_VALUE_FOR_OTHER_CATEGORY = "valueForOtherCategory";
    public static final String PAR_CASE_SENSITIVE = "caseSensitive";
    private double valueForSameCategory = 0;
    private double valueForOtherCategory = 1;
    private boolean caseSensitive = true;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_VALUE_FOR_SAME_CATEGORY, "0.0");
        internalParameters.addParameter(PAR_VALUE_FOR_OTHER_CATEGORY, "1.0");
        internalParameters.addParameter(PAR_CASE_SENSITIVE, "true");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String sSame = getSimpleParameter(PAR_VALUE_FOR_SAME_CATEGORY);
        String sOther = getSimpleParameter(PAR_VALUE_FOR_OTHER_CATEGORY);
        String sCase = getSimpleParameter(PAR_CASE_SENSITIVE);

        valueForSameCategory = sSame != null ? Double.parseDouble(sSame.trim()) : 0;
        valueForOtherCategory = sOther != null ? Double.parseDouble(sOther.trim()) : 0;
        caseSensitive = sCase != null ? "true".equalsIgnoreCase(sCase) : true;
    }

    public boolean isDistanceFunction() {
        return valueForSameCategory < valueForOtherCategory;
    }

    @Override
    protected double internalGetSimilarity(LocationDescription ldA, LocationDescription ldB) {
        String catA = ldA.getCategory();
        String catB = ldB.getCategory();
        if (catA == null || catB == null) {
            return missingValue;
        } else {
            boolean same = caseSensitive ? catA.equals(catB) : catA.equalsIgnoreCase(catB);
            if (same) {
                return valueForSameCategory;
            } else {
                return valueForOtherCategory;
            }
        }
    }
}
