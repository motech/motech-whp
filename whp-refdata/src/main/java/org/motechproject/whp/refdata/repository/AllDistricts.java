package org.motechproject.whp.refdata.repository;


import org.ektorp.support.GenericRepository;
import org.motechproject.whp.refdata.domain.District;

public interface AllDistricts extends GenericRepository<District>{
    public void refresh();
}