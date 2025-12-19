package org.com.br.CRUD.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.com.br.CRUD.App;
import org.com.br.CRUD.DAO.UsuarioDAO;

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoSenha;
    @FXML private Hyperlink link_cadastro;
    @FXML private Button button_entrar;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
        System.out.println("LoginController inicializado");
        campoSenha.setOnAction(event -> fazerLogin());
    }

    @FXML
    public void fazerLogin() {
        String email = campoUsuario.getText();
        String senha = campoSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!");
            return;
        }

        try {
            boolean loginValido = usuarioDAO.verificarLogin(email, senha);

            if (loginValido) {
                // Obtém o nome do usuário do banco
                String nomeUsuario = usuarioDAO.obterNomeUsuario(email);

                // Abre a tela de tarefas
                App.abrirTelaTarefas(nomeUsuario);

            } else {
                mostrarAlerta("Erro", "Email ou senha incorretos!");
                campoSenha.clear();
            }

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaCadastro() {
        try {
            App.setRoot("telaCadastro");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limparCampos() {
        campoUsuario.clear();
        campoSenha.clear();
        campoUsuario.requestFocus();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public boolean camposValidos() {
        return !campoUsuario.getText().isEmpty() &&
                !campoSenha.getText().isEmpty();
    }
}