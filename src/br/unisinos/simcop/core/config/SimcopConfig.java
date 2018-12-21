package br.unisinos.simcop.core.config;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.process.SimcopProcess;
import br.unisinos.simcop.core.process.SimcopTask;
import br.unisinos.simcop.data.source.ISequenceSource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiago
 */
public abstract class SimcopConfig implements ISimcopConfig {

    private String name;
    private String description;
    private String author;
    private Date created;
    private Date modified;
    private String modifiedBy;
    private SimcopProcess simcopProcess;
    private Map<String, SimcopTask> sequenceSources;
    private List<SimcopTask> processTasks;
    private Parameters parameters;

    public SimcopConfig() {
        this.parameters = getDefaultParameters();
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public Parameter addParameter(String key, String value) {
        return addParameter(key, value, null);
    }

    public Parameter addParameter(String key, String value, String pattern) {
        if (parameters == null) {
            parameters = new Parameters();
        }
        return parameters.addParameter(key, value, pattern);
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Parameter setParameter(String key, String value) {
        return setParameter(key, value, null);
    }

    public Parameter setParameter(String key, String value, String pattern) {
        return getParameters().addParameter(key, value, pattern);
    }

    public Parameter getParameter(String key) {
        return getParameters().get(key);
    }

    public String getSimpleParameter(String key) {
        Parameter par = getParameters().get(key);
        return par.getValue();
    }

    public SimcopTask addSource(String uid, ISequenceSource source) {
        if (source != null && uid != null && uid.trim().length() > 0) {
            SimcopTask task = new SimcopTask(SimcopTask.SEQUENCE_SOURCE);
            task.setUid(uid);
            task.setClassName(source.getClass().getName());
            task.setLabel(source.getClass().getSimpleName());
            task.setInstance(source);
            task.setParameters(source.getParameters());
            getSequenceSources().put(uid, task);
            return task;
        } else {
            return null;
        }
    }

    public SimcopProcess createProcess() {
        Utils.log("Creating process from config");
        SimcopProcess result = new SimcopProcess();
        for (SimcopTask task : getProcessTasks()) {
            Utils.log("  TASK: " + task.getSequence() + "-" + task.getTypeName() + ": " + task.getClassName());
            result.addTask(task, false);
        }
        return result;
    }

    public void loadFromProcess(SimcopProcess process) {
        getProcessTasks().clear();
        if (process != null) {
            for (SimcopTask task : process.getTasks()) {
                getProcessTasks().add(task);
            }
        }

    }

    public ISequenceSource createSequenceSource(String uid) throws IllegalArgumentException {
        if (uid == null || uid.trim().length() == 0) {
            return null;
        }

        SimcopTask task = getSequenceSources().get(uid);
        if (task == null) {
            throw new IllegalArgumentException("Sequence source not found: [" + uid + "]");
        }

        ISimcopClass result = task.createInstance();
        if (result instanceof ISequenceSource) {
            return (ISequenceSource) result;
        } else {
            throw new IllegalArgumentException("Invalid class for sequence source [" + uid + "]: " + task.getClassName());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public SimcopProcess getSimcopProcess() {
        return simcopProcess;
    }

    public void setSimcopProcess(SimcopProcess simcopProcess) {
        this.simcopProcess = simcopProcess;
    }

    public Map<String, SimcopTask> getSequenceSources() {
        if (sequenceSources == null) {
            sequenceSources = new HashMap<String, SimcopTask>();
        }
        return sequenceSources;
    }

    public void setSequenceSources(Map<String, SimcopTask> sequenceSources) {
        this.sequenceSources = sequenceSources;
    }

    public List<SimcopTask> getProcessTasks() {
        if (processTasks == null) {
            processTasks = new ArrayList<SimcopTask>();
        }
        return processTasks;
    }
    
    public SimcopTask findSequenceSimilarityTask() {
        SimcopTask result = null;
        Iterator<SimcopTask> it = getProcessTasks().iterator();
        while (result == null && it.hasNext()) {
            SimcopTask task = it.next();
            if (task.getType() == SimcopTask.SEQUENCE_SIMILARITY) {
                result = task;
            }
        }
        return result;
    }

    public void setProcessTasks(List<SimcopTask> processTasks) {
        this.processTasks = processTasks;
    }

    public void updateTasksSequence() {
        DecimalFormat df = new DecimalFormat("000");
        List<SimcopTask> preProc = new ArrayList<SimcopTask>();
        List<SimcopTask> simProc = new ArrayList<SimcopTask>();
        List<SimcopTask> postProc = new ArrayList<SimcopTask>();

        for (SimcopTask task : getProcessTasks()) {
            switch (task.getType()) {
                case SimcopTask.INPUT_FILTER:
                    preProc.add(task);
                    break;

                case SimcopTask.INPUT_TRANSFORMATION:
                    preProc.add(task);
                    break;

                case SimcopTask.SEQUENCE_SIMILARITY:
                    simProc.add(task);
                    break;

                case SimcopTask.OUTPUT_FILTER:
                    postProc.add(task);
                    break;

                case SimcopTask.OUTPUT_TRANSFORMATION:
                    postProc.add(task);
                    break;
            }

        }

        int globalSequence = -1;
        for (SimcopTask task : preProc) {
            globalSequence++;
            task.setSequence(globalSequence);
            task.setUid("#" + task.getTypeName() + ":" + df.format(task.getSequence()));
        }
        for (SimcopTask task : simProc) {
            globalSequence++;
            task.setSequence(globalSequence);
            task.setUid("#" + task.getTypeName() + ":" + df.format(task.getSequence()));
        }
        for (SimcopTask task : postProc) {
            globalSequence++;
            task.setSequence(globalSequence);
            task.setUid("#" + task.getTypeName() + ":" + df.format(task.getSequence()));
        }

        Collections.sort(processTasks);
    }

    public void cloneInto(ISimcopConfig other) {
        other.setAuthor(author);
        other.setCreated(created);
        other.setDescription(description);
        other.setModified(modified);
        other.setModifiedBy(modifiedBy);
        other.setName(name);
        for (String parName : getParameters().keySet()) {
            Parameter par = getParameters().get(parName);
            other.getParameters().addParameter(par.getKey(), par.getValue(), par.getPattern());
        }
        for (SimcopTask task : getProcessTasks()) {
            other.getProcessTasks().add(task.clone());
        }
        for (String sourceName : getSequenceSources().keySet()) {
            SimcopTask thisSource = getSequenceSources().get(sourceName);
            other.getSequenceSources().put(sourceName, thisSource.clone());
        }
    }

    @Override
    public abstract ISimcopConfig clone();

    public void add(SimcopTask task) {
        boolean ok = true;
        int at;
        switch (task.getType()) {
            case SimcopTask.SEQUENCE_SIMILARITY:
                //only 1 sequence similarity task allowed
                at = indexOfType(SimcopTask.SEQUENCE_SIMILARITY);
                if (at >= 0) {
                    getProcessTasks().set(at, task);
                    ok = false;
                }                
                break;
            case SimcopTask.CONTEXT_SIMILARITY:
                //adds the context similarity task as a subtask of sequence similarity task
                ok = false;                
                at = indexOfType(SimcopTask.SEQUENCE_SIMILARITY);
                if (at >= 0) {
                    SimcopTask sequenceSim = getProcessTasks().get(at);
                    sequenceSim.addSubTask(task);
                }                
                break;
            case SimcopTask.ATTRIBUTE_SIMILARITY:
                //adds a attribute similarity task as a subtask of context similarity task
                if (Utils.isEmpty(task.getForAttribute())) {
                    task.setForAttribute(SimcopTask.FOR_SITUATION_DEFAULT);
                }
                ok = false;
                at = indexOfType(SimcopTask.SEQUENCE_SIMILARITY);
                if (at >= 0) {
                    SimcopTask sequenceSim = getProcessTasks().get(at);
                    int atCtx = indexOfType(sequenceSim.getSubTasks(), SimcopTask.CONTEXT_SIMILARITY);
                    if (atCtx >= 0) {
                        SimcopTask contextSim = sequenceSim.getSubTasks().get(atCtx);
                        //add or update (key = "for" parameter)
                        boolean adds = true;
                        for (SimcopTask attrTask : contextSim.getSubTasks()) {
                            if (task.getForAttribute().equalsIgnoreCase(attrTask.getForAttribute())) {
                                adds = false;
                                attrTask.setClassName(task.getClassName());
                                attrTask.setInstance(task.getInstance());
                                attrTask.setLabel(task.getLabel());
                                attrTask.setParameters(task.getParameters());
                                attrTask.setSequence(task.getSequence());
                                attrTask.setSubTasks(task.getSubTasks());
                                attrTask.setUid(task.getUid());
                            }
                            
                        }
                        if (adds) {
                            contextSim.addSubTask(task);
                        }
                    }
                    
                }                
                break;
        }

        if (ok) {
            getProcessTasks().add(task);
        }
    }
    
    public int indexOfType(int type) {
        return indexOfType(getProcessTasks(), type);
    }
    
    private int indexOfType(List<SimcopTask> tasks, int type) {
        int result = -1;
        Iterator<SimcopTask> it = tasks.iterator();
        int index = -1;
        while (result < 0 && it.hasNext()) {
            index++;
            SimcopTask test = it.next();
            if (test.getType() == type) {
                result = index;
            }
        }
        
        return result;
    }
}

