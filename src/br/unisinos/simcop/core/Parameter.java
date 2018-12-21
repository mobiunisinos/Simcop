package br.unisinos.simcop.core;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parameter implements Cloneable {

    private String key;
    private String value;
    private String pattern;

    @Override
    protected Parameter clone() {
        Parameter newPar = new Parameter();
        newPar.key = key;
        newPar.value = value;
        newPar.pattern = pattern;
        return newPar;
    }

    public boolean hasValue() {
        return value != null && value.trim().length() > 0;
    }

    public Number getAsNumber() throws ParseException {
        if (!hasValue()) {
            return null;
        } else {
            if (pattern != null && pattern.trim().length() > 0) {
                DecimalFormat df = new DecimalFormat(pattern);
                return df.parse(value);
            } else {
                if (value.contains(".")) {
                    return Double.parseDouble(value);
                } else {
                    return Long.parseLong(value);
                }
            }
        }
    }

    public Date getAsDate() throws ParseException {
        if (!hasValue()) {
            return null;
        } else {
            if (pattern != null && pattern.trim().length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(value);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat();
                return sdf.parse(value);
            }
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Parameter other;
        if (obj instanceof Parameter) {
            other = (Parameter) obj;
            boolean result;
            if (this.key == null) {
                result = other.key == null;
            } else {
                result = this.key.equals(other.key);
            }
            if (result) {
                if (this.value == null) {
                    result = other.value == null;
                } else {
                    result = this.value.equals(other.value);
                }
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.key != null ? this.key.hashCode() : 0);
        hash = 89 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(":");
        sb.append(value);
        if (pattern != null && pattern.trim().length() > 0)  {
            sb.append(" (pattern=\"");
            sb.append(pattern);
            sb.append("\")");
        }
        return sb.toString();
    }


}
