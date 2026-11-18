package br.com.fiap.cidadeativa.bo;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.cidadeativa.dao.BairroDAO;
import br.com.fiap.cidadeativa.dto.BairroEntrada;
import br.com.fiap.cidadeativa.dto.BairroSaida;
import br.com.fiap.cidadeativa.exception.NaoEncontradoException;
import br.com.fiap.cidadeativa.model.Bairro;

@Service
public class BairroBO {
    private final BairroDAO dao;
    private final ModelMapper mapper;

    public BairroBO(BairroDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Transactional
    public BairroSaida cadastrar(BairroEntrada entrada) {
        long id = dao.cadastrar(mapper.map(entrada, Bairro.class));
        return buscar(id);
    }

    public List<BairroSaida> listar() {
        return dao.listar().stream().map(modelo -> mapper.map(modelo, BairroSaida.class)).toList();
    }

    public BairroSaida buscar(long id) {
        return dao.buscar(id)
                .map(modelo -> mapper.map(modelo, BairroSaida.class))
                .orElseThrow(() -> naoEncontrado(id));
    }

    @Transactional
    public BairroSaida atualizar(long id, BairroEntrada entrada) {
        if (!dao.atualizar(id, mapper.map(entrada, Bairro.class))) {
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
        return new NaoEncontradoException("Bairro não encontrado para o id " + id + ".");
    }
}
