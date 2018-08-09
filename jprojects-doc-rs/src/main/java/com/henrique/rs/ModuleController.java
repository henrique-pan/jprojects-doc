package com.henrique.rs;

import com.henrique.dto.CollectionDTO;
import com.henrique.dto.ModuleDTO;
import com.henrique.entity.Module;
import com.henrique.entity.Scope;
import com.henrique.exception.RequestException;
import com.henrique.service.ModuleService;
import com.henrique.service.ScopeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.TreeSet;

@RestController
@RequestMapping("/scope")
public class ModuleController {

    @Autowired
    private ScopeService scopeService;

    @Autowired
    private ModuleService moduleService;

    @ResponseBody
    @RequestMapping(path = "{scopeName}", method = RequestMethod.GET)
    public ModuleDTO getScope(@PathVariable String scopeName, @RequestParam String moduleName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);
        Module module = moduleService.getModule(scope, moduleName);

        ModuleDTO resultDTO = new ModuleDTO(module);
        resultDTO.httpStatus = HttpStatus.OK.value();
        resultDTO.apiMessage = "The module has been found successfully.";

        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}/list", method = RequestMethod.GET)
    public CollectionDTO<ModuleDTO> getModuleList(@PathVariable String scopeName) throws Exception {
        CollectionDTO<ModuleDTO> resultCollection = new CollectionDTO<>(new TreeSet<>());

        Scope scope = scopeService.getScope(scopeName);

        Set<Module> modules = moduleService.getModules(scope);
        if (!modules.isEmpty()) {
            modules.stream().forEach(module -> {
                resultCollection.add(new ModuleDTO(module));
            });
            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Module list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}", method = RequestMethod.POST)
    public ModuleDTO createModule(@PathVariable String scopeName, @RequestBody ModuleDTO moduleDTO) throws Exception {
        Scope scope = scopeService.getScope(scopeName);

        Module newModule = new Module();
        newModule.setName(moduleDTO.getName());
        newModule.setLabel(moduleDTO.getLabel());
        newModule.setDescription(moduleDTO.getDescription());

        if (scope.getModular()) {
            moduleService.createModule(scope, newModule);
        } else {
            throw new RequestException("The scope is not modular.");
        }

        ModuleDTO resultDTO = new ModuleDTO(newModule);
        resultDTO.httpStatus = HttpStatus.CREATED.value();
        resultDTO.apiMessage = "The module has been created successfully.";

        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}/{moduleName}/doc", method = RequestMethod.POST)
    public ModuleDTO addDocumentation(@PathVariable String scopeName, @PathVariable String moduleName, @RequestParam String directory) throws Exception {
        Scope scope = scopeService.getScope(scopeName);
        Module module = moduleService.getModule(scope, moduleName);

        if (scope.getModular()) {
            moduleService.addDocumentation(scope, module, directory);
        } else {
            throw new RequestException("The scope is not modular.");
        }

        ModuleDTO resultDTO = new ModuleDTO(module);
        resultDTO.httpStatus = HttpStatus.CREATED.value();
        resultDTO.apiMessage = "The documentation has been added successfully.";

        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}/{moduleName}", method = RequestMethod.PUT)
    public ModuleDTO renameScope(@PathVariable String scopeName, @PathVariable String moduleName, @RequestParam String newModuleName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);
        Module module = moduleService.getModule(scope, moduleName);

        moduleService.renameModule(scope, module, newModuleName);

        ModuleDTO moduleDTO = new ModuleDTO(module);
        moduleDTO.httpStatus = HttpStatus.OK.value();
        moduleDTO.apiMessage = "The module has been renamed successfully.";

        return moduleDTO;
    }

    @ResponseBody
    @RequestMapping(path = "/{scopeName}/{moduleName}", method = RequestMethod.PATCH)
    public ModuleDTO updateScopeProperties(@PathVariable String scopeName, @PathVariable String moduleName, @RequestBody ModuleDTO moduleDTO) throws Exception {

        Scope scope = scopeService.getScope(scopeName);
        Module module = moduleService.getModule(scope, moduleName);

        if (moduleDTO.getDescription() != null) scope.setDescription(moduleDTO.getDescription());
        if (moduleDTO.getLabel() != null) scope.setLabel(moduleDTO.getLabel());

        moduleService.updateProperties(scope, module);

        ModuleDTO resultDTO = new ModuleDTO(module);
        resultDTO.httpStatus = HttpStatus.OK.value();
        resultDTO.apiMessage = "The module has been updated successfully.";

        return resultDTO;
    }

    @ResponseBody
    @RequestMapping(path = "{scopeName}", method = RequestMethod.DELETE)
    public ModuleDTO removeScope(@PathVariable String scopeName, @RequestParam String moduleName) throws Exception {
        Scope scope = scopeService.getScope(scopeName);
        Module module = moduleService.getModule(scope, moduleName);

        module = moduleService.removeModule(scope, module);

        ModuleDTO resultDTO = new ModuleDTO(module);
        resultDTO.httpStatus = HttpStatus.OK.value();
        resultDTO.apiMessage = "The module has been removed successfully.";

        return resultDTO;
    }

}
