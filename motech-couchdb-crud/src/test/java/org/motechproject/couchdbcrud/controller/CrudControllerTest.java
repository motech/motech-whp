package org.motechproject.couchdbcrud.controller;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.couchdbcrud.model.CrudEntity;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.couchdbcrud.service.CrudService;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class CrudControllerTest {

    public static final String ALTERNATE_DIAGNOSIS = "AlternateDiagnosis";
    CrudController crudController;

    @Mock
    CrudService crudService;

    @Before
    public void setUp() {
        initMocks(this);
        crudController = new CrudController(crudService);
    }

    @Test
    public void shouldSaveEntity() throws Exception {
        when(crudService.getEntity(ALTERNATE_DIAGNOSIS)).thenReturn(new AlternateDiagnosisCrudEntity());

        standaloneSetup(crudController).build()
                .perform(post("/crud/AlternateDiagnosis/save").body("{\"type\":\"AlternateDiagnosis\",\"code\":\"code\",\"name\":\"name\"}".getBytes()))
                .andExpect(status().isOk());

        AlternateDiagnosis expectedEntity = new AlternateDiagnosis("name", "code");
        verify(crudService).saveEntity(ALTERNATE_DIAGNOSIS, expectedEntity);
    }
}

@Data
class AlternateDiagnosis extends MotechBaseDataObject {

    private String name;
    private String code;

    //Required for Ektorp
    public AlternateDiagnosis() {
    }

    public AlternateDiagnosis(String name, String code) {
        this.name = name;
        this.code = code;
    }
}

class AlternateDiagnosisCrudEntity extends CrudEntity<AlternateDiagnosis>{

    @Override
    public List<String> getDisplayFields() {
        return null;
    }

    @Override
    public List<String> getFilterFields() {
        return null;
    }

    @Override
    public CrudRepository<AlternateDiagnosis> getRepository() {
        return null;
    }

    @Override
    public Class getEntityType() {
        return AlternateDiagnosis.class;
    }

    @Override
    public String entityName() {
        return CrudControllerTest.ALTERNATE_DIAGNOSIS;
    }
}