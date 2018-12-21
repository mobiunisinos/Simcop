package br.unisinos.simcop.impl.sources;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.dataType.SimcopCategories;
import br.unisinos.simcop.core.dataType.SimcopValue;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.data.source.AbstractSequenceSource;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Loads contexts stored in SituationsReports described in XML.<br/>
 * <br/>
 * <i>
 * HECKMANN, D. <b>Ubiquitous User Modeling.</b> 2005. PhD Tesis — Universitat
 * des Saarlandes, 2005.
 * </i>
 *
 * @author tiago
 */
public class SituationMLSource extends AbstractSequenceSource {

    public static final String PAR_LOAD_FROM_FILE = "loadFromFile";
    public static final String PAR_TIME_FORMAT = "timeFormat";
    public static final String PAR_POSITION_SEPARATOR = "separatorForPositionArray";
    private Document xml;
    private List<SMLStatement> statements;

    public Parameters getDefaultParameters() {
        Parameters pars = new Parameters();
        pars.addParameter(PAR_LOAD_FROM_FILE, "situationML.xml");
        pars.addParameter(PAR_TIME_FORMAT, "dd/MM/yyyy HH:mm:ss.S");
        pars.addParameter(PAR_POSITION_SEPARATOR, ",");
        return pars;
    }

    public SituationMLSource() {
    }

    public SituationMLSource(Document xml) {
        this.xml = xml;
    }

    public SituationMLSource(File xml) throws Exception {
        if (xml == null || !xml.exists()) {
            throw new IllegalArgumentException("File not exists: " + (xml == null ? "null" : xml.getAbsolutePath()));
        }

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.xml = builder.parse(xml);
    }

    public SituationMLSource(String uri) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.xml = builder.parse(uri);
    }

    public SituationMLSource(InputStream xml) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.xml = builder.parse(xml);
    }

    public SituationMLSource(InputSource xml) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.xml = builder.parse(xml);
    }

    public boolean connectToSource() {
        statements = null;
        if (xml == null) {
            File f = new File(getSimpleParameter(PAR_LOAD_FROM_FILE));
            if (f.exists() && f.canRead()) {
                try {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    xml = builder.parse(f);
                } catch (Exception ex) {
                    Logger.getLogger(SituationMLSource.class.getName()).log(Level.SEVERE, null, ex);
                    addError(ex);
                    return false;
                }
            } else {
                addError("File not exists: " + f.getAbsolutePath());
            }
        }

        if (xml != null) {
            try {
                statements = loadStatements();
            } catch (Throwable ex) {
                Logger.getLogger(SituationMLSource.class.getName()).log(Level.SEVERE, null, ex);
                addError(ex);
                return false;
            }
            return !Utils.isEmpty(statements);
        } else {
            return false;
        }
    }

    public List<Entity> getListEntities(Object... entityNameFilter) throws Exception {
        Set<String> subjects = new HashSet<String>();
        if (isConnected()) {
            for (SMLStatement statement : statements) {
                String subject = statement.subject;
                boolean ok = false;
                if (subject != null && !Utils.isEmpty(entityNameFilter)) {
                    for (int i = 0; i < entityNameFilter.length && !ok; i++) {
                        Object filter = entityNameFilter[i];
                        if (filter != null) {
                            ok = subject.contains(filter.toString());
                        }
                    }
                } else {
                    ok = !Utils.isEmpty(subject);
                }
                if (ok) {
                    subjects.add(subject);
                }
            }
            List<Entity> entities = new ArrayList<Entity>();
            for (String subject : subjects) {
                entities.add(new Entity(subject, subject));
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

    public Entity loadEntity(Object uid) throws Exception {
        if (uid != null && isConnected()) {
            for (SMLStatement statement : statements) {
                if (statement != null && uid.equals(statement.subject)) {
                    return new Entity(statement.subject, statement.subject);
                }
            }
        }
        return null;
    }

    public void loadIntoSequence(ContextSequence sequence) throws Exception {
        if (sequence != null && statements != null && sequence.getEntity() != null) {
            sequence.resetContexts();
            for (int i = 0; i < statements.size(); i++) {
                SMLStatement statement = statements.get(i);
                if (statement.subject.equals(sequence.getEntity().getUid())) {
                    Context ctx = createContext(statement);
                    ctx.setIndex(i);
                    ctx.setSequence(sequence);
                    sequence.addContext(ctx);
                }
            }
        }
    }

    private Context createContext(SMLStatement statement) throws Exception {
        Context ctx = new Context();
        if (!Utils.isEmpty(statement.start)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(getSimpleParameter(PAR_TIME_FORMAT));
            TimeDescription td = new TimeDescription(dateFormat.parse(statement.start));
            ctx.setTime(td);
        }

        if (!Utils.isEmpty(statement.location)) {
            ctx.setLocation(new LocationDescription(statement.location));

        }
        if (!Utils.isEmpty(statement.position)) {
            if (ctx.getLocation() == null) {
                ctx.setLocation(new LocationDescription());
            }
            double[] positions = loadPosition(statement.position, getSimpleParameter(PAR_POSITION_SEPARATOR));
            ctx.getLocation().setPosition(positions);
        }
        //TODO: load extended data of context        

        //future work: group situations by time and/or location and/or extended data
        ctx.addSituation(statement.source, statement.auxiliary, statement.predicate, new SimcopCategories(statement.range.split(PAR_POSITION_SEPARATOR)), SimcopValue.createFromObject(statement.object));


        return ctx;
    }

    /*
     * AUX METHODS
     */
    private List<SMLStatement> loadStatements() throws Exception {
        List<SMLStatement> result = new ArrayList<SMLStatement>();
        NodeList nl = xml.getElementsByTagName("statement");
        if (nl != null) {
            for (int i = 0; i < nl.getLength(); i++) {
                result.add(loadStatement(nl.item(i)));
            }
        }


        return result;
    }

    private SMLStatement loadStatement(Node statementNode) throws Exception {
        SMLStatement result = new SMLStatement();
        //verbosity:        
        //min 
        if (statementNode.hasAttributes()) {
            NamedNodeMap attributes = statementNode.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attrNode = attributes.item(i);
                result.set(attrNode.getNodeName(), attrNode.getNodeValue());
            }

        } else {
            NodeList partNodes = statementNode.getChildNodes();
            for (int i = 0; i < partNodes.getLength(); i++) {
                Node partNode = partNodes.item(i);
                //mix
                if (partNode.hasAttributes()) {
                    NamedNodeMap attributes = partNode.getAttributes();
                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node attrNode = attributes.item(j);
                        result.set(attrNode.getNodeName(), attrNode.getNodeValue());
                    }

                } else {
                    //max
                    NodeList attributeNodes = partNode.getChildNodes();
                    for (int j = 0; j < attributeNodes.getLength(); j++) {
                        Node attributeNode = attributeNodes.item(j);
                        if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                            result.set(attributeNode.getNodeName(), attributeNode.getTextContent());
                        }
                    }
                }
            }
        }

        return result;
    }

    public boolean disconnectFromSource() {
        statements = null;
        return true;
    }

    public boolean isConnected() {
        return !Utils.isEmpty(statements);
    }

    private class SMLStatement {

        private String subject;
        private String auxiliary;
        private String predicate;
        private String range;
        private String object;
        private String start;
        private String end;
        private String durability;
        private String location;
        private String position;
        private String source;
        private String creator;
        private String method;
        private String evidence;
        private String confidence;
        private String key;
        private String owner;
        private String access;
        private String purpouse;
        private String retention;
        private String id;
        private String unique;
        private String replaces;
        private String group;
        private String notes;

        public void set(String attribute, String value) throws Exception {
            Field[] fields = getClass().getDeclaredFields();
            boolean found = false;
            for (int i = 0; i < fields.length && !found; i++) {
                if (fields[i].getName().equalsIgnoreCase(attribute)) {
                    fields[i].set(this, value);
                    found = true;
                }
            }
            if (!found) {
                throw new Exception("Unknow tag or attribute in xml: '" + attribute + "'    (" + value + ")");
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + (this.subject != null ? this.subject.hashCode() : 0);
            hash = 67 * hash + (this.auxiliary != null ? this.auxiliary.hashCode() : 0);
            hash = 67 * hash + (this.predicate != null ? this.predicate.hashCode() : 0);
            hash = 67 * hash + (this.range != null ? this.range.hashCode() : 0);
            hash = 67 * hash + (this.object != null ? this.object.hashCode() : 0);
            hash = 67 * hash + (this.start != null ? this.start.hashCode() : 0);
            hash = 67 * hash + (this.end != null ? this.end.hashCode() : 0);
            hash = 67 * hash + (this.durability != null ? this.durability.hashCode() : 0);
            hash = 67 * hash + (this.location != null ? this.location.hashCode() : 0);
            hash = 67 * hash + (this.position != null ? this.position.hashCode() : 0);
            hash = 67 * hash + (this.source != null ? this.source.hashCode() : 0);
            hash = 67 * hash + (this.creator != null ? this.creator.hashCode() : 0);
            hash = 67 * hash + (this.method != null ? this.method.hashCode() : 0);
            hash = 67 * hash + (this.evidence != null ? this.evidence.hashCode() : 0);
            hash = 67 * hash + (this.confidence != null ? this.confidence.hashCode() : 0);
            hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
            hash = 67 * hash + (this.owner != null ? this.owner.hashCode() : 0);
            hash = 67 * hash + (this.access != null ? this.access.hashCode() : 0);
            hash = 67 * hash + (this.purpouse != null ? this.purpouse.hashCode() : 0);
            hash = 67 * hash + (this.retention != null ? this.retention.hashCode() : 0);
            hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
            hash = 67 * hash + (this.unique != null ? this.unique.hashCode() : 0);
            hash = 67 * hash + (this.replaces != null ? this.replaces.hashCode() : 0);
            hash = 67 * hash + (this.group != null ? this.group.hashCode() : 0);
            hash = 67 * hash + (this.notes != null ? this.notes.hashCode() : 0);
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
            final SMLStatement other = (SMLStatement) obj;
            if ((this.subject == null) ? (other.subject != null) : !this.subject.equals(other.subject)) {
                return false;
            }
            if ((this.auxiliary == null) ? (other.auxiliary != null) : !this.auxiliary.equals(other.auxiliary)) {
                return false;
            }
            if ((this.predicate == null) ? (other.predicate != null) : !this.predicate.equals(other.predicate)) {
                return false;
            }
            if ((this.range == null) ? (other.range != null) : !this.range.equals(other.range)) {
                return false;
            }
            if ((this.object == null) ? (other.object != null) : !this.object.equals(other.object)) {
                return false;
            }
            if ((this.start == null) ? (other.start != null) : !this.start.equals(other.start)) {
                return false;
            }
            if ((this.end == null) ? (other.end != null) : !this.end.equals(other.end)) {
                return false;
            }
            if ((this.durability == null) ? (other.durability != null) : !this.durability.equals(other.durability)) {
                return false;
            }
            if ((this.location == null) ? (other.location != null) : !this.location.equals(other.location)) {
                return false;
            }
            if ((this.position == null) ? (other.position != null) : !this.position.equals(other.position)) {
                return false;
            }
            if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
                return false;
            }
            if ((this.creator == null) ? (other.creator != null) : !this.creator.equals(other.creator)) {
                return false;
            }
            if ((this.method == null) ? (other.method != null) : !this.method.equals(other.method)) {
                return false;
            }
            if ((this.evidence == null) ? (other.evidence != null) : !this.evidence.equals(other.evidence)) {
                return false;
            }
            if ((this.confidence == null) ? (other.confidence != null) : !this.confidence.equals(other.confidence)) {
                return false;
            }
            if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
                return false;
            }
            if ((this.owner == null) ? (other.owner != null) : !this.owner.equals(other.owner)) {
                return false;
            }
            if ((this.access == null) ? (other.access != null) : !this.access.equals(other.access)) {
                return false;
            }
            if ((this.purpouse == null) ? (other.purpouse != null) : !this.purpouse.equals(other.purpouse)) {
                return false;
            }
            if ((this.retention == null) ? (other.retention != null) : !this.retention.equals(other.retention)) {
                return false;
            }
            if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
                return false;
            }
            if ((this.unique == null) ? (other.unique != null) : !this.unique.equals(other.unique)) {
                return false;
            }
            if ((this.replaces == null) ? (other.replaces != null) : !this.replaces.equals(other.replaces)) {
                return false;
            }
            if ((this.group == null) ? (other.group != null) : !this.group.equals(other.group)) {
                return false;
            }
            if ((this.notes == null) ? (other.notes != null) : !this.notes.equals(other.notes)) {
                return false;
            }
            return true;
        }
    }
}
