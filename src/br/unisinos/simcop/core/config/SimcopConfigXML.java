package br.unisinos.simcop.core.config;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.process.SimcopTask;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//TODO caracteres especiais <, > &
public class SimcopConfigXML extends SimcopConfig {

    public static final String P_FILE_NAME = "fileName";
    public static final String NS = "http://www.unisinos.br/pipca/simcop";
    public static final String TAG_ROOT = "SimcopConfig";
    public static final String TAG_CFG_NAME = "name";
    public static final String TAG_CFG_DESCRIPTION = "description";
    public static final String TAG_CFG_AUTHOR = "author";
    public static final String TAG_CFG_CREATED = "created";
    public static final String TAG_CFG_MODIFIED = "modified";
    public static final String TAG_CFG_MODIFIED_BY = "modifiedBy";
    public static final String TAG_PROCESS = "SimcopProcess";
    public static final String TAG_TASK = "task";
    public static final String TAG_PARAMETER = "parameter";
    public static final String TAG_CONTEXT_SIM = "contextSimilarity";
    public static final String TAG_CONTEXT_SELECTOR = "contextSelector";
    public static final String TAG_ATTRIBUTE_SELECTOR = "attributeSelector";
    public static final String TAG_SEMANTIC_MAP = "semanticMap";//revisar
    public static final String TAG_ONTOLOGIES = "Ontologies";
    public static final String TAG_ONTOLOGY = "ontology";
    public static final String TAG_SEQUENCE_SOURCES = "SequenceSources";
    public static final String TAG_SEQUENCE_SOURCE = "sequenceSource";
    public static final String ATTR_TASK_SEQUENCE = "sequence";
    public static final String ATTR_TASK_LABEL = "label";
    public static final String ATTR_FOR_LABEL = "for";
    public static final String ATTR_TASK_TYPE = "type";
    public static final String ATTR_TASK_CLASS = "class";
    public static final String ATTR_PARAMETER_NAME = "name";
    public static final String ATTR_PARAMETER_VALUE = "value";
    public static final String ATTR_PARAMETER_PATTERN = "pattern";
    public static final String ATTR_CONTEXT_CLASS = "className";
    public static final String ATTR_CONTEXT_SELECTOR_CLASS = "className";
    public static final String ATTR_EVALUATOR_TYPE = "type";
    public static final String ATTR_EVALUATOR_CLASS = "className";
    public static final String ATTR_EVALUATOR_SOURCE = "source";
    public static final String ATTR_EVALUATOR_PREDICATE = "predicate";
    public static final String ATTR_SELECTOR_AUX = "aux";
    public static final String ATTR_ONTOLOGY = "ontology";
    public static final String ATTR_SEMANTICMAP_WORD = "contextWord"; //revisar
    public static final String ATTR_SEMANTICMAP_CLASS = "ontologyClass"; //revisar
    public static final String ATTR_SEMANTICMAP_CASE = "caseInsensitive"; //revisar
    public static final String ATTR_ONTOLOGY_LABEL = "label";
    public static final String ATTR_ONTOLOGY_NAMESPACE = "nameSpace";
    public static final String ATTR_ONTOLOGY_CLASS = "className";
    public static final String ATTR_UID = "uid";
    public static final String ATTR_SOURCE_LABEL = "label";
    public static final String ATTR_SOURCE_CLASS = "className";
    public static final String DEFAULT_PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATTERN_DATE = "yyyy-MM-dd";
    public static final String DEFAULT_PATTERN_TIME = "HH:mm";
    public static final String DEFAULT_PATTERN_NUMBER = "###,###,##0.00";
    public static final String DEFAULT_PATTERN_INTEGER = "###,###,##0";
    private InputStream configInputStream;
    private OutputStream configOutputStream;

    public Parameters getDefaultParameters() {
        Parameters parameters = new Parameters();
        String homeDir = System.getProperty("user.home");
        parameters.addParameter(P_FILE_NAME, homeDir + "/simcop.xml");
        return parameters;
    }

    public SimcopConfigXML() {
        super();
    }

    public SimcopConfigXML(File configFile) {
        super();
        setParameter(P_FILE_NAME, configFile.getAbsolutePath());
    }

    public SimcopConfigXML(InputStream configInputStream) {
        this.configInputStream = configInputStream;
    }

    public Element getRootTag(Document document) throws Exception {
        NodeList nl = document.getElementsByTagName(TAG_ROOT);
        if (nl == null || nl.getLength() == 0) {
            throw new Exception("Invalid XML. Tag '" + TAG_ROOT + "' not found!");
        }
        if (nl.getLength() > 1) {
            throw new Exception("Invalid XML. Tag '" + TAG_ROOT + "' duplicated!");
        }
        return (Element) nl.item(0);

    }

