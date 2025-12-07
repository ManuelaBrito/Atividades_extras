package org.com.br.CRUD.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.com.br.CRUD.App;
import org.com.br.CRUD.model.Tarefa;
import org.com.br.CRUD.DAO.TarefaDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TelaTarefasController {

    // Componentes FXML
    @FXML private TextField txt_novatarefa;
    @FXML private DatePicker txt_calendario;
    @FXML private Button btn_logout;
    @FXML private Label labelUsuario;
    @FXML private Button btn_adicionar;

    // TableView
    @FXML private TableView<Tarefa> tabelaTarefas;
    @FXML private TableColumn<Tarefa, Boolean> colConcluida;
    @FXML private TableColumn<Tarefa, String> colTarefa;
    @FXML private TableColumn<Tarefa, String> colDataFinal;
    @FXML private TableColumn<Tarefa, Void> colAcoes;

    // Conexão e DAO
    private Connection conexao;
    private TarefaDAO tarefaDAO;
    private ObservableList<Tarefa> listaTarefas;

    @FXML
    public void initialize() {
        System.out.println("✅ TelaTarefasController inicializado");

        // Label de boas-vindas
        String nome = App.getUsuarioLogadoNome();
        if (labelUsuario != null) {
            labelUsuario.setText("👋 Bem-vindo, " + nome + "!");
        }

        // Botão logout
        if (btn_logout != null) {
            btn_logout.setOnAction(event -> fazerLogout());
        }

        // Configurar TableView
        configurarTableView();

        // Carregar tarefas
        carregarTarefas();

        // Botão Adicionar
        if (btn_adicionar != null) {
            btn_adicionar.setOnAction(e -> adicionarTarefa());
        }
    }

    private void configurarTableView() {
        // Configurar bindings
        colConcluida.setCellValueFactory(new PropertyValueFactory<>("concluida"));
        colTarefa.setCellValueFactory(new PropertyValueFactory<>("tarefa"));
        colDataFinal.setCellValueFactory(new PropertyValueFactory<>("dataFinal"));

        // CheckBox com clique funcional
        colConcluida.setCellFactory(col -> {
            CheckBoxTableCell<Tarefa, Boolean> cell = new CheckBoxTableCell<Tarefa, Boolean>() {
                @Override
                public void updateItem(Boolean checked, boolean empty) {
                    super.updateItem(checked, empty);
                    if (empty || getTableRow() == null) {
                        setGraphic(null);
                    } else {
                        Tarefa tarefa = getTableRow().getItem();
                        if (tarefa != null) {
                            CheckBox checkBox = (CheckBox) getGraphic();
                            if (checkBox != null) {
                                checkBox.setSelected(tarefa.isConcluida());
                            }
                        }
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                Tarefa tarefa = cell.getTableRow().getItem();
                if (tarefa != null) {
                    boolean novoStatus = !tarefa.isConcluida();
                    tarefa.setConcluida(novoStatus);

                    try {
                        if (tarefaDAO == null) conectarBanco();
                        tarefaDAO.atualizar(tarefa);
                        System.out.println("✅ Status: " + tarefa.getTarefa() + " = " + novoStatus);

                        // Atualiza visualmente
                        cell.updateItem(novoStatus, false);
                        tabelaTarefas.refresh();
                    } catch (SQLException e) {
                        mostrarErro("Erro ao salvar: " + e.getMessage());
                        tarefa.setConcluida(!novoStatus);
                    }
                }
            });

            return cell;
        });

        // Coluna de ações
        colAcoes.setCellFactory(col -> new TableCell<Tarefa, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");

            {
                btnEditar.setOnAction(e -> {
                    Tarefa tarefa = getTableView().getItems().get(getIndex());
                    editarTarefa(tarefa);
                });

                btnExcluir.setOnAction(e -> {
                    Tarefa tarefa = getTableView().getItems().get(getIndex());
                    excluirTarefa(tarefa);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox botoes = new HBox(5, btnEditar, btnExcluir);
                    setGraphic(botoes);
                }
            }
        });

        // Lista observável
        listaTarefas = FXCollections.observableArrayList();
        tabelaTarefas.setItems(listaTarefas);
    }

    private void conectarBanco() {
        try {
            String url = "jdbc:mysql://localhost:3306/bancotarefa";
            String user = "root";
            String password = "Matuzalem.09";
            conexao = DriverManager.getConnection(url, user, password);
            tarefaDAO = new TarefaDAO(conexao);
            System.out.println("✅ Conectado ao banco");
        } catch (SQLException e) {
            mostrarErro("Erro ao conectar ao banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarTarefas() {
        try {
            conectarBanco();
            List<Tarefa> tarefas = tarefaDAO.listarTodas();
            System.out.println("📋 Tarefas carregadas: " + tarefas.size());
            for (Tarefa t : tarefas) {
                System.out.println(" - " + t.getTarefa() + " | " + t.isConcluida());
            }
            listaTarefas.setAll(tarefas);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar tarefas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void adicionarTarefa() {
        String texto = txt_novatarefa.getText().trim();
        if (texto.isEmpty()) {
            mostrarErro("Digite uma tarefa!");
            return;
        }

        Tarefa nova = new Tarefa();
        nova.setTarefa(texto);
        nova.setDescricao("");
        nova.setDataFinal(txt_calendario.getValue() != null ?
                txt_calendario.getValue().toString() : "");
        nova.setConcluida(false);

        try {
            tarefaDAO.inserir(nova);
            carregarTarefas();
            txt_novatarefa.clear();
            txt_calendario.setValue(null);
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarTarefa(Tarefa tarefa) {
        TextInputDialog dialog = new TextInputDialog(tarefa.getTarefa());
        dialog.setTitle("Editar Tarefa");
        dialog.setHeaderText("Editar texto da tarefa");
        dialog.setContentText("Novo texto:");

        dialog.showAndWait().ifPresent(novoTexto -> {
            tarefa.setTarefa(novoTexto);
            try {
                tarefaDAO.atualizar(tarefa);
                carregarTarefas();
            } catch (SQLException e) {
                mostrarErro("Erro ao atualizar: " + e.getMessage());
            }
        });
    }

    private void excluirTarefa(Tarefa tarefa) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Excluir tarefa: " + tarefa.getTarefa());
        confirmacao.setContentText("Tem certeza?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    tarefaDAO.excluir(tarefa.getId());
                    carregarTarefas();
                } catch (SQLException e) {
                    mostrarErro("Erro ao excluir: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void fazerLogout() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Logout");
        confirmacao.setHeaderText("Sair do Sistema");
        confirmacao.setContentText("Tem certeza?");

        confirmacao.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                App.voltarParaLogin();
            }
        });
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}