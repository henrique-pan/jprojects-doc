package com.henrique.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.henrique.utils.TimeStampSerializer;
import lombok.Getter;
import lombok.Setter;
import com.henrique.entity.Module;

import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDTO extends HttpDTO implements Comparable {

    private String name;
    private String description;
    private String label;
    @JsonSerialize(using = TimeStampSerializer.class)
    private Date creationDate;
    private Integer lastVersion;

    private Set<DocumentationDTO> documentations;

    public ModuleDTO() {}

    public ModuleDTO(Module module) {
        this.name = module.getName();
        this.description = module.getDescription();
        this.label = module.getLabel();
        this.creationDate = module.getCreationDate();
        this.lastVersion = module.getNextVersion() > 1 ? module.getNextVersion() - 1 : null;

        if(module.getDocumentations() != null && !module.getDocumentations().isEmpty()) {
            if(documentations == null) documentations = new TreeSet<>();
            module.getDocumentations().stream().forEach(doc -> {
                documentations.add(new DocumentationDTO(doc));
            });
        }
    }

    @Override
    public int compareTo(Object o) {
        return ((ModuleDTO)o).name.compareTo(name);
    }

}
