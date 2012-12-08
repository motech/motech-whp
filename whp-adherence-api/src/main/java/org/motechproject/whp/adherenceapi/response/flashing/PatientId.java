package org.motechproject.whp.adherenceapi.response.flashing;

import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;

@EqualsAndHashCode
class PatientId {

    @XmlElement
    String id;

    PatientId() {
    }

    PatientId(String id) {
        this.id = id;
    }
}
