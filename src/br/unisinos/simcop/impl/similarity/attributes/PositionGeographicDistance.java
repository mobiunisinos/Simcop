package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.CommonFunctions;
import br.unisinos.simcop.core.GeoPoint;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.LocationDescription;
import br.unisinos.simcop.impl.similarity.base.LocationSimilarity;

/**
 * Uses the ‘haversine’ formula to calculate the great-circle distance between two points<br/>
 * http://www.movable-type.co.uk/scripts/latlong.html<br/>
 * <br/>
 * The coordinates in position arrays must be in signed decimal degrees without compass direction, 
 * where negative indicates west/south (e.g. 40.7486, -73.9864):
 *
 * @author tiago
 */
public class PositionGeographicDistance extends LocationSimilarity {
    public static final String PAR_METRIC = "metric";
    public static final String PAR_LATITUDE_INDEX = "latitudeIndex";
    public static final String PAR_LONTITUDE_INDEX = "longitudeIndex";

    private static final int METER = 0;
    private static final int KM = 1;
    private static final int FEET = 2;


    //------------------------------------------------------------------------------------
    public static double getDistanceInFeets(GeoPoint p1, GeoPoint p2) {
      return getDistanceInKm(p1, p2) * 3280.8399;
    }
    
    public static double getDistanceInMeters(GeoPoint p1, GeoPoint p2) {
       return getDistanceInKm(p1, p2) * 1000;
    }

    public static double getDistanceInKm(GeoPoint p1, GeoPoint p2) {
        return CommonFunctions.haversineDistance(p1, p2);
    }
    //------------------------------------------------------------------------------------

    
    private int metric = KM;
    private int latIndexInArray = 0;
    private int lonIndexInArray = 1;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_METRIC, "km");
        internalParameters.addParameter(PAR_LATITUDE_INDEX, "0");
        internalParameters.addParameter(PAR_LONTITUDE_INDEX, "1");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String sMetric = getSimpleParameter(PAR_METRIC);
        String sLatI = getSimpleParameter(PAR_LATITUDE_INDEX);
        String sLonI = getSimpleParameter(PAR_LONTITUDE_INDEX);

        if ("m".equalsIgnoreCase(sMetric)) {
            metric = METER;
        } else if ("km".equalsIgnoreCase(sMetric)) {
            metric = KM;
        } else if ("ft".equalsIgnoreCase(sMetric)) {
            metric = FEET;
        } else  {
            throw new IllegalArgumentException("Invalid metric type. Valid values are {m, km, ft}");
        }

        if (sLatI != null) {
            latIndexInArray = Integer.parseInt(sLatI.trim());
        }
        if (sLonI != null) {
            lonIndexInArray = Integer.parseInt(sLonI.trim());
        }
    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected double internalGetSimilarity(LocationDescription ldA, LocationDescription ldB) throws Exception {
        double[] coordA = ldA.getPosition();
        double[] coordB = ldB.getPosition();
        if (coordA == null || coordB == null) {
            return missingValue;
        }
        GeoPoint pA = new GeoPoint(coordA[latIndexInArray], coordA[lonIndexInArray]);
        GeoPoint pB = new GeoPoint(coordB[latIndexInArray], coordB[lonIndexInArray]);

        switch (metric) {
            case METER: return getDistanceInMeters(pA, pB);
            case KM: return getDistanceInKm(pA, pB);
            case FEET: return getDistanceInFeets(pA, pB);
            default:
                throw new IllegalArgumentException("Invalid Metric: " + getSimpleParameter(PAR_METRIC) + " [" + metric + "]");
        }

    }

}
