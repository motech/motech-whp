package org.motechproject.whp.common.ektorp;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.DesignDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SearchFunctionUpdaterTest {

    @Mock
    private CouchDbConnector db;
    private SearchFunctionUpdater searchFunctionUpdater;
    private ArgumentCaptor<DesignDocument> documentArgumentCaptor;

    @Before
    public void setup() {
        initMocks(this);
        searchFunctionUpdater = new SearchFunctionUpdater();
        documentArgumentCaptor = ArgumentCaptor.forClass(DesignDocument.class);
    }

    @Test
    public void testAppendsDesignDocPrefixToViewName() {
        String viewName = "viewName";
        String expectedDesignDocName = "_design/viewName";

        searchFunctionUpdater.updateAnalyzer(db, viewName, "functionName", "analyzer");
        verify(db).contains(expectedDesignDocName);
    }

    @Test
    public void testDoesNotUpdateChecksumWhenAnalyzerChanges() {
        String viewName = "viewName";

        when(db.contains("_design/" + viewName)).thenReturn(false);
        searchFunctionUpdater.updateAnalyzer(db, viewName, "functionName", "analyzer");
        verify(db).create(documentArgumentCaptor.capture());
        assertEquals("_design/viewName", documentArgumentCaptor.getValue().getId());
    }

    @Test
    public void testAppendingAnalyzerCreatesNewDesignDocument() {
        String viewName = "viewName";

        when(db.contains("_design/" + viewName)).thenReturn(false);

        searchFunctionUpdater.updateAnalyzer(db, viewName, "functionName", "analyzer");
        verify(db).create(documentArgumentCaptor.capture());
        assertEquals("_design/" + viewName, documentArgumentCaptor.getValue().getId());
    }

    @Test
    public void testAppendingAnalyzerDoesNotCreateDuplicateDesignDocument() {
        String viewName = "viewName";

        when(db.contains("_design/" + viewName)).thenReturn(true);
        when(db.get(DesignDocument.class, "_design/" + viewName)).thenReturn(new DesignDocument("_design/" + viewName));

        searchFunctionUpdater.updateAnalyzer(db, viewName, "functionName", "analyzer");
        verify(db, never()).create(documentArgumentCaptor.capture());
    }

    @Test
    public void testUpdateAnalyzerValue() {
        String viewName = "viewName";
        DesignDocument document = documentWithFullText();

        when(db.contains("_design/" + viewName)).thenReturn(true);
        when(db.get(DesignDocument.class, "_design/" + viewName)).thenReturn(document);

        searchFunctionUpdater.updateAnalyzer(db, viewName, "functionName", "analyzer");
        verify(db).update(documentArgumentCaptor.capture());
        assertEquals("analyzer", ((Map<String, Map<String, String>>) documentArgumentCaptor.getValue().getAnonymous().get("fulltext")).get("functionName").get("analyzer"));
    }

    private DesignDocument documentWithFullText() {
        DesignDocument document = mock(DesignDocument.class);
        when(document.getAnonymous()).thenReturn(fullTextMap());
        return document;
    }

    private Map<String, Object> fullTextMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("fulltext", new HashMap<String, String>());
        return result;
    }
}
