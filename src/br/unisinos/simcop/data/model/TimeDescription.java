package br.unisinos.simcop.data.model;

import br.unisinos.simcop.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//TODO JUnit para metodo equals
public class TimeDescription implements Comparable {

    private long uid;
    public static final short INDEX = 0;
    public static final short DAY = 1;
    public static final short MONTH = 2;
    public static final short YEAR = 3;
    public static final short HOUR = 4;
    public static final short MINUTE = 5;
    public static final short SECOND = 6;
    public static final short MILLISECOND = 7;
    public static final short WEEKDAY = 8;
    public static final short SEASON = 9;
    public static final short AGE = 10;
    private Integer[] internalData;

    public TimeDescription() {
        clear();
    }

    public TimeDescription(int index) {
        clear();
        internalData[INDEX] = index;
    }

    public TimeDescription(int age, int season) {
        clear();
        internalData[AGE] = age;
        internalData[SEASON] = season;
    }

    public TimeDescription(int hour, int minute, int second, int millisecond) {
        clear();
        internalData[HOUR] = hour;
        internalData[MINUTE] = minute;
        internalData[SECOND] = second;
        internalData[MILLISECOND] = millisecond;
    }

    public TimeDescription(Date date) {
        clear();
        loadFrom(date);
    }

    public TimeDescription(GregorianCalendar gc) {
        clear();
        loadFrom(gc);
    }

    public TimeDescription(long time) {
        clear();
        loadFrom(new Date(time));
    }

    public final void loadFrom(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        loadFrom(gc);
    }

    public final void loadFrom(GregorianCalendar gc) {
        clear();
        if (gc != null) {
            setYear(gc.get(GregorianCalendar.YEAR));
            setMonth(gc.get(GregorianCalendar.MONTH) + 1);
            setDay(gc.get(GregorianCalendar.DAY_OF_MONTH));
            setHour(gc.get(GregorianCalendar.HOUR_OF_DAY));
            setMinute(gc.get(GregorianCalendar.MINUTE));
            setSecond(gc.get(GregorianCalendar.SECOND));
            setMillisecond(gc.get(GregorianCalendar.MILLISECOND));
            setWeekDay(gc.get(GregorianCalendar.DAY_OF_WEEK));
        }
    }

