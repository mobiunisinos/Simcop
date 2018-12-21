package br.unisinos.simcop;

import br.unisinos.simcop.core.ISimcopClass;
import br.unisinos.simcop.core.Parameter;
import br.unisinos.simcop.core.Parameters;
import br.unisinos.simcop.core.dataType.SimcopValue;
import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Situation;
import br.unisinos.simcop.data.model.TimeDescription;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Stack;

/**
 *
 * @author tiago
 */
public class Utils {

    public static PrintStream logOutput;

    private static PrintStream getLogOutput() {
        return logOutput;
    }

    public static boolean isEmpty(Integer value) {
        return value == null || value == 0;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isEmpty(Collection value) {
        return value == null || value.isEmpty();
    }

    public static boolean isEmpty(Object[] value) {
        return value == null || value.length == 0;
    }

    public static String getNotNull(String value) {
        return value == null ? "" : value;
    }

    public static void logParameters(ISimcopClass simcopClass, String prefix) {
        if (simcopClass != null) {
            Parameters pars = simcopClass.getParameters();
            if (pars != null) {
                for (Parameter par : pars.asList()) {

                    String fromLog = simcopClass.getClass().getSimpleName();
                    fromLog = (fromLog + "               ").substring(0, 15);

                    if (logOutput != null) {
                        getLogOutput().println(new SimpleDateFormat("HH:mm:ss.S").format(new Date()) + " | " + fromLog + " | " + prefix + par.toString());
                    }

                }
            }
        }
    }

    public static void log(String msg) {
        StackTraceElement from = Thread.currentThread().getStackTrace()[2];
        String fromLog = "";
        if (from != null) {
            fromLog = from.getClassName();
            int idx = fromLog.lastIndexOf(".");
            if (idx > 0 && idx < fromLog.length() - 1) {
                fromLog = fromLog.substring(idx + 1);
            }
        }
        fromLog = (fromLog + "               ").substring(0, 15);

        if (logOutput != null) {
            getLogOutput().println(new SimpleDateFormat("HH:mm:ss.S").format(new Date()) + " | " + fromLog + " | " + msg);
        }
    }

    public static void log(Exception e) {
        StringBuilder txt = new StringBuilder();
        StackTraceElement from = Thread.currentThread().getStackTrace()[2];
        String fromLog = "";
        if (from != null) {
            fromLog = from.getClassName();
            int idx = fromLog.lastIndexOf(".");
            if (idx > 0 && idx < fromLog.length() - 1) {
                fromLog = fromLog.substring(idx + 1);
            }
        }
        fromLog = (fromLog + "               ").substring(0, 15);

        txt.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());

        if (logOutput != null) {
            getLogOutput().println(new SimpleDateFormat("HH:mm:ss.S").format(new Date()) + " | " + fromLog + " | " + txt.toString());
            for (StackTraceElement ste : e.getStackTrace()) {
                getLogOutput().println("\tat: " + ste.toString());
            }
        }


    }

    public static String alignLeft(String str, int size) {
        String result = getNotNull(str) + repeat(" ", size);
        return result.substring(0, size);
    }

