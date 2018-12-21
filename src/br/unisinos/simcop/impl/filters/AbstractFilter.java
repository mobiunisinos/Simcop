package br.unisinos.simcop.impl.filters;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;

/**
 *
 * @author tiago
 */
public abstract class AbstractFilter implements ISimcopClass {

    private Parameters parameters;

    public AbstractFilter() {
        this.parameters = getDefaultParameters();
    }

    /**
     *
     * @return the {@link #parameters}
     */
    public Parameters getParameters() {
        if (parameters == null) {
            parameters = new Parameters();
        }
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
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

}
