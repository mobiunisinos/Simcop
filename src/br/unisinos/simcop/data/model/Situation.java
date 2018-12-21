package br.unisinos.simcop.data.model;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.dataType.SimcopCategories;
import br.unisinos.simcop.core.dataType.SimcopRange;
import br.unisinos.simcop.core.dataType.SimcopValue;

//TODO: ontologias
public class Situation {
    /*=================================================================================*
     * IDENTIFICATION
     *=================================================================================*/

    private Context context;
    private String source;
    private String auxiliary;
    private String predicate;

    public Situation() {
    }

    public Situation(String source, String auxiliary, String predicate, SimcopRange range, SimcopValue value, ExtendedData extendedData) {
        this.source = source;
        this.auxiliary = auxiliary;
        this.predicate = predicate;
        this.range = range;
        this.value = value;
        this.extendedData = extendedData;
    }

    public Situation(String predicate,  Object value) {
        this.predicate = predicate;
        this.value = SimcopValue.createFromObject(value);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Situation other = (Situation) obj;
        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }
        if ((this.auxiliary == null) ? (other.auxiliary != null) : !this.auxiliary.equals(other.auxiliary)) {
            return false;
        }
        if ((this.predicate == null) ? (other.predicate != null) : !this.predicate.equals(other.predicate)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 97 * hash + (this.auxiliary != null ? this.auxiliary.hashCode() : 0);
        hash = 97 * hash + (this.predicate != null ? this.predicate.hashCode() : 0);
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    /*=================================================================================*
     * VALUE
     *=================================================================================*/
    private SimcopRange range;
    private SimcopValue value;
    /*=================================================================================*
     * ADDITIONAL DATA
     *=================================================================================*/
    private ExtendedData extendedData;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuxiliary() {
        return auxiliary;
    }

    public void setAuxiliary(String auxiliary) {
        this.auxiliary = auxiliary;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public SimcopRange getRange() {
        return range;
    }

    public void setRange(SimcopRange range) {
        this.range = range;
    }

    public SimcopValue getValue() {
        return value;
    }

    public void setValue(SimcopValue value) {
        this.value = value;
    }

    public SimcopValue set(Object value) {
        this.value = SimcopValue.createFromObject(value);
        return this.value;
    }

    public ExtendedData getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(ExtendedData extendedData) {
        this.extendedData = extendedData;
    }

    public boolean isOverRange() {
        if (range == null) {
            return false;
        } else {
            if (value == null) {
                return true;
            } else {
                if (range instanceof SimcopCategories) {
                    return !range.isInRange(value);
                } else {
                    return (value.compareTo(range.getLast()) > 0);
                }
            }
        }
    }

    public boolean isBelowRange() {
        if (range == null) {
            return false;
        } else {
            if (value == null) {
                return true;
            } else {
                if (range instanceof SimcopCategories) {
                    return !range.isInRange(value);
                } else {
                    return (value.compareTo(range.getFirst()) < 0);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String p = "";
        if (!Utils.isEmpty(source)) {
            sb.append(source);
            p = ".";
        }
        if (!Utils.isEmpty(auxiliary)) {
            sb.append(p);
            sb.append(auxiliary);
            p = ".";
        }
        if (!Utils.isEmpty(predicate)) {
            sb.append(p);
            sb.append(predicate);
        }
        sb.append("=");
        sb.append(value != null ? value.printValue(false) : "<gap>");
        return sb.toString();
    }
    
    public String getKey() {
        StringBuilder sb = new StringBuilder();
        String p = "";
        if (!Utils.isEmpty(source)) {
            sb.append(source);
            p = ".";
        }
        if (!Utils.isEmpty(auxiliary)) {
            sb.append(p);
            sb.append(auxiliary);
            p = ".";
        }
        if (!Utils.isEmpty(predicate)) {
            sb.append(p);
            sb.append(predicate);
        }
        return sb.toString();        
    }
}
