package br.com.fiap.cidadeativa.bo;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.cidadeativa.dao.AtualizacaoDAO;
import br.com.fiap.cidadeativa.dao.OcorrenciaDAO;
import br.com.fiap.cidadeativa.dto.AtualizacaoEdicao;
import br.com.fiap.cidadeativa.dto.AtualizacaoEntrada;
import br.com.fiap.cidadeativa.dto.AtualizacaoSaida;
import br.com.fiap.cidadeativa.exception.NaoEncontradoException;
import br.com.fiap.cidadeativa.exception.RegraDeNegocioException;
import br.com.fiap.cidadeativa.model.Atualizacao;
import br.com.fiap.cidadeativa.model.Ocorrencia;
import br.com.fiap.cidadeativa.model.StatusOcorrencia;

@Service
public class AtualizacaoBO {
    private final AtualizacaoDAO dao;
    private final OcorrenciaDAO ocorrencias;
    private final ModelMapper mapper;

    public AtualizacaoBO(AtualizacaoDAO dao, OcorrenciaDAO ocorrencias, ModelMapper mapper) {
        this.dao = dao;
        this.ocorrencias = ocorrencias;
        this.mapper = mapper;
    }

    @Transactional
    public AtualizacaoSaida cadastrar(long ocorrenciaId, AtualizacaoEntrada entrada) {
        Ocorrencia ocorrencia = bloquearOcorrencia(ocorrenciaId);
        validarReabertura(ocorrencia.getStatus(), entrada.getStatus(), entrada.getObservacao());

        long id = dao.cadastrar(ocorrenciaId, mapper.map(entrada, Atualizacao.class));
        ocorrencias.atualizarStatus(ocorrenciaId, entrada.getStatus());
        return buscar(ocorrenciaId, id);
    }

    public List<AtualizacaoSaida> listar(long ocorrenciaId) {
        buscarOcorrencia(ocorrenciaId);
        return dao.listar(ocorrenciaId).stream()
                .map(atualizacao -> mapper.map(atualizacao, AtualizacaoSaida.class)).toList();
    }

    public AtualizacaoSaida buscar(long ocorrenciaId, long id) {
        buscarOcorrencia(ocorrenciaId);
        return dao.buscar(ocorrenciaId, id)
                .map(atualizacao -> mapper.map(atualizacao, AtualizacaoSaida.class))
                .orElseThrow(() -> atualizacaoInexistente(id));
    }

    @Transactional
    public AtualizacaoSaida atualizar(long ocorrenciaId, long id, AtualizacaoEdicao entrada) {
        bloquearOcorrencia(ocorrenciaId);
        Atualizacao atual = dao.buscar(ocorrenciaId, id).orElseThrow(() -> atualizacaoInexistente(id));
        if (atual.getStatus() == StatusOcorrencia.PENDENTE
                && atual.getObservacao() != null && !atual.getObservacao().isBlank()
                && (entrada.observacao() == null || entrada.observacao().isBlank())) {
            throw new RegraDeNegocioException("Mantenha uma justificativa na atualização para pendente.");
        }
        dao.atualizarObservacao(ocorrenciaId, id, entrada.observacao());
        return buscar(ocorrenciaId, id);
    }

    @Transactional
    public void excluir(long ocorrenciaId, long id) {
        Ocorrencia ocorrencia = bloquearOcorrencia(ocorrenciaId);
        if (!dao.excluir(ocorrenciaId, id)) {
            throw atualizacaoInexistente(id);
        }

        StatusOcorrencia novoStatus = dao.ultima(ocorrenciaId).map(Atualizacao::getStatus)
                .orElse(StatusOcorrencia.PENDENTE);
        validarReabertura(ocorrencia.getStatus(), novoStatus, null);
        ocorrencias.atualizarStatus(ocorrenciaId, novoStatus);
    }

    private void validarReabertura(StatusOcorrencia anterior, StatusOcorrencia novo, String observacao) {
        if (anterior == StatusOcorrencia.RESOLVIDA && novo == StatusOcorrencia.PENDENTE
                && (observacao == null || observacao.isBlank())) {
            throw new RegraDeNegocioException("Para reabrir uma ocorrência resolvida, informe uma justificativa.");
        }
    }

    private Ocorrencia bloquearOcorrencia(long id) {
        if (!ocorrencias.bloquear(id)) {
            throw ocorrenciaInexistente(id);
        }
        return buscarOcorrencia(id);
    }

    private Ocorrencia buscarOcorrencia(long id) {
        return ocorrencias.buscar(id).orElseThrow(() -> ocorrenciaInexistente(id));
    }

    private NaoEncontradoException ocorrenciaInexistente(long id) {
        return new NaoEncontradoException("Ocorrência não encontrada para o id " + id + ".");
    }

    private NaoEncontradoException atualizacaoInexistente(long id) {
        return new NaoEncontradoException("Atualização não encontrada para o id " + id + ".");
    }
}
