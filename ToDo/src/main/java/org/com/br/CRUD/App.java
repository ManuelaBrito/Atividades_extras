package org.com.br.CRUD;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {
    private static Scene scene;
    private static Stage primaryStage;
    private static String usuarioLogadoNome;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        carregarTelaLogin();
    }

    private void carregarTelaLogin() {
        try {
            String fxmlPath = "/org/com/br/CRUD/view/telalogin.fxml";
            System.out.println("📂 Procurando: " + fxmlPath);

            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                System.err.println("❌ ERRO: Arquivo telalogin.fxml não encontrado!");
                return;
            }

            System.out.println("✅ Arquivo encontrado: " + fxmlUrl);
            Parent root = FXMLLoader.load(fxmlUrl);
            scene = new Scene(root, 640, 480);
            primaryStage.setTitle("ToDo App - Login");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar tela de login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        String fxmlPath = "/org/com/br/CRUD/view/" + fxml + ".fxml";
        System.out.println("🔁 Trocando para tela: " + fxmlPath);

        URL fxmlUrl = App.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("❌ ERRO: Arquivo " + fxml + ".fxml não encontrado!");
            throw new IOException("Arquivo FXML não encontrado: " + fxmlPath);
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        scene.setRoot(root);
    }

    public static void abrirTelaCadastro() {
        try {
            System.out.println("📝 Abrindo tela de cadastro...");
            setRoot("telaCadastro");
            if (primaryStage != null) {
                primaryStage.setTitle("ToDo App - Cadastro");
            }
        } catch (Exception e) {
            System.err.println("Erro ao abrir cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // MÉTODO QUE ESTAVA FALTANDO - ADICIONE ESTE!
    public static void abrirTelaTarefas(String nomeUsuario) {
        try {
            System.out.println("📋 Abrindo tela de tarefas para: " + nomeUsuario);
            usuarioLogadoNome = nomeUsuario;
            setRoot("telatarefas");
            if (primaryStage != null) {
                primaryStage.setTitle("ToDo App - Minhas Tarefas");
                primaryStage.setWidth(800);
                primaryStage.setHeight(600);
            }
        } catch (Exception e) {
            System.err.println("Erro ao abrir tela de tarefas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void voltarParaLogin() {
        try {
            System.out.println("🔙 Voltando para tela de login...");
            setRoot("telalogin");
            if (primaryStage != null) {
                primaryStage.setTitle("ToDo App - Login");
                primaryStage.setWidth(640);
                primaryStage.setHeight(480);
            }
            usuarioLogadoNome = null;
        } catch (Exception e) {
            System.err.println("Erro ao voltar para login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getUsuarioLogadoNome() {
        return usuarioLogadoNome;
    }

    public static void setUsuarioLogadoNome(String nome) {
        usuarioLogadoNome = nome;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando aplicação ToDo App...");
        launch(args);
    }
}