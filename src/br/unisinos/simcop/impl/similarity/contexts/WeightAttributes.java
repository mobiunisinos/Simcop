package br.unisinos.simcop.impl.similarity.contexts;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.impl.similarity.base.ContextSimilarity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Measures the similarity by two contexts by counting equals attributes (time, location and situations)
 * and then dividing by the amount of unique attributes between the two contexts.
 * Is possible assign different weights for  each attribute.<br/>
 *
 * Eg:<br/>
<TABLE CELLSPACING="0" COLS="6" BORDER="1">
<COLGROUP SPAN="6" WIDTH="85"></COLGROUP>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B><BR></B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Weight</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Context A</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Context B</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Similar</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>SimilarWeight</B></TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="16" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Time</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0,5" SDNUM="1046;">0,5</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0,333333333333333" SDNUM="1046;0;HH:MM:SS">08:00:00</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0,333333333333333" SDNUM="1046;0;HH:MM:SS">08:00:00</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0,5" SDNUM="1046;">0,5</TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Location</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT"> Home     </TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT"> Home     </TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>testA</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT">X</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT"><BR></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0" SDNUM="1046;">0</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0" SDNUM="1046;">0</TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>aux.testB</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT">Y</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT">Y</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>otherPredicate</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="1" SDNUM="1046;">1</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT"><BR></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="LEFT">X</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0" SDNUM="1046;">0</TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" SDVAL="0" SDNUM="1046;">0</TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" HEIGHT="17" ALIGN="LEFT" BGCOLOR="#E6E6E6"><B>Max Similarity</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" BGCOLOR="#E6E6E6" SDVAL="4,5" SDNUM="1046;"><B>4,5</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" COLSPAN=3 ALIGN="RIGHT" VALIGN=MIDDLE BGCOLOR="#E6E6E6"><B>Weight Similarity</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" BGCOLOR="#E6E6E6" SDVAL="2,5" SDNUM="1046;"><B>2,5</B></TD>
</TR>
<TR>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" COLSPAN=5 HEIGHT="17" ALIGN="RIGHT" VALIGN=MIDDLE BGCOLOR="#E6E6E6"><B>Context Similarity</B></TD>
<TD STYLE="border-top: 1px solid #000000; border-bottom: 1px solid #000000; border-left: 1px solid #000000; border-right: 1px solid #000000" ALIGN="RIGHT" BGCOLOR="#E6E6E6" SDVAL="0,555555555555556" SDNUM="1046;0;0,00%"><B>55,56%</B></TD>
</TR>
</TABLE>
 *
 *
 * @author tiago
 */
public class WeightAttributes extends ContextSimilarity {

