package br.unisinos.simcop;

import br.unisinos.simcop.core.ContextPair;
import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.core.config.ISimcopConfig;
import br.unisinos.simcop.core.config.SimcopConfig;
import br.unisinos.simcop.core.process.SimcopProcess;
import br.unisinos.simcop.core.process.SimcopTask;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.interfaces.filters.IFilterInput;
import br.unisinos.simcop.interfaces.filters.IFilterOutput;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;
import br.unisinos.simcop.interfaces.transformations.ITransformInput;
import br.unisinos.simcop.interfaces.transformations.ITransformOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simcop {

    private SimcopProcess simcopProcess;
    private ISimcopConfig simcopConfig;
    private Boolean distanceFunction;

    public Simcop() {
    }

    public Simcop(SimcopProcess process) {
        this.simcopProcess = process;
    }

    public Simcop(SimcopConfig config) {
        this.simcopProcess = config.createProcess();
        this.simcopConfig = config;
    }

    public SimilarityResult analyze(ContextSequence S1, ContextSequence S2) throws Exception {
        //TODO configure Utils.log and create class Utils.logUtil
        distanceFunction = null;

        if (simcopProcess == null) {
            throw new Exception("SIMCOP: Process not defined");
        } else if (S1 == null)     {
            throw new Exception("SIMCOP: Sequence S1 not defined");
        } else if (S2 == null)     {
            throw new Exception("SIMCOP: Sequence S2 not defined");
        } else {            
            if (Utils.isEmpty(S1.getContexts()) || Utils.isEmpty(S2.getContexts())) {
                return null;
            }
            
            SimilarityResult result = null;            

            //create local objects
            List<SimcopTask> tasks = new ArrayList<SimcopTask>();
            tasks.addAll(simcopProcess.getTasks());
            Collections.sort(tasks);
            ContextSequence localCS1 = S1.clone();
            ContextSequence localCS2 = S2.clone();

            //MAIN CONTROL LOOP
            Utils.log("------[ BEGIN ]------");
            for (SimcopTask currentTask : tasks) {
                //load task object
                ISimcopClass taskObject = (ISimcopClass) getOrCreateInstance(currentTask);

                //execute task
                if (taskObject != null) {
                    Utils.log("* Begining task: " + currentTask);
                    //filter task
                    if (taskObject instanceof IFilterInput) {
                        Context[] list1 = localCS1.getContexts().toArray(new Context[0]);
                        Context[] list2 = localCS2.getContexts().toArray(new Context[0]);

                        localCS1.resetContexts();
                        localCS2.resetContexts();
                        for (int idx = 0; idx < list1.length && idx < list2.length; idx++) {
                            Context ctx1 = idx < list1.length ? list1[idx] : null;
                            Context ctx2 = idx < list2.length ? list2[idx] : null;
                            boolean[] accept = ((IFilterInput) taskObject).accept(idx, ctx1, ctx2);
                            if (accept == null || accept.length != 2) {
                                throw new Exception("Invalid Task: " + currentTask.getSequence() + " " + currentTask.getLabel() + ". Input Filters should return an array of 2 booleans that means: [0] = accept ctx1, [1] = accept ctx2.");
                            }
                            if (accept[0]) {
                                localCS1.addContext(ctx1);
                            }
                            if (accept[1]) {
                                localCS2.addContext(ctx2);
                            }
                        }

                        //transformation task
                    } else if (taskObject instanceof ITransformInput) {
                        ContextSequence[] transformated = ((ITransformInput) taskObject).transform(localCS1, localCS2);
                        if (transformated == null || transformated.length != 2) {
                            throw new Exception("Invalid Task: " + currentTask.getSequence() + " " + currentTask.getLabel() + ". Input Transformations should return an array of 2 transformated ContextSequence's ");
                        }

                        if (transformated[0] != null) {
                            localCS1 = transformated[0];
                        }
                        if (transformated[1] != null) {
                            localCS2 = transformated[1];
                        }

                        //main similarity task
                    } else if (taskObject instanceof ISequenceSimilarity) {
                        ISequenceSimilarity seqSimObject = (ISequenceSimilarity) taskObject;

                        if (currentTask.hasSubTasks()) {
                            //Find and load ContextSimilarity Task
                            for (SimcopTask subTaskLevel1 : currentTask.getSubTasks()) {
                                if (subTaskLevel1.getType() == SimcopTask.CONTEXT_SIMILARITY) {
                                    IContextSimilarity ctxSimObject = (IContextSimilarity) getOrCreateInstance(subTaskLevel1);
                                    seqSimObject.setContextSimilarity(ctxSimObject);
                                    Utils.log("  * Selected Context Similarity Function: " + subTaskLevel1.getLabel());

                                    if (subTaskLevel1.hasSubTasks()) {

                                        //Find and load AttributeSimilarity Tasks
                                        for (SimcopTask subTaskLevel2 : subTaskLevel1.getSubTasks()) {
                                            if (subTaskLevel2.getType() == SimcopTask.ATTRIBUTE_SIMILARITY) {
                                                //Select the attribute on which this function is applied

                                                if (SimcopTask.FOR_TIME.equalsIgnoreCase(subTaskLevel2.getForAttribute())) {
                                                    //Time comparator
                                                    Utils.log("    * Selected Time Attribute Similarity Function: " + subTaskLevel2.getLabel());
                                                    ctxSimObject.setTDSimilarity((ITimeSimilarity) getOrCreateInstance(subTaskLevel2));

                                                } else if (SimcopTask.FOR_LOCATION.equalsIgnoreCase(subTaskLevel2.getForAttribute())) {
                                                    //Location comparator
                                                    Utils.log("    * Selected Location Attribute Similarity Function: " + subTaskLevel2.getLabel());
                                                    ctxSimObject.setLDSimilarity((ILocationSimilarity) getOrCreateInstance(subTaskLevel2));

                                                } else if (SimcopTask.FOR_EXTENDED.equalsIgnoreCase(subTaskLevel2.getForAttribute())) {
                                                    //Extended data comparator
                                                    Utils.log("    * Selected Extended Data Attribute Similarity Function: " + subTaskLevel2.getLabel());
                                                    ctxSimObject.setEDSimilarity((IExtendedDataSimilarity) getOrCreateInstance(subTaskLevel2));

                                                } else if (subTaskLevel2.getForAttribute() != null && subTaskLevel2.getForAttribute().startsWith(SimcopTask.FOR_SITUATION)) {
                                                    //Situation comparator
                                                    ISituationSimilarity SDFuncion = (ISituationSimilarity) getOrCreateInstance(subTaskLevel2);
                                                    if (SimcopTask.FOR_SITUATION_DEFAULT.equalsIgnoreCase(subTaskLevel2.getForAttribute())) {
                                                        //default
                                                        Utils.log("    * Selected Default Situation Attribute Similarity Function: " + subTaskLevel2.getLabel());
                                                        ctxSimObject.addSDSimilarity(null, null, null, SDFuncion);
                                                    } else {
                                                        //for a specific predicate
                                                        Utils.log("    * Selected Situation Attribute Similarity Function: " + subTaskLevel1.getLabel() + " for=" + subTaskLevel2.getForAttribute());
                                                        String[] sd = subTaskLevel2.getForAttribute().split("\\.");
                                                        switch (sd.length) {
                                                            case 2: //SD.<predicate-name>
                                                                ctxSimObject.addSDSimilarity(null, null, sd[1], SDFuncion);
                                                                break;

                                                            case 3: //SD.<aux-name>.<predicate-name>
                                                                ctxSimObject.addSDSimilarity(null, sd[1], sd[2], SDFuncion);
                                                                break;

                                                            case 4: //SD.<source-name>.<aux-name>.<predicate-name>
                                                                ctxSimObject.addSDSimilarity(sd[1], sd[2], sd[3], SDFuncion);
                                                                break;

                                                            default:
                                                                Utils.log("Invalid configuration: SEQUENCE SIMILARITY: " + currentTask.getClassName() + "\nATTRIBUTE SIMILARITY: " + subTaskLevel2.getClassName() + "\n   FOR='" + subTaskLevel2.getForAttribute() + "'");
                                                                throw new IllegalArgumentException("Invalid SD selector. Expected: SD[.source][.aux][.predicate]. Actual: " + subTaskLevel2.getForAttribute());
                                                        }
                                                    }
                                                } else {
                                                    //invalid
                                                    Utils.log("Invalid configuration: SEQUENCE SIMILARITY: " + currentTask.getClassName() + "\nATTRIBUTE SIMILARITY: " + subTaskLevel2.getClassName() + "\n   FOR='" + subTaskLevel2.getForAttribute() + "'");
                                                    throw new IllegalArgumentException("You must set the attribute of the context in which this function should be applied.");
                                                }
                                            }
                                        }
                                    } else {
                                        if (ctxSimObject.isAttributeSimilarityNeeded()) {
                                            Utils.log("Invalid configuration: SEQUENCE SIMILARITY: " + currentTask.getClassName() + "\n:CONTEXT SIMILARITY " + subTaskLevel1.getClassName());
                                            throw new IllegalArgumentException("You must set the attribute similarity functions for this context similarity task. ");
                                        }
                                    }

                                    break;
                                }
                            }
                        }

                        result = ((ISequenceSimilarity) taskObject).getSimilarity(localCS1, localCS2);
                        distanceFunction = ((ISequenceSimilarity) taskObject).isDistanceFunction();

                        //output filter task
                    } else if (taskObject instanceof IFilterOutput) {
                        if (result != null && result.getContextPairs() != null && result.getContextPairs().size() > 0) {
                            ContextPair[] pairs = result.getContextPairs().toArray(new ContextPair[0]);
                            result.getContextPairs().clear();

                            int index = 0;
                            for (ContextPair pair : pairs) {
                                index++;
                                if (((IFilterOutput) taskObject).accept(result, pair, index)) {
                                    result.getContextPairs().add(pair);
                                }
                            }
                        }

                        //output transformation task
                    } else if (taskObject instanceof ITransformOutput) {
                        if (result != null) {
                            ((ITransformOutput) taskObject).transform(result);
                        }
                    } else {
                        throw new IllegalArgumentException("Unknow task object class: [" + currentTask.getClassName() + "] Sequence: " + currentTask.getSequence() + " Type: " + currentTask.getTypeName());
                    }
                } else {
                    throw new IllegalArgumentException("Cannot load task object: [" + currentTask.getClassName() + "] Sequence: " + currentTask.getSequence() + " Type: " + currentTask.getTypeName());
                }
                Utils.log("* Ending task: " + currentTask);
            }
            Utils.log("------[ END ]------");
            //END OF MAIN CONTROL LOOP

            return result;
        }
    }

    private ISimcopClass getOrCreateInstance(SimcopTask task) {
        if (task.getInstance() != null) {
            return task.getInstance();
        } else {
            return task.createInstance();
        }

    }

    public Boolean isDistanceFunction() {
        return distanceFunction;
    }
    
    public boolean isDistance() {
        SimcopTask task = simcopProcess.getSequenceSimilarity();
        if (task != null)  {
            if (task.getInstance() == null) {
                task.createInstance();
            }
            ISequenceSimilarity ss = (ISequenceSimilarity)task.getInstance();
            return ss.isDistanceFunction();
        }
        return false;
    }
}
