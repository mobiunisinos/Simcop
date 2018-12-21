package br.unisinos.simcop.impl.similarity.base;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.interfaces.similarity.IContextSelector;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class SequenceSimilarity implements ISequenceSimilarity {

    private Parameters parameters;
    private Set<Context> uniqueContexts;
    protected IContextSimilarity contextSimilarity;
    protected IContextSelector contextSelector;

    public void validateParameters() throws Exception {
        Utils.log("  * Validating Sequence Similarity parameters: " + getClass().getSimpleName());
        Utils.logParameters(this, "      - ");
        if (contextSimilarity == null) {
            if (isContextSimilarityNeeded()) {
                throw new Exception("Mandatory class not defined: " + getClass().getSimpleName() + " needs a IContextSimilarity task");
            }
        } else {
            contextSimilarity.validateParameters();
        }
        internalValidateParameters();
        Utils.log("  * OK: Sequence Similarity parameters: " + getClass().getSimpleName());
    }

    protected abstract void internalValidateParameters() throws Exception;

    public IContextSimilarity getContextSimilarity() {
        return contextSimilarity;
    }

    public void setContextSimilarity(IContextSimilarity contextSimilarity) {
        this.contextSimilarity = contextSimilarity;
    }

    public SequenceSimilarity() {
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

    protected abstract SimilarityResult internalGetSimilarity(ContextSequence s1, ContextSequence s2) throws Exception;

    public SimilarityResult getSimilarity(ContextSequence s1, ContextSequence s2) throws Exception {
        if (s1 == null) {
            throw new IllegalArgumentException("Sequence 1 undefined");
        }
        if (s2 == null) {
            throw new IllegalArgumentException("Sequence 2 undefined");
        }
        validateParameters();
        Utils.log("  * Getting similarity between " + s1.toString() + " and " + s2.toString());
        Date d1 = new Date();
        SimilarityResult result = internalGetSimilarity(s1, s2);
        double ms = new Date().getTime() - d1.getTime();

        if (result != null) {
            Utils.log("  * End of calculating similarity. Result: " + result.getCalculatedValue() + " (" + result.size() + " pairs). Time: " + (ms / 1000) + " seconds");
        } else {
            Utils.log("  * End of calculating similarity. Result: null. Time: " + (ms / 1000) + " seconds");
        }

        return result;
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

    public void resetUniqueContexts() {
        uniqueContexts = new HashSet<Context>();
    }

    public Set<Context> getUniqueContexts() {
        if (uniqueContexts == null) {
            resetUniqueContexts();
        }
        return uniqueContexts;
    }

    public void setContextSelector(IContextSelector contextSelector) {
        this.contextSelector = contextSelector;
    }
}
