package org.motechproject.whp.common.collections;

import org.motechproject.whp.common.collections.iterator.PageIterator;

import java.util.Iterator;
import java.util.List;

public class PaginatedList<E> implements Iterable<List<E>> {

    private List<E> elements;
    private int pageSize;

    public PaginatedList(List<E> elements, int pageSize) {
        this.elements = elements;
        this.pageSize = pageSize;
    }

    @Override
    public Iterator<List<E>> iterator() {
        return new PageIterator<>(elements, pageSize);
    }
}
