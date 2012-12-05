package org.motechproject.whp.adherenceapi.response;

import javax.xml.bind.annotation.XmlElement;

class PatientId {

    @XmlElement
    String id;

    PatientId() {
    }

    PatientId(String id) {
        this.id = id;
    }
}
