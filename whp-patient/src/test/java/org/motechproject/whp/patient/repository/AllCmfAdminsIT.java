package org.motechproject.whp.patient.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.domain.CmfAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:/applicationPatientContext.xml")
public class AllCmfAdminsIT extends SpringIntegrationTest{

    @Autowired
    AllCmfAdmins allCmfAdmins;

    @Before
    @After
    public void setUp(){
        allCmfAdmins.removeAll();
    }
    @Test
    public void shouldAddCmfAdminToDb() {
        allCmfAdmins.add(new CmfAdmin("user","email","dept", "location"));

        assertNotNull(allCmfAdmins.findByUserId("user"));
    }

    @Test
    public void findByUserIdShouldBeCaseInsensitive() {
        allCmfAdmins.add(new CmfAdmin("user","email","dept", "location"));

        assertNotNull(allCmfAdmins.findByUserId("USER"));
    }
}
