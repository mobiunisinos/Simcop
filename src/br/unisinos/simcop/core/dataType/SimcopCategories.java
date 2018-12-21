package br.unisinos.simcop.core.dataType;

public class SimcopCategories extends SimcopRange {

    private SimcopValue[] categories;

    public SimcopCategories(String[] categories) {
        if (categories != null && categories.length > 0) {
            this.categories = new SimcopValue[categories.length];
            for (int idx = 0; idx < categories.length; idx++) {
                this.categories[idx] = SimcopValue.createFromObject(categories[idx]);
            }
        }
    }

    public SimcopCategories(SimcopValue[] categories) {
        this.categories = categories;
    }

    @Override
    public boolean isInRange(SimcopValue value) {
        long idx = indexOf(value);
        return (idx >= 0) && (idx < getRangeSize());
    }

    @Override
    public long indexOf(SimcopValue value) {
        int result = -1;
        if (getRangeSize() > 0) {
            for (int idx = 0; result < 0 && idx < categories.length; idx++) {
                boolean equal = (value == null ? categories[idx] == null : value.equals(categories[idx]));
                if (equal) {
                    result = idx;
                }
            }
        }
        return result;
    }

    @Override
    public long getRangeSize() {
        return categories == null ? 0 : categories.length;
    }

    @Override
    public SimcopValue getRangeValueAt(long index) {
        if (categories != null && index >= 0 && index < categories.length) {
            return categories[(int) index];
        } else {
            return null;
        }
    }

    @Override
    public SimcopValue[] getRangeValues() {
        return categories;
    }

    @Override
    public void clear() {
        this.categories = null;
    }

    @Override
    public SimcopValue getFirst() {
        if (categories != null && categories.length > 0) {
            return categories[0];
        } else {
            return null;
        }
    }

    @Override
    public SimcopValue getLast() {
        if (categories != null && categories.length > 0) {
            return categories[categories.length - 1];
        } else {
            return null;
        }
    }

    public SimcopValue[] getCategories() {
        return categories;
    }

    public void setCategories(SimcopValue[] categories) {
        this.categories = categories;
    }
}
