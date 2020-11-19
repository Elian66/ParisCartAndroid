package com.localplatze.parisrider.Model;

public class Restaurant {
    private String name;
    private String image;
    private String cat;
    private String address;
    private String restId;
    private String status;
    private String entrega;
    private String senha;
    private String numReviews;
    private String saldoReais;
    private String saldoBitcoin;
    private String totalPedidos;
    private String ultimoPedido;
    private String aceitaBitcoin;

    public Restaurant() {
    }

    public Restaurant(String name,String image, String address, String restId, String status, String entrega, String senha) {
        this.name = name;
        this.image = image;
        this.cat = cat;
        this.address = address;
        this.restId = restId;
        this.status = status;
        this.entrega = entrega;
        this.senha = senha;
        this.numReviews = numReviews;
        this.saldoReais = saldoReais;
        this.saldoBitcoin = saldoBitcoin;
        this.totalPedidos = totalPedidos;
        this.ultimoPedido = ultimoPedido;
        this.aceitaBitcoin = aceitaBitcoin;
    }

    public String getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(String numReviews) {
        this.numReviews = numReviews;
    }

    public String getSaldoReais() {
        return saldoReais;
    }

    public void setSaldoReais(String saldoReais) {
        this.saldoReais = saldoReais;
    }

    public String getSaldoBitcoin() {
        return saldoBitcoin;
    }

    public void setSaldoBitcoin(String saldoBitcoin) {
        this.saldoBitcoin = saldoBitcoin;
    }

    public String getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(String totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public String getUltimoPedido() {
        return ultimoPedido;
    }

    public void setUltimoPedido(String ultimoPedido) {
        this.ultimoPedido = ultimoPedido;
    }

    public String getAceitaBitcoin() {
        return aceitaBitcoin;
    }

    public void setAceitaBitcoin(String aceitaBitcoin) {
        this.aceitaBitcoin = aceitaBitcoin;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
