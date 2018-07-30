package com.henrique.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class Documentation {

    private Integer version;
    private Date creationDate;
    private String directory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documentation that = (Documentation) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Documentation [");
        sb.append("version = ").append(version);
        sb.append(", creationDate = ").append(creationDate);
        sb.append(']');
        return sb.toString();
    }
}
