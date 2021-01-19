package com.example.wareregy;

public class Utilizador extends Nivel {
    private int userId;
    private String userNome;
    private String userEmail;
    private int primeiroLogin;
    private int userExp;
    private int nrReg;

    public Utilizador(int userId, String userNome, String userEmail, int primeiroLogin, int userExp, int nivel, int minXp, int maxXp, int nrReg) {
        super(nivel, minXp, maxXp);
        this.userId = userId;
        this.userNome = userNome;
        this.userEmail = userEmail;
        this.primeiroLogin = primeiroLogin;
        this.userExp = userExp;
        this.nrReg = nrReg;
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

    public int getNrReg() {

        return nrReg;
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

    public void setNrReg(int nrReg) {
        this.nrReg = nrReg;
    }

    @Override
    public String toString() {
        return "Utilizador{" +
                "userId=" + userId +
                ", userNome='" + userNome + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", primeiroLogin=" + primeiroLogin +
                ", userExp=" + userExp +
                '}';
    }
}
