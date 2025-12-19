package org.com.br.CRUD.DAO;

import org.com.br.CRUD.model.Cadastro;
import java.sql.*;
import java.security.MessageDigest;

public class CadastroDAO {
    private Connection conexao;

    public CadastroDAO() {
        conectar();
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/BancoToDoList";
            String user = "root";
            String password = "Matuzalem.09";
            conexao = DriverManager.getConnection(url, user, password);
            criarTabelaSeNaoExistir();
        } catch (Exception e) {
            System.out.println("❌ Erro na conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) UNIQUE NOT NULL," +
                "senha_hash VARCHAR(64) NOT NULL," +
                "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Tabela 'usuarios' verificada/criada");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao criar tabela: " + e.getMessage());
        }
    }

    public boolean cadastrarUsuario(Cadastro cadastro) {
        if (emailJaExiste(cadastro.getEmail())) {
            System.out.println("❌ Email já cadastrado: " + cadastro.getEmail());
            return false;
        }

        String sql = "INSERT INTO usuarios (nome, email, senha_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cadastro.getNome());
            stmt.setString(2, cadastro.getEmail());
            stmt.setString(3, hashSenha(cadastro.getSenha()));

            int rows = stmt.executeUpdate();
            System.out.println("✅ Usuário cadastrado: " + cadastro.getNome());
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("❌ Erro ao cadastrar: " + e.getMessage());
            return false;
        }
    }

    public boolean verificarLogin(String email, String senha) {
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

    public String obterNomeUsuario(String email) {
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

    private boolean emailJaExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            System.out.println("❌ Erro ao verificar email: " + e.getMessage());
            return false;
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
            throw new RuntimeException("Erro ao gerar hash da senha", e);
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