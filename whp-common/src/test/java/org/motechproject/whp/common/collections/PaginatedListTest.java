package org.motechproject.whp.common.collections;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PaginatedListTest {

    @Test
    public void testPaginationWhenPageSizeEqualsListSize() {
        PaginatedList<String> list = new PaginatedList<>(asList("element1"), 1);

        Iterator<List<String>> iterator = list.iterator();
        assertEquals(asList("element1"), iterator.next());
    }

    @Test
    public void testPaginationWhenPageSizeIsLessThanListSize() {
        PaginatedList<String> list = new PaginatedList<>(asList("element1", "element2"), 1);

        Iterator<List<String>> iterator = list.iterator();
        assertEquals(asList("element1"), iterator.next());
        assertEquals(asList("element2"), iterator.next());
    }

    @Test
    public void testPaginationWhenPageSizeIsGreaterThanListSize() {
        PaginatedList<String> list = new PaginatedList<>(asList("element1"), 2);

        Iterator<List<String>> iterator = list.iterator();
        assertEquals(asList("element1"), iterator.next());
    }

    @Test
    public void testPaginationWhenPageSizeIsZero() {
        PaginatedList<String> list = new PaginatedList<>(asList("element1"), 0);

        Iterator<List<String>> iterator = list.iterator();
        assertTrue(iterator.next().isEmpty());
    }

    @Test
    public void testPaginationWhenPageSizeIsNotFactorOfListSize() {
        PaginatedList<String> list = new PaginatedList<>(asList("element1", "element2", "element3"), 2);

        Iterator<List<String>> iterator = list.iterator();
        assertEquals(asList("element1", "element2"), iterator.next());
        assertEquals(asList("element3"), iterator.next());
    }

    @Test
    public void testPaginationWhenListIsEmpty() {
        PaginatedList<String> list = new PaginatedList<>(Collections.<String>emptyList(), 1);

        Iterator<List<String>> iterator = list.iterator();
        assertTrue(iterator.next().isEmpty());
    }
}
