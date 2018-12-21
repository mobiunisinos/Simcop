package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.impl.similarity.base.LocationSimilarity;

/**
 * Structural Similarity:<br/>
 * <span style='font-family: monospace'>
 *    category = categoryWeight;<br/>
 *    name = nameWeight;<br/>
 *    similarity = equals(category) + equals(name) / category + name<br/>
 *    <br/>
 *
 *   <b>Exemples:</b><br/>
 *    category = 2<br/>
 *    name = 1<br/>
 *    LocationA =  'sameCategory'.'sameLocation'<br/>
 *    LocationB =  'sameCategory'.'sameLocation'<br/>
 *    result = 2 + 1 / 2 + 1 = 1<br/>
 *    <br/>
 *
 *    LocationA =  'sameCategory'.'sameLocation'<br/>
 *    LocationB =  'sameCategory'.'other'<br/>
 *    result = 2 / 2 + 1 = 1 = 0.666<br/>
 *    <br/>
 *
 *    LocationA =  'sameCategory'.'sameLocation'<br/>
 *    LocationB =  'other'.'sameLocation'<br/>
 *    result = 0 / 2 + 1 = 1 = 0   //stop at first inequality, see reference.<br/>
 * </span>
 * <br/><br/>
 *
 * Reference:<br/>
 * ABRAHAM, S.; LAL, P. S. Spatio-temporal similarity of network-constrained moving object
 * trajectories using sequence alignment of travel locations. <b>Transportation Research Part C:
 * Emerging Technologies,</b> [S.l.], v. 23, n. 0, p. 109 – 123, 2012. Data Management in Vehicular
 * Networks.
 * @author tiago
 */
public class LocationNameCategory extends LocationSimilarity {

    public static final String PAR_CATEGORY_WEIGHT = "categoryWeight";
    public static final String PAR_NAME_WEIGHT = "nameWeight";
    public static final String PAR_CASE_SENSITIVE = "caseSensitive";
    private double categoryWeight = 2;
    private double nameWeight = 1;
    private double totalWeight;
    private boolean caseSensitive = true;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_CATEGORY_WEIGHT, "2");
        internalParameters.addParameter(PAR_NAME_WEIGHT, "1");
        internalParameters.addParameter(PAR_CASE_SENSITIVE, "true");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String sCat = getSimpleParameter(PAR_CATEGORY_WEIGHT);
        String sName = getSimpleParameter(PAR_NAME_WEIGHT);
        String sCase = getSimpleParameter(PAR_CASE_SENSITIVE);

        categoryWeight = sCat != null ? Double.parseDouble(sCat.trim()) : 2;
        nameWeight = sName != null ? Double.parseDouble(sName.trim()) : 1;
        caseSensitive = sCase != null ? "true".equalsIgnoreCase(sCase) : true;

        if (categoryWeight <= 0) {
            throw new IllegalArgumentException(PAR_CATEGORY_WEIGHT + " must be > 0");
        }
        if (nameWeight <= 0) {
            throw new IllegalArgumentException(PAR_NAME_WEIGHT + " must be > 0");
        }
        totalWeight = categoryWeight + nameWeight;
    }

    public boolean isDistanceFunction() {
        return false;
    }

    @Override
    protected double internalGetSimilarity(LocationDescription ldA, LocationDescription ldB) {
        String catA = ldA.getCategory() != null ? ldA.getCategory() : "";
        String catB = ldB.getCategory();

        String nameA = ldA.getName() != null ? ldA.getName() : "";
        String nameB = ldB.getName();

        double sum = 0;
        if (caseSensitive ? catA.equals(catB) : catA.equalsIgnoreCase(catB)) {
            sum += categoryWeight;
            if (caseSensitive ? nameA.equals(nameB) : nameA.equalsIgnoreCase(nameB)) {
                sum += nameWeight;
            }
        }
        return sum / totalWeight;
    }
}
