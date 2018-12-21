package br.unisinos.simcop.core;

import br.unisinos.simcop.Utils;
import java.util.List;

/**
 * Implementation of commons similarity functions
 * @author tiago
 */
public class CommonFunctions {

    /**
     * TreeEdit (aka LevenshteinDistance) <br/>
     * REFERENCE: http://en.wikipedia.org/wiki/Levenshtein_distance (adapted)<br/>
     * Using the Iterative With Two Matrix Rows approach.
     * 
     * @param s
     * @param t
     * @return
     */
    public static int levenshteinDistance(String s, String t) {
        // degenerate cases
        if (s == null ? t == null : s.equals(t)) {
            return 0;
        }
        if (s == null || s.length() == 0) {
            return t.length();
        }
        if (t == null || t.length() == 0) {
            return s.length();
        }

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++) {
            v0[i] = i;
        }

        for (int i = 0; i < s.length(); i++) {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < t.length(); j++) {
                int cost = (s.charAt(i) == t.charAt(j) ? 0 : 1);
                v1[j + 1] = Utils.min(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }
            System.arraycopy(v1, 0, v0, 0, v0.length);
        }

        return v1[t.length()];
    }

    /**
     * Calculates the distance in KM between two geographic coordinates
     * using the ‘haversine’ formula to calculate the great-circle
     * distance between the points.
     * <br/>
     * REFERENCE: http://www.movable-type.co.uk/scripts/latlong.html
     *
     */
    public static double haversineDistance(GeoPoint p1, GeoPoint p2) {
        int R = 6371; // km
        double dLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double dLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());
        double lat1 = Math.toRadians(p1.getLatitude());
        double lat2 = Math.toRadians(p2.getLatitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    /**
     * The Lp norm. This metric is also know as:<br>
     * City Block or Manhattan Distance, when p = 1<br/>
     * Euclidean Distance, when p = 2<br/>
     * Minkowsky Distance, when p > 2<br/>
     * (CHA, 2007)<br/><br/>
     *
     * When 0 &lt; p &lt; 1 this metric is called Fractional Lp Distance
     * (SKOPAL; BUSTOS, 2011)<br/>
     *
     *<br/>
     * REFERENCES:
     * <ol>
     * <li>
     * CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
    Density Functions. International Journal of Mathematical Models and Methods in
    Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     * </li>
     *  <li> SKOPAL, T.; BUSTOS, B. On nonmetric similarity search problems in complex domains. 
     *       ACM Comput. Surv., New York, NY, USA, v. 43, n. 4, p. 34:1–34:50, oct 2011.
    </li>
     *  <li> http://fitelson.org/coherence/deza_lp.pdf   </li>
     *  <li> http://en.wikipedia.org/wiki/Euclidean_distance   </li>
     *  <li> http://en.wikipedia.org/wiki/Lp_space   </li>
     * </ol>
     * 
     */
    public static double lpDistance(List<Double> differences, double p) {
        Double sum = new Double(0);
        for (Double difference : differences) {            
            if (difference != null) {
                sum += Math.pow(Math.abs(difference), p);
            }
        }
        return Math.pow(sum, 1 / p);
    }

    /**
     * A special case of Lp norm, when p goes to infinte. In this case, this metric
     * is called Chessboard distance in 2D, MinMax Approximation or Chebyshev distance
     * (CHA, 2007).<br/><br/>
     *
     * REFRENCES:<br/>
     * CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
    Density Functions. International Journal of Mathematical Models and Methods in
    Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     */
    public static double chebyshevDistance(List<Double> diferences) {
        Double max = Double.MIN_VALUE;
        for (Double difference : diferences) {
            if (difference != null) {
                difference = Math.abs(difference);
                if (difference > max) {
                    max = difference;
                }
            }
        }
        return max;
    }


    /**
     * The Sorensen (aka Bray-Curtis) Distance, which is widely used in ecology
     * (CHA, 2007).<br/><br/>
     *
     * REFRENCES:<br/>
     * CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
    Density Functions. International Journal of Mathematical Models and Methods in
    Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     */
    public static double sorensenDistance(double[] P, double[] Q) {
        double sumDifferences = 0;
        double sumCoordinates = 0;
        if (P != null && Q != null) {
            for (int i = 0; i < Math.max(P.length, Q.length); i++) {
                double p = i < P.length ? P[i] : 0;
                double q = i < Q.length ? Q[i] : 0;
                sumDifferences += Math.abs(p - q);
                sumCoordinates += (p + q);
            }
        }

        if (sumCoordinates != 0) {
            return sumDifferences / sumCoordinates;
        } else {
            return 0;
        }
    }


    /**
     * The Normalized Inner Product also called Cosine Coefficient measures the angle between
     * two vectors. This metric evaluates the orientation of vectors and not its magnitude.
     * If angle == 0, then the two vectors are paralel and the result is cos(0) == 1.
     * Else, the result is cos(angle) &lt; 1<br/>
     *
     * REFERENCES<br/>
     * <ol>
     * <li> http://en.wikipedia.org/wiki/Cosine_similarity </li>
     * <li>CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
    Density Functions. International Journal of Mathematical Models and Methods in
    Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     * </li>
     * </ol>
*
     * @param P
     * @param Q
     * @return
     *
     */
    public static double cosineSimilarity(double[] P, double[] Q) {
        double sumPQ = 0;
        double sumSquareP = 0;
        double sumSquareQ = 0;

        if (P != null && Q != null) {
            for (int i = 0; i < Math.max(P.length, Q.length); i++) {
                double p = i < P.length ? P[i] : 0;
                double q = i < Q.length ? Q[i] : 0;

                sumPQ += (p * q);
                sumSquareP += Math.pow(p, 2);
                sumSquareQ += Math.pow(q, 2);
            }
        }


        double sqrProduct = Math.sqrt(sumSquareQ) * Math.sqrt(sumSquareP);
        if (sqrProduct != 0) {
            return sumPQ / sqrProduct;
        } else {
            return 0;
        }
    }



    /**
     * The Jaccard Similarity
     * (CHA, 2007).<br/><br/>
     *
     * REFRENCES:<br/>
     * CHA, S.-H. Comprehensive Survey on Distance/Similarity Measures between Probability
    Density Functions. International Journal of Mathematical Models and Methods in
    Applied Sciences, [S.l.], v. 1, n. 4, p. 300–307, 2007
     * <br/><br/>
     * http://en.wikipedia.org/wiki/Jaccard_index
     */
    public static double jaccardSimilarity(double[] P, double[] Q) {
        double sumPQ = 0;
        double sumSquareP = 0;
        double sumSquareQ = 0;

        if (P != null && Q != null) {
            for (int i = 0; i < Math.max(P.length, Q.length); i++) {
                double p = i < P.length ? P[i] : 0;
                double q = i < Q.length ? Q[i] : 0;

                sumPQ += (p * q);
                sumSquareP += Math.pow(p, 2);
                sumSquareQ += Math.pow(q, 2);
            }
        }


        double div = sumSquareP + sumSquareQ - sumPQ;
        if (div != 0) {
            return sumPQ / div;
        } else {
            return 0;
        }
    }


}
