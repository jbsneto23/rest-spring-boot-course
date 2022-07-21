package br.com.jbsneto.data.dto.v1.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountCredentialsDTO implements Serializable {
    private String username;
    private String password;
}
