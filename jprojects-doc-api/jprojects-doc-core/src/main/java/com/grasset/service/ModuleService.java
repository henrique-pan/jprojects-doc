package com.grasset.service;

import com.grasset.entity.Module;
import com.grasset.entity.Scope;

import java.util.Set;

public interface ModuleService {

    Module getModule(Scope scope, String moduleName) throws Exception;
    Set<Module> getModules(Scope scope) throws Exception;
    void createModule(Scope scope, Module module) throws Exception;
    Module renameModule(Scope scope, Module module, String newName) throws Exception;
    void updateProperties(Scope scope, Module module) throws Exception;
    void addDocumentation(Scope scope, Module module, String docDirectory) throws Exception;
    Module removeModule(Scope scope, Module module) throws Exception;

}
