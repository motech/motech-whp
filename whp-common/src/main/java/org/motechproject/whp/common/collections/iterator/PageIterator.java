package org.motechproject.whp.common.collections.iterator;

import java.util.Iterator;
import java.util.List;

import static java.lang.StrictMath.min;

public class PageIterator<E> implements Iterator<List<E>> {

    private final List<E> elements;
    private final int pageSize;
    private int currentPage;

    public PageIterator(List<E> elements, int pageSize) {
        this.elements = elements;
        this.pageSize = pageSize;
        this.currentPage = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex() < elements.size();
    }

    @Override
    public List<E> next() {
        List<E> result = currentElements();
        currentPage++;
        return result;
    }

    @Override
    public void remove() {
        elements.removeAll(currentElements());
    }

    private List<E> currentElements() {
        return elements.subList(currentIndex(), min(nextIndex(), elements.size()));
    }

    private int currentIndex() {
        return pageSize * currentPage;
    }

    private int nextIndex() {
        return pageSize * (currentPage + 1);
    }
}
