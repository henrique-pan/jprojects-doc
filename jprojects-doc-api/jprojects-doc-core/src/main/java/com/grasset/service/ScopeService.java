package com.grasset.service;

import com.grasset.entity.Scope;
import java.util.Set;

public interface ScopeService {

    Scope getScope(String scopeName) throws Exception;
    Set<Scope> getScopes() throws Exception;
    void createScope(Scope scope) throws Exception;
    Scope renameScope(Scope scope, String newName) throws Exception;
    void updateProperties(Scope scope) throws Exception;
    void addDocumentation(Scope scope, String docDirectory) throws Exception;
    Scope removeScope(Scope scope) throws Exception;

}
