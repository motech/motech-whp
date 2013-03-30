package org.motechproject.couchdbcrud.controller;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.motechproject.couchdbcrud.service.CrudEntity;
import org.motechproject.couchdbcrud.service.CrudService;
import org.motechproject.model.MotechBaseDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class CrudController {
    private CrudService crudService;
    private ObjectMapper objectMapper;

    @Autowired
    public CrudController(CrudService crudService) {
        this.crudService = crudService;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/crud/{entity}/list")
    public String list(@PathVariable("entity") String entityName, Model model){
        CrudEntity entity = crudService.getCrudEntity(entityName);

        model.addAttribute("entity", entityName);
        model.addAttribute("displayFields", entity.getDisplayFields());
        model.addAttribute("filterFields", entity.getFilterFields());
        return "crud/list";
    }

    @RequestMapping(value = "/crud/{entity}/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("entity") String entityName, @PathVariable("id") String entityId){
        crudService.deleteEntity(entityName, entityId);
    }

    @RequestMapping(value = "/crud/{entity}/get/{id}")
    @ResponseBody
    public Object get(@PathVariable("entity") String entityName, @PathVariable("id") String entityId){
        return crudService.getEntity(entityName, entityId);
    }

    @RequestMapping(value = "/crud/{entity}/save")
    @ResponseBody
    public void save(@PathVariable("entity") String entityName, @RequestBody String entityJson) throws IOException {
        CrudEntity crudEntity = crudService.getCrudEntity(entityName);
        MotechBaseDataObject object = (MotechBaseDataObject) objectMapper.readValue(entityJson, crudEntity.getEntityType());
        crudService.saveEntity(entityName, object);
    }

    @ResponseBody
    @RequestMapping(value = "/crud/{entity}/schema")
    public JsonSchema schema(@PathVariable("entity") String entityName) throws JsonMappingException {
        Class entityType = crudService.getCrudEntity(entityName).getEntityType();
        return objectMapper.generateJsonSchema(entityType);
    }
}