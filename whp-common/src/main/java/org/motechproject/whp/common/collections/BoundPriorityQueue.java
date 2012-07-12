package org.motechproject.whp.common.collections;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoundPriorityQueue<T extends Comparable<T>> {

    @JsonProperty
    private List<T> list = new ArrayList<>();

    @JsonProperty
    private int size;

    /*Ektorp*/
    public BoundPriorityQueue() {
    }

    public BoundPriorityQueue(int size) {
        this.size = size;
    }

    public T insert(T element) {
        if (!list.isEmpty() && size() == size) {
            list.remove(0);
        }
        list.add(element);
        return element;
    }

    public T delete() {
        if (!list.isEmpty()) {
            T element = sorted().get(list.size() - 1);
            list.remove(element);
            return element;
        }
        return null;
    }

    public T peek() {
        if (!list.isEmpty()) {
            return sorted().get(list.size() - 1);
        }
        return null;
    }

    public T get(int index) {
        return sorted().get(index);
    }

    public int size() {
        return list.size();
    }

    public Object[] toArray() {
        return sorted().toArray();
    }

    private List<T> sorted() {
        List<T> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        return sortedList;
    }
}
