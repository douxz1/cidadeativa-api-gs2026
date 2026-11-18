package br.com.fiap.cidadeativa.bo;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.fiap.cidadeativa.dao.CategoriaDAO;
import br.com.fiap.cidadeativa.dto.CategoriaEntrada;
import br.com.fiap.cidadeativa.dto.CategoriaSaida;
import br.com.fiap.cidadeativa.exception.NaoEncontradoException;
import br.com.fiap.cidadeativa.model.Categoria;

@Service
public class CategoriaBO {
    private final CategoriaDAO dao;
    private final ModelMapper mapper;

    public CategoriaBO(CategoriaDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Transactional
    public CategoriaSaida cadastrar(CategoriaEntrada entrada) {
        long id = dao.cadastrar(mapper.map(entrada, Categoria.class));
        return buscar(id);
    }

    public List<CategoriaSaida> listar() {
        return dao.listar().stream().map(modelo -> mapper.map(modelo, CategoriaSaida.class)).toList();
    }

    public CategoriaSaida buscar(long id) {
        return dao.buscar(id)
                .map(modelo -> mapper.map(modelo, CategoriaSaida.class))
                .orElseThrow(() -> naoEncontrado(id));
    }

    @Transactional
    public CategoriaSaida atualizar(long id, CategoriaEntrada entrada) {
        if (!dao.atualizar(id, mapper.map(entrada, Categoria.class))) {
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
        return new NaoEncontradoException("Categoria não encontrado para o id " + id + ".");
    }
}
