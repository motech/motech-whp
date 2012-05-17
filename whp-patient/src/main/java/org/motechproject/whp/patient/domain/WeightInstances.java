package org.motechproject.whp.patient.domain;

import org.motechproject.whp.refdata.domain.WeightInstance;

import java.util.ArrayList;

public class WeightInstances extends ArrayList<WeightStatistics> {

    @Override
    public boolean add(WeightStatistics weightStatistics) {
        WeightStatistics existingResult = resultForInstance(weightStatistics.getWeight_instance());
        if (existingResult != null) {
            remove(existingResult);
        }
        return super.add(weightStatistics);
    }

    private WeightStatistics resultForInstance(WeightInstance weightInstance) {
        for (WeightStatistics weightStatistics : this) {
            if (weightStatistics.isOfInstance(weightInstance))
                return weightStatistics;
        }
        return null;
    }

    public WeightStatistics latestResult() {
        return get(size() - 1);
    }
}
