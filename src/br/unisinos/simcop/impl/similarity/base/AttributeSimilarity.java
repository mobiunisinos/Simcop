package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.interfaces.similarity.IAttributeSimilarity;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;

public abstract class AttributeSimilarity implements IAttributeSimilarity {   
    /**
     * Value to be returned when the attribute does not exists in one of contexts
     */
    public static final String PAR_MISSING_VALUE = "missingValue";

    private Parameters parameters;
    protected IContextSimilarity callerObject;
    protected double missingValue = 0;

    public AttributeSimilarity() {
        this.parameters = getDefaultParameters();
    }

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_MISSING_VALUE, "0");
        addDefaultParameters(pars);
        return pars;
    }

    public void validateParameters() throws Exception {
        Utils.log("    * Validating Attributes Similarity parameters: " + getClass().getSimpleName());
        Utils.logParameters(this, "      - ");
        String missing = getSimpleParameter(PAR_MISSING_VALUE);
        this.missingValue = (missing != null ? Double.parseDouble(missing) : 0);
        internalValidateParameters();
        Utils.log("    * OK: Validating Attributes Similarity parameters: " + getClass().getSimpleName());        
    }
        

    protected abstract void internalValidateParameters() throws Exception;
    
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public Parameter addParameter(String key, String value) {
        return addParameter(key, value, null);
    }

    public Parameter addParameter(String key, String value, String pattern) {
        if (parameters == null)  {
            parameters = new Parameters();
        }
        return parameters.addParameter(key, value, pattern);
    }

    public Parameters getParameters() {
        if (parameters == null)  {
            parameters = new Parameters();
        }
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
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

    public void setCallerObject(IContextSimilarity callerObject) {
       this.callerObject = callerObject;
    }

    protected abstract void addDefaultParameters(Parameters internalParameters);

}