    public static final String TIME_WEIGHT = "timeWeight";
    public static final String LOCATION_WEIGHT = "locationWeight";
    public static final String SITUATION_WEIGHT_PREFIX = "situation";
    public static final String EXTENDED_WEIGHT = "extendedWeight";
    public static final String DEFAULT_WEIGHT = "defaultWeight";
    public static final String SITUATION_DIMENSION_NULL = "{null}";
    protected double defaultWeight;
    protected double timeWeight;
    protected double locationWeight;
    protected double extendedWeight;
    protected Map<String, Double> situationsWeight;
    private double totalWeight;
    private double similarWeight;
    private double maxSimilarity;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(DEFAULT_WEIGHT, "1");
        pars.addParameter(TIME_WEIGHT, "0.5");
        pars.addParameter(LOCATION_WEIGHT, "1");
        pars.addParameter(EXTENDED_WEIGHT, "0");
        pars.addParameter(SITUATION_WEIGHT_PREFIX + "?source=sample&aux=sample&predicate=sample", "1");
        pars.addParameter(SITUATION_WEIGHT_PREFIX + "?source={null}&aux={null}&predicate=sample", "1");
        return pars;
    }

    protected double loadWeight(String parName) throws Exception {
        try {
            return Double.parseDouble(getSimpleParameter(parName));
        } catch (Exception e) {
            Utils.log(e);
            Parameter p = getParameter(parName);
            throw new IllegalArgumentException("key[" + parName + "] value[" + (p == null ? "null" : p.getValue()) + "]: " + e.getClass().getName() + " {" + e.getMessage() + "}");
        }
    }

    protected void internalValidateParameters() throws Exception {
        defaultWeight = loadWeight(DEFAULT_WEIGHT);
        timeWeight = loadWeight(TIME_WEIGHT);
        locationWeight = loadWeight(LOCATION_WEIGHT);
        extendedWeight = loadWeight(EXTENDED_WEIGHT);

        situationsWeight = new HashMap<String, Double>();
        List<Parameter> parameters = getParameters().asList(SITUATION_WEIGHT_PREFIX);
        for (Parameter par : parameters) {
            String key = par.getKey() != null ? par.getKey().trim() : "";
            if (key.length() > 1 && key.startsWith("situation?")) {
                try {
                    situationsWeight.put(key, par.getAsNumber().doubleValue());
                } catch (Exception e) {
                    //Utils.log(e);
                    throw new IllegalArgumentException("key[" + key + "] value[" + (par == null ? "null" : par.getValue()) + "]: " + e.getClass().getName() + " {" + e.getMessage() + "}");
                }
            }
        }
        maxSimilarity = 0;
    }

    public String getSituationKey(String source, String aux, String predicate) {
        StringBuilder sb = new StringBuilder();
        sb.append("situation?source=").append(source != null ? source : SITUATION_DIMENSION_NULL);
        sb.append("&aux=").append(aux != null ? aux : SITUATION_DIMENSION_NULL);
        sb.append("&predicate=").append(predicate != null ? predicate : SITUATION_DIMENSION_NULL);
        return sb.toString();
    }

    public double getWeight(Situation situation) {
        String key = getSituationKey(situation.getSource(), situation.getAuxiliary(), situation.getPredicate());
        return getWeight(key);
    }

    private double getWeight(String sitKey) {
        Double result = sitKey != null ? situationsWeight.get(sitKey) : null;
        return (result != null ? result : defaultWeight);
    }

    public boolean isDistanceFunction() {
        return false;
    }

    public boolean isAttributeSimilarityNeeded() {
        return false;
    }

    public double internalGetSimilarity(Context c1, Context c2) {
        if (c1 == null || c2 == null) {
            return 0;
        }
        totalWeight = 0;
        similarWeight = 0;

        /*
         * BEGIN OF SIMILARITY CALCULATION
         */
        AttributeData data;
        //time
        if (c1.getTime() != null || c2.getTime() != null) {
            data = new AttributeData(timeWeight, c1.getTime(), c2.getTime());
            totalWeight += timeWeight;
            similarWeight += data.getSimilarWeight();
        }

        //location
        if (c1.getLocation() != null || c2.getLocation() != null) {
            data = new AttributeData(locationWeight, c1.getLocation(), c2.getLocation());
            totalWeight += locationWeight;
            similarWeight += data.getSimilarWeight();
        }

        //extended data
        if (c1.getExtendedData() != null || c2.getExtendedData() != null) {
            data = new AttributeData(extendedWeight, c1.getExtendedData(), c2.getExtendedData());
            totalWeight += extendedWeight;
            similarWeight += data.getSimilarWeight();
        }

        //situations of context A
        Map<String, AttributeData> compareTable = new HashMap<String, AttributeData>();

        double sitWeight;
        for (Situation sitA : c1.getSituations()) {
            if (sitA != null) {
                String key = getSituationKey(sitA.getSource(), sitA.getAuxiliary(), sitA.getPredicate());
                sitWeight = getWeight(key);

                compareTable.put(key, new AttributeData(sitWeight, sitA.getValue(), null));
            }
        }

        //situations of context B
        for (Situation sitB : c2.getSituations()) {
            if (sitB != null) {
                String key = getSituationKey(sitB.getSource(), sitB.getAuxiliary(), sitB.getPredicate());
                sitWeight = getWeight(key);
                AttributeData sitInA = compareTable.get(key);
                if (sitInA == null) {
                    compareTable.put(key, new AttributeData(sitWeight, null, sitB.getValue()));
                } else {
                    sitInA.contextB = sitB.getValue();
                }
            }
        }

        //measures similarity of situations
        for (String key : compareTable.keySet()) {
            AttributeData compareData = compareTable.get(key);
            if (compareData != null) {
                totalWeight += compareData.weight;
                similarWeight += compareData.getSimilarWeight();
            }
        }

        /*
         * END OF SIMILARITY CALCULATION
         */

        maxSimilarity += totalWeight;
        return (totalWeight != 0 ? similarWeight / totalWeight : 0);

    }

    public double getLargestPossibleValue() {
        return maxSimilarity;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public double getSimilarWeight() {
        return similarWeight;
    }

    /*
     * AUXILIARY CLASS
     */
    private class AttributeData {

        private double weight;
        private Object contextA;
        private Object contextB;

        public AttributeData(double weight, Object contextA, Object contextB) {
            this.weight = weight;
            this.contextA = contextA;
            this.contextB = contextB;
        }

        public double getSimilarWeight() {
            if (contextA == null) {
                return contextB == null ? weight : 0;
            } else {
                return contextA.equals(contextB) ? weight : 0;
            }
        }
    }
}
