package br.unisinos.simcop.data.source;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Base class for load entities and contexts representations.<br/>
 *
 * @author tiago
 */
public abstract class AbstractSequenceSource implements ISequenceSource, ISimcopClass {

    private List<String> errors;
    /**
     * Can be used to store the parameters to be used to connect to the data
     * source and the criteria for selection and identification of the
     * attributes of contexts
     */
    private Parameters parameters;

    public AbstractSequenceSource() {
        this.parameters = getDefaultParameters();
        this.errors = new ArrayList<String>();
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
        return par != null ? par.getValue() : null;
    }

    public Entity loadEntityAndSequence(Object entityUID) throws Exception {
        Entity entity = loadEntity(entityUID);
        if (entity != null) {
            loadIntoSequence(entity.getSequence());
        }
        return entity;
    }

    protected void loadData(Object source, String mapKey) {
    }

    public List<String> getErrors() {
        return errors;

    }

    public void resetErrors() {
        this.errors = new ArrayList<String>();
    }

    public void addError(Throwable ex) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.extractClassName(ste.getClassName()));
        sb.append(" (Line ").append(ste.getLineNumber()).append(") Exception={");
        sb.append(ex.getClass().getSimpleName()).append(": \"").append(ex.getMessage()).append("\"");
        if (ex.getCause() != null) {
            sb.append(" Cause: ").append(ex.getCause().getClass().getSimpleName()).append(": \"").append(ex.getCause().getMessage()).append("\"");
        }
        sb.append("}");
        this.errors.add(sb.toString());
    }

    public void addError(String msg) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.extractClassName(ste.getClassName()));
        sb.append(" (Line ").append(ste.getLineNumber()).append("): ");
        sb.append(msg);
        this.errors.add(sb.toString());
    }

    protected String[] split(String str, String separator) {
        List<String> parts = new ArrayList<String>();
        if (str != null) {
            char s = separator.charAt(0);
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == s) {
                    parts.add(buf.toString());
                    buf.setLength(0);
                } else {
                    buf.append(c);
                }
            }
            if (buf.length() > 0) {
                parts.add(buf.toString());
            }
        }
        String[] result = new String[parts.size()];
        parts.toArray(result);
        return result;
    }

    protected Date loadDate(String value, String format) throws Exception {
        value = value != null ? value.trim() : "";
        if (value.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(value);
        } else {
            return null;
        }

    }

    protected double[] loadPosition(String value, String separator) {
        String[] sPositions = split(value, separator);
        double[] positions = new double[sPositions.length];
        for (int i = 0; i < sPositions.length; i++) {
            if (Utils.isEmpty(sPositions[i])) {
                positions[i] = 0;
            } else {
                positions[i] = Double.parseDouble(sPositions[i].trim());
            }
        }
        return positions;
    }
}
