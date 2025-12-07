package org.com.br.CRUD.DAO;

import java.sql.*;
import java.security.MessageDigest;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO() {
        conectar();
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/BancoToDoList";
            String user = "root";
            String password = "Matuzalem.09";
            conexao = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexão MySQL estabelecida!");
        } catch (Exception e) {
            System.out.println("❌ Erro na conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean verificarLogin(String email, String senha) {
        if (conexao == null) {
            conectar();
            if (conexao == null) return false;
        }

        String sql = "SELECT senha_hash FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String senhaHashArmazenada = rs.getString("senha_hash");
                String senhaHashDigitada = hashSenha(senha);
                return senhaHashArmazenada.equals(senhaHashDigitada);
            }
            return false;

        } catch (SQLException e) {
            System.out.println("❌ Erro no login: " + e.getMessage());
            return false;
        }
    }

    public boolean cadastrarUsuario(String nome, String email, String senha) {
        if (conexao == null) {
            conectar();
            if (conexao == null) return false;
        }

        String sql = "INSERT INTO usuarios (nome, email, senha_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, hashSenha(senha));

            int rows = stmt.executeUpdate();
            System.out.println("✅ Usuário cadastrado: " + nome);
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erro ao cadastrar: " + e.getMessage());
            return false;
        }
    }

    public String obterNomeUsuario(String email) {
        if (conexao == null) {
            conectar();
            if (conexao == null) return "Usuário";
        }

        String sql = "SELECT nome FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            }
            return "Usuário";

        } catch (SQLException e) {
            System.out.println("❌ Erro ao obter nome: " + e.getMessage());
            return "Usuário";
        }
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("✅ Conexão fechada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}