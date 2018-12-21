package br.unisinos.simcop.data.model;

import br.unisinos.simcop.core.dataType.SimcopValue;
import java.util.HashMap;
import java.util.Map;

public class Entity {

    private Object uid;
    private String name;
    private Map<String, EntityAttribute> attributes;
    private ContextSequence sequence;

    public Entity() {
    }

    public Entity(Object uid) {
        this.uid = uid;
    }

    public Entity(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Object getUid() {
        return uid;
    }

    public void setUid(Object uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, EntityAttribute> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<String, EntityAttribute>();
        }
        return attributes;
    }

    public void setEntityAttribute(Map<String, EntityAttribute> attributes) {
        this.attributes = attributes;
    }

    public void clearAttributes() {
        attributes = new HashMap<String, EntityAttribute>();
    }

    public EntityAttribute addAttribute(String key, String label, Object value) {
        EntityAttribute attr = new EntityAttribute();
        attr.setEntity(this);
        attr.setUid(key);
        attr.setLabel(label);
        attr.setValue(SimcopValue.createFromObject(value));
        getAttributes().put(key, attr);
        return attr;
    }

    public boolean hasSequence() {
        return sequence != null && !sequence.isEmpty();
    }
    public ContextSequence getSequence() {
        if (sequence == null) {
            sequence = new ContextSequence(this);
        }
        return sequence;
    }

    public void setSequence(ContextSequence sequence) {
        this.sequence = sequence;
    }

    public Context getContext(int index) {
        return getSequence().get(index);
    }

    public Context getContext(TimeDescription td) {
         return getSequence().at(td);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.uid != null ? this.uid.hashCode() : 0);
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.uid != other.uid && (this.uid == null || !this.uid.equals(other.uid))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }


}
