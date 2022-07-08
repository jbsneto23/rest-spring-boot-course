package br.com.jbsneto.data.dto.v1;

import lombok.Data;

@Data
public class PersonDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
