package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ContextSimilarity implements IContextSimilarity {

    private ISequenceSimilarity callerObject;
    private Parameters parameters;
    private Set<Integer> uniqueHashes;
    protected ITimeSimilarity TDFunction;
    protected ILocationSimilarity LDFunction;
    protected IExtendedDataSimilarity EDFunction;
    protected Map<String, ISituationSimilarity> SDFunctions;

    public ContextSimilarity() {
        this.parameters = getDefaultParameters();
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public Parameter addParameter(String key, String value) {
        return addParameter(key, value, null);
    }

    public Parameter addParameter(String key, String value, String pattern) {
        if (parameters == null) {
            parameters = new Parameters();
        }
        return parameters.addParameter(key, value, pattern);
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public void setCallerObject(ISequenceSimilarity callerObject) {
        this.callerObject = callerObject;
    }

    public Parameter setParameter(String key, String value) {
        return setParameter(key, value, null);
    }

    public Parameter setParameter(String key, String value, String pattern) {
        return getParameters().addParameter(key, value, pattern);
    }

    public Parameter getParameter(String key) {
        return getParameters().get(key);
    }

    public String getSimpleParameter(String key) {
        Parameter par = getParameters().get(key);
        return par.getValue();
    }

    public ISequenceSimilarity getCallerObject() {
        return callerObject;
    }

    public void resetUniqueHashes() {
        uniqueHashes = new HashSet<Integer>();
    }

    public Set<Integer> getUniqueHashes() {
        if (uniqueHashes == null) {
            resetUniqueHashes();
        }
        return uniqueHashes;
    }

    public int getHashForName(Situation sit) {
        int hash = 5;
        hash = 97 * hash + (sit.getSource() != null ? sit.getSource().hashCode() : 0);
        hash = 97 * hash + (sit.getAuxiliary() != null ? sit.getAuxiliary().hashCode() : 0);
        hash = 97 * hash + (sit.getPredicate() != null ? sit.getPredicate().hashCode() : 0);
        return hash;
    }

    public double getSimilarity(Context c1, Context c2) throws Exception {        
        return internalGetSimilarity(c1, c2);
    }

    public void validateParameters() throws Exception {
        Utils.log("    * Validating Context Similarity parameters: " + getClass().getSimpleName());
        Utils.logParameters(this, "      - ");
        if (TDFunction != null) {
            TDFunction.validateParameters();
        }
        if (LDFunction != null) {
            LDFunction.validateParameters();
        }
        if (SDFunctions != null && !SDFunctions.isEmpty()) {
            for (String sitKey : SDFunctions.keySet()) {
                ISituationSimilarity fun = SDFunctions.get(sitKey);
                if (fun != null) {
                    fun.validateParameters();
                }
            }
        }
        if (EDFunction != null) {
            EDFunction.validateParameters();
        }        
        internalValidateParameters();
        Utils.log("    * OK: Validating Context Similarity parameters: " + getClass().getSimpleName());
    }

    protected abstract void internalValidateParameters() throws Exception;

    protected abstract double internalGetSimilarity(Context c1, Context c2) throws Exception;

    public void setTDSimilarity(ITimeSimilarity TDFunction) {
        this.TDFunction = TDFunction;
    }

    public void setLDSimilarity(ILocationSimilarity LDFunction) {
        this.LDFunction = LDFunction;
    }

    public void setEDSimilarity(IExtendedDataSimilarity EDFunction) {
        this.EDFunction = EDFunction;
    }

    public void clearSDSimilarityMap() {
        getSDFunctions().clear();
    }

    public void addSDSimilarity(String sourceName, String auxName, String predicateName, ISituationSimilarity SDFunction) throws IllegalArgumentException {
        String key;
        if (sourceName == null && auxName == null && predicateName == null) {
            key = getDefaultSDKey();
        } else {
            key = getSDKey(sourceName, auxName, predicateName);
        }
        getSDFunctions().put(key, SDFunction);
    }

    public ITimeSimilarity getTDSimilarity() {
        return TDFunction;
    }

    public ILocationSimilarity getLDSimilarity() {
        return LDFunction;
    }

    public IExtendedDataSimilarity getEDSimilarity() {
        return EDFunction;
    }

    public ISituationSimilarity getSituationSimilarity(String sourceName, String auxName, String predicateName) {
        String key = getSDKey(sourceName, auxName, predicateName);
        return getSituationSimilarity(key);
    }

    public ISituationSimilarity getSituationSimilarity(String SDKey) {
        ISituationSimilarity result = getSDFunctions().get(SDKey);
        if (result == null) {
            return getSDFunctions().get(getDefaultSDKey());
        } else {
            return result;
        }
    }

    protected String getDefaultSDKey() {
        StringBuilder key = new StringBuilder();
        key.append(SituationSimilarity.KEY);
        key.append(SituationSimilarity.DEFAULT_KEY);
        return key.toString();
    }

    protected String getSDKey(String sourceName, String auxName, String predicateName) {
        StringBuilder key = new StringBuilder();
        key.append(SituationSimilarity.KEY);
        key.append("{");
        key.append(sourceName != null ? sourceName : "");
        key.append("}{");
        key.append(auxName != null ? auxName : "");
        key.append("}{");
        key.append(predicateName != null ? predicateName : "");
        key.append("}");
        return key.toString();
    }

    public boolean isSDKeyValid(String key) {
        if (key == null) {
            return true;
        } else {
            return !key.contains("{")
                    && !key.contains("}");
        }
    }

    protected Map<String, ISituationSimilarity> getSDFunctions() {
        if (SDFunctions == null) {
            SDFunctions = new HashMap<String, ISituationSimilarity>();
        }
        return SDFunctions;
    }

    protected Map<String, Situation[]> unionSituations(Context c1, Context c2) {
        //situations of context A
        Map<String, Situation[]> unionTable = new HashMap<String, Situation[]>();

        for (Situation sitA : c1.getSituations()) {
            if (sitA != null) {
                String key = getSDKey(sitA.getSource(), sitA.getAuxiliary(), sitA.getPredicate());
                unionTable.put(key, new Situation[]{sitA, null});
            }
        }

        //situations of context B
        for (Situation sitB : c2.getSituations()) {
            if (sitB != null) {
                String key = getSDKey(sitB.getSource(), sitB.getAuxiliary(), sitB.getPredicate());
                Situation[] sitPair = unionTable.get(key);
                if (sitPair == null) {
                    unionTable.put(key, new Situation[]{null, sitB});
                } else {
                    sitPair[1] = sitB;
                }
            }
        }

        return unionTable;
    }

    protected List<Double> getAttributesSimilarities(Context c1, Context c2)  throws Exception {
        if (c1 == null || c2 == null) {
            return null;
        }
        List<Double> result = new ArrayList<Double>();

        if (TDFunction != null) {
            result.add(TDFunction.getSimilarity(c1.getTime(), c2.getTime()));
        }
        if (LDFunction != null) {
            result.add(LDFunction.getSimilarity(c1.getLocation(), c2.getLocation()));
        }
        if (SDFunctions != null && !SDFunctions.isEmpty()) {
            Map<String, Situation[]> allSituations = unionSituations(c1, c2);
            if (allSituations != null) {
                for (String sitKey : allSituations.keySet()) {
                    Situation[] pair = allSituations.get(sitKey);
                    if (pair != null && pair.length == 2) {
                        ISituationSimilarity SDFunction = getSituationSimilarity(sitKey);
                        if (SDFunction != null) {
                            result.add(SDFunction.getSimilarity(pair[0], pair[1]));
                        }
                    }
                }
            }
        }
        if (EDFunction != null) {
            result.add(EDFunction.getSimilarity(c1.getExtendedData(), c2.getExtendedData()));
        }
        return result;
    }

    /**
     * Maps the numeric value's attributes of two context as coordinates in a n-Dimensional space.
     * @param c1
     * @param c2
     * @return
     * @throws Exception
     */
    protected List<double[]> getAttributesAsCoord(Context c1, Context c2, boolean time, boolean position, boolean situations)  throws Exception {
        if (c1 == null || c2 == null) {
            return null;
        }
        List<double[]> result = new ArrayList<double[]>();

        if (time) {
            Long v1 = c1.getTime() != null ? c1.getTime().asLong() : null;
            Long v2 = c2.getTime() != null ? c2.getTime().asLong() : null;

            result.add( new double[] { v1 != null ? v1 : 0,  v2 != null ? v2 : 0} );           
        }
        if (position) {
            double[] coordA = c1.getLocation() != null ? c1.getLocation().getPosition() : null;
            double[] coordB = c2.getLocation() != null ? c2.getLocation().getPosition() : null;

            if (coordA != null && coordB != null && coordA.length == coordB.length) {
                for (int i = 0; i < coordA.length; i++) {
                    result.add( new double[] { coordA[i], coordB[i]} );
                }
            }

        }
        if (situations) {
            Map<String, Situation[]> allSituations = unionSituations(c1, c2);
            if (allSituations != null) {
                for (String sitKey : allSituations.keySet()) {
                    Situation[] pair = allSituations.get(sitKey);
                    if (pair != null && pair.length == 2) {
                        Situation sitA = pair[0];
                        Situation sitB = pair[1];
                        if (sitA != null && sitB != null) {
                            Number valueA = Utils.getAsNumber(sitA.getValue());
                            Number valueB = Utils.getAsNumber(sitB.getValue());
                            if (valueA != null && valueB != null) {
                                result.add( new double[] { valueA.doubleValue(), valueB.doubleValue()} );
                            }
                        }                        
                    }
                }
            }
        }
        return result;
    }

    /**
     * Compares (.equals()) two objects.
     * @param atr1
     * @param atr2
     * @return
     * An int array of length 3:<br/>
     *   result[0] = 1 if atr1.equals(atr2);
     *   result[1] = 0 if tests atr1 is empty (null);<br/>
     *   result[2] = 0 if tests atr2 is empty (null);<br/>
     *
     */
    protected int[] isSame(Object atr1, Object atr2) {
        int[] result = new int[3];
        result[2] = atr2 == null ? 0 : 1;
        result[1] = atr1 == null ? 0 : 1;
        result[0] = atr1 == null ? 0 : atr1.equals(atr2) ? 1 : 0;
        return result;
    }

}
