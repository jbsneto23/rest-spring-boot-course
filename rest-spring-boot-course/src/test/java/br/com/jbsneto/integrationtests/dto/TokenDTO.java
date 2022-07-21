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
public class TokenDTO implements Serializable {
    private String username;
    private boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;
}
