package br.com.fiap.cidadeativa.dto;

import jakarta.validation.constraints.Size;

public record AtualizacaoEdicao(@Size(max = 500) String observacao) {}
