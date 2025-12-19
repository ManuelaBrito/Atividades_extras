package org.com.br.CRUD.DAO;

import org.com.br.CRUD.conf.ConexaoBD;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class UsuarioDAO {

    private static Connection getConexao() {
        try {
            InputStream input = UsuarioDAO.class.getClassLoader()
                    .getResourceAsStream("database.properties");
            Properties props = new Properties();
            props.load(input);

            return DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.pass")
            );
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static boolean verificarLogin(String email, String senha) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true se encontrou usuário

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cadastrarUsuario(String nome, String email, String senha) {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String buscarNomeUsuario(String email) {
        String sql = "SELECT nome FROM usuarios WHERE email = ?";

        try (Connection conn = getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fecharConexao() {
    }

    public String obterNomeUsuario(String email) {
        String sql = "SELECT nome FROM usuarios WHERE email = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }

        return null; // Retorna null se não encontrar
    }}