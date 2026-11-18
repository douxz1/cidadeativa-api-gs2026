package br.com.fiap.cidadeativa.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.com.fiap.cidadeativa.model.Ocorrencia;
import br.com.fiap.cidadeativa.model.StatusOcorrencia;

@Repository
public class OcorrenciaDAO {
    private static final String SELECT = "SELECT cd_ocorrencia, cd_cidadao, cd_bairro, cd_categoria, ds_ocorrencia, ds_endereco, nr_prioridade, ds_status, dt_ocorrencia FROM tb_ocorrencia";
    private final JdbcTemplate jdbc;

    public OcorrenciaDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public long cadastrar(Ocorrencia modelo) {
        long id = jdbc.queryForObject("SELECT sq_tb_ocorrencia.NEXTVAL FROM dual", Long.class);
        String sql = "INSERT INTO tb_ocorrencia (cd_ocorrencia, cd_cidadao, cd_bairro, cd_categoria, ds_ocorrencia, ds_endereco, nr_prioridade) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, id, modelo.getCidadaoId(), modelo.getBairroId(), modelo.getCategoriaId(), modelo.getDescricao(), modelo.getEndereco(), modelo.getPrioridade());
        return id;
    }

    public List<Ocorrencia> listar() {
        return jdbc.query(SELECT + " ORDER BY cd_ocorrencia", this::mapear, new Object[0]);
    }

    public Optional<Ocorrencia> buscar(long id) {
        return jdbc.query(SELECT + " WHERE cd_ocorrencia = ?", this::mapear, id).stream().findFirst();
    }

    public boolean atualizar(long id, Ocorrencia modelo) {
        String sql = "UPDATE tb_ocorrencia SET cd_cidadao = ?, cd_bairro = ?, cd_categoria = ?, ds_ocorrencia = ?, ds_endereco = ?, nr_prioridade = ? WHERE cd_ocorrencia = ?";
        return jdbc.update(sql, modelo.getCidadaoId(), modelo.getBairroId(), modelo.getCategoriaId(), modelo.getDescricao(), modelo.getEndereco(), modelo.getPrioridade(), id) > 0;
    }

    public boolean excluir(long id) {
        return jdbc.update("DELETE FROM tb_ocorrencia WHERE cd_ocorrencia = ?", id) > 0;
    }

    public boolean bloquear(long id) {
        String sql = "SELECT cd_ocorrencia FROM tb_ocorrencia WHERE cd_ocorrencia = ? FOR UPDATE";
        return !jdbc.query(sql, (rs, linha) -> rs.getLong(1), id).isEmpty();
    }

    public void atualizarStatus(long id, StatusOcorrencia status) {
        jdbc.update("UPDATE tb_ocorrencia SET ds_status = ? WHERE cd_ocorrencia = ?", status.name(), id);
    }

    private Ocorrencia mapear(ResultSet rs, int linha) throws SQLException {
        Ocorrencia modelo = new Ocorrencia();
        modelo.setId(rs.getLong("cd_ocorrencia"));
        modelo.setCidadaoId(rs.getLong("cd_cidadao"));
        modelo.setBairroId(rs.getLong("cd_bairro"));
        modelo.setCategoriaId(rs.getLong("cd_categoria"));
        modelo.setDescricao(rs.getString("ds_ocorrencia"));
        modelo.setEndereco(rs.getString("ds_endereco"));
        modelo.setPrioridade(rs.getInt("nr_prioridade"));
        modelo.setStatus(StatusOcorrencia.valueOf(rs.getString("ds_status")));
        modelo.setData(rs.getTimestamp("dt_ocorrencia").toLocalDateTime());
        return modelo;
    }
}
