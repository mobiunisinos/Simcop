
package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.ExtendedData;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;

/**
 * Used to integrate SimCoP functions with third-party libraries
 * @author tiago
 */
public interface ISimcopAdapter {    
    public SimilarityResult getSimilarityFor(ContextSequence s1, ContextSequence s2, ISequenceSimilarity function) throws Exception;
    public double getSimilarityFor( Context ctxA, Context ctxB, IContextSimilarity function ) throws Exception;
    public double getSimilarityFor( LocationDescription ldA, LocationDescription ldB, ILocationSimilarity function ) throws Exception;
    public double getSimilarityFor( Situation sitA, Situation sitB, ISituationSimilarity function ) throws Exception;
    public double getSimilarityFor( ExtendedData edA, ExtendedData edB, IExtendedDataSimilarity function) throws Exception;
    public double getSimilarityFor( TimeDescription tdA, TimeDescription tdB, ITimeSimilarity function ) throws Exception;

}
