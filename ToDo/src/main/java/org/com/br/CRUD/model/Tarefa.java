package org.com.br.CRUD.model;

import java.time.LocalDateTime;

public class Tarefa {
    private int id;
    private String tarefa;
    private String descricao;
    private LocalDateTime dataCriacao;
    private String dataFinal;
    private boolean concluida;

    public Tarefa() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTarefa() { return tarefa; }
    public void setTarefa(String tarefa) { this.tarefa = tarefa; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDataFinal() { return dataFinal; }
    public void setDataFinal(String dataFinal) { this.dataFinal = dataFinal; }

    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}