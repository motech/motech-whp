package org.motechproject.whp.ivr.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableList<T extends Serializable> extends ArrayList<T> implements Serializable {

    public SerializableList(List<T> object) {
        super(object);
    }

}
