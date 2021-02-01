package com.example.wareregy;

import org.json.JSONException;
import org.json.JSONObject;

public class Registo {
    private int userId;
    private int produtoId;
    private String produtoNome;
    private double produtoPeso;
    private String registoData;
    private int registoHora;

    public Registo(int userId, int produtoId, String produtoNome, double produtoPeso, String registoData, int registoHora) {
        this.userId = userId;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoPeso = produtoPeso;
        this.registoData = registoData;
        this.registoHora = registoHora;
    }

    public Registo() {

    }

    public int getUserId() {
        return userId;
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

    public String getRegistoData() {
        return registoData;
    }

    public int getRegistoHora() {
        return registoHora;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public void setProdutoPeso(double produtoPeso) {
        this.produtoPeso = produtoPeso;
    }

    public void setRegistoData(String registoData) {
        this.registoData = registoData;
    }

    public void setRegistoHora(int registoHora) {
        this.registoHora = registoHora;
    }

    @Override
    public String toString() {
        return "Registo{" +
                "userId=" + userId +
                ", produtoId=" + produtoId +
                ", produtoNome='" + produtoNome + '\'' +
                ", produtoPeso=" + produtoPeso +
                ", registoData='" + registoData + '\'' +
                ", registoHora=" + registoHora +
                '}';
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", getUserId());
            jsonObject.put("produto_id", getProdutoId());
            jsonObject.put("registo_data", getRegistoData());
            jsonObject.put("registo_hora", getRegistoHora());
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
