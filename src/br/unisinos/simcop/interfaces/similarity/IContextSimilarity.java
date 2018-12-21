package br.unisinos.simcop.interfaces.similarity;

import br.unisinos.simcop.data.model.Context;

public interface IContextSimilarity extends ISimilarity {

    public double getSimilarity(Context c1, Context c2) throws Exception;
    public void setCallerObject(ISequenceSimilarity callerObject);
    public ISequenceSimilarity getCallerObject();
    /**
     * returns the max similarity  (or distance)  value that can be returned by this function
     * @return
     */
    public double getLargestPossibleValue();
    public void setTDSimilarity( ITimeSimilarity TDFunction);
    public void setLDSimilarity( ILocationSimilarity LDFunction);
    public void setEDSimilarity( IExtendedDataSimilarity EDFunction);
    public ITimeSimilarity getTDSimilarity( );
    public ILocationSimilarity getLDSimilarity( );
    public IExtendedDataSimilarity getEDSimilarity( );

    public void clearSDSimilarityMap();
    public void addSDSimilarity( String sourceName, String auxName, String predicateName, ISituationSimilarity SDFunction ) throws IllegalArgumentException;
    public ISituationSimilarity getSituationSimilarity( String sourceName, String auxName, String predicateName);
    public boolean isSDKeyValid(String key);
    
    public boolean isAttributeSimilarityNeeded();

}
