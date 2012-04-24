package org.motechproject.whp.tools;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RecreateDB {
    public static final String APPLICATION_CONTEXT_XML = "applicationToolsContext.xml";

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        CouchDB couchDB = context.getBean(CouchDB.class);
        couchDB.recreate();
    }
}
