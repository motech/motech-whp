package org.motechproject.whp.common.ektorp;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.DesignDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class SearchFunctionUpdater {

    private final Logger log = LoggerFactory.getLogger(SearchFunctionUpdater.class);
    private static final String CHECKSUM_SUFFIX = "_checksum";

    public void updateAnalyzer(CouchDbConnector db, String viewName, String searchFunction, String analyzer) {

        String designDocName = viewName.startsWith(DesignDocument.ID_PREFIX) ? viewName : (DesignDocument.ID_PREFIX + viewName);

        Checksum chk = new CRC32();
        byte[] bytes = analyzer.getBytes();
        chk.update(bytes, 0, bytes.length);
        long actualChk = chk.getValue();

        if (!db.contains(designDocName)) {
            DesignDocument doc = new DesignDocument(designDocName);
            db.create(doc);
            updateAnalyzer(db, doc, searchFunction, analyzer, actualChk);
        } else {
            DesignDocument doc = db.get(DesignDocument.class, designDocName);
            log.info("Updating the search document");
            updateAnalyzer(db, doc, searchFunction, analyzer, actualChk);
        }
    }

    private String getChecksumFieldName(String searchFunction) {
        return searchFunction + CHECKSUM_SUFFIX;
    }

    private void updateAnalyzer(CouchDbConnector db, DesignDocument doc, String searchFunctionName, String value, long jsChecksum) {
        Map<String, Map<String, String>> fullText = (Map<String, Map<String, String>>) doc.getAnonymous().get("fulltext");
        if (fullText == null) {
            fullText = new HashMap<>();
            doc.setAnonymous("fulltext", fullText);
        }
        Map<String, String> searchObject = fullText.get(searchFunctionName);
        if (searchObject == null) {
            searchObject = new HashMap<>();
            fullText.put(searchFunctionName, searchObject);
        }
        searchObject.put("analyzer", value);
        doc.setAnonymous(getChecksumFieldName(searchFunctionName), jsChecksum);
        db.update(doc);
    }

}
