package br.com.fiap.cidadeativa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.fiap.cidadeativa.model.Bairro;

@Repository
public class BairroDAO {
    private static final String SELECT = "SELECT cd_bairro, nm_bairro, nm_cidade FROM tb_bairro";
    private final JdbcTemplate jdbc;

    public BairroDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public long cadastrar(Bairro modelo) {
        long id = jdbc.queryForObject("SELECT sq_tb_bairro.NEXTVAL FROM dual", Long.class);
        String sql = "INSERT INTO tb_bairro (cd_bairro, nm_bairro, nm_cidade) "
                + "VALUES (?, ?, ?)";
        jdbc.update(sql, id, modelo.getNome(), modelo.getCidade());
        return id;
    }

    public List<Bairro> listar() {
        return jdbc.query(SELECT + " ORDER BY cd_bairro", this::mapear, new Object[0]);
    }

    public Optional<Bairro> buscar(long id) {
        return jdbc.query(SELECT + " WHERE cd_bairro = ?", this::mapear, id).stream().findFirst();
    }

    public boolean atualizar(long id, Bairro modelo) {
        String sql = "UPDATE tb_bairro SET nm_bairro = ?, nm_cidade = ? WHERE cd_bairro = ?";
        return jdbc.update(sql, modelo.getNome(), modelo.getCidade(), id) > 0;
    }

    public boolean excluir(long id) {
        return jdbc.update("DELETE FROM tb_bairro WHERE cd_bairro = ?", id) > 0;
    }

    private Bairro mapear(ResultSet rs, int linha) throws SQLException {
        Bairro modelo = new Bairro();
        modelo.setId(rs.getLong("cd_bairro"));
        modelo.setNome(rs.getString("nm_bairro"));
        modelo.setCidade(rs.getString("nm_cidade"));
        return modelo;
    }
}
