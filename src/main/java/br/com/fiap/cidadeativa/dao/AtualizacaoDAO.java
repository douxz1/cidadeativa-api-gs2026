package br.com.fiap.cidadeativa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.fiap.cidadeativa.model.Atualizacao;
import br.com.fiap.cidadeativa.model.StatusOcorrencia;

@Repository
public class AtualizacaoDAO {
    private static final String SELECT = """
            SELECT cd_atualizacao, cd_ocorrencia, ds_observacao, ds_status, dt_atualizacao
            FROM tb_atualizacao WHERE cd_ocorrencia = ?
            """;
    private final JdbcTemplate jdbc;

    public AtualizacaoDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public long cadastrar(long ocorrenciaId, Atualizacao atualizacao) {
        long id = jdbc.queryForObject("SELECT sq_tb_atualizacao.NEXTVAL FROM dual", Long.class);
        String sql = """
                INSERT INTO tb_atualizacao (cd_atualizacao, cd_ocorrencia, ds_observacao, ds_status)
                VALUES (?, ?, ?, ?)
                """;
        jdbc.update(sql, id, ocorrenciaId, atualizacao.getObservacao(), atualizacao.getStatus().name());
        return id;
    }

    public List<Atualizacao> listar(long ocorrenciaId) {
        return jdbc.query(SELECT + " ORDER BY cd_atualizacao DESC", this::mapear, ocorrenciaId);
    }

    public Optional<Atualizacao> buscar(long ocorrenciaId, long id) {
        return jdbc.query(SELECT + " AND cd_atualizacao = ?", this::mapear, ocorrenciaId, id)
                .stream().findFirst();
    }

    public Optional<Atualizacao> ultima(long ocorrenciaId) {
        String sql = SELECT + " ORDER BY cd_atualizacao DESC FETCH FIRST 1 ROWS ONLY";
        return jdbc.query(sql, this::mapear, ocorrenciaId).stream().findFirst();
    }

    public boolean atualizarObservacao(long ocorrenciaId, long id, String observacao) {
        String sql = "UPDATE tb_atualizacao SET ds_observacao = ? WHERE cd_ocorrencia = ? AND cd_atualizacao = ?";
        return jdbc.update(sql, observacao, ocorrenciaId, id) > 0;
    }

    public boolean excluir(long ocorrenciaId, long id) {
        String sql = "DELETE FROM tb_atualizacao WHERE cd_ocorrencia = ? AND cd_atualizacao = ?";
        return jdbc.update(sql, ocorrenciaId, id) > 0;
    }

    private Atualizacao mapear(ResultSet rs, int linha) throws SQLException {
        Atualizacao atualizacao = new Atualizacao();
        atualizacao.setId(rs.getLong("cd_atualizacao"));
        atualizacao.setOcorrenciaId(rs.getLong("cd_ocorrencia"));
        atualizacao.setObservacao(rs.getString("ds_observacao"));
        atualizacao.setStatus(StatusOcorrencia.valueOf(rs.getString("ds_status")));
        atualizacao.setData(rs.getTimestamp("dt_atualizacao").toLocalDateTime());
        return atualizacao;
    }
}
