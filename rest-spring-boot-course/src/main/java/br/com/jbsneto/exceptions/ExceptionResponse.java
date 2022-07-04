package br.com.jbsneto.exceptions;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
public class ExceptionResponse implements Serializable {
    Date timestamp;
    String message;
    String details;
}
