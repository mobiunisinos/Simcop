package br.unisinos.simcop.impl.similarity.thirdParty.adapters;

import br.unisinos.simcop.Utils;
import br.unisinos.simcop.core.SimilarityResult;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.ExtendedData;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.impl.similarity.thirdParty.adapters.base.BaseSequenceAdapter;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.dtw.DTW;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.dtw.FastDTW;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.dtw.TimeWarpInfo;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.matrix.ColMajorCell;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.timeseries.TimeSeries;
import br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.util.DistanceFunction;
import br.unisinos.simcop.interfaces.similarity.IContextSimilarity;
import br.unisinos.simcop.interfaces.similarity.IExtendedDataSimilarity;
import br.unisinos.simcop.interfaces.similarity.ILocationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISequenceSimilarity;
import br.unisinos.simcop.interfaces.similarity.ISimcopAdapter;
import br.unisinos.simcop.interfaces.similarity.ISimilarity;
import br.unisinos.simcop.interfaces.similarity.ISituationSimilarity;
import br.unisinos.simcop.interfaces.similarity.ITimeSimilarity;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface between SimCoP Similarity Functions and FastDTW API.<br/>
 * <ol>
 * <li><b>For Sequences:</b><br/>
 * Transform each sequence in a {@link TimeSeries} object. Uses a extended
 * version of TimeSeries class that considers each context a point in each time
 * serie. <br/>
 * Extends {@link BaseSequenceAdapter}, and implements FastDTW interface's
 * {@link DistanceFunction} where the {@link DistanceFunction#calcDistance(double[], double[])
 * } method will call the {@link IContextSimilarity} function for each pair of
 * context;
 *
 *
 * <br/><br/>
 * <li><b>For Contexts:</b><br/>
 * <br/><br/>
 * <li><b>For Attributes:</b><br/>
 * Not Applicable
 * <br/>
 * </ol>
 *
 * @author tiago
 * @see FastDTW
 * @see DTW
 */
public class FastDTWAdapter implements ISimcopAdapter {

    public static final String PAR_SEARCH_RADIUS = "searchReadius";
    private Map<Context, Map<Context, Double>> distanceMap;


    /* ===========================================================================================================================================================
     * ADAPTER FOR SEQUENCE SIMILARITY
     * =========================================================================================================================================================== */
    public SimilarityResult getSimilarityFor(ContextSequence s1, ContextSequence s2, ISequenceSimilarity function) throws Exception {
        if (function == null) {
            throw new IllegalArgumentException("Sequence Similarity Class not defined");
        }

        SimilarityResult sr = new SimilarityResult(s1, s2);
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            sr.setCalculatedValue(new Double(0));
        } else {
            //initialize distanceMap
            distanceMap = new HashMap<Context, Map<Context, Double>>();

            //configurate FastDTW
            int searchRadius = 1; //see FastDTW.DEFAULT_SEARCH_RADIUS = 1;

            String sSearchReadius = function.getSimpleParameter(PAR_SEARCH_RADIUS);
            if (!Utils.isEmpty(sSearchReadius)) {
                searchRadius = Integer.parseInt(sSearchReadius.trim());
            }


            if (function.getContextSimilarity() != null) {
                if (function.getContextSimilarity().isDistanceFunction()) {
                    function.getContextSimilarity().validateParameters();
                } else {
                    throw new IllegalArgumentException("Context Similarity Class must be a Distance Function");
                }
            } else {
                throw new IllegalArgumentException("Context Similarity Class not defined");
            }


            FastDTWSequenceAdapter fastDTWFunction = new FastDTWSequenceAdapter(function);
            fastDTWFunction.s1 = s1;
            fastDTWFunction.s2 = s2;

            //-- call the fastDTW
            TimeWarpInfo result = FastDTW.getWarpInfoBetween(createTimeSerieFor(s1), createTimeSerieFor(s2), searchRadius, fastDTWFunction);
            //-------------------

            //build the result
            sr.setCalculatedValue(result.getDistance());
            if (result.getPath() != null) {
                for (int ipath = 0; ipath < result.getPath().size(); ipath++) {
                    ColMajorCell cell = result.getPath().get(ipath);
                    int indexInA = cell.getCol();
                    int indexInB = cell.getRow();

                    Context ctxA = indexInA >= 0 && indexInA < s1.size() ? s1.get(indexInA) : null;
                    Context ctxB = indexInB >= 0 && indexInB < s2.size() ? s2.get(indexInB) : null;

                    //get similarity/distance value
                    Map<Context, Double> distancesOfA = distanceMap.get(ctxA);
                    Double distance = null;
                    if (distancesOfA != null) {
                        distance = distancesOfA.get(ctxB);
                    }
                    sr.add(ctxA, ctxB, distance);

                }
            }

        }

        return sr;
    }

    private TimeSeries createTimeSerieFor(ContextSequence sequence) {
        ContextSequenceTimeSeries result = new ContextSequenceTimeSeries(1);
        result.sequence = sequence;
        return result;
    }

    class ContextSequenceTimeSeries extends TimeSeries {

        private ContextSequence sequence;

        @Override
        public int size() {
            return sequence.size();
        }

        @Override
        public double[] getMeasurementVector(int pointIndex) {
            //return the index of context in sequece to the DistanceFunction
            return new double[]{pointIndex};
        }

        @Override
        public double getTimeAtNthPoint(int n) {
            return n;
        }

        /* CALLS TO DEFAULT CONSTRUCTORS */
        public ContextSequenceTimeSeries(int numOfDimensions) {
            super(numOfDimensions);
        }

        public ContextSequenceTimeSeries(TimeSeries origTS) {
            super(origTS);
        }

        public ContextSequenceTimeSeries(String inputFile, boolean isFirstColTime) {
            super(inputFile, isFirstColTime);
        }

        public ContextSequenceTimeSeries(String inputFile, char delimiter) {
            super(inputFile, delimiter);
        }

        public ContextSequenceTimeSeries(String inputFile, boolean isFirstColTime, char delimiter) {
            super(inputFile, isFirstColTime, delimiter);
        }

        public ContextSequenceTimeSeries(String inputFile, boolean isFirstColTime, boolean isLabeled, char delimiter) {
            super(inputFile, isFirstColTime, isLabeled, delimiter);
        }

        public ContextSequenceTimeSeries(String inputFile, int[] colToInclude, boolean isFirstColTime) {
            super(inputFile, colToInclude, isFirstColTime);
        }

        public ContextSequenceTimeSeries(String inputFile, int[] colToInclude, boolean isFirstColTime, boolean isLabeled, char delimiter) {
            super(inputFile, colToInclude, isFirstColTime, isLabeled, delimiter);
        }
        /* END OF DEFAULT CONSTRUCTORS */
    }

    private class FastDTWSequenceAdapter extends BaseSequenceAdapter implements ISequenceSimilarity, DistanceFunction {

        private ContextSequence s1;
        private ContextSequence s2;

        public FastDTWSequenceAdapter(ISimilarity function) {
            super(function);
            this.sequenceFunction = (ISequenceSimilarity) function;

        }

        public double calcDistance(double[] vector1, double[] vector2) {
            
            int indexInA = (int) vector1[0];
            int indexInB = (int) vector2[0];

            Context ctxA = indexInA >= 0 && indexInA < s1.size() ? s1.get(indexInA) : null;
            Context ctxB = indexInB >= 0 && indexInB < s2.size() ? s2.get(indexInB) : null;

            try {
                double distance = sequenceFunction.getContextSimilarity().getSimilarity(ctxA, ctxB);
                addToDistanceMap(ctxA, ctxB, distance);
                return distance;
            } catch (Exception e) {
                Utils.log(e);
                return Double.MAX_VALUE;
            }
        }
    }

    private void addToDistanceMap(Context ctxA, Context ctxB, double distance) {
        Map<Context, Double> distancesOfA = distanceMap.get(ctxA);
        if (distancesOfA == null) {
            distancesOfA = new HashMap<Context, Double>();
            distanceMap.put(ctxA, distancesOfA);
        }
        distancesOfA.put(ctxB, distance);
    }


    /* ===========================================================================================================================================================
     * ADAPTER FOR CONTEXT SIMILARITY
     * =========================================================================================================================================================== */
    public double getSimilarityFor(Context ctxA, Context ctxB, IContextSimilarity function) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* ===========================================================================================================================================================
     * NOT SUPPORTED
     * =========================================================================================================================================================== */
    public double getSimilarityFor(LocationDescription ldA, LocationDescription ldB, ILocationSimilarity function) throws Exception {
        throw new Exception("DTW is not applicable to attributes");
    }

    public double getSimilarityFor(Situation sitA, Situation sitB, ISituationSimilarity function) throws Exception {
        throw new Exception("DTW is not applicable to attributes");
    }

    public double getSimilarityFor(ExtendedData edA, ExtendedData edB, IExtendedDataSimilarity function) throws Exception {
        throw new Exception("DTW is not applicable to attributes");
    }

    public double getSimilarityFor(TimeDescription tdA, TimeDescription tdB, ITimeSimilarity function) throws Exception {
        throw new Exception("DTW is not applicable to attributes");
    }
}
