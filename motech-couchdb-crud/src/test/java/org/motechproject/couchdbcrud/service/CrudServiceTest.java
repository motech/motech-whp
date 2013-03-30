package org.motechproject.couchdbcrud.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.couchdbcrud.repository.CrudRepository;
import org.motechproject.model.MotechBaseDataObject;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CrudServiceTest {

    CrudService crudService;
    @Mock
    CrudEntity crudEntity1;
    @Mock
    CrudEntity crudEntity2;
    @Mock
    CrudRepository crudRepository1;

    private final String entity1 = "entity1";
    private final String entity2 = "entity2";

    @Before
    public void setUp() {
        initMocks(this);

        when(crudEntity1.entityName()).thenReturn(entity1);
        when(crudEntity1.getRepository()).thenReturn(crudRepository1);
        when(crudEntity2.entityName()).thenReturn(entity2);

        Set<CrudEntity> crudEntities = new HashSet<>();
        crudEntities.add(crudEntity1);
        crudEntities.add(crudEntity2);

        crudService = new CrudService(crudEntities);
    }

    @Test
    public void shouldGetCrudEntityForGivenName() {
        assertEquals(crudEntity1, crudService.getCrudEntity(entity1));
        assertEquals(crudEntity2, crudService.getCrudEntity(entity2));
        assertNull(crudService.getCrudEntity("unknown"));
    }

    @Test
    public void shouldReturnEntityByDocId() {
        Object value = mock(Object.class);
        String docId = "docId";
        when(crudRepository1.get(docId)).thenReturn(value);

        assertEquals(value, crudService.getEntity(entity1, docId));
    }

    @Test
    public void shouldCreateNewEntityIfDocIdIsNull() {
        MotechBaseDataObject value = mock(MotechBaseDataObject.class);
        crudService.saveEntity(entity1, value);

        verify(crudRepository1).add(value);
    }

    @Test
    public void shouldUpdateEntityIfDocIdIsNotNull() {
        MotechBaseDataObject value = mock(MotechBaseDataObject.class);
        when(value.getId()).thenReturn("anId");

        crudService.saveEntity(entity1, value);

        verify(crudRepository1).update(value);
    }

    @Test
    public void shouldDeleteEntity() {
        String docId = "docId";
        MotechBaseDataObject value = mock(MotechBaseDataObject.class);
        when(crudRepository1.get(docId)).thenReturn(value);

        crudService.deleteEntity(entity1, docId);

        verify(crudRepository1).remove(value);
    }
}
