package br.unisinos.simcop.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parameters {

    private Map<String, Parameter> parameterMap;

    public Parameters() {
        init();
    }

    public Set<String> keySet() {
        if (parameterMap == null) {
            init();
        }
        return parameterMap.keySet();
    }
    
    public List<Parameter> asList() {
        return asList(null);
    }

    /**
     * returns all parameters that starts with "beginName"
     * @param beginName
     * @return 
     */
    public List<Parameter> asList(String beginName) {
        if (parameterMap == null) {
            init();
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (String key : parameterMap.keySet()) {
            if (beginName == null || key.startsWith(beginName)) {
                result.add(parameterMap.get(key));
            } 
        }
        Collections.sort(result, new Comparator<Parameter>() {
            public int compare(Parameter o1, Parameter o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return result;
    }

    public Parameter get(String name) {
        if (parameterMap == null) {
            init();
        }
        return parameterMap.get(name);
    }
    
    public boolean isEmpty() {
        return parameterMap == null || parameterMap.isEmpty();
    }

    public final void init() {
        this.parameterMap = new HashMap<String, Parameter>();
    }

    public Parameter addParameter(String key, String value) {
        return addParameter(key, value, null);
    }

    public Parameter addParameter(String key, String value, String pattern) {
        if (parameterMap == null) {
            init();
        }
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);
        parameter.setPattern(pattern);
        parameterMap.put(key, parameter);

        return parameter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parameters(");
        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                Parameter par = parameterMap.get(key);
                sb.append("{").append(key).append("=").append(par != null ? par.getValue() : "Not Available").append("} ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Parameters other;
        if (obj instanceof Parameters) {
            other = (Parameters)obj;
            Map<String, Parameter> thisPars = this.parameterMap;
            Map<String, Parameter> otherPars = other.parameterMap;
            if (thisPars == null) {
                return otherPars == null;
            } else {
                if (otherPars == null) {
                    return false;
                } else {
                    Iterator<String> thisIterator = thisPars.keySet().iterator();
                    boolean result = true;
                    while (result && thisIterator.hasNext()) {                        
                        String key = thisIterator.next();
                        Parameter thisPar = thisPars.get(key);
                        Parameter otherPar = otherPars.get(key);
                        if (thisPar == null) {
                            result = thisPar == null;
                        } else {
                            if (otherPar == null) {
                                result = false;
                            } else {
                                result = thisPar.equals(otherPar);
                            }
                        }                        
                    }
                    return result;
                }
            }

        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.parameterMap != null ? this.parameterMap.hashCode() : 0);
        return hash;
    }


}
