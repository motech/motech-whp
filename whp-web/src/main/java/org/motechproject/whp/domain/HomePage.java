package org.motechproject.whp.domain;


import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@EqualsAndHashCode
public class HomePage {

    private static final String EMPTY = "";

    private List<String> pages;

    public HomePage() {
        pages = new ArrayList<>();
    }

    public HomePage(List<String> pages) {
        this.pages = pages;
    }

    public void add(String page) {
        if (isNotBlank(page)) {
            pages.add(page);
        }
    }

    public String getHomePage() {
        if (CollectionUtils.isNotEmpty(pages)) {
            return pages.get(0);
        } else {
            return EMPTY;
        }
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(pages);
    }
}
