package com.example.wareregy;

public class Utilizador {
    private int userId;
    private String userNome;
    private String userEmail;
    private int primeiroLogin;
    private int userExp;

    public Utilizador(int userId, String userNome, String userEmail, int primeiroLogin, int userExp) {
        this.userId = userId;
        this.userNome = userNome;
        this.userEmail = userEmail;
        this.primeiroLogin = primeiroLogin;
        this.userExp = userExp;
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

    public void setUserExp(int userExp) {
        this.userExp = userExp;
    }

    public void setExp(int userExp) {
        this.userExp = userExp;
    }

    public int getPrimeiroLogin() {
        return primeiroLogin;
    }

    public void setPrimeiroLogin(int primeiroLogin) {
        this.primeiroLogin = primeiroLogin;
    }
}
