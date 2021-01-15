package com.example.wareregy;

public class Nivel {
    private int minXp;
    private int maxXp;
    private int nivel;

    public Nivel(int nivel, int minXp, int maxXp) {
        this.nivel = nivel;
        this.minXp = minXp;
        this.maxXp = maxXp;

    }

    public int getMinXp() {
        return minXp;
    }

    public int getMaxXp() {
        return maxXp;
    }

    public int getNivel() {
        return nivel;
    }

    public void setMinXp(int minXp) {
        this.minXp = minXp;
    }

    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Nivel{" +
                "minXp=" + minXp +
                ", maxXp=" + maxXp +
                ", nivel=" + nivel +
                '}';
    }
}
