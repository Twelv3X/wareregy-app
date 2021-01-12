package com.example.wareregy;

public class Utilizador {
    private int userId;
    private String userNome;
    private String userEmail;
    private int userExp;

    public Utilizador(int userId, String userNome, String userEmail, int userExp) {
        this.userId = userId;
        this.userNome = userNome;
        this.userEmail = userEmail;
        this.userExp = userExp;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "userId=" + userId +
                ", userNome='" + userNome + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userExp=" + userExp +
                '}';
    }

    public int getId() {
        return userId;
    }

    public String getNome() {
        return userNome;
    }

    public String getEmail() {
        return userEmail;
    }

    public int getExp() {
        return userExp;
    }

    public void setExp(int userExp) {
        this.userExp = userExp;
    }
}
