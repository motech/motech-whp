package org.motechproject.whp.common.collections;

import org.junit.Test;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class BoundPriorityQueueTest {

    @Test
    public void shouldBeBoundBySize() {
        BoundPriorityQueue<Integer> queue = new BoundPriorityQueue<>(2);
        queue.insert(1);
        queue.insert(2);
        queue.insert(3);
        assertEquals(2, queue.size());
    }

    @Test
    public void shouldDeleteHighestPriorityElement() {
        BoundPriorityQueue<Integer> queue = new BoundPriorityQueue<>(2);

        queue.insert(2);
        queue.insert(1);

        Integer firstElement = queue.delete();
        Integer secondElement = queue.delete();

        assertFalse(firstElement.equals(secondElement));
    }

    @Test
    public void shouldPeekIntoHighestPriorityElement() {
        BoundPriorityQueue<Integer> queue = new BoundPriorityQueue<>(2);
        queue.insert(2);
        queue.insert(1);

        Integer highestPriorityElement = queue.peek();

        assertEquals(2, highestPriorityElement.intValue());
    }

    @Test
    public void shouldNotDeleteHighestPriorityElementOnPeek() {
        BoundPriorityQueue<Integer> queue = new BoundPriorityQueue<>(2);
        queue.insert(2);
        queue.insert(1);

        Integer firstElement = queue.peek();
        Integer secondElement = queue.peek();

        assertEquals(firstElement, secondElement);
    }

    @Test
    public void shouldReturnPrioritizedQueueOfElementsWhichRetainsElementsFIFO() {
        BoundPriorityQueue<Integer> queue = new BoundPriorityQueue<>(5);
        queue.insert(2);
        queue.insert(1);
        queue.insert(9);
        queue.insert(1);
        queue.insert(10);
        queue.insert(2);
        queue.insert(6);
        queue.insert(7);

        assertArrayEquals(new Integer[]{1, 2, 6, 7, 10}, queue.toArray());
    }

    @Test
    public void shouldReturnNullOnDeleteIfEmpty() {
        assertNull(new BoundPriorityQueue<Integer>(3).delete());
    }
}
