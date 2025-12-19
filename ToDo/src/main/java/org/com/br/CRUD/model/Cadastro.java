package org.com.br.CRUD.model;

import java.util.Arrays;

public class Cadastro {
    private Integer id;
    private String nome;
    private String email;
    private char[] senha;

    public Cadastro() {
    }

    public Cadastro(String nome, String email, char[] senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha != null ? senha.clone() : null;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() {
        return senha != null ? Arrays.toString(senha.clone()) : null;
    }

    public void setSenha(char[] senha) {
        // Limpa senha anterior
        if (this.senha != null) {
            Arrays.fill(this.senha, ' ');
        }
        this.senha = senha != null ? senha.clone() : null;
    }

    // Limpar senha da memória
    public void limparSenha() {
        if (this.senha != null) {
            Arrays.fill(this.senha, ' ');
            this.senha = null;
        }
    }

    @Override
    public String toString() {
        return "Cadastro{nome='" + nome + "', email='" + email + "'}";
    }
}