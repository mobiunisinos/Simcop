package br.unisinos.simcop.data.model;

public class ExtendedData {

    private Situation situation;
    private Context context;
    private double confidence;
    private double support;
    private String creator;
    private String owner;
    private String evidence;
    private String group;
    private String access;
    private String purpose;
    private String retention;
    private String privacyKey;
    private String notes;

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRetention() {
        return retention;
    }

    public void setRetention(String retention) {
        this.retention = retention;
    }

    public String getPrivacyKey() {
        return privacyKey;
    }

    public void setPrivacyKey(String privacyKey) {
        this.privacyKey = privacyKey;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExtendedData other = (ExtendedData) obj;
        if (Double.doubleToLongBits(this.confidence) != Double.doubleToLongBits(other.confidence)) {
            return false;
        }
        if (Double.doubleToLongBits(this.support) != Double.doubleToLongBits(other.support)) {
            return false;
        }
        if ((this.creator == null) ? (other.creator != null) : !this.creator.equals(other.creator)) {
            return false;
        }
        if ((this.owner == null) ? (other.owner != null) : !this.owner.equals(other.owner)) {
            return false;
        }
        if ((this.evidence == null) ? (other.evidence != null) : !this.evidence.equals(other.evidence)) {
            return false;
        }
        if ((this.group == null) ? (other.group != null) : !this.group.equals(other.group)) {
            return false;
        }
        if ((this.access == null) ? (other.access != null) : !this.access.equals(other.access)) {
            return false;
        }
        if ((this.purpose == null) ? (other.purpose != null) : !this.purpose.equals(other.purpose)) {
            return false;
        }
        if ((this.retention == null) ? (other.retention != null) : !this.retention.equals(other.retention)) {
            return false;
        }
        if ((this.privacyKey == null) ? (other.privacyKey != null) : !this.privacyKey.equals(other.privacyKey)) {
            return false;
        }
        if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.confidence) ^ (Double.doubleToLongBits(this.confidence) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.support) ^ (Double.doubleToLongBits(this.support) >>> 32));
        hash = 59 * hash + (this.creator != null ? this.creator.hashCode() : 0);
        hash = 59 * hash + (this.owner != null ? this.owner.hashCode() : 0);
        hash = 59 * hash + (this.evidence != null ? this.evidence.hashCode() : 0);
        hash = 59 * hash + (this.group != null ? this.group.hashCode() : 0);
        hash = 59 * hash + (this.access != null ? this.access.hashCode() : 0);
        hash = 59 * hash + (this.purpose != null ? this.purpose.hashCode() : 0);
        hash = 59 * hash + (this.retention != null ? this.retention.hashCode() : 0);
        hash = 59 * hash + (this.privacyKey != null ? this.privacyKey.hashCode() : 0);
        hash = 59 * hash + (this.notes != null ? this.notes.hashCode() : 0);
        return hash;
    }

    
}
