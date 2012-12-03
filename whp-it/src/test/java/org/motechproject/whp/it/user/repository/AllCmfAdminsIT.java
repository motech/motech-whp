package org.motechproject.whp.it.user.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.user.domain.CmfAdmin;
import org.motechproject.whp.user.repository.AllCmfAdmins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = "classpath*:/applicationUserContext.xml")
public class AllCmfAdminsIT extends SpringIntegrationTest {

    @Autowired
    AllCmfAdmins allCmfAdmins;

    @Before
    @After
    public void setUp(){
        allCmfAdmins.removeAll();
    }
    @Test
    public void shouldAddCmfAdminToDb() {
        allCmfAdmins.add(new CmfAdmin("user","email","dept", "location", "staffName"));

        assertNotNull(allCmfAdmins.findByUserId("user"));
    }

    @Test
    public void findByUserIdShouldBeCaseInsensitive() {
        allCmfAdmins.add(new CmfAdmin("user","email","dept", "location", "staffName"));

        assertNotNull(allCmfAdmins.findByUserId("USER"));
    }

    @Test
    public void shouldListAll_sortedByStaffName() {
        List<CmfAdmin> testCmfAdmins = new ArrayList<>();
        testCmfAdmins.add(new CmfAdmin("user1", "email", "dept", "location", "a"));
        testCmfAdmins.add(new CmfAdmin("user1", "email", "dept", "location", "b"));
        testCmfAdmins.add(new CmfAdmin("user1", "email", "dept", "location", "c"));
        for(CmfAdmin cmfAdmin : testCmfAdmins) {
            allCmfAdmins.add(cmfAdmin);
        }

        List<CmfAdmin> cmfAdmins = allCmfAdmins.list();

        assertNotNull(cmfAdmins);
        assertTrue(cmfAdmins.containsAll(testCmfAdmins));
        assertTrue(cmfAdmins.indexOf(testCmfAdmins.get(0)) < cmfAdmins.indexOf(testCmfAdmins.get(1)));
        assertTrue(cmfAdmins.indexOf(testCmfAdmins.get(1)) < cmfAdmins.indexOf(testCmfAdmins.get(2)));
        assertTrue(cmfAdmins.indexOf(testCmfAdmins.get(0)) < cmfAdmins.indexOf(testCmfAdmins.get(2)));
    }
}
