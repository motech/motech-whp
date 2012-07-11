package org.motechproject.whp.common;

import java.util.ArrayList;
import java.util.List;

public class UniqueElementList<T> extends ArrayList<T> {

    public UniqueElementList() {
        super();
    }

    public UniqueElementList(List<T> elements) {
        super(elements);
    }

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
