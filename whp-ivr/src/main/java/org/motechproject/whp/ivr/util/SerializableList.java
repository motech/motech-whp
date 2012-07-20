package org.motechproject.whp.ivr.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableList extends ArrayList implements Serializable {

    public SerializableList(List object) {
        this.addAll(object);
    }
}
