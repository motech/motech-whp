package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.common.mapping.WHPCustomMapper;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringToTreatmentCategory extends WHPCustomMapper {

    AllTreatmentCategories allTreatmentCategories;

    @Autowired
    public StringToTreatmentCategory(AllTreatmentCategories allTreatmentCategories) {
        this.allTreatmentCategories = allTreatmentCategories;
    }

    @Override
    public Object convert(Object src, Class<?> destClass) {
        if (src == null) {
            return null;
        } else {
            return allTreatmentCategories.findByCode((String) src);
        }
    }
}
