package br.com.fiap.cidadeativa.bo;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.cidadeativa.dao.OcorrenciaDAO;
import br.com.fiap.cidadeativa.dto.OcorrenciaEntrada;
import br.com.fiap.cidadeativa.dto.OcorrenciaSaida;
import br.com.fiap.cidadeativa.exception.NaoEncontradoException;
import br.com.fiap.cidadeativa.model.Ocorrencia;

@Service
public class OcorrenciaBO {
    private final OcorrenciaDAO dao;
    private final ModelMapper mapper;

    public OcorrenciaBO(OcorrenciaDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Transactional
    public OcorrenciaSaida cadastrar(OcorrenciaEntrada entrada) {
        long id = dao.cadastrar(mapper.map(entrada, Ocorrencia.class));
        return buscar(id);
    }

    public List<OcorrenciaSaida> listar() {
        return dao.listar().stream().map(modelo -> mapper.map(modelo, OcorrenciaSaida.class)).toList();
    }

    public OcorrenciaSaida buscar(long id) {
        return dao.buscar(id)
                .map(modelo -> mapper.map(modelo, OcorrenciaSaida.class))
                .orElseThrow(() -> naoEncontrado(id));
    }

    @Transactional
    public OcorrenciaSaida atualizar(long id, OcorrenciaEntrada entrada) {
        if (!dao.atualizar(id, mapper.map(entrada, Ocorrencia.class))) {
            throw naoEncontrado(id);
        }
        return buscar(id);
    }

    public void excluir(long id) {
        if (!dao.excluir(id)) {
            throw naoEncontrado(id);
        }
    }

    private NaoEncontradoException naoEncontrado(long id) {
        return new NaoEncontradoException("Ocorrencia não encontrado para o id " + id + ".");
    }
}
