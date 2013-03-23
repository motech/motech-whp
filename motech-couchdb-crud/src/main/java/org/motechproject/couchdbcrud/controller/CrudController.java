package org.motechproject.couchdbcrud.controller;

import org.motechproject.couchdbcrud.model.CrudEntity;
import org.motechproject.couchdbcrud.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CrudController {
    private CrudService crudService;

    @Autowired
    public CrudController(CrudService crudService) {
        this.crudService = crudService;
    }

    @RequestMapping(value = "/crud/display/{entity}")
    public String all(@PathVariable("entity") String entityName, Model model){
        CrudEntity entity = crudService.getEntity(entityName);

        model.addAttribute("entity", entityName);
        model.addAttribute("displayFields", entity.getDisplayFields());
        model.addAttribute("filterFields", entity.getFilterFields());
        return "crud/list";
    }
}
