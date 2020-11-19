package com.newcart.pariscartrestaurant.Model;

public class Motorista {
    private String numero;
    private String nome;
    private String placa;
    private String senha;
    private String localizacao;
    private String restaurantId;
    private String corridas;
    private String status;
    private String requestId;

    public Motorista() {
    }

    public Motorista(String numero, String nome, String placa, String senha, String restaurantId) {
        this.numero = numero;
        this.nome = nome;
        this.placa = placa;
        this.senha = senha;
        this.localizacao = "";
        this.restaurantId = restaurantId;
        this.status = "0";
        this.corridas = "0";
        this.requestId = "";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCorridas() {
        return corridas;
    }

    public void setCorridas(String corridas) {
        this.corridas = corridas;
    }
}
