package com.henrique.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.henrique.entity.Documentation;
import com.henrique.entity.Module;
import com.henrique.entity.Scope;
import com.henrique.utils.TimeStampSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentationDTO extends HttpDTO implements Comparable {

    private Integer version;
    @JsonSerialize(using = TimeStampSerializer.class)
    private Date creationDate;
    private String directory;

    public DocumentationDTO() {}

    public DocumentationDTO(Documentation documentation) {
        this.version = documentation.getVersion();
        this.creationDate = documentation.getCreationDate();
        this.directory = documentation.getDirectory();
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(((DocumentationDTO) o).getVersion(), version);
    }
}
