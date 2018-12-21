package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;

/**
 * Measures the similarity by two contexts by counting equals attributes (time, location and situations)
 * and then dividing by the amount of unique attributes between the two contexts.
 * @author tiago
 */
public class SimpleAttributes extends ContextSimilarity {

    public static String COMPARE_TIME = "compareTime";
    public static String COMPARE_LOCATION = "compareLocation";
    public static String COMPARE_SITUATIONS = "compareSituations";
    public static String COMPARE_EXTENDED_DATA = "compareExtendedData";

    protected boolean compareTime;
    protected boolean compareLocation;
    protected boolean compareSituations;
    protected boolean compareExtendedData;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(COMPARE_TIME, "true");
        pars.addParameter(COMPARE_LOCATION, "true");
        pars.addParameter(COMPARE_SITUATIONS, "true");
        pars.addParameter(COMPARE_EXTENDED_DATA, "true");
        return pars;
    }

    protected void internalValidateParameters() throws Exception {
        compareTime = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_TIME));
        compareLocation = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_LOCATION));
        compareSituations = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_SITUATIONS));
        compareExtendedData = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_EXTENDED_DATA));
    }

    public boolean isAttributeSimilarityNeeded() {
        return false;
    }

    public boolean isDistanceFunction() {
        return false;
    }

    public double internalGetSimilarity(Context c1, Context c2) {
        if (c1 == null) {
            return c2 == null ? 1 : 0;
        } else if (c2 == null) {
            return 0;
        }

        resetUniqueHashes();

        int similar = 0;
        int[] aux;
        int dimensions = 0;
        if (compareTime) {
            aux = isSame(c1.getTime(), c2.getTime());
            similar += aux[0];
            if (aux[1] > 0 || aux[2] > 0) {
                dimensions++;
            }
        }
        if (compareLocation) {
            aux = isSame(c1.getLocation(), c2.getLocation());
            similar += aux[0];
            if (aux[1] > 0 || aux[2] > 0) {
                dimensions++;
            }
        }
        if (compareExtendedData) {
            aux = isSame(c1.getExtendedData(), c2.getExtendedData());
            similar += aux[0];
            if (aux[1] > 0 || aux[2] > 0) {
                dimensions++;
            }
        }
        if (compareSituations && !Utils.isEmpty(c1.getSituations())) {
            for (Situation sitA : c1.getSituations()) {
                if (sitA != null) {
                    Situation sitB;
                    if (!Utils.isEmpty(sitA.getSource())) {
                        sitB = c2.findSituation(sitA.getSource(), sitA.getAuxiliary(), sitA.getPredicate());
                    } else if (!Utils.isEmpty(sitA.getAuxiliary())) {
                        sitB = c2.findSituation(sitA.getAuxiliary(), sitA.getPredicate());
                    } else {
                        sitB = c2.findSituation(sitA.getPredicate());
                    }
                    aux = isSame(sitA, sitB);
                    similar += aux[0];
                    if (sitA != null) {
                        getUniqueHashes().add(getHashForName(sitA));
                    }
                }
            }
            //count unique situations in context B
            if (!Utils.isEmpty(c2.getSituations())) {
                for (Situation sitB : c2.getSituations()) {
                    if (sitB != null) {
                        getUniqueHashes().add(getHashForName(sitB));
                    }
                }
            }

            dimensions += getUniqueHashes().size();
        }

        if (dimensions > 0) {
            return ((double) similar) / dimensions;
        } else {
            return 0;
        }
    }


    public double getLargestPossibleValue() {
        return 1;
    }

}
