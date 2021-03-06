package br.com.jbsneto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFountException extends RuntimeException{
    public ResourceNotFountException(String message) {
        super(message);
    }
}