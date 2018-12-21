package br.unisinos.simcop.core.process;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.impl.similarity.base.ExtendedDataSimilarity;
import br.unisinos.simcop.impl.similarity.base.LocationSimilarity;
import br.unisinos.simcop.impl.similarity.base.SituationSimilarity;
import br.unisinos.simcop.impl.similarity.base.TimeSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimcopTask implements Comparable, Cloneable {

    public static final int INPUT_FILTER = 0;
    public static final int INPUT_TRANSFORMATION = 1;
    public static final int SEQUENCE_SIMILARITY = 2;
    public static final int OUTPUT_FILTER = 3;
    public static final int OUTPUT_TRANSFORMATION = 4;
    //TODO public static final int ONTOLOGY = 100;
    public static final int SEQUENCE_SOURCE = 101;
    public static final int CONTEXT_SIMILARITY = 102;
    public static final int ATTRIBUTE_SIMILARITY = 103;
    public static final String FOR_TIME= TimeSimilarity.KEY;
    public static final String FOR_LOCATION= LocationSimilarity.KEY;
    public static final String FOR_SITUATION= SituationSimilarity.KEY;
    public static final String FOR_EXTENDED= ExtendedDataSimilarity.KEY;
    public static final String FOR_SITUATION_DEFAULT=SituationSimilarity.KEY + SituationSimilarity.DEFAULT_KEY;
    

    public static String getTypeName(int type) {
        switch (type) {
            case INPUT_FILTER:
                return "InputFilter";
            case INPUT_TRANSFORMATION:
                return "InputTransformation";
            case SEQUENCE_SIMILARITY:
                return "SequenceSimilarity";
            case OUTPUT_FILTER:
                return "OutputFilter";
            case OUTPUT_TRANSFORMATION:
                return "OutputTransformation";
            case SEQUENCE_SOURCE:
                return "SequenceSource";
            case CONTEXT_SIMILARITY:
                return "ContextSimilarity";
            case ATTRIBUTE_SIMILARITY:
                return "AttributeSimilarity";
            default:
                return "unknow";
        }
    }

    public static int getTypeCode(String name) {
        int[] types = new int[]{INPUT_FILTER, INPUT_TRANSFORMATION, SEQUENCE_SIMILARITY,
            OUTPUT_FILTER, OUTPUT_TRANSFORMATION, SEQUENCE_SOURCE,
            CONTEXT_SIMILARITY, ATTRIBUTE_SIMILARITY};

        for (int idx = 0; idx < types.length; idx++) {
            if (getTypeName(types[idx]).equalsIgnoreCase(name)) {
                return types[idx];
            }
        }

        return -1;
    }

    public SimcopTask(int type) {
        this.type = type;
        this.label = getTypeName(type);
    }
    private String uid;
    private int sequence;
    private int type;
    private String className;
    private ISimcopClass instance;
    private Parameters parameters;
    private String label;
    private List<SimcopTask> subTasks;
    private String forAttribute;

    public boolean hasSubTasks() {
        return subTasks != null && !subTasks.isEmpty();
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ISimcopClass getInstance() {
        return instance;
    }

    public void setInstance(ISimcopClass instance) {
        this.instance = instance;
    }

    public Parameters getParameters() {
        if (parameters == null) {
            parameters = new Parameters();
        }
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return getTypeName(type);
    }

    public ISimcopClass createInstance() throws IllegalArgumentException {
        if (className != null && className.trim().length() > 0) {
            Class c = null;
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new IllegalArgumentException("Cannot instantiate class for task " + sequence + ". " + label + ": Class not found: [" + className + "]");
            }
            ISimcopClass result = null;
            try {
                result = (ISimcopClass) c.newInstance();
                result.setParameters(parameters != null ? parameters : result.getDefaultParameters());
                this.instance = result;
            } catch (Exception ex) {
                Logger.getLogger(SimcopTask.class.getName()).log(Level.SEVERE, null, ex);

                IllegalArgumentException exc = new IllegalArgumentException("Cannot instantiate class for task " + sequence + ". " + label + ": " + ex.getMessage());
                exc.initCause(ex);
                throw exc;
            }

            return result;
        } else {
            throw new IllegalArgumentException("Cannot instantiate class for task " + sequence + ". " + label + ": className undefined");
        }
    }

    public int compareTo(Object o) {
        if (o == null || !(o instanceof SimcopTask)) {
            return -1;
        } else {
            SimcopTask other = ((SimcopTask)o);
            return this.getSequence() - other.getSequence();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimcopTask(");
        sb.append("[").append(sequence).append("] [").append(label).append("] [").append(className).append("] ");
        if (parameters != null) {
            sb.append(parameters.toString());
        }
        sb.append(")");

        return sb.toString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        SimcopTask other;
        if (obj instanceof SimcopTask) {
            other = (SimcopTask)obj;
            return this.type == other.type
                    && (this.className == null ? other.className == null : this.className.equals(other.className))
                    && (this.parameters == null ? other.parameters == null : this.parameters.equals(other.parameters));
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.type;
        hash = 43 * hash + (this.className != null ? this.className.hashCode() : 0);
        hash = 43 * hash + (this.instance != null ? this.instance.hashCode() : 0);
        hash = 43 * hash + (this.parameters != null ? this.parameters.hashCode() : 0);
        return hash;
    }

    @Override
    public SimcopTask clone() {
        SimcopTask newTask = new SimcopTask(type);
        newTask.className = this.className;
        newTask.label = this.label;
        newTask.sequence = this.sequence;
        newTask.uid = this.uid;
        for (String parName : getParameters().keySet()) {
            Parameter par = getParameters().get(parName);
            newTask.getParameters().addParameter(par.getKey(), par.getValue(), par.getPattern());                        
        }

        return newTask;
    }

    public List<SimcopTask> getSubTasks() {
        if (subTasks == null) {
            subTasks = new ArrayList<SimcopTask>();
        }
        return subTasks;
    }

    public void setSubTasks(List<SimcopTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTask(SimcopTask task) {
        getSubTasks().add(task);
    }

    public SimcopTask addSubTask(int type, ISimcopClass instance) {
        SimcopTask task = new SimcopTask(type);
        task.setInstance(instance);
        getSubTasks().add(task);
        return task;
    }
    
    public String getForAttribute() {
        return forAttribute;
    }

    public void setForAttribute(String forAttribute) {
        this.forAttribute = forAttribute;
    }
    
    public SimcopTask findSubTask(int type) {
        SimcopTask result = null;
        Iterator<SimcopTask> it = getSubTasks().iterator();
        while (result == null && it.hasNext()) {
            SimcopTask task = it.next();
            if (task.getType() == type) {
                result = task;
            }
        }
        return result;
        
    }
}
