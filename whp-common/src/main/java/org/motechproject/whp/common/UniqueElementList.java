package org.motechproject.whp.common;


import java.util.ArrayList;

public class UniqueElementList<T> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        if (!contains(t)) {
            return super.add(t);
        }
        return false;
    }

    @Override
    public void add(int index, T element) {
        if (!contains(element)) {
            super.add(index, element);
        }
    }
}
