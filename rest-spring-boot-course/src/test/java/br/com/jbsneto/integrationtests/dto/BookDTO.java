package br.com.jbsneto.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement
public class BookDTO implements Serializable {
    private Long id;
    private String author;
    private String title;
    private Date launchDate;
    private Double price;
}
