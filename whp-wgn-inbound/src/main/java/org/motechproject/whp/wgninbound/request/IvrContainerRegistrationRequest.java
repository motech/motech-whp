package org.motechproject.whp.wgninbound.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.common.domain.ContainerPhase;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "register_container")
@Data
@EqualsAndHashCode
public class IvrContainerRegistrationRequest {

    @NotNullOrEmpty
    @Size(min = 10, message = "should be atleast 10 digits in length")
    private String msisdn;


    @NotNullOrEmpty
    private String container_id;

    @NotNullOrEmpty
    @Enumeration(type = ContainerPhase.class)
    private String phase;

    @NotNullOrEmpty
    private String call_id;

    //for Spring MVC
    public IvrContainerRegistrationRequest() {
    }
}

