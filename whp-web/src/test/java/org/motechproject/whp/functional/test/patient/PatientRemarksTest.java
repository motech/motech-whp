package org.motechproject.whp.functional.test.patient;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.PatientDashboardPage;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.page.provider.UpdateAdherencePage;
import org.motechproject.whp.functional.test.BasePatientTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class PatientRemarksTest extends BasePatientTest {

    @Before
    public void setUp() {
        super.setUp();
        setupProvider();
    }

    @Test
    public void shouldDisplayNoRemarksMessageOnlyWhenNoRemarksFromProviderAndCmfAdmin() {
        setupPatientForProvider();

        PatientDashboardPage patientDashboard = assertNoRemarksMessage();

        adjustDateTime(getLastSunday());
        patientDashboard = patientDashboard.addRemarks("test remark");
        assertThat(patientDashboard.getRemarks(), is(not(containsString("No remarks added for the patient."))));

        setupPatientForProvider();

        ProviderPage providerPage = LoginPage.fetch(webDriver).loginAsProvider(provider.getProviderId(), provider.getPassword());
        UpdateAdherencePage updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(2).setRemarks("provider Remarks").submit().logout();

        patientDashboard = getListPageFor(provider).clickOnPatientWithTherapyNotYetStarted(testPatient.getCaseId());
        assertThat(patientDashboard.getRemarks(), is(not(containsString("No remarks added for the patient."))));
    }

    @Test
    public void shouldDisplayCmfAdminRemarksChronologically() {
        setupPatientForProvider();

        PatientDashboardPage patientDashboard = getListPageFor(provider).clickOnPatientWithTherapyNotYetStarted(testPatient.getCaseId());


        String olderRemark = "older remark";
        String oldRemark = "old remark";
        String newRemark = "new remark";
        DateTime now  = DateUtil.now();

        DateTime olderRemarkTime = now.minusMonths(1);
        adjustDateTime(olderRemarkTime);
        patientDashboard.addRemarks(olderRemark);

        DateTime oldRemarkTime = now.minusMinutes(1);
        adjustDateTime(oldRemarkTime);
        patientDashboard.addRemarks(oldRemark);

        adjustDateTime(now);
        patientDashboard.addRemarks(newRemark);

        patientDashboard.verifyCmfAdminRemark("admin", now, newRemark, 0);
        patientDashboard.verifyCmfAdminRemark("admin", oldRemarkTime, oldRemark, 1);
        patientDashboard.verifyCmfAdminRemark("admin", olderRemarkTime, olderRemark, 2);
    }
    
    @Test
    public void shouldDisplayProviderRemarksChronologically() {
        setupPatientForProvider();

        DateTime sunday = getLastSunday();

        String olderRemark = "older remark";
        String oldRemark = "old remark";
        String newRemark = "new remark";
        
        ProviderPage providerPage = LoginPage.fetch(webDriver).loginAsProvider(provider.getProviderId(), provider.getPassword());

        DateTime olderRemarkTime = sunday.minusWeeks(2);
        adjustDateTime(olderRemarkTime);
        UpdateAdherencePage updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(0).setRemarks(olderRemark).submit();

        DateTime oldRemarkTime = sunday.minusWeeks(1);
        adjustDateTime(oldRemarkTime);
        updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(2).setRemarks(oldRemark).submit();

        adjustDateTime(sunday);
        updateAdherencePage = providerPage.clickEditAdherenceLink(testPatient.getCaseId());
        updateAdherencePage.setNumberOfDosesTaken(2).setRemarks(newRemark).submit().logout();

        TreatmentCardPage treatmentCardPage = getListPageFor(provider).clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        treatmentCardPage.verifyProviderRemark(provider.getProviderId(), sunday, newRemark, 0);
        treatmentCardPage.verifyProviderRemark(provider.getProviderId(), oldRemarkTime, oldRemark, 1);
        treatmentCardPage.verifyProviderRemark(provider.getProviderId(), olderRemarkTime, olderRemark, 2);
    }

    private DateTime getLastSunday() {
        int lastSundayOffset = DateUtil.daysPast(DateUtil.today(), DayOfWeek.Sunday);
        return DateUtil.now().minusDays(lastSundayOffset);
    }

    private PatientDashboardPage assertNoRemarksMessage() {
        ListAllPatientsPage listAllPatientsPage = getListPageFor(provider);
        PatientDashboardPage patientDashboard = listAllPatientsPage.clickOnPatientWithTherapyNotYetStarted(testPatient.getCaseId());
        assertThat(patientDashboard.getRemarks(), is("No remarks added for the patient."));
        return patientDashboard;
    }

    private ListAllPatientsPage getListPageFor(TestProvider provider) {
        return loginAsCMFAdmin().searchByDistrictAndProvider(provider.getDistrict(), provider.getProviderId());
    }
}
