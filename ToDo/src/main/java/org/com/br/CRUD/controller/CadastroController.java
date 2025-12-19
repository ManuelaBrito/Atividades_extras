package org.com.br.CRUD.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.com.br.CRUD.App;
import org.com.br.CRUD.DAO.UsuarioDAO;

public class CadastroController {

    @FXML private TextField txt_nome;
    @FXML private TextField txt_email;
    @FXML private PasswordField txt_senha;
    @FXML private PasswordField txt_conf_senha;
    @FXML private Button button_cadastrar;
    @FXML private Button linkLogin;

    @FXML
    private void cadastrarUsuario() {
        String nome = txt_nome.getText();
        String email = txt_email.getText();
        String senha = txt_senha.getText();
        String confirmar = txt_conf_senha.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Atenção", "Preencha todos os campos!");
            return;
        }

        if (!senha.equals(confirmar)) {
            mostrarAlerta("Atenção", "As senhas não coincidem!");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        boolean sucesso = dao.cadastrarUsuario(nome, email, senha);
        dao.fecharConexao();

        if (sucesso) {
            mostrarConfirmacao();
            limparCampos();
        } else {
            mostrarAlerta("Erro", "Não foi possível cadastrar. Email já existe?");
        }
    }

    @FXML
    private void voltarParaLogin() {
        try {
            App.setRoot("telalogin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarConfirmacao() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cadastro realizado!");
        alert.setHeaderText(null);
        alert.setContentText("Usuário cadastrado com sucesso!");
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void limparCampos() {
        txt_nome.clear();
        txt_email.clear();
        txt_senha.clear();
        txt_conf_senha.clear();
        txt_nome.requestFocus();
    }
}