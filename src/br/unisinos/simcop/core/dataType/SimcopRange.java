package br.unisinos.simcop.core.dataType;

public abstract class SimcopRange {

    public abstract boolean isInRange(SimcopValue value);

    public abstract long getRangeSize();

    public abstract long indexOf(SimcopValue value);

    public abstract SimcopValue getRangeValueAt(long index);

    public abstract SimcopValue[] getRangeValues();

    public abstract void clear();

    public abstract SimcopValue getFirst();

    public abstract SimcopValue getLast();
}
