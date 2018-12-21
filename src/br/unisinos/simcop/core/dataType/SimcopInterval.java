package br.unisinos.simcop.core.dataType;

import br.unisinos.simcop.data.model.TimeDescription;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimcopInterval extends SimcopRange {

    protected SimcopValue begin;
    protected SimcopValue end;

    public SimcopInterval(double begin, double end) {
        this.begin = SimcopValue.createFromObject(begin);
        this.end = SimcopValue.createFromObject(end);
    }

    public SimcopInterval(long begin, long end) {
        this.begin = SimcopValue.createFromObject(begin);
        this.end = SimcopValue.createFromObject(end);
    }

    public SimcopInterval(Object begin, Object end) {
        this.begin = SimcopValue.createFromObject(begin);
        this.end = SimcopValue.createFromObject(end);
    }


    @Override
    public boolean isInRange(SimcopValue value) {
        if (value == null) {
            return false;
        } else {
            return (value.compareTo(begin) >= 0) && (value.compareTo(end) <= 0);
        }
    }

    @Override
    public long getRangeSize() {
        return indexOf(end) + 1;
    }

    @Override
    public SimcopValue getRangeValueAt(long index) {
        SimcopValue result = null;
        if (index >= 0 && begin != null && end != null) {
            result = new SimcopValue();

            /*
             * only this types actually corresponds to a range
             */
            switch (begin.getType()) {
                case SimcopValue.INTEGER:
                    result.setIntegerValue(begin.getIntegerValue() + (int) index);
                    break;
                case SimcopValue.DOUBLE:
                    result.setDoubleValue(begin.getDoubleValue() + index);
                    break;
                case SimcopValue.CATEGORY_INDEX:
                    result.setCategoryIndex(begin.getCategoryIndex() + (int) index);
                    break;
                case SimcopValue.TIME:
                    TimeDescription tdB = begin.getDateTimeValue();
                    TimeDescription tdIdx = new TimeDescription();

                    if (tdB.getIndex() != null) {
                        tdIdx.setIndex(tdB.getIndex() + (int) index);
                    } else if (tdB.getAge() != null) {
                        tdIdx.setAge(tdB.getAge() + (int) index);
                    } else if (tdB.getSeason() != null) {
                        tdIdx.setAge(tdB.getSeason() + (int) index);
                    } else {
                        GregorianCalendar gcB = tdB.getCalendarRepresentation();
                        long m = gcB.getTimeInMillis();
                        gcB.setTimeInMillis(m + index);
                        tdIdx.setFromCalendar(gcB);
                    }
                    result.setDateTimeValue(tdIdx);
                    break;


                default:
                    result = null;
            }

        }
        if (isInRange(result)) {
            return result;
        } else {
            return null;
        }
    }

    @Override
    public SimcopValue[] getRangeValues() {
        return null;
    }

    @Override
    public void clear() {
        this.begin = null;
        this.end = null;
    }

    @Override
    public SimcopValue getFirst() {
        return this.begin;
    }

    @Override
    public SimcopValue getLast() {
        return this.end;
    }

    @Override
    public long indexOf(SimcopValue value) {
        if (begin == null || value == null) {
            return -1;
        } else {
            int beginType = begin.getType();
            int endType = value.getType();
            if (beginType != endType) {
                return -1;
            } else {
                /*
                 * only this types actually corresponds to a range
                 */
                switch (beginType) {
                    case SimcopValue.INTEGER:
                        return value.getIntegerValue() - begin.getIntegerValue();
                    case SimcopValue.DOUBLE:
                        return Math.round(value.getDoubleValue()) - Math.round(begin.getDoubleValue());
                    case SimcopValue.CATEGORY_INDEX:
                        return value.getCategoryIndex() - begin.getCategoryIndex();
                    case SimcopValue.TIME:
                        TimeDescription tdB = begin.getDateTimeValue();
                        TimeDescription tdE = value.getDateTimeValue();

                        if (tdB.getIndex() != null && tdE.getIndex() != null) {
                            return tdE.getIndex() - tdB.getIndex();
                        } else if (tdB.getAge() != null && tdE.getAge() != null) {
                            return tdE.getAge() - tdB.getAge();
                        } else if (tdB.getSeason() != null && tdE.getSeason() != null) {
                            return tdE.getSeason() - tdB.getSeason();
                        } else {
                            GregorianCalendar gcB = tdB.getCalendarRepresentation();
                            GregorianCalendar gcE = tdE.getCalendarRepresentation();
                            return gcE.getTimeInMillis() - gcB.getTimeInMillis();
                        }

                    default:
                        return -1;
                }

            }
        }

    }
}