    public static String repeat(String str, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= times; i++) {
            result.append(str);
        }
        return result.toString();
    }

    public static Stack<Context> convertToStack(ContextSequence seq) {
        Stack<Context> result = new Stack<Context>();
        if (seq != null) {
            for (int idx = seq.size() - 1; idx >= 0; idx--) {
                result.push(seq.get(idx));
            }
        }

        return result;
    }

    public static String extractClassName(String className) {
        if (className != null) {
            int idx = className.lastIndexOf(".");
            if (idx >= 0 && idx < className.length() - 1) {
                return className.substring(idx + 1);
            } else {
                return className;
            }
        } else {
            return "";
        }
    }

    /**
     * Extract the numeric value stored in a
     * {@link br.unisinos.simcop.core.dataType.SimcopValue SimcopValue}
     * object.<br/>
     *
     * @param value a SimcopValue to be converted.
     *
     * @return SimcopValue == null : return new Integer(0);<br/><br/>
     * SimcopValue.getType() of:<br/>
     * &nbsp;&nbsp;&nbsp; | SimcopValue.[INTEGER, LONG, DOUBLE]: return
     * respective type.<br/>
     * &nbsp;&nbsp;&nbsp; | SimcopValue.[TIME]: return a long that corresponds
     * to Date.getTime() or AGE or SEASON.<br/>
     * &nbsp;&nbsp;&nbsp; | SimcopValue.[INTEGER_ARRAY, DOUBLE_ARRAY,
     * TIME_ARRAY]: return the sum of the values in array.<br/>
     * &nbsp;&nbsp;&nbsp; | SimcopValue.[CATEGORY_INDEX]: return Integer
     * value.<br/>
     * else: return null<br/>
     */
    public static Number getAsNumber(SimcopValue value) {
        if (value == null || value.getType() == SimcopValue.NULL) {
            return new Integer(0);
        }

        switch (value.getType()) {
            case SimcopValue.INTEGER:
                return value.getIntegerValue() != null ? value.getIntegerValue() : new Integer(0);

            case SimcopValue.DOUBLE:
                return value.getDoubleValue() != null ? value.getDoubleValue() : new Double(0);

            case SimcopValue.LONG:
                return value.getLongValue() != null ? value.getLongValue() : new Long(0);

            case SimcopValue.TIME:
                TimeDescription td = value.getDateTimeValue();
                if (td != null) {
                    if (td.getAge() != null) {
                        return td.getAge();
                    } else if (td.getSeason() != null) {
                        return td.getSeason();
                    } else {
                        return td.getCalendarRepresentation().getTimeInMillis();
                    }
                } else {
                    return new Long(0);
                }

            case SimcopValue.INTEGER_ARRAY:
                if (value.getIntegers() != null) {
                    long result = 0;
                    for (int v : value.getIntegers()) {
                        result += v;
                    }
                    return new Long(result);
                } else {
                    return new Integer(0);
                }

            case SimcopValue.DOUBLE_ARRAY:
                if (value.getDoubles() != null) {
                    double result = 0;
                    for (double v : value.getDoubles()) {
                        result += v;
                    }
                    return new Double(result);
                } else {
                    return new Integer(0);
                }

            case SimcopValue.CATEGORY_INDEX:
                return value.getCategoryIndex() != null ? value.getCategoryIndex() : new Integer(0);
        }

        return null;
    }

    public static String getPathInfo(ContextSequence seq) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sequence of");
        if (seq.getEntity() != null) {
            sb.append(" entity: [").append(seq.getEntity().getUid()).append("] name: ").append(seq.getEntity().getName());
        } else {
            sb.append(" unknow entity");
        }
        return sb.toString();
    }

    public static String getPathInfo(Context ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("Context at ");
        if (ctx.getTime() != null) {
            sb.append(ctx.getTime().toString());
        } else {
            sb.append("unknow time ");
        }
        sb.append(" in ");
        if (ctx.getSequence() != null) {
            sb.append(getPathInfo(ctx.getSequence()));
        } else {
            sb.append(" unknow sequence");
        }
        return sb.toString();

    }

    public static String getPathInfo(Situation sit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Situation (");
        if (sit != null) {
            sb.append(sit.toString()).append(")");
            sb.append(" in ");
            if (sit.getContext() != null) {
                sb.append(getPathInfo(sit.getContext()));
            } else {
                sb.append(" unknow context ");
            }

        } else {
            sb.append("null)");
        }
        return sb.toString();
    }

    public static int max(int... values) {
        if (values == null) {
            return 0;
        }
        int result = Integer.MIN_VALUE;
        for (int value : values) {
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    public static int min(int... values) {
        if (values == null) {
            return 0;
        }
        int result = Integer.MAX_VALUE;
        for (int value : values) {
            if (value < result) {
                result = value;
            }
        }
        return result;
    }
};
