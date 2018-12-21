package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.data.source.AbstractFileSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiago
 */
public class DefaultFileSource extends AbstractFileSource {

    public static final String PAR_SEPARATOR = "columnSeparator";
    public static final String PAR_ENTITY_UID = "entityUID_columnIndex";
    public static final String PAR_ENTITY_NAME = "entityName_columnIndex";
    public static final String PAR_TIME = "time_columnIndex";
    public static final String PAR_TIME_FORMAT = "timeFormat";
    public static final String PAR_LOCATION_NAME = "locationName_columnIndex";
    public static final String PAR_LOCATION_CATEGORY = "locationCategory_columnIndex";
    public static final String PAR_LOCATION_POSITION = "locationPosition_columnIndex";
    public static final String PAR_POSITION_SEPARATOR = "separatorForPositionArray";
    public static final String PAR_SITUATION = "situation_";
    public static final String PAR_PREDICATE_NAME = "predicateInColumn_";

    public DefaultFileSource() {
    }

    public DefaultFileSource(File file) {
        super(file);
    }

    @Override
    protected void internalDefaultParameters(Parameters pars) {
        pars.addParameter(PAR_SEPARATOR, ";");
        pars.addParameter(PAR_ENTITY_UID, "-1");
        pars.addParameter(PAR_ENTITY_NAME, "-1");
        pars.addParameter(PAR_TIME, "-1");
        pars.addParameter(PAR_TIME_FORMAT, "dd/MM/yyyy HH:mm:ss.S");
        pars.addParameter(PAR_LOCATION_CATEGORY, "-1");
        pars.addParameter(PAR_LOCATION_NAME, "-1");
        pars.addParameter(PAR_LOCATION_POSITION, "-1");
        pars.addParameter(PAR_POSITION_SEPARATOR, ",");
        pars.addParameter(PAR_SITUATION, "-1");
        pars.addParameter(PAR_PREDICATE_NAME, "situation");
    }

