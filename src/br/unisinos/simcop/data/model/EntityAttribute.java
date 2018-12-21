package br.unisinos.simcop.data.model;

import br.unisinos.simcop.core.dataType.SimcopValue;

public class EntityAttribute {

    private Entity entity;
    private String uid;
    private SimcopValue value;
    private String label;

    /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @param entity the entity to set
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the value
     */
    public SimcopValue getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(SimcopValue value) {
        this.value = value;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
