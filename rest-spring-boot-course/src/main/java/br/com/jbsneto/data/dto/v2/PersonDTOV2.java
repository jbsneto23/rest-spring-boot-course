package br.com.jbsneto.data.dto.v2;

import lombok.Data;

import java.util.Date;

@Data
public class PersonDTOV2 {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Date birthDate;
}