    @Override
    public void load() throws Exception {
        Document document;

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        File destFile = getConfigFile();
        if (destFile != null) {
            document = builder.parse(destFile);
        } else {
            document = builder.parse(configInputStream);
        }

        Element root = getRootTag(document);

        //0. metadata
        Node authorNode = getNode(root, TAG_CFG_AUTHOR, false, false);
        Node createdNode = getNode(root, TAG_CFG_CREATED, false, false);
        Node descriptionNode = getNode(root, TAG_CFG_DESCRIPTION, false, false);
        Node modifiedNode = getNode(root, TAG_CFG_MODIFIED, false, false);
        Node modifiedByNode = getNode(root, TAG_CFG_MODIFIED_BY, false, false);
        Node nameNode = getNode(root, TAG_CFG_NAME, false, false);

        if (authorNode != null) {
            setAuthor(authorNode.getTextContent());
        }
        if (descriptionNode != null) {
            setDescription(descriptionNode.getTextContent());
        }
        if (modifiedByNode != null) {
            setModifiedBy(modifiedByNode.getTextContent());
        }
        if (nameNode != null) {
            setName(nameNode.getTextContent());
        }
        if (createdNode != null) {
            String created = createdNode.getTextContent();
            created = created != null ? created.trim() : "";
            if (created.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN_DATETIME);
                setCreated(sdf.parse(created));
            }
        }
        if (modifiedNode != null) {
            String modified = modifiedNode.getTextContent();
            modified = modified != null ? modified.trim() : "";
            if (modified.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN_DATETIME);
                setModified(sdf.parse(modified));
            }
        }


        //1. Load Process
        Node processNode = getNode(root, TAG_PROCESS, true, true);
        NodeList tasksNodes = getChild(processNode, true);
        getProcessTasks().clear();
        for (int seq = 0; seq < tasksNodes.getLength(); seq++) {
            Node taskNode = tasksNodes.item(seq);
            if (taskNode.getNodeType() == Node.ELEMENT_NODE) {
                validateTag(taskNode, TAG_TASK, TAG_PROCESS);
                add(createTask(taskNode));

                //subtasks: level 1
                NodeList nl1 = taskNode.getChildNodes();
                for (int st1 = 0; st1 < nl1.getLength(); st1++) {
                    Node subTask1 = nl1.item(st1);
                    if (TAG_TASK.equals(subTask1.getNodeName())) {
                        add(createTask(subTask1));

                        //subtasks: level 2
                        NodeList nl2 = subTask1.getChildNodes();
                        for (int st2 = 0; st2 < nl2.getLength(); st2++) {
                            Node subTask2 = nl2.item(st2);
                            if (TAG_TASK.equals(subTask2.getNodeName())) {
                                add(createTask(subTask2));
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(getProcessTasks());


        //2. Load ontologies
        Node ontologiesNode = getNode(root, TAG_ONTOLOGIES, false, true);
        if (ontologiesNode != null) {
            NodeList ontologiesList = ontologiesNode.getChildNodes();
            if (ontologiesList != null) {
                for (int idx = 0; idx < ontologiesList.getLength(); idx++) {
                    //todo: criar objeto para armazenar dados da ontologia
                }
            }
        }

        //3. Load sources
        Node sourcesNode = getNode(root, TAG_SEQUENCE_SOURCES, false, true);
        getSequenceSources().clear();
        if (sourcesNode != null) {
            NodeList sourcesList = sourcesNode.getChildNodes();
            if (sourcesList != null) {
                for (int idx = 0; idx < sourcesList.getLength(); idx++) {
                    Node sourceNode = sourcesList.item(idx);
                    if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
                        validateTag(sourceNode, TAG_SEQUENCE_SOURCE, TAG_SEQUENCE_SOURCES);
                        SimcopTask sourceTask = createTask(sourceNode);
                        if (sourceTask.getUid() == null || sourceTask.getUid().trim().length() == 0) {
                            throw new Exception("Invalid XML. Use of '" + ATTR_UID + "' is mandatory for tag '" + TAG_SEQUENCE_SOURCE + "'");
                        }
                        getSequenceSources().put(sourceTask.getUid(), sourceTask);
                    }
                }
            }
        }
    }

    public Document createDocument() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElementNS(NS, TAG_ROOT);
        document.appendChild(root);
        return document;
    }

    private void createDescriptionTag(Document document, Element root, String tag, String value) {
        Element el = document.createElement(tag);
        el.setTextContent(value != null ? value : "");
        root.appendChild(el);


    }

    @Override
    public void save() throws Exception {
        Document document = createDocument();
        Element root = getRootTag(document);

        createDescriptionTag(document, root, TAG_CFG_NAME, getName());
        createDescriptionTag(document, root, TAG_CFG_DESCRIPTION, getDescription());
        createDescriptionTag(document, root, TAG_CFG_AUTHOR, getAuthor());
        createDescriptionTag(document, root, TAG_CFG_MODIFIED_BY, getModifiedBy());
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN_DATETIME);
        createDescriptionTag(document, root, TAG_CFG_CREATED, getCreated() != null ? sdf.format(getCreated()) : "");
        createDescriptionTag(document, root, TAG_CFG_MODIFIED, getModified() != null ? sdf.format(getModified()) : "");

        Element processTag = document.createElement(TAG_PROCESS);
        root.appendChild(processTag);


        for (SimcopTask task : getProcessTasks()) {
            Element taskElement = createTaskTag(document, task);
            processTag.appendChild(taskElement);

            //subtasks level 1
            if (task.hasSubTasks()) {
                for (SimcopTask subtask1 : task.getSubTasks()) {
                    Element st1Element = createTaskTag(document, subtask1);
                    taskElement.appendChild(st1Element);

                    //subtasks level 2
                    if (subtask1.hasSubTasks()) {
                        for (SimcopTask subtask2 : subtask1.getSubTasks()) {
                            Element st2Element = createTaskTag(document, subtask2);
                            st1Element.appendChild(st2Element);
                        }
                    }
                }
            }

        }
        //TODO ontologias

        if (!getSequenceSources().isEmpty()) {
            Element sourcesElement = document.createElement(TAG_SEQUENCE_SOURCES);
            root.appendChild(sourcesElement);

            for (String uid : getSequenceSources().keySet()) {
                Element taskElement = createTaskTag(document, getSequenceSources().get(uid));
                sourcesElement.appendChild(taskElement);
            }
        }

        DOMSource source = new DOMSource(root);
        StreamResult out;
        File destFile = getConfigFile();
        if (destFile != null) {
            out = new StreamResult(destFile);
        } else {
            out = new StreamResult(configOutputStream);
        }
        Transformer trans = TransformerFactory.newInstance().newTransformer();


        trans.transform(source, out);
    }

    private Node getNode(Element parentElement, String tagName, boolean mandatory, boolean unique) throws Exception {
        if (parentElement == null || tagName == null || tagName.trim().length() == 0) {
            return null;
        }
        NodeList nl = parentElement.getElementsByTagName(tagName);
        if (nl.getLength() == 0) {
            if (mandatory) {
                throw new Exception("Invalid XML. Tag '" + tagName + "' not found");
            } else {
                return null;
            }
        }
        if (nl.getLength() > 1) {
            if (unique) {
                throw new Exception("Invalid XML. Duplicated Tag '" + tagName + "'");
            }
        }
        return nl.item(0);
    }

    private NodeList getNodeList(Element parentElement, String tagName, boolean mandatory) throws Exception {
        if (parentElement == null || tagName == null || tagName.trim().length() == 0) {
            return null;
        }
        NodeList nl = parentElement.getElementsByTagName(tagName);
        if (nl.getLength() == 0) {
            if (mandatory) {
                throw new Exception("Invalid XML. Tag '" + tagName + "' not found");
            } else {
                return null;
            }
        }
        return nl;
    }

    private NodeList getChild(Node parentNode, boolean mandatory) throws Exception {
        if (parentNode == null) {
            return null;
        }
        NodeList nl = parentNode.getChildNodes();
        if (mandatory && nl.getLength() == 0) {
            throw new Exception("Invalid XML. Tag '" + parentNode.getNodeName() + "' has no childs");
        }
        return nl;
    }

    private void validateTag(Node nodeFound, String expectedTag, String parentTag) throws Exception {
        if (!expectedTag.equals(nodeFound.getNodeName())) {
            throw new Exception("Invalid XML. Invalid tag for " + parentTag + ": Expected '" + expectedTag + "' but '" + nodeFound.getNodeName() + "' was found.");
        }

    }

    private Element createTaskTag(Document document, SimcopTask task) {
        if (document == null || task == null) {
            return null;
        }
        Element taskElement;
        if (task.getType() == SimcopTask.SEQUENCE_SOURCE) {
            taskElement = document.createElement(TAG_SEQUENCE_SOURCE);
        } else {
            taskElement = document.createElement(TAG_TASK);
        }
        taskElement.setAttribute(ATTR_UID, task.getUid());
        taskElement.setAttribute(ATTR_TASK_CLASS, task.getClassName());
        taskElement.setAttribute(ATTR_TASK_LABEL, task.getLabel());
        taskElement.setAttribute(ATTR_TASK_SEQUENCE, Integer.toString(task.getSequence()));
        taskElement.setAttribute(ATTR_TASK_TYPE, task.getTypeName());
        if (!Utils.isEmpty(task.getForAttribute())) {
            taskElement.setAttribute(ATTR_FOR_LABEL, task.getForAttribute());
        }
        List<Parameter> pars = task.getParameters().asList();
        for (Parameter par : pars) {
            Element parElement = document.createElement(TAG_PARAMETER);
            parElement.setAttribute(ATTR_PARAMETER_NAME, par.getKey());
            parElement.setAttribute(ATTR_PARAMETER_PATTERN, par.getPattern());
            parElement.setAttribute(ATTR_PARAMETER_VALUE, par.getValue());
            taskElement.appendChild(parElement);
        }
        //TODO: context similarity task
        return taskElement;

    }

    private SimcopTask createTask(Node xmlNode) throws Exception {
        String uid = readAttribute(xmlNode, ATTR_UID, false);
        String sequence = readAttribute(xmlNode, ATTR_TASK_SEQUENCE, true).trim();
        String type = readAttribute(xmlNode, ATTR_TASK_TYPE, true).trim();
        String className = readAttribute(xmlNode, ATTR_TASK_CLASS, true).trim();
        String taskLabel = readAttribute(xmlNode, ATTR_TASK_LABEL, false);
        String taskFor = readAttribute(xmlNode, ATTR_FOR_LABEL, false);
        
        int typeCode = SimcopTask.getTypeCode(type);
        if (typeCode == -1) {
            throw new Exception("Invalid XML. Unknow task type: '" + type + "'");
        }

        SimcopTask task = new SimcopTask(typeCode);
        task.setUid(uid);
        task.setSequence(Integer.parseInt(sequence));
        task.setClassName(className);
        task.setLabel(taskLabel);
        task.setForAttribute(taskFor);

        //parameters
        NodeList nl = getChild(xmlNode, false);
        if (nl != null && nl.getLength() > 0) {
            for (int idx = 0; idx < nl.getLength(); idx++) {
                Node childNode = nl.item(idx);
                if (TAG_PARAMETER.equals(childNode.getNodeName())) {
                    String key = readAttribute(childNode, ATTR_PARAMETER_NAME, true);
                    String value = readAttribute(childNode, ATTR_PARAMETER_VALUE, true);
                    String pattern = readAttribute(childNode, ATTR_PARAMETER_PATTERN, false);
                    task.getParameters().addParameter(key, value, pattern);
                }
                if (TAG_CONTEXT_SIM.equals(childNode.getNodeName())) {
                    task.addSubTask(createTask(childNode));
                }
            }
        }
        return task;
    }

    private String readAttribute(Node xmlNode, String name, boolean mandatory) throws Exception {
        if (xmlNode == null || name == null || name.trim().length() == 0) {
            return null;
        }

        NamedNodeMap attrs = xmlNode.getAttributes();
        Node attrNode = attrs.getNamedItem(name);
        if (attrNode == null) {
            if (mandatory) {
                throw new Exception("Invalid XML. Attribute '" + name + "' not found for tag '" + xmlNode.getNodeName() + "'");
            } else {
                return null;
            }
        }
        return attrNode.getNodeValue();

    }

    public File getConfigFile() {
        String file = getSimpleParameter(P_FILE_NAME);
        if (!Utils.isEmpty(file)) {
            return new File(file);
        } else {
            return null;
        }
    }

    public void setConfigFile(File configFile) {
        setParameter(P_FILE_NAME, configFile.getAbsolutePath());
    }

    public InputStream getConfigInputStream() {
        return configInputStream;
    }

    public void setConfigInputStream(InputStream configInputStream) {
        this.configInputStream = configInputStream;
    }

    public OutputStream getConfigOutputStream() {
        return configOutputStream;
    }

    public void setConfigOutputStream(OutputStream configOutputStream) {
        this.configOutputStream = configOutputStream;
    }

    @Override
    public SimcopConfigXML clone() {
        SimcopConfigXML result = new SimcopConfigXML();
        cloneInto(result);
        return result;
    }
}
