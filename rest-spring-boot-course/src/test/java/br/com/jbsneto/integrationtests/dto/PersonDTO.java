package br.com.jbsneto.integrationtests.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonDTO implements Serializable  {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}