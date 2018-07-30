package com.henrique.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Collection;

@Getter
@Setter
public class CollectionDTO<T> extends HttpDTO {

    private Collection<T> collection;

    public CollectionDTO(Collection<T> collection) {
        this.collection = collection;
    }

    public CollectionDTO(Integer httpStatus, Collection<T> collection, String apiMessage) {
        super.httpStatus = httpStatus;
        super.apiMessage = apiMessage;
        this.collection = collection;
    }

    public boolean add(T e) {
        return collection.add(e);
    }

}
