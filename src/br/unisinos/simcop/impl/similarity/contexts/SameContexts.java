package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;

/**
 * Evaluates if the two contexts haves the same attributes (equals);<br/>
 * returns 1 if contexts are equals, 0 otherwise;
 * @author tiago
 */
public class SameContexts extends ContextSimilarity {

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

    @Override
    protected void internalValidateParameters() throws Exception {
        compareTime = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_TIME));
        compareLocation = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_LOCATION));
        compareSituations = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_SITUATIONS));
        compareExtendedData = "true".equalsIgnoreCase(getSimpleParameter(COMPARE_EXTENDED_DATA));
    }

    public boolean isDistanceFunction() {
        return false;
    }

    public boolean isAttributeSimilarityNeeded() {
        return false;
    }

    @Override
    protected double internalGetSimilarity(Context c1, Context c2) throws Exception {
        if (c1 == null) {
            return c2 == null ? 1 : 0;
        } else if (c2 == null) {
            return 0;
        }

        int[] aux;
        if (compareTime) {
            aux = isSame(c1.getTime(), c2.getTime());
            if (aux[0] == 0) {
                return 0;
            }
        }
        if (compareLocation) {
            aux = isSame(c1.getLocation(), c2.getLocation());
            if (aux[0] == 0) {
                return 0;
            }
        }
        if (compareExtendedData) {
            aux = isSame(c1.getExtendedData(), c2.getExtendedData());
            if (aux[0] == 0) {
                return 0;
            }
        }
        if (compareSituations && !Utils.isEmpty(c1.getSituations())) {
            for (Situation sitA : c1.getSituations()) {
                if (sitA != null) {

                    //identify the correspondent situation attribute in context B
                    Situation sitB;
                    if (!Utils.isEmpty(sitA.getSource())) {
                        sitB = c2.findSituation(sitA.getSource(), sitA.getAuxiliary(), sitA.getPredicate());
                    } else if (!Utils.isEmpty(sitA.getAuxiliary())) {
                        sitB = c2.findSituation(sitA.getAuxiliary(), sitA.getPredicate());
                    } else {
                        sitB = c2.findSituation(sitA.getPredicate());
                    }
                    
                    //test equality
                    aux = isSame(sitA, sitB);
                    if (aux[0] == 0) {
                        return 0;
                    }
                }
            }

        }

        return 1;
    }

    public double getLargestPossibleValue() {
        return 1;
    }
}
