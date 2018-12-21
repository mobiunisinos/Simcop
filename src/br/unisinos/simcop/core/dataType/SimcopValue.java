package br.unisinos.simcop.core.dataType;

import br.unisinos.simcop.data.model.TimeDescription;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimcopValue implements Comparable {

    public static final short NULL = 0;
    public static final short INTEGER = 1;
    public static final short STRING = 2;
    public static final short DOUBLE = 3;
    public static final short TIME = 4;
    public static final short OBJECT = 5;
    public static final short LONG = 6;
    public static final short INTEGER_ARRAY = 51;
    public static final short STRING_ARRAY = 52;
    public static final short DOUBLE_ARRAY = 53;
    public static final short TIME_ARRAY = 54;
    public static final short OBJECT_ARRAY = 55;
    public static final short CATEGORY_INDEX = 101;
    public static final short BINARY_VALUE = 102;

    /*=================================================================================*
     * SINGLE VALUE
     *=================================================================================*/
    private Integer integerValue;
    private Long longValue;
    private String stringValue;
    private Double doubleValue;
    private TimeDescription dateTimeValue;
    private Object objectValue;
    /*=================================================================================*
     * ARRAY VALUES
     *=================================================================================*/
    private int[] integers;
    private double[] doubles;
    private String[] strings;
    private Object[] objects;
    private TimeDescription[] dateTimes;

    /*=================================================================================*
     * SPECIAL VALUE
     *=================================================================================*/
    private Integer categoryIndex;
    private byte[] binaryValue;


    /*=================================================================================*
     * PUBLIC METHODS
     *=================================================================================*/
    public int getType() {
        if (integerValue != null) {
            return INTEGER;
        } else if (longValue != null) {
            return LONG;
        } else if (doubleValue != null) {
            return DOUBLE;
        } else if (categoryIndex != null) {
            return CATEGORY_INDEX;
        } else if (stringValue != null) {
            return STRING;
        } else if (objectValue != null) {
            return OBJECT;
        } else if (dateTimeValue != null) {
            return TIME;
        } else if (integers != null) {
            return INTEGER_ARRAY;
        } else if (doubles != null) {
            return DOUBLE_ARRAY;
        } else if (strings != null) {
            return STRING_ARRAY;
        } else if (dateTimes != null) {
            return TIME_ARRAY;
        } else if (objects != null) {
            return OBJECT_ARRAY;
        } else if (binaryValue != null) {
            return BINARY_VALUE;
        } else {
            return NULL;
        }
    }

    public void clear() {
        integerValue = null;
        doubleValue = null;
        categoryIndex = null;
        stringValue = null;
        objectValue = null;
        dateTimeValue = null;
        integers = null;
        doubles = null;
        strings = null;
        objects = null;
        binaryValue = null;
        longValue = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || (!(obj instanceof SimcopValue))) {
            return false;
        } else {
            SimcopValue other = (SimcopValue) obj;
            int thisType = this.getType();
            int otherType = other.getType();
            if (thisType != otherType) {
                return false;
            } else {
                switch (thisType) {
                    case NULL:
                        return true;
                    case INTEGER:
                        return this.integerValue.equals(other.integerValue);
                    case LONG:
                        return this.longValue.equals(other.longValue);
                    case DOUBLE:
                        return this.doubleValue.equals(other.doubleValue);
                    case CATEGORY_INDEX:
                        return this.categoryIndex.equals(other.categoryIndex);
                    case STRING:
                        return this.stringValue.equals(other.stringValue);
                    case OBJECT:
                        return this.objectValue.equals(other.objectValue);
                    case TIME:
                        return this.dateTimeValue.equals(other.dateTimeValue);
                    case INTEGER_ARRAY:
                        return Arrays.equals(this.integers, other.integers);
                    case DOUBLE_ARRAY:
                        return Arrays.equals(this.doubles, other.doubles);
                    case STRING_ARRAY:
                        return Arrays.equals(this.strings, other.strings);
                    case OBJECT_ARRAY:
                        return Arrays.equals(this.objects, other.objects);
                    case TIME_ARRAY:
                        return Arrays.equals(this.dateTimes, other.dateTimes);
                    case BINARY_VALUE:
                        return Arrays.equals(this.binaryValue, other.binaryValue);
                    default:
                        return false;
                }
            }

        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.integerValue != null ? this.integerValue.hashCode() : 0);
        hash = 53 * hash + (this.stringValue != null ? this.stringValue.hashCode() : 0);
        hash = 53 * hash + (this.doubleValue != null ? this.doubleValue.hashCode() : 0);
        hash = 53 * hash + (this.dateTimeValue != null ? this.dateTimeValue.hashCode() : 0);
        hash = 53 * hash + (this.objectValue != null ? this.objectValue.hashCode() : 0);
        hash = 53 * hash + (this.longValue != null ? this.longValue.hashCode() : 0);
        hash = 53 * hash + Arrays.hashCode(this.integers);
        hash = 53 * hash + Arrays.hashCode(this.doubles);
        hash = 53 * hash + Arrays.deepHashCode(this.strings);
        hash = 53 * hash + Arrays.deepHashCode(this.objects);
        hash = 53 * hash + (this.categoryIndex != null ? this.categoryIndex.hashCode() : 0);
        hash = 53 * hash + Arrays.hashCode(this.binaryValue);
        return hash;
    }

    public int compareTo(Object o) {
        if (o == null || !(o instanceof SimcopValue)) {
            return -1;
        } else {
            SimcopValue other = (SimcopValue) o;
            int thisType = this.getType();
            Number otherNumber;
            switch (thisType) {
                case NULL:
                    return 0;
                case INTEGER:
                    otherNumber = other.getIntegerValue();
                    if (otherNumber == null) {
                        otherNumber = other.getLongValue();
                        if (otherNumber == null) {
                            otherNumber = other.getDoubleValue();

                        }
                    }
                    return this.integerValue - otherNumber.intValue();
                case LONG:
                    otherNumber = other.getLongValue();
                    if (otherNumber == null) {
                        otherNumber = other.getIntegerValue();
                        if (otherNumber == null) {
                            otherNumber = other.getDoubleValue();
                        }
                    }
                    return this.longValue.compareTo(otherNumber.longValue());
                case DOUBLE:
                    otherNumber = other.getDoubleValue();
                    if (otherNumber == null) {
                        otherNumber = other.getLongValue();
                        if (otherNumber == null) {
                            otherNumber = other.getIntegerValue();
                        }
                    }
                    return this.doubleValue.compareTo(otherNumber.doubleValue());
                case CATEGORY_INDEX:
                    return this.categoryIndex.compareTo(other.categoryIndex);
                case STRING:
                    return this.stringValue.compareTo(other.stringValue);
                case OBJECT:
                    if (this.objectValue instanceof Comparable && other.objectValue instanceof Comparable) {
                        return ((Comparable) this.objectValue).compareTo((Comparable) other.objectValue);
                    } else {
                        return 0;
                    }
                case TIME:
                    return this.dateTimeValue.compareTo(other.dateTimeValue);
                case INTEGER_ARRAY:
                    return this.integers.length - other.integers.length;
                case DOUBLE_ARRAY:
                    return this.doubles.length - other.doubles.length;
                case STRING_ARRAY:
                    return this.strings.length - other.strings.length;
                case OBJECT_ARRAY:
                    return this.objects.length - other.objects.length;
                case TIME_ARRAY:
                    return this.dateTimes.length - other.dateTimes.length;
                case BINARY_VALUE:
                    return this.binaryValue.length - other.binaryValue.length;
                default:
                    return 0;
            }

        }
    }

    /*=================================================================================*
     * GETTERS / SETTERS
     *=================================================================================*/
    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Integer integerValue) {
        clear();
        this.integerValue = integerValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        clear();
        this.stringValue = stringValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        clear();
        this.doubleValue = doubleValue;
    }

    public TimeDescription getDateTimeValue() {
        return dateTimeValue;
    }

    public void setDateTimeValue(TimeDescription dateTimeValue) {
        clear();
        this.dateTimeValue = dateTimeValue;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(Object objectValue) {
        clear();
        this.objectValue = objectValue;
    }

    public int[] getIntegers() {
        return integers;
    }

    public void setIntegers(int[] integers) {
        clear();
        this.integers = integers;
    }

    public double[] getDoubles() {
        return doubles;
    }

    public void setDoubles(double[] doubles) {
        clear();
        this.doubles = doubles;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        clear();
        this.strings = strings;
    }

    public Integer getCategoryIndex() {
        return categoryIndex;
    }

    public void setCategoryIndex(Integer categoryIndex) {
        clear();
        this.categoryIndex = categoryIndex;
    }

    public byte[] getBinaryValue() {
        return binaryValue;
    }

    public void setBinaryValue(byte[] binaryValue) {
        clear();
        this.binaryValue = binaryValue;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        clear();
        this.objects = objects;
    }

    public TimeDescription[] getDateTimes() {
        return dateTimes;
    }

    public void setDateTimes(TimeDescription[] dateTimes) {
        clear();
        this.dateTimes = dateTimes;
    }

    public static SimcopValue createFromObject(Object object) {
        SimcopValue result = new SimcopValue();
        if (object != null) {
            if (object instanceof Integer) {
                result.setIntegerValue((Integer) object);

            } else if (object instanceof Long) {
                result.setLongValue((Long) object);

            } else if (object instanceof Double) {
                result.setDoubleValue((Double) object);

            } else if (object instanceof BigDecimal) {
                result.setDoubleValue(((BigDecimal) object).doubleValue());

            } else if (object instanceof TimeDescription) {
                result.setDateTimeValue((TimeDescription) object);

            } else if (object instanceof TimeDescription[]) {
                result.setDateTimes((TimeDescription[]) object);

            } else if (object instanceof Date) {
                TimeDescription td = new TimeDescription();
                td.loadFrom((Date) object);
                result.setDateTimeValue(td);

            } else if (object instanceof GregorianCalendar) {
                TimeDescription td = new TimeDescription();
                td.loadFrom((GregorianCalendar) object);
                result.setDateTimeValue(td);

            } else if (object instanceof String) {
                result.setStringValue((String) object);

            } else if (object instanceof byte[]) {
                result.setBinaryValue((byte[]) object);

            } else if (object instanceof int[]) {
                result.setIntegers((int[]) object);

            } else if (object instanceof double[]) {
                result.setDoubles((double[]) object);

            } else if (object instanceof String[]) {
                result.setStrings((String[]) object);


            } else {
                result.setObjectValue(object);
            }
        }

        return result;

    }

    private String toString(String arrayType, Object[] array, boolean withType) {
        StringBuilder sb = new StringBuilder();
        if (withType) {
            sb.append("SV.").append(arrayType).append("Array[");
        }
        if (array != null) {
            for (Object obj : array) {
                sb.append(obj);
                sb.append(",");
            }
        } else {
            sb.append("null");
        }
        if (withType) {
            sb.append("]");
        }
        return sb.toString();
    }

    private String toString(double[] array, boolean withType) {
        StringBuilder sb = new StringBuilder();
        if (withType) {
            sb.append("SV.integerArray[");
        }
        if (array != null) {
            for (double value : array) {
                sb.append(value);
                sb.append(",");
            }
        } else {
            sb.append("null");
        }
        if (withType) {
            sb.append("]");
        }
        return sb.toString();
    }

    private String toString(int[] array, boolean withType) {
        StringBuilder sb = new StringBuilder();
        if (withType) {
            sb.append("SV.integerArray[");
        }
        if (array != null) {
            for (int value : array) {
                sb.append(value);
                sb.append(",");
            }
        } else {
            sb.append("null");
        }
        if (withType) {
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return printValue(true);
    }

    private String print(String type, String value, boolean withType) {
        StringBuilder sb = new StringBuilder();
        if (withType) {
            sb.append("SV.");
            sb.append(type);
            sb.append("[");
        }
        sb.append(value);
        if (withType) {
            sb.append("]");
        }
        return sb.toString();

    }

    public String printValue(boolean withType) {
        int thisType = this.getType();
        switch (thisType) {
            case NULL:
                return withType ? "SV[null]" : "null";
            case INTEGER:
                return print("Integer", Integer.toString(this.integerValue), withType);
            case LONG:
                return print("Long", Long.toString(this.longValue), withType);
            case DOUBLE:
                return print("Double", Double.toString(this.doubleValue), withType);
            case CATEGORY_INDEX:
                return print("CategoryIndex", Integer.toString(this.categoryIndex), withType);
            case STRING:
                return print("String", this.stringValue, withType);
            case OBJECT:
                return print("Object", this.objectValue.toString(), withType);
            case TIME:
                return print("Time", this.dateTimeValue.toString(), withType);
            case INTEGER_ARRAY:
                return toString(this.integers, withType);
            case DOUBLE_ARRAY:
                return toString(this.doubles, withType);
            case STRING_ARRAY:
                return toString("string", this.strings, withType);
            case OBJECT_ARRAY:
                return toString("object", this.objects, withType);
            case TIME_ARRAY:
                return toString("time", this.dateTimes, withType);
            case BINARY_VALUE:
                String md5 = "";
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    BigInteger hash = new BigInteger(1, md.digest(binaryValue));
                    md5 = hash.toString(16);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(SimcopValue.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (withType) {
                    return "SV.binaryArray[MD5=" + md5 + " | length=" + this.binaryValue.length + " + bytes]";
                } else {
                    return md5;
                }
            default:
                return "SV[unknow: '" + thisType + "' ]";
        }


    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }
}
