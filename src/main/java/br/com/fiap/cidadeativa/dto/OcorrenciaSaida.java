package br.com.fiap.cidadeativa.dto;

import java.time.LocalDateTime;
import br.com.fiap.cidadeativa.model.StatusOcorrencia;

public class OcorrenciaSaida {
    private Long id;
    private Long cidadaoId;
    private Long bairroId;
    private Long categoriaId;
    private String descricao;
    private String endereco;
    private Integer prioridade;
    private StatusOcorrencia status;
    private LocalDateTime data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCidadaoId() {
        return cidadaoId;
    }

    public void setCidadaoId(Long cidadaoId) {
        this.cidadaoId = cidadaoId;
    }

    public Long getBairroId() {
        return bairroId;
    }

    public void setBairroId(Long bairroId) {
        this.bairroId = bairroId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