    public final void clear() {
        this.internalData = new Integer[11];
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Integer getIndex() {
        return internalData[INDEX];
    }

    public void setIndex(Integer index) {
        this.internalData[INDEX] = index;
    }

    public Integer getDay() {
        return internalData[DAY];
    }

    public void setDay(Integer day) {
        this.internalData[DAY] = day;
    }

    public Integer getMonth() {
        return internalData[MONTH];
    }

    public void setMonth(Integer month) {
        this.internalData[MONTH] = month;
    }

    public Integer getYear() {
        return internalData[YEAR];
    }

    public void setYear(Integer year) {
        this.internalData[YEAR] = year;
    }

    public Integer getHour() {
        return internalData[HOUR];
    }

    public void setHour(Integer hour) {
        this.internalData[HOUR] = hour;
    }

    public Integer getMinute() {
        return internalData[MINUTE];
    }

    public void setMinute(Integer minute) {
        this.internalData[MINUTE] = minute;
    }

    public Integer getSecond() {
        return internalData[SECOND];
    }

    public void setSecond(Integer second) {
        this.internalData[SECOND] = second;
    }

    public Integer getMillisecond() {
        return internalData[MILLISECOND];
    }

    public void setMillisecond(Integer millisecond) {
        this.internalData[MILLISECOND] = millisecond;
    }

    public Integer getWeekDay() {
        return internalData[WEEKDAY];
    }

    public void setWeekDay(Integer weekDay) {
        this.internalData[WEEKDAY] = weekDay;
    }

    public Integer getSeason() {
        return internalData[SEASON];
    }

    public void setSeason(Integer season) {
        this.internalData[SEASON] = season;
    }

    public Integer getAge() {
        return internalData[AGE];
    }

    public void setAge(Integer age) {
        this.internalData[AGE] = age;
    }

    public boolean equals(TimeDescription other, short... fields) {
        return internalEquals(other, fields);
    }

    private boolean internalEquals(TimeDescription other, short[] fields) {
        if (fields == null) {
            return this.isNull();
        } else {
            boolean result = (fields != null && fields.length > 0);
            for (int idx = 0; idx < fields.length && result; idx++) {
                Integer i = this.internalData[fields[idx]];
                Integer j = other.internalData[fields[idx]];
                result = (i == null ? j == null : i.equals(j));
            }
            return result;
        }
    }

    public short[] getFields() {
        List<Short> fields = new ArrayList<Short>();
        for (short idx = 0; idx < internalData.length; idx++) {
            if (internalData[idx] != null) {
                fields.add(idx);
            }
        }

        if (fields.size() > 0) {
            short[] result = new short[fields.size()];
            for (int idx = 0; idx < fields.size(); idx++) {
                result[idx] = fields.get(idx);
            }
            return result;
        } else {
            return null;
        }
    }

    public boolean isNull() {
        return getFields() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TimeDescription)) {
            return false;
        } else {
            TimeDescription other = (TimeDescription) obj;
            return this.internalEquals(other, this.getFields());
        }
    }

    @Override
    public int hashCode() {
        if (internalData != null) {
            int hash = 7;
            hash = 59 * hash + (int) (this.uid ^ (this.uid >>> 32));
            hash = 59 * hash + Arrays.hashCode(this.internalData);
            return hash;
        } else {
            return 1;
        }
    }

    public int compareTo(Object o) {
        if (o == null || !(o instanceof TimeDescription)) {
            return -1;
        } else {
            TimeDescription other = (TimeDescription) o;
            if (internalData[INDEX] != null) {
                return internalData[INDEX].compareTo(other.internalData[INDEX]);
            } else if (internalData[AGE] != null) {
                return internalData[AGE].compareTo(other.internalData[INDEX]);
            } else if (internalData[SEASON] != null) {
                return internalData[SEASON].compareTo(other.internalData[INDEX]);
            } else {
                return this.getCalendarRepresentation().compareTo(other.getCalendarRepresentation());
            }
        }
    }

    public GregorianCalendar getCalendarRepresentation() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(GregorianCalendar.MILLISECOND, (internalData[MILLISECOND] != null ? internalData[MILLISECOND] : 0));
        gc.set(GregorianCalendar.SECOND, (internalData[SECOND] != null ? internalData[SECOND] : 0));
        gc.set(GregorianCalendar.MINUTE, (internalData[MINUTE] != null ? internalData[MINUTE] : 0));
        gc.set(GregorianCalendar.HOUR_OF_DAY, (internalData[HOUR] != null ? internalData[HOUR] : 0));
        gc.set(GregorianCalendar.DAY_OF_MONTH, (internalData[DAY] != null ? internalData[DAY] : 0));
        gc.set(GregorianCalendar.MONTH, (internalData[MONTH] != null ? internalData[MONTH] - 1 : 0));
        gc.set(GregorianCalendar.YEAR, (internalData[YEAR] != null ? internalData[YEAR] : 0));
        //System.out.print(" {" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S").format(gc.getTime()) + "}");
        return gc;
    }

    public Date asDate() {
        GregorianCalendar gc = getCalendarRepresentation();
        return gc.getTime();
    }

    public void setFromCalendar(GregorianCalendar gc) {
        clear();
        internalData[MILLISECOND] = gc.get(GregorianCalendar.MILLISECOND);
        internalData[SECOND] = gc.get(GregorianCalendar.SECOND);
        internalData[MINUTE] = gc.get(GregorianCalendar.MINUTE);
        internalData[HOUR] = gc.get(GregorianCalendar.HOUR_OF_DAY);
        internalData[DAY] = gc.get(GregorianCalendar.DAY_OF_MONTH) + 1;
        internalData[MONTH] = gc.get(GregorianCalendar.MONTH);
        internalData[YEAR] = gc.get(GregorianCalendar.YEAR);
        internalData[WEEKDAY] = gc.get(GregorianCalendar.DAY_OF_WEEK);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getSeason() != null) {
            sb.append("[Season ").append(getSeason()).append("] ");
        }
        if (getAge() != null) {
            sb.append("[Age ").append(getSeason()).append("] ");
        }
        String format = "";
        if (!Utils.isEmpty(getYear()) || !Utils.isEmpty(getMonth()) || !Utils.isEmpty(getDay())) {
            format = "yyyy-MM-dd ";
        }
        if (getHour() != null || getMinute() != null || getSecond() != null) {
            format = format + "HH:mm:ss";
        }
        if (getMillisecond() != null) {
            format = format + ".S";
        }



        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sb.append(sdf.format(getCalendarRepresentation().getTime()));

        return sb.toString();
    }

    public String asString(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(asDate());
    }

    public Long asLong() {
        if (getAge() != null) {
            return (long) getAge();
        } else if (getSeason() != null) {
            return (long) getSeason();
        } else {
            GregorianCalendar gc = getCalendarRepresentation();
            if (gc != null) {
                return gc.getTimeInMillis();
            } else {
                return null;
            }
        }
    }
}
