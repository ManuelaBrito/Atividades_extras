package org.com.br.CRUD.model;

import java.util.Arrays;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private char[] senha;

    // Construtores
    public Usuario() {}

    public Usuario(String email, char[] senha) {
        this.email = email;
        this.senha = senha != null ? senha.clone() : null;
    }

    public Usuario(String id, String nome, String email, char[] senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha != null ? senha.clone() : null;
    }

    public Usuario(String nome, String email, char[] senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha != null ? senha.clone() : null;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getSenha() {
        return senha != null ? senha.clone() : null;
    }

    public void setSenha(char[] senha) {
        // Limpa a senha anterior da memória
        if (this.senha != null) {
            Arrays.fill(this.senha, ' ');
        }
        this.senha = senha != null ? senha.clone() : null;
    }

    // Método para limpar senha da memória
    public void limparSenha() {
        if (this.senha != null) {
            Arrays.fill(this.senha, ' ');
            this.senha = null;
        }
    }

    // Métodos úteis
    public boolean isValid() {
        return email != null && !email.trim().isEmpty() &&
                senha != null && senha.length > 0;
    }

    public boolean hasValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha=[PROTEGIDO]" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return email.equals(usuario.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    // Destrutor para limpeza de memória
    @Override
    protected void finalize() throws Throwable {
        limparSenha();
        super.finalize();
    }
}