package br.com.fiap.cidadeativa.bo;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.cidadeativa.dao.CidadaoDAO;
import br.com.fiap.cidadeativa.dto.CidadaoEntrada;
import br.com.fiap.cidadeativa.dto.CidadaoSaida;
import br.com.fiap.cidadeativa.exception.NaoEncontradoException;
import br.com.fiap.cidadeativa.model.Cidadao;

@Service
public class CidadaoBO {
    private final CidadaoDAO dao;
    private final ModelMapper mapper;

    public CidadaoBO(CidadaoDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Transactional
    public CidadaoSaida cadastrar(CidadaoEntrada entrada) {
        long id = dao.cadastrar(mapper.map(entrada, Cidadao.class));
        return buscar(id);
    }

    public List<CidadaoSaida> listar() {
        return dao.listar().stream().map(modelo -> mapper.map(modelo, CidadaoSaida.class)).toList();
    }

    public CidadaoSaida buscar(long id) {
        return dao.buscar(id)
                .map(modelo -> mapper.map(modelo, CidadaoSaida.class))
                .orElseThrow(() -> naoEncontrado(id));
    }

    @Transactional
    public CidadaoSaida atualizar(long id, CidadaoEntrada entrada) {
        if (!dao.atualizar(id, mapper.map(entrada, Cidadao.class))) {
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
        return new NaoEncontradoException("Cidadao não encontrado para o id " + id + ".");
    }
}
