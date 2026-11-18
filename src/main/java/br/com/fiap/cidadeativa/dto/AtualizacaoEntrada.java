package br.com.fiap.cidadeativa.dto;

import br.com.fiap.cidadeativa.model.StatusOcorrencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AtualizacaoEntrada {
    @Size(max=500)
    private String observacao;
    @NotNull
    private StatusOcorrencia status;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }
}
