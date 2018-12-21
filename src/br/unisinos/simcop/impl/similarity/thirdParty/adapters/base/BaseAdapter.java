package br.unisinos.simcop.impl.similarity.thirdParty.adapters.base;

import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;

/**
 *
 * @author tiago
 */
public abstract class BaseAdapter implements ISimilarity {
    protected ISimilarity similarityFunction;

    public BaseAdapter(ISimilarity similarityFunction) {
        this.similarityFunction = similarityFunction;
    }


    public boolean isDistanceFunction() {
        return similarityFunction != null ? similarityFunction.isDistanceFunction() : false;
    }

    public void validateParameters() throws Exception {
        if (similarityFunction == null) {
            throw new Exception("SIMCOP Internal Error: Similarity Function not defined for " + getClass().getSimpleName());
        }
        similarityFunction.validateParameters();
    }

    public boolean hasParameters() {
        return similarityFunction.hasParameters();
    }

    public Parameter setParameter(String key, String value) {
        return similarityFunction.setParameter(key, value);
    }

    public Parameter getParameter(String key) {
        return similarityFunction.getParameter(key);
    }

    public Parameter setParameter(String key, String value, String pattern) {
        return similarityFunction.setParameter(key, value, pattern);
    }

    public String getSimpleParameter(String key) {
        return similarityFunction.getSimpleParameter(key);
    }

    public Parameters getParameters() {
        return similarityFunction.getParameters();
    }

    public void setParameters(Parameters parameters) {
        similarityFunction.setParameters(parameters);
    }

    public Parameters getDefaultParameters() {
        return similarityFunction.getDefaultParameters();
    }

}
