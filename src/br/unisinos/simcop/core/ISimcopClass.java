package br.unisinos.simcop.core;

import java.text.DateFormat;
import java.text.DecimalFormat;

/**
 * Generic interface for all objects that can be configured by {@link Parameters}
 *
 * @author tiago
 */
public interface ISimcopClass {
    /**
     * Return true if the objects has Configuration {@link Parameters}
     * @return
     */
    public boolean hasParameters();
    /**
     * Adds or update a {@link Parameter} <i>key</i> with the specified <i>value</i>
     * @param key
     * @param value
     * @return The {@link Parameter} that was created or updated
     */
    public Parameter setParameter(String key, String value);
    /**
     * Returns the {@link Parameter} specified by <i>key</i>
     * @param key
     * @return
     */
    public Parameter getParameter(String key);
    /**
     * Adds or update a {@link Parameter} <i>key</i> with the specified <i>value</i>, and
     * stores the conversion pattern (eg: {@link DateFormat} or {@link DecimalFormat}).
     * @param key
     * @param value
     * @return The {@link Parameter that was created or updated}
     */
    public Parameter setParameter(String key, String value, String pattern);
    /**
     * Shortcut to access a {@link Parameter} without pattern
     * @param key The parameter's UID
     * @return
     */
    public String getSimpleParameter(String key);
    /**
     * returns the configuration {@link Parameters} of this object
     * @return
     */
    public Parameters getParameters();
    /**
     * sets the configuration {@link Parameters} of this object
     * @param parameters
     */
    public void setParameters(Parameters parameters);

    /**
     * returns the list {@link Parameters} expected by the object, with their default values.
     * @return
     */
    public Parameters getDefaultParameters();
    
}
