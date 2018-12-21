package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.data.source.AbstractJdbcSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample class that loads an entity and its contexts from a database.
 * <br/><br/>
 * Each record returned by the SQL Query {@link #P_SEQUENCE_QUERY} corresponds to a unique context.
 * The description of the context ({@link Situation}) is read from the table fields, where the field name (or alias)
 * corresponds to the {@link Situation#predicate}, and the field value corresponds to the {@link Situation#value}.
 * The first {@link java.util.Date} field was converted to a {@link TimeDescription} representation.
 * <br/><br/>
 * The {@link Entity#uid} is provided by the application, and the {@link Entity#name} is read from the first {@link java.lang.String} field.
 * The subsequent fields corresponds to the {@link Entity#attributes}.
 * 
 * @author tiago
 */
public class SimpleJdbcSource extends AbstractJdbcSource {

    public static final String P_DRIVER = "driver";
    public static final String P_URL = "url";
    public static final String P_ENTITIES_QUERY = "listEntitiesQuery";
    public static final String P_ENTITY_QUERY = "entityQuery";
    public static final String P_SEQUENCE_QUERY = "contextQuery";
    public static final String P_USERNAME = "connectUser";
    public static final String P_PASSWORD = "connectPassword";
    private String connectionUserName;
    private String connectionPassword;

    public SimpleJdbcSource() {
    }

    public SimpleJdbcSource(String connectionUserName, String connectionPassword) {
        this.connectionUserName = connectionUserName;
        this.connectionPassword = connectionPassword;
    }

    public Parameters getDefaultParameters() {
        Parameters parameters = new Parameters();
        parameters.addParameter(P_URL, "jdbc:<dbms>://<host>[:port]/<database>");
        parameters.addParameter(P_DRIVER, "");
        parameters.addParameter(P_ENTITY_QUERY, "select * from <entity_table(s)> where <uid> = ?");
        parameters.addParameter(P_SEQUENCE_QUERY, "select * from <contexts_table(s)> where <entity_uid> = ?");
        parameters.addParameter(P_ENTITIES_QUERY, "select <uid_field>, <other_fields> from <entity_table(s)> where <filter1> = ? and <filter2> = ? ... <filterN> = ? order by <fields>");
        parameters.addParameter(P_USERNAME, "");
        parameters.addParameter(P_PASSWORD, "");
        return parameters;
    }

    /**
     * Connect to Database
     * @return
     */
    public boolean connectToSource() {
        String driver = getSimpleParameter("driver");
        String url = getSimpleParameter("url");
        try {
            return createConnection(driver, url, getConnectionUserName(), getConnectionPassword());
        } catch (Throwable ex) {
            Logger.getLogger(SimpleJdbcSource.class.getName()).log(Level.SEVERE, null, ex);
            addError(ex);
            return false;
        }
    }

    /**
     * Build a list of entities from a Database
     * @param queryParameters  list of parameters defined in P_ENTITIES_QUERY parameter
     * @return
     * @throws Exception
     */
    public List<Entity> getListEntities(Object... queryParameters) throws Exception {
        PreparedStatement stm = prepare(getSimpleParameter(P_ENTITIES_QUERY));
        List<Entity> entities = new ArrayList<Entity>();
        if (stm != null) {
            if (queryParameters != null) {
                for (int ip = 0; ip < queryParameters.length; ip++) {
                    stm.setObject(ip+1, queryParameters[ip]);
                }
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Entity entity = new Entity(null);
                internalLoadEntity(rs, entity);
                entities.add(entity);
            }
        }
        return entities;
    }

    /**
     * First Field = (entity.getUid() == nul ? Entity UID : Ignore)
     * First String Field = Entity Name
     * Other Fields = Entity's Attributes
     * @param rs
     * @param entity
     * @throws Exception
     */
    private void internalLoadEntity(ResultSet rs, Entity entity) throws Exception {
        int colCount = rs.getMetaData().getColumnCount();
        for (int col = 1; col <= colCount; col++) {
            int colType = rs.getMetaData().getColumnType(col);
            if (col == 1 && entity.getUid() == null) {
                entity.setUid( rs.getObject(col) );
            } else if (colType == java.sql.Types.VARCHAR && entity.getName() == null) {
                String name = rs.getString(col);
                entity.setName(Utils.getNotNull(name).trim());
            } else {
                String colName = rs.getMetaData().getColumnName(col);
                String colLabel = rs.getMetaData().getColumnLabel(col);
                Object value = rs.getObject(col);
                entity.addAttribute(colName, colLabel, value);
            }
        }
    }

    /**
     * Build an Entity from a Database
     * @return
     */
    public Entity loadEntity(Object uid) throws Exception {
        PreparedStatement stm = prepare(getSimpleParameter(P_ENTITY_QUERY));
        Entity entity = null;
        if (stm != null) {
            stm.setObject(1, uid);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                entity = new Entity(uid);
                internalLoadEntity(rs, entity);
            }
        }
        return entity;
    }

    /**
     * Load Sequence data from Database query
     * @param sequence
     * @throws Exception
     */
    public void loadIntoSequence(ContextSequence sequence) throws Exception {
        PreparedStatement stm = prepare(getSimpleParameter(P_SEQUENCE_QUERY));
        if (stm != null) {
            sequence.clear();
            stm.setObject(1, sequence.getEntity().getUid());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Context ctx = createContext(rs);
                sequence.addContext(ctx);
            }
        }
    }

    public Context createContext(ResultSet rs) throws Exception {
        Context result = new Context();

        int colCount = rs.getMetaData().getColumnCount();
        for (int col = 1; col <= colCount; col++) {
            int colType = rs.getMetaData().getColumnType(col);
            
            //first date/time column in the resultset
            if (result.getTime() == null && (colType == java.sql.Types.DATE || colType == java.sql.Types.TIME || colType == java.sql.Types.TIMESTAMP)) {
                Timestamp tsValue = rs.getTimestamp(col);
                if (tsValue != null) {
                    result.setTime(new TimeDescription(tsValue.getTime()));
                } else {
                    Time timeValue = rs.getTime(col);
                    if (timeValue != null) {
                        result.setTime(new TimeDescription(timeValue.getTime()));
                    } else {
                        Date dateValue = rs.getDate(col);
                        if (dateValue != null) {
                            result.setTime(new TimeDescription(dateValue));
                        }
                    }
                }
                
            } else {
                //subsequents columns = situation
                String colName = rs.getMetaData().getColumnName(col);
                Object value = rs.getObject(col);
                result.addSituation(colName, value);
            }
        }

        return result;
    }

    public String getConnectionUserName() {
        if (Utils.isEmpty(connectionUserName)) {
            connectionUserName = getSimpleParameter(P_USERNAME);
        }
        return connectionUserName;
    }

    public void setConnectionUserName(String connectionUserName) {
        this.connectionUserName = connectionUserName;
    }

    public String getConnectionPassword() {
        if (Utils.isEmpty(connectionPassword)) {
            connectionPassword = getSimpleParameter(P_PASSWORD);
        }
        return connectionPassword;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }
}
