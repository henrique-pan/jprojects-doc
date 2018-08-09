package com.henrique.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class Module {

    private String name;
    private String description;
    private String label;
    private Date creationDate;
    private Integer nextVersion;

    private Set<Documentation> documentations;

    public void add(Documentation documentation) {
        if(documentations == null || documentations.isEmpty()) documentations = new HashSet<>();
        documentations.add(documentation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(name, module.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Module [");
        sb.append("name = ").append(name);
        sb.append(", description = ").append(description);
        sb.append(", label = ").append(label);
        sb.append(", creationDate = ").append(creationDate);

        if(documentations != null && !documentations.isEmpty()) {
            documentations.forEach(documentation -> {
                sb.append(", ").append(documentation);
            });
        }

        sb.append(", nextVersion = ").append(nextVersion);
        sb.append(']');
        return sb.toString();
    }
}
