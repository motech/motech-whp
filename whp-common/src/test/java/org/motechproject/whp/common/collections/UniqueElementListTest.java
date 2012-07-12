package org.motechproject.whp.common.collections;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueElementListTest {

    @Test
    public void shouldNotContainDuplicateElementsOnAdd() {
        List<String> list = new UniqueElementList<>();
        list.add("data");
        list.add("data");
        assertEquals(1, list.size());
    }

    @Test
    public void shouldNotContainDuplicateElementsOnInsert() {
        List<String> list = new UniqueElementList<>();
        list.add(0, "data");
        list.add(1, "data");
        assertEquals(1, list.size());
    }

    @Test
    public void shouldNotContainDuplicateElementsOnUpdate() {
        List<String> list = new UniqueElementList<>();
        list.add(0, "data");
        list.add(0, "data");
        assertEquals(1, list.size());
    }
}