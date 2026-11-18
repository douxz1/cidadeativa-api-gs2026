package br.com.fiap.cidadeativa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BairroEntrada {
    @NotBlank
    @Size(max=100)
    private String nome;
    @NotBlank
    @Size(max=100)
    private String cidade;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
