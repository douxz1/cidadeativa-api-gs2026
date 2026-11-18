package br.com.fiap.cidadeativa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.fiap.cidadeativa.model.Cidadao;

@Repository
public class CidadaoDAO {
    private static final String SELECT = "SELECT cd_cidadao, nm_cidadao, ds_email FROM tb_cidadao";
    private final JdbcTemplate jdbc;

    public CidadaoDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public long cadastrar(Cidadao modelo) {
        long id = jdbc.queryForObject("SELECT sq_tb_cidadao.NEXTVAL FROM dual", Long.class);
        String sql = "INSERT INTO tb_cidadao (cd_cidadao, nm_cidadao, ds_email) "
                + "VALUES (?, ?, ?)";
        jdbc.update(sql, id, modelo.getNome(), modelo.getEmail());
        return id;
    }

    public List<Cidadao> listar() {
        return jdbc.query(SELECT + " ORDER BY cd_cidadao", this::mapear, new Object[0]);
    }

    public Optional<Cidadao> buscar(long id) {
        return jdbc.query(SELECT + " WHERE cd_cidadao = ?", this::mapear, id).stream().findFirst();
    }

    public boolean atualizar(long id, Cidadao modelo) {
        String sql = "UPDATE tb_cidadao SET nm_cidadao = ?, ds_email = ? WHERE cd_cidadao = ?";
        return jdbc.update(sql, modelo.getNome(), modelo.getEmail(), id) > 0;
    }

    public boolean excluir(long id) {
        return jdbc.update("DELETE FROM tb_cidadao WHERE cd_cidadao = ?", id) > 0;
    }

    private Cidadao mapear(ResultSet rs, int linha) throws SQLException {
        Cidadao modelo = new Cidadao();
        modelo.setId(rs.getLong("cd_cidadao"));
        modelo.setNome(rs.getString("nm_cidadao"));
        modelo.setEmail(rs.getString("ds_email"));
        return modelo;
    }
}
