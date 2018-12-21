package br.unisinos.simcop.data.model;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.dataType.SimcopRange;
import br.unisinos.simcop.core.dataType.SimcopValue;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Context {

    private long index;
    private ContextSequence sequence;
    /**
     * when
     */
    private TimeDescription time;
    /**
     * where
     */
    private LocationDescription location;
    /**
     * what
     */
    private List<Situation> situations;
    private ExtendedData extendedData;
    /**
     * Auxiliary data
     */
    private int frequency;
    private Double probability;

    public void resetSituations() {
        this.situations = new ArrayList<Situation>();
    }

    public void addSituation(Situation situation) {
        if (this.situations == null) {
            resetSituations();
        }
        this.situations.add(situation);
    }

    public Situation addSituation(String predicate, Object value) {
        return addSituation(predicate, SimcopValue.createFromObject(value));
    }

    public Situation addSituation(String predicate, SimcopValue value) {
        return addSituation(null, null, predicate, null, value, null);
    }

    public Situation addSituation(String source, String auxiliary, String predicate, SimcopRange range, SimcopValue value) {
        return addSituation(source, auxiliary, predicate, null, value, null);
    }

    public Situation addSituation(String source, String auxiliary, String predicate, SimcopRange range, SimcopValue value, ExtendedData extendedData) {
        Situation result = new Situation();
        result.setSource(source);
        result.setAuxiliary(auxiliary);
        result.setPredicate(predicate);
        result.setRange(range);
        result.setValue(value);
        result.setExtendedData(null);

        addSituation(result);

        return result;
    }

    /**
     * @return the index
     */
    public long getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(long index) {
        this.index = index;
    }

    /**
     * @return the sequence
     */
    public ContextSequence getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(ContextSequence sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the time
     */
    public TimeDescription getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(TimeDescription time) {
        this.time = time;
    }

    /**
     * @return the location
     */
    public LocationDescription getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(LocationDescription location) {
        this.location = location;
    }

    /**
     * @return the situations
     */
    public List<Situation> getSituations() {
        if (situations == null) {
            situations = new ArrayList<Situation>();
        }
        return situations;
    }

    /**
     * @param situations the situations to set
     */
    public void setSituations(List<Situation> situations) {
        this.situations = situations;
    }

    public ExtendedData getExtendedData() {
        return extendedData;
    }

    public void setExtendedData(ExtendedData extendedData) {
        this.extendedData = extendedData;
    }

    public Situation get(int index) {
        return getSituations().get(index);
    }

    public Situation findSituation(String predicate) {
        Situation result = null;
        if (!Utils.isEmpty(predicate)) {
            Iterator<Situation> it = getSituations().iterator();
            while (result == null && it.hasNext()) {
                Situation test = it.next();
                if (test != null && predicate.equals(test.getPredicate())) {
                    result = test;
                }
            }
        }
        return result;
    }

    public Situation findSituation(String aux, String predicate) {
        Situation result = null;
        if (!Utils.isEmpty(predicate)) {
            Iterator<Situation> it = getSituations().iterator();
            while (result == null && it.hasNext()) {
                Situation test = it.next();
                if (predicate.equals(test.getPredicate())) {
                    if (aux == null) {
                        if (test.getAuxiliary() == null) {
                            result = test;
                        }
                    } else {
                        if (aux.equals(test.getAuxiliary())) {
                            result = test;
                        }
                    }
                }
            }
        }
        return result;
    }

    public Situation findSituation(String source, String aux, String predicate) {
        Situation result = null;
        if (!Utils.isEmpty(predicate)) {
            Iterator<Situation> it = getSituations().iterator();
            while (result == null && it.hasNext()) {
                Situation test = it.next();
                if (predicate.equals(test.getPredicate())) {
                    if (aux == null) {
                        if (test.getAuxiliary() == null) {
                            result = test;
                        }
                    } else {
                        if (aux.equals(test.getAuxiliary())) {
                            if (source == null) {
                                if (test.getSource() == null) {
                                    result = test;
                                }
                            } else {
                                if (source.equals(test.getSource())) {
                                    result = test;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Context other = (Context) obj;
        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
            return false;
        }
        if (this.location != other.location && (this.location == null || !this.location.equals(other.location))) {
            return false;
        }
        if (this.situations != other.situations && (this.situations == null || !this.situations.equals(other.situations))) {
            return false;
        }
        if (this.extendedData != other.extendedData && (this.extendedData == null || !this.extendedData.equals(other.extendedData))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 11 * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = 11 * hash + (this.situations != null ? this.situations.hashCode() : 0);
        hash = 11 * hash + (this.extendedData != null ? this.extendedData.hashCode() : 0);
        return hash;
    }

    public double getProbability() {
        return probability != null ? probability : 0.0;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    private void addSituationsDesc(StringBuilder sb, boolean html) {
        Situation[] sortedSituations = new Situation[getSituations().size()];
        getSituations().toArray(sortedSituations);
        Arrays.sort(sortedSituations, new Comparator<Situation>() {
            public int compare(Situation s1, Situation s2) {
                if (s1 == null || s2 == null) {
                    return -1;
                }
                String ks1 = s1.getKey();
                String ks2 = s2.getKey();                
                if (ks1 != null && ks2 != null) {
                    return ks1.compareTo(ks2);
                } else {
                    return -1;
                }
                
            }
        });

        for (Situation situation : sortedSituations) {
            if (html) {
                sb.append("<b>");
            }
            sb.append(situation.getKey());
            if (html) {
                sb.append("</b>");
            }
            sb.append(": ").append(situation.getValue() != null ? situation.getValue().printValue(false) : "<gap>");
            if (html) {
                sb.append("<br/>");
            } else {
                sb.append("\n");
            }
        }        
    }
    
    public String getSituationDescHtml() {
        StringBuilder sb = new StringBuilder();
        addSituationsDesc(sb, true);
        return sb.toString();
    }

    public String getSituationDesc() {
        StringBuilder sb = new StringBuilder();
        addSituationsDesc(sb, false);
        return sb.toString();        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (time != null) {
            sb.append("Time: ");
            sb.append(time.toString());
            sb.append("\n");
        }
        if (location != null) {
            sb.append("Location: ");
            sb.append(location.toString());
            sb.append("\n");
        }

        addSituationsDesc(sb, false);
        
        if (probability != null) {
            sb.append("Probability: ").append(new DecimalFormat("###,###,##0.00####").format(probability));
        }

        return sb.toString();
    }
}
