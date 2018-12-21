package br.unisinos.simcop.data.model;

import br.unisinos.simcop.core.dataType.SimcopRange;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.tools.JavaFileManager.Location;

public class ContextSequence implements Cloneable {

    private Entity entity;
    private List<Context> contexts;
    private SimcopRange timeRange;
    private double[][] positionGrid;
    private Map<String, List<Location>> locationMap;
    private Map<String, List<Situation>> situationMap;
    private String notes;
    //TODO manter?
    private boolean autoBuildTimeRange;
    private boolean autoBuildPositionGrid;
    private boolean autoBuildLocationMap;
    private boolean autoBuildSituationMap;

    public ContextSequence(Entity entity) {
        this.entity = entity;
    }

    public void refreshTimeRange() {
        //TODO finish this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void refreshPositionGrid() {
        //TODO finish this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void refreshLocationMap() {
        //TODO finish this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public void refreshSituationMap() {
        //TODO finish this method
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean hasNotes() {
        return notes != null && notes.trim().length() > 0;
    }



    /* TODO: retorna Location ou contexts?
     *
    public List<Location> getOtherLocations(LocationDescription location) {
    if (locationMap != null)  {
    return locationMap.get(location.hashMap());
    } else {
    return null;
    }
    }

     */
    //TODO: metodos getOtherSituations e getAdjacentLocations -> retornar os objetos ou o contexto?
    public void resetContexts() {
        this.contexts = new ArrayList<Context>();
    }

    public void addContext(Context ctx) {
        if (this.contexts == null) {
            resetContexts();
        }
        if (ctx != null) {
            ctx.setSequence(this);
        }
        this.contexts.add(ctx);
    }

    @Override
    public ContextSequence clone() throws CloneNotSupportedException {
        ContextSequence result = new ContextSequence(entity);
        result.contexts = new ArrayList<Context>();       
        result.contexts.addAll(getContexts());
        result.notes = this.notes;
        return result;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Context> getContexts() {
        if (contexts == null) {
            contexts = new ArrayList<Context>();
        }
        return contexts;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

    public SimcopRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(SimcopRange timeRange) {
        this.timeRange = timeRange;
    }

    public double[][] getPositionGrid() {
        return positionGrid;
    }

    public void setPositionGrid(double[][] positionGrid) {
        this.positionGrid = positionGrid;
    }

    public Map<String, List<Location>> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(Map<String, List<Location>> locationMap) {
        this.locationMap = locationMap;
    }

    public Map<String, List<Situation>> getSituationMap() {
        return situationMap;
    }

    public void setSituationMap(Map<String, List<Situation>> situationMap) {
        this.situationMap = situationMap;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAutoBuildTimeRange() {
        return autoBuildTimeRange;
    }

    public void setAutoBuildTimeRange(boolean autoBuildTimeRange) {
        this.autoBuildTimeRange = autoBuildTimeRange;
    }

    public boolean isAutoBuildPositionGrid() {
        return autoBuildPositionGrid;
    }

    public void setAutoBuildPositionGrid(boolean autoBuildPositionGrid) {
        this.autoBuildPositionGrid = autoBuildPositionGrid;
    }

    public boolean isAutoBuildLocationMap() {
        return autoBuildLocationMap;
    }

    public void setAutoBuildLocationMap(boolean autoBuildLocationMap) {
        this.autoBuildLocationMap = autoBuildLocationMap;
    }

    public boolean isAutoBuildSituationMap() {
        return autoBuildSituationMap;
    }

    public void setAutoBuildSituationMap(boolean autoBuildSituationMap) {
        this.autoBuildSituationMap = autoBuildSituationMap;
    }

    public void clear() {
        contexts = new ArrayList<Context>();
    }

    public int size() {
        return getContexts().size();
    }

    public boolean isEmpty() {
        return getContexts().isEmpty();
    }

    public Context get(int index) {
        return getContexts().get(index);
    }

    public Context at(TimeDescription td) {
        Context result = null;
        if (td != null) {
            Iterator<Context> it = getContexts().iterator();
            while (result == null && it.hasNext()) {
                Context test = it.next();
                if (td.equals(test.getTime())) {
                    result = test;
                }
            }
        }
        return result;
    }

    public Context findEqual(Context other) {
        Context result = null;
        Iterator<Context> it = getContexts().iterator();
        while (it.hasNext() && result == null) {
            Context ctx = it.next();
            if (ctx.equals(other)) {
                result = ctx;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sequence of ");
        if (entity != null) {
            sb.append(entity.getUid());
            sb.append("-");
            sb.append(entity.getName());
        } else {
            sb.append("<entity undefined>");
        }
        sb.append(" (");
        sb.append(getContexts().size()).append(" contexts)");
        
        return sb.toString();
    }
    
    
    
}
