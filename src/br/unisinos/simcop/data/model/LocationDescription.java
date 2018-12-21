package br.unisinos.simcop.data.model;

import br.unisinos.simcop.Utils;
import java.util.Arrays;

public class LocationDescription implements Comparable {

    private long uid;
    private int index;
    private String name;
    private String category;
    private String ontologyUID;
    private String ontologyClass;
    private double[] position;

    public LocationDescription() {
    }

    public LocationDescription(long uid, int index, String name, String category, String ontologyUID, String ontologyClass, double[] position) {
        this.uid = uid;
        this.index = index;
        this.name = name;
        this.category = category;
        this.ontologyUID = ontologyUID;
        this.ontologyClass = ontologyClass;
        this.position = position;
    }

    public LocationDescription(double... coords) {
        position = Arrays.copyOf(coords, coords.length);
    }

    public LocationDescription(String name) {
        this.name = name;
    }

    public LocationDescription(String name, String category, String ontologyUID, String ontologyClass) {
        this.name = name;
        this.category = category;
        this.ontologyClass = ontologyClass;
        this.ontologyUID = ontologyUID;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOntologyUID() {
        return ontologyUID;
    }

    public void setOntologyUID(String ontologyUID) {
        this.ontologyUID = ontologyUID;
    }

    public String getOntologyClass() {
        return ontologyClass;
    }

    public void setOntologyClass(String ontologyClass) {
        this.ontologyClass = ontologyClass;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Utils.isEmpty(name)) {
            sb.append(name);
            sb.append(" ");
        }
        if (!Utils.isEmpty(category)) {
            sb.append("(");
            sb.append(category);
            sb.append(") ");
        }
        if (!Utils.isEmpty(ontologyClass)) {
            sb.append("ontology: ['");
            sb.append(ontologyClass);
            sb.append("' in '");
            sb.append(ontologyUID);
            sb.append("'] ");
        }
        if (position != null) {
            sb.append("pos=");
            for (int i = 0; i < position.length; i++) {
                sb.append(position[i]);
                if (i < position.length - 1) {
                    sb.append(",");
                }
            }
        }


        return sb.toString();
    }

    public int compareTo(Object o) {
        if (o == null || !(o instanceof LocationDescription)) {
            return -1;
        } else {
            LocationDescription other = (LocationDescription) o;
            if (name != null) {
                return name.compareTo(other.name);
            }
            if (category != null) {
                return category.compareTo(category);
            }
            if (this.position != null && other.position != null && this.position.length == other.position.length) {
                int result = 0;
                for (int d = 0; d < position.length; d++) {
                    double d1 = this.position[d];
                    double d2 = other.position[d];
                    result += (d1 < d2 ? -1 : (d1 > d2 ? +1 : 0));
                }
                return result;
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocationDescription other = (LocationDescription) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.category == null) ? (other.category != null) : !this.category.equals(other.category)) {
            return false;
        }
        if ((this.ontologyUID == null) ? (other.ontologyUID != null) : !this.ontologyUID.equals(other.ontologyUID)) {
            return false;
        }
        if ((this.ontologyClass == null) ? (other.ontologyClass != null) : !this.ontologyClass.equals(other.ontologyClass)) {
            return false;
        }
        if (!Arrays.equals(this.position, other.position)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 59 * hash + (this.ontologyUID != null ? this.ontologyUID.hashCode() : 0);
        hash = 59 * hash + (this.ontologyClass != null ? this.ontologyClass.hashCode() : 0);
        hash = 59 * hash + Arrays.hashCode(this.position);
        return hash;
    }
}
