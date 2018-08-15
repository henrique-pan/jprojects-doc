package com.grasset.rs;

import com.grasset.dto.CollectionDTO;
import com.grasset.dto.ScopeDTO;
import com.grasset.entity.Scope;
import com.grasset.exception.RequestException;
import com.grasset.service.ModuleService;
import com.grasset.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/scope")
public class ScopeController {

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private ModuleService moduleService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public ScopeDTO getScope(@RequestParam String scopeName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);

        ScopeDTO scopeDTO = new ScopeDTO(scope);
        scopeDTO.httpStatus = HttpStatus.OK.value();
        scopeDTO.apiMessage = "The scope has been found successfully.";

        return scopeDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public CollectionDTO<ScopeDTO> getScopeList() throws Exception {
        CollectionDTO<ScopeDTO> resultCollection = new CollectionDTO<>(new TreeSet<>());

        Set<Scope> scopes = scopeService.getScopes();

        if (!scopes.isEmpty()) {
            scopes.stream().forEach(scope -> {
                resultCollection.add(new ScopeDTO(scope));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Scope list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ScopeDTO createScope(@RequestBody ScopeDTO scope) throws Exception {
        Scope newScope = new Scope();
        newScope.setName(scope.getName());
        newScope.setDescription(scope.getDescription());
        newScope.setLabel(scope.getLabel());

        newScope.setModular(scope.getModular());

        scopeService.createScope(newScope);

        ScopeDTO scopeDTO = new ScopeDTO(newScope);
        scopeDTO.httpStatus = HttpStatus.CREATED.value();
        scopeDTO.apiMessage = "The scope has been created successfully.";

        return scopeDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}/doc", method = RequestMethod.POST)
    public ScopeDTO addDocumentation(@PathVariable String scopeName, @RequestParam String directory) throws Exception {
        Scope scope = scopeService.getScope(scopeName);

        if (!scope.getModular()) {
            scopeService.addDocumentation(scope, directory);
        } else {
            throw new RequestException("The scope is modular.");
        }

        ScopeDTO scopeDTO = new ScopeDTO(scope);
        scopeDTO.httpStatus = HttpStatus.CREATED.value();
        scopeDTO.apiMessage = "The documentation has been added successfully.";

        return scopeDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}", method = RequestMethod.PUT)
    public ScopeDTO renameScope(@PathVariable String scopeName, @RequestParam String newScopeName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);

        scopeService.renameScope(scope, newScopeName);

        ScopeDTO scopeDTO = new ScopeDTO(scope);
        scopeDTO.httpStatus = HttpStatus.OK.value();
        scopeDTO.apiMessage = "The scope has been renamed successfully.";

        return scopeDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}", method = RequestMethod.PATCH)
    public ScopeDTO updateScopeProperties(@PathVariable String scopeName, @RequestBody ScopeDTO scopeDTO) throws Exception {

        Scope scope = scopeService.getScope(scopeName);

        if (scopeDTO.getDescription() != null) scope.setDescription(scopeDTO.getDescription());
        if (scopeDTO.getLabel() != null) scope.setLabel(scopeDTO.getLabel());
        if (scopeDTO.getModular() != null && (scopeDTO.getModular() != scope.getModular())) {
            if (scope.getDocumentations() == null && moduleService.getModules(scope).isEmpty()) {
                scope.setModular(scopeDTO.getModular());
            } else {
                throw new RequestException("It is not possible to update. Scope has already content.");
            }
        }

        scopeService.updateProperties(scope);

        ScopeDTO resultDTO = new ScopeDTO(scope);
        resultDTO.httpStatus = HttpStatus.OK.value();
        resultDTO.apiMessage = "The scope has been updated successfully.";

        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE)
    public ScopeDTO removeScope(@RequestParam String scopeName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);

        scopeService.removeScope(scope);

        ScopeDTO scopeDTO = new ScopeDTO(scope);
        scopeDTO.httpStatus = HttpStatus.OK.value();
        scopeDTO.apiMessage = "The scope has been removed successfully.";

        return scopeDTO;
    }

}
