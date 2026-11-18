package br.com.fiap.cidadeativa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.fiap.cidadeativa.model.Categoria;

@Repository
public class CategoriaDAO {
    private static final String SELECT = "SELECT cd_categoria, nm_categoria, ds_categoria FROM tb_categoria";
    private final JdbcTemplate jdbc;

    public CategoriaDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public long cadastrar(Categoria modelo) {
        long id = jdbc.queryForObject("SELECT sq_tb_categoria.NEXTVAL FROM dual", Long.class);
        String sql = "INSERT INTO tb_categoria (cd_categoria, nm_categoria, ds_categoria) "
                + "VALUES (?, ?, ?)";
        jdbc.update(sql, id, modelo.getNome(), modelo.getDescricao());
        return id;
    }

    public List<Categoria> listar() {
        return jdbc.query(SELECT + " ORDER BY cd_categoria", this::mapear, new Object[0]);
    }

    public Optional<Categoria> buscar(long id) {
        return jdbc.query(SELECT + " WHERE cd_categoria = ?", this::mapear, id).stream().findFirst();
    }

    public boolean atualizar(long id, Categoria modelo) {
        String sql = "UPDATE tb_categoria SET nm_categoria = ?, ds_categoria = ? WHERE cd_categoria = ?";
        return jdbc.update(sql, modelo.getNome(), modelo.getDescricao(), id) > 0;
    }

    public boolean excluir(long id) {
        return jdbc.update("DELETE FROM tb_categoria WHERE cd_categoria = ?", id) > 0;
    }

    private Categoria mapear(ResultSet rs, int linha) throws SQLException {
        Categoria modelo = new Categoria();
        modelo.setId(rs.getLong("cd_categoria"));
        modelo.setNome(rs.getString("nm_categoria"));
        modelo.setDescricao(rs.getString("ds_categoria"));
        return modelo;
    }
}
