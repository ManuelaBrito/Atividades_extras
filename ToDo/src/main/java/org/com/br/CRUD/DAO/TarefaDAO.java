package org.com.br.CRUD.DAO;

import org.com.br.CRUD.model.Tarefa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private Connection conexao;

    public TarefaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // CREATE
    public void inserir(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO Tarefas (tarefa, descricao, data_final, concluida) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tarefa.getTarefa());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getDataFinal());
            stmt.setBoolean(4, tarefa.isConcluida());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tarefa.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ todas
    public List<Tarefa> listarTodas() throws SQLException {
        List<Tarefa> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tarefas ORDER BY data_criacao DESC";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tarefa t = new Tarefa();
                t.setId(rs.getInt("id"));
                t.setTarefa(rs.getString("tarefa"));
                t.setDescricao(rs.getString("descricao"));
                t.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                t.setDataFinal(rs.getString("data_final"));
                t.setConcluida(rs.getBoolean("concluida"));
                lista.add(t);
            }
        }
        return lista;
    }

    // UPDATE
    public void atualizar(Tarefa tarefa) throws SQLException {
        String sql = "UPDATE Tarefas SET tarefa=?, descricao=?, data_final=?, concluida=? WHERE id=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tarefa.getTarefa());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getDataFinal());
            stmt.setBoolean(4, tarefa.isConcluida());
            stmt.setInt(5, tarefa.getId());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM Tarefas WHERE id=?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}