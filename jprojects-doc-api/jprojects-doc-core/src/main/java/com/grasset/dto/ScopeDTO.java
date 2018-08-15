package com.grasset.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.grasset.utils.TimeStampSerializer;
import com.grasset.entity.Scope;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScopeDTO extends HttpDTO implements Comparable {

    private String name;
    private String description;
    private String label;
    private Boolean modular;
    @JsonSerialize(using = TimeStampSerializer.class)
    private Date creationDate;
    private Integer lastVersion;

    private Set<DocumentationDTO> documentations;

    private Set<ModuleDTO> modules;

    public ScopeDTO() {}

    public ScopeDTO(Scope scope) {
        this.name = scope.getName();
        this.description = scope.getDescription();
        this.label = scope.getLabel();
        this.modular = scope.getModular();
        this.creationDate = scope.getCreationDate();
        this.lastVersion = scope.getNextVersion() > 1 ? scope.getNextVersion() - 1 : null;

        if(scope.getModules() != null && !scope.getModules().isEmpty()) {
            if(modules == null) modules = new TreeSet<>();
            scope.getModules().stream().forEach(module -> {
                modules.add(new ModuleDTO(module));
            });
        }

        if(scope.getDocumentations() != null && !scope.getDocumentations().isEmpty()) {
            if(documentations == null) documentations = new TreeSet<>();
            scope.getDocumentations().stream().forEach(doc -> {
                documentations.add(new DocumentationDTO(doc));
            });
        }
    }

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((ScopeDTO)o).name);
    }
}
