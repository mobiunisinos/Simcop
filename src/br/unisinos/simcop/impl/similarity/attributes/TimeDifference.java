package br.unisinos.simcop.impl.similarity.attributes;

import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.data.model.TimeDescription;
import br.unisinos.simcop.impl.similarity.base.TimeSimilarity;
import java.util.GregorianCalendar;

/**
 *
 * @author tiago
 */
public class TimeDifference extends TimeSimilarity {

    /**
     * Milliseconds (default)<br/>
     * Seconds, Minutes, Hours<br/>
     * Days, Weeks, Months, Years<br/>
     * Age</br>
     * Season<br/>
     */
    public static final String PAR_SCALE = "scale";
    /*
     * if true: return Math.abs(result);<br/>
     * if false: return result;
     */
    public static final String PAR_ABSOLUTE_DIFERENCE = "absoluteDifference";

    /* scales */
    public static final String SCALE_MILLISECONDS = "milliseconds";
    public static final String SCALE_SECONDS = "seconds";
    public static final String SCALE_MINUTES = "minutes";
    public static final String SCALE_HOURS = "hours";
    public static final String SCALE_DAYS = "days";
    public static final String SCALE_WEEKS = "weeks";
    public static final String SCALE_MONTHS = "months";
    public static final String SCALE_YEARS = "years";
    public static final String SCALE_AGES = "ages";
    public static final String SCALE_SEASONS = "seasons";
    /* ------ */
    private boolean absoluteDifference;
    private int scale;

    @Override
    protected void addDefaultParameters(Parameters internalParameters) {
        internalParameters.addParameter(PAR_SCALE, SCALE_MILLISECONDS);
        internalParameters.addParameter(PAR_ABSOLUTE_DIFERENCE, "false");
    }

    @Override
    protected void internalValidateParameters() throws Exception {
        String abs = getSimpleParameter(PAR_ABSOLUTE_DIFERENCE);
        absoluteDifference = abs != null ? "true".equalsIgnoreCase(abs) : false;

        String sScale = getSimpleParameter(PAR_SCALE);
        if (SCALE_AGES.equalsIgnoreCase(sScale)) {
            scale = -2;
        } else if (SCALE_SEASONS.equalsIgnoreCase(sScale)) {
            scale = -1;
        } else if (SCALE_MILLISECONDS.equalsIgnoreCase(sScale)) {
            scale = 0;
        } else if (SCALE_SECONDS.equalsIgnoreCase(sScale)) {
            scale = 1;
        } else if (SCALE_MINUTES.equalsIgnoreCase(sScale)) {
            scale = 2;
        } else if (SCALE_HOURS.equalsIgnoreCase(sScale)) {
            scale = 3;
        } else if (SCALE_DAYS.equalsIgnoreCase(sScale)) {
            scale = 4;
        } else if (SCALE_WEEKS.equalsIgnoreCase(sScale)) {
            scale = 5;
        } else if (SCALE_MONTHS.equalsIgnoreCase(sScale)) {
            scale = 6;
        } else if (SCALE_YEARS.equalsIgnoreCase(sScale)) {
            scale = 7;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid parameter for ").append(PAR_SCALE).append(" parameter. \n");
            sb.append("Valid values are:\n");
            sb.append("{");
            sb.append(SCALE_AGES).append(", ");
            sb.append(SCALE_SEASONS).append(", ");
            sb.append(SCALE_MILLISECONDS).append(", ");
            sb.append(SCALE_SECONDS).append(", ");
            sb.append(SCALE_MINUTES).append(", ");
            sb.append(SCALE_HOURS).append(", ");
            sb.append(SCALE_DAYS).append(", ");
            sb.append(SCALE_WEEKS).append(", ");
            sb.append(SCALE_MONTHS).append(", ");
            sb.append(SCALE_YEARS);
            sb.append("}");
            throw new IllegalArgumentException(sb.toString());
        }

    }

    public boolean isDistanceFunction() {
        return true;
    }

    @Override
    protected double internalGetSimilarity(TimeDescription tdA, TimeDescription tdB) {
        if (tdA == null || tdB == null) {
            return missingValue;
        } else {
            long diff;
            if (scale == -2) {
                Integer ia = tdA.getAge();
                Integer ib = tdB.getAge();
                if (ia == null || ib == null) {
                    return missingValue;
                } else {
                    diff = ia - ib;
                }
            } else if (scale == -1) {
                Integer ia = tdA.getSeason();
                Integer ib = tdB.getSeason();
                if (ia == null || ib == null) {
                    return missingValue;
                } else {
                    diff = ia - ib;
                }
            } else {
                GregorianCalendar gcA = tdA.getCalendarRepresentation();
                GregorianCalendar gcB = tdB.getCalendarRepresentation();

                //get diff in milliseconds
                if (gcA == null || gcB == null) {
                    return missingValue;
                } else {
                    diff = gcA.getTimeInMillis() - gcB.getTimeInMillis();
                }

                //convert to scale
                if (scale > 0) {
                    diff = diff / 1000; //millisecond to second
                    if (scale > 1) {
                        diff = diff / 60; //second to minute
                        if (scale > 2) {
                            diff = diff / 60; //minute to hour
                            if (scale > 3) {
                                diff = diff / 24; //hour to day
                                if (scale > 4) {
                                    if (scale == 5) {
                                        diff = diff / 7; //day to week
                                    } else if (scale == 6) {
                                        diff = diff / 30; //day to 30-day month
                                    } else {
                                        diff = diff / 365; //day to 365-day year
                                    }
                                }
                            }
                        }
                    }
                }

            }

            if (absoluteDifference) {
                diff = Math.abs(diff);
            }
            return diff;




        }
    }
}