    private int indexFor(String par) {
        String s = getSimpleParameter(par);
        s = (s != null ? s.trim() : "");
        if (s.length() > 0) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public List<Entity> getListEntities(Object... entityNameFilter) throws Exception {
        resetReader();

        Map<String, Entity> cache = new HashMap<String, Entity>();
        List<Entity> entities = new ArrayList<Entity>();
        if (isConnected()) {
            String line;
            String colSeparator = getSimpleParameter(PAR_SEPARATOR);
            int idxUid = indexFor(PAR_ENTITY_UID);
            int idxName = indexFor(PAR_ENTITY_NAME);

            while ((line = fileReader.readLine()) != null) {
                String[] columns = split(line, colSeparator);
                //
                String uid = null;
                String name = null;
                if (idxUid >= 0 && idxUid < columns.length) {
                    uid = columns[idxUid];
                }
                if (idxName >= 0 && idxName < columns.length) {
                    name = columns[idxName];
                }
                String key = uid != null ? uid : name;
                if (key != null) {
                    Entity entity = cache.get(key);
                    if (entity == null) {
                        entity = new Entity(uid, name);
                        cache.put(key, entity);
                        //
                        boolean ok = false;
                        if (name != null && !Utils.isEmpty(entityNameFilter)) {
                            for (int i = 0; i < entityNameFilter.length && !ok; i++) {
                                Object filter = entityNameFilter[i];
                                if (filter != null) {
                                    ok = name.contains(filter.toString());
                                }
                            }
                        } else {
                            ok = !Utils.isEmpty(name);
                        }
                        if (ok) {
                            entities.add(entity);
                        }
                    }

                }
            }

            Comparator<Entity> comparator = new Comparator<Entity>() {
                public int compare(Entity o1, Entity o2) {
                    if (o1 == null || o2 == null || o1.getName() == null) {
                        return -1;
                    } else {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                }
            };
            Collections.sort(entities, comparator);
            return entities;
        } else {
            return null;
        }
    }

    public Entity loadEntity(Object objUid) throws Exception {
        Entity result = null;
        if (objUid != null) {
            String uid = objUid.toString();
            resetReader();
            String line;
            String colSeparator = getSimpleParameter(PAR_SEPARATOR);
            int idxUid = indexFor(PAR_ENTITY_UID);
            int idxName = indexFor(PAR_ENTITY_NAME);
            if (idxUid >= 0) {
                while (result == null && (line = fileReader.readLine()) != null) {
                    String[] columns = split(line, colSeparator);
                    if (idxUid < columns.length) {
                        String uidAtLine = columns[idxUid];
                        if (uid.equals(uidAtLine)) {
                            String name = null;
                            if (idxName >= 0 && idxName < columns.length) {
                                name = columns[idxName];
                            }
                            result = new Entity(uid, name);
                        }
                    }
                }
            }
        }

        return result;

    }

    public void loadIntoSequence(ContextSequence sequence) throws Exception {
        if (sequence != null && sequence.getEntity() != null && sequence.getEntity().getUid() != null) {
            resetReader();
            sequence.resetContexts();
            String uid = sequence.getEntity().getUid().toString();
            resetReader();
            String line;
            String colSeparator = getSimpleParameter(PAR_SEPARATOR);
            int idxUid = indexFor(PAR_ENTITY_UID);
            if (idxUid >= 0) {
                int index = -1;
                while ((line = fileReader.readLine()) != null) {
                    String[] columns = split(line, colSeparator);
                    if (idxUid < columns.length) {
                        String uidAtLine = columns[idxUid];
                        if (uid.equals(uidAtLine)) {
                            index++;
                            //load context data
                            Context ctx = loadContext(index, columns);
                            ctx.setIndex(index);
                            ctx.setSequence(sequence);
                            //----                            
                            sequence.addContext(ctx);
                        }
                    }
                }
            } else {
                throw new Exception("You must set the column index for Entity.UID");
            }

        }
    }

    private boolean testRange(int index, String[] columns) {
        return columns != null && index >= 0 && index < columns.length;
    }

    //context
    private Context loadContext(int sequence, String[] columns) throws Exception {
        Context ctx = new Context();
        loadTime(ctx, sequence, columns);
        loadLocation(ctx, columns);
        loadSituations(ctx, columns);
        return ctx;
    }

    //time
    private void loadTime(Context ctx, int sequence, String[] columns) throws Exception {
        int idxTime = indexFor(PAR_TIME);
        TimeDescription td;
        if (testRange(idxTime, columns)) {
            Date time = loadDate(columns[idxTime], getSimpleParameter(PAR_TIME_FORMAT));
            td = new TimeDescription(time);
            td.setIndex(sequence);
        } else {
            td = new TimeDescription(sequence);
        }
        ctx.setTime(td);
    }

    //location
    private void loadLocation(Context ctx, String[] columns) throws Exception {
        int idxCategory = indexFor(PAR_LOCATION_CATEGORY);
        int idxName = indexFor(PAR_LOCATION_NAME);

        LocationDescription ld = null;
        if (testRange(idxCategory, columns)) {
            ld = new LocationDescription();
            ld.setCategory(columns[idxCategory]);
        }
        if (testRange(idxName, columns)) {
            if (ld == null) {
                ld = new LocationDescription();
            }
            ld.setName(columns[idxName]);
        }


        String pPosition = getSimpleParameter(PAR_LOCATION_POSITION);
        if (!Utils.isEmpty(pPosition)) {
            if (ld == null) {
                ld = new LocationDescription();
            }

            if (pPosition.contains(",")) {
                String[] sIndexes = pPosition.split(",");
                double[] position = new double[sIndexes.length];
                for (int iCoord = 0; iCoord < sIndexes.length; iCoord++) {
                    int colIndex = Integer.parseInt(sIndexes[iCoord].trim());
                    if (testRange(colIndex, columns)) {
                        position[iCoord] = Double.parseDouble(columns[colIndex]);
                    }
                }
                ld.setPosition(position);

            } else {
                int idxPosition = indexFor(PAR_LOCATION_POSITION);

                if (testRange(idxPosition, columns)) {
                    String separator = getSimpleParameter(PAR_POSITION_SEPARATOR);
                    double[] position = loadPosition(columns[idxPosition], separator);
                    ld.setPosition(position);
                }
            }
            ctx.setLocation(ld);
        }
    }

    //situations
    private void loadSituations(Context ctx, String[] columns) throws Exception {
        List<Parameter> pSituations = getParameters().asList(PAR_SITUATION);
        if (!Utils.isEmpty(pSituations)) {
            for (Parameter pSituation : pSituations) {
                String pName = pSituation.getKey();
                String pValue = getSimpleParameter(pName);
                if (pValue == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parameters: \n");
                    for (String key : getParameters().keySet()) {
                        sb.append(key).append("=[").append(getSimpleParameter(key)).append("]\n");
                    }
                    
                    
                    throw new Exception("Internal Error: Parameter not found: '" + pName + "'");
                }
                int columnIndex = Integer.parseInt(pValue.trim());
                if (testRange(columnIndex, columns)) {
                    //get value
                    String object = columns[columnIndex];

                    //get situation index for obtain the predicate name
                    int idxChar = pName.indexOf("_");
                    if (idxChar >= pName.length()) {
                        throw new Exception("Invalid situation index: '" + pName + "'.\nExpected: the index of the situation: " + PAR_SITUATION + "0, " + PAR_SITUATION + "1, " + PAR_SITUATION + "...");
                    }
                    String sitIndex = pName.substring(idxChar + 1).trim();
                    String predicate = getSimpleParameter(PAR_PREDICATE_NAME + sitIndex);

                    //add situation to context
                    ctx.addSituation(predicate, object);
                }

            }

        }
    }
}
