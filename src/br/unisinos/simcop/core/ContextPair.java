package br.unisinos.simcop.core;

import br.unisinos.simcop.data.model.Context;

/**
 * Stores two contexts and its similarity value
 * @author tiago
 */
public class ContextPair {
    private int index;
    private Context c1;
    private Context c2;
    private Double calculatedValue;
    private Object additionalInfo;

    public ContextPair(int index, Context c1, Context c2, Double calculatedValue, Object additionalInfo) {
        this.index = index;
        this.c1 = c1;
        this.c2 = c2;
        this.calculatedValue = calculatedValue;
        this.additionalInfo = additionalInfo;
    }

    public ContextPair(int index, Context c1, Context c2, Double calculatedValue) {
        this.index = index;
        this.c1 = c1;
        this.c2 = c2;
        this.calculatedValue = calculatedValue;
    }

    public Context getC1() {
        return c1;
    }

    public void setC1(Context c1) {
        this.c1 = c1;
    }

    public Context getC2() {
        return c2;
    }

    public void setC2(Context c2) {
        this.c2 = c2;
    }

    public Double getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(Double calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public Object getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pair {A[");
        getDescription(c1, sb);
        sb.append("]\n");
        sb.append("      B[");
        getDescription(c2,sb);
        sb.append("]\n}");
        sb.append(this.calculatedValue);
        return sb.toString();
    }

    private void getDescription(Context c, StringBuilder sb) {
        if (c != null) {
            sb.append(c.getIndex());
            if (c.getTime() != null) {
                sb.append("| at ").append(c.getTime().toString());
            }
            if (c.getLocation() != null) {
                sb.append("| in ").append(c.getLocation().toString());
            }
            if (!c.getSituations().isEmpty()) {
                sb.append("| ").append(c.getSituations().get(0).toString());
                if (c.getSituations().size() > 1) {
                    sb.append("; ...");
                }
            }
        } else {
            sb.append("null");
        }
    }


}
