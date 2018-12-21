/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unisinos.simcop.core.config;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.process.SimcopProcess;
import br.unisinos.simcop.core.process.SimcopTask;
import br.unisinos.simcop.data.source.ISequenceSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tiago
 */
public interface ISimcopConfig extends ISimcopClass, Cloneable {

    public SimcopTask addSource(String uid, ISequenceSource source);

    public SimcopProcess createProcess();

    public ISequenceSource createSequenceSource(String uid) throws IllegalArgumentException;

    public String getAuthor();

    public Date getCreated();

    public String getDescription();

    public Date getModified();

    public String getModifiedBy();

    public String getName();

    public List<SimcopTask> getProcessTasks();
    
    public SimcopTask findSequenceSimilarityTask();
    
    public void add(SimcopTask task);

    public Map<String, SimcopTask> getSequenceSources();

    public SimcopProcess getSimcopProcess();

    public void load() throws Exception;

    public void loadFromProcess(SimcopProcess process);

    public void save() throws Exception;

    public void setAuthor(String author);

    public void setCreated(Date created);

    public void setDescription(String description);

    public void setModified(Date modified);

    public void setModifiedBy(String modifiedBy);

    public void setName(String name);

    public void setProcessTasks(List<SimcopTask> processTasks);

    public void setSequenceSources(Map<String, SimcopTask> sequenceSources);

    public void setSimcopProcess(SimcopProcess simcopProcess);

    public void updateTasksSequence();

    public ISimcopConfig clone();
}
