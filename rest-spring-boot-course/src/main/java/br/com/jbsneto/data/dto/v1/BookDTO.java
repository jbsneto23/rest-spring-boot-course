package br.com.jbsneto.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Data
public class BookDTO extends RepresentationModel<PersonDTO> implements Serializable {
    @JsonProperty(value = "id", index = 0)
    @Mapping("id")
    private Long key;
    private String author;
    private String title;
    private Date launchDate;
    private Double price;
}
