package com.henrique.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Scope {

    private String name;
    private String description;
    private String label;
    private Boolean modular;
    private Date creationDate;
    private Integer nextVersion;

    private Set<Module> modules;

    private Set<Documentation> documentations;

    public void add(Module module) {
        if(modules == null || modules.isEmpty()) {
            modules = new HashSet<>();
        }
        modules.add(module);
    }

    public void add(Documentation documentation) {
        if(documentations == null || documentations.isEmpty()) {
            documentations = new HashSet<>();
        }
        documentations.add(documentation);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Scope [");
        sb.append("name = ").append(name);
        sb.append(", description = ").append(description);
        sb.append(", label = ").append(label);
        sb.append(", modular = ").append(modular);
        sb.append(", creationDate = ").append(creationDate);
        sb.append(", nextVersion = ").append(nextVersion);

        if(documentations != null && !documentations.isEmpty()) {
            documentations.stream().forEach(documentation -> {
                sb.append("\n\t").append(documentation);
            });
        }

        if(modules != null && !modules.isEmpty()) {
            modules.stream().forEach(module -> {
                sb.append("\n\t").append(module);
            });
        }

        sb.append(']');
        return sb.toString();
    }
}
