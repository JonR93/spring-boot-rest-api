package com.springboot.rest.api.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private final String resourceName;
    private final String fieldName;
    private final transient Object fieldValue;

    public ResourceNotFoundException(@NotNull Class clazz, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", clazz.getSimpleName(), fieldName, fieldValue));
        this.resourceName = clazz.getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
