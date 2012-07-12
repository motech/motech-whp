package org.motechproject.whp.migration.v1.migration;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TherapyMigration {

    private static final String APPLICATION_CONTEXT_XML = "applicationMigrationContext.xml";

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        ToVersion1 toVersion1 = (ToVersion1) context.getBean("toVersion1");
        toVersion1.doo();
    }

}
