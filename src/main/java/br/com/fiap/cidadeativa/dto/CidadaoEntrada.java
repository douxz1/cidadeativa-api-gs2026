package br.com.fiap.cidadeativa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CidadaoEntrada {
    @NotBlank
    @Size(max=120)
    private String nome;
    @NotBlank
    @Email
    @Size(max=160)
    private String email;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
