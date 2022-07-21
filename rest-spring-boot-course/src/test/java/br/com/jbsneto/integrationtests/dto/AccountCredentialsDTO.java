package br.com.jbsneto.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement
public class AccountCredentialsDTO implements Serializable {
    private String username;
    private String password;
}
