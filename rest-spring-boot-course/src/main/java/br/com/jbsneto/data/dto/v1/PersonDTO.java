package br.com.jbsneto.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class PersonDTO extends RepresentationModel<PersonDTO> {
    @JsonProperty(value = "id", index = 0)
    @Mapping("id")
    private Long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
}
