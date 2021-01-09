package com.example.wareregy;

import java.util.Date;

public class Registo {
        private int produtoId;
        private String produtoNome;
        private double produtoPeso;
        private String registoHora;

        public Registo(int produtoId, String produtoNome, double produtoPeso, String registoHora) {
            this.produtoId = produtoId;
            this.produtoNome = produtoNome;
            this.produtoPeso = produtoPeso;
            this.registoHora = registoHora;
        }

        public int getProdutoId() {
            return produtoId;
        }

        public String getProdutoNome() {
            return produtoNome;
        }

        public double getProdutoPeso() {
            return produtoPeso;
        }

        public String getRegistoHora() {
            return registoHora;
        }

    }
