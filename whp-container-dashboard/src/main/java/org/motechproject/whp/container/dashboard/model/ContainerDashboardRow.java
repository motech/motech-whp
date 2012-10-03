package org.motechproject.whp.container.dashboard.model;


import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.user.domain.Provider;

@TypeDiscriminator("doc.type == 'ContainerDashboardRow'")
@Data
public class ContainerDashboardRow extends MotechBaseDataObject {

    private Container container;

    private Patient patient;

    private Provider provider;

}
