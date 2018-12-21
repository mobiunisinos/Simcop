package br.unisinos.simcop.core.process;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.interfaces.filters.IFilterInput;
import br.unisinos.simcop.interfaces.filters.IFilterOutput;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.transformations.ITransformInput;
import br.unisinos.simcop.interfaces.transformations.ITransformOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimcopProcess {

    private List<SimcopTask> tasks;

    public boolean isEmpty() {
        return tasks == null || tasks.isEmpty();
    }

    public void clear() {
        this.tasks = new ArrayList<SimcopTask>();
    }

    private SimcopTask createTask(int type, ISimcopClass instance) {
        SimcopTask task = new SimcopTask(type);
        task.setSequence(getTasks().size() + 1);
        task.setClassName(instance.getClass().getName());
        task.setInstance(instance);
        task.setParameters(instance.getParameters() == null ? instance.getDefaultParameters() : instance.getParameters());
        getTasks().add(task);
        return task;
    }

    public void addTask(SimcopTask task, boolean changeSequence) {
        getTasks().add(task);
        if (changeSequence) {
            task.setSequence(getTasks().size());
        }
    }

    public SimcopTask addInputFilter(IFilterInput filter) {
        return createTask(SimcopTask.INPUT_FILTER, filter);
    }

    public SimcopTask addInputTransformation(ITransformInput transformation) {
        return createTask(SimcopTask.INPUT_TRANSFORMATION, transformation);
    }

    public SimcopTask getSequenceSimilarity() {
        Iterator<SimcopTask> it = getTasks().iterator();
        SimcopTask result = null;
        while (result == null && it.hasNext()) {
            SimcopTask task = it.next();
            if (task.getType() == SimcopTask.SEQUENCE_SIMILARITY) {
                result = task;
            }
        }
        return result;
    }

    public SimcopTask setSequenceSimilarity(ISequenceSimilarity similarity) {
        //ONLY 1 CLASS ALOWED
        SimcopTask task = getSequenceSimilarity();
        if (task == null) {
            return createTask(SimcopTask.SEQUENCE_SIMILARITY, similarity);
        } else {
            task.setSequence(getTasks().size() + 1);
            task.setClassName(similarity.getClass().getName());
            task.setInstance(similarity);
            task.setParameters(similarity.getParameters() == null ? similarity.getDefaultParameters() : similarity.getParameters());
            return task;
        }
    }

    public SimcopTask addOutputFilter(IFilterOutput filter) {
        return createTask(SimcopTask.OUTPUT_FILTER, filter);
    }

    public SimcopTask addOutputTransformation(ITransformOutput transformation) {
        return createTask(SimcopTask.OUTPUT_TRANSFORMATION, transformation);
    }

    public List<SimcopTask> getTasks() {
        if (tasks == null) {
            clear();
        }
        return tasks;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        SimcopProcess other;
        if (obj instanceof SimcopProcess) {
            other = (SimcopProcess) obj;
        } else {
            return false;
        }
        if (other.getTasks().size() != this.getTasks().size()) {
            return false;
        } else {
            boolean result = true;
            for (int idx = 0; result && idx < this.getTasks().size(); idx++) {
                SimcopTask thisTask = this.getTasks().get(idx);
                SimcopTask otherTask = other.getTasks().get(idx);
                result = thisTask.equals(otherTask);
            }
            return result;
        }


    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.tasks != null ? this.tasks.hashCode() : 0);
        return hash;
    }

    /*TODO
    public void addOntology(IOntology ontology) {
    }
     */
}
