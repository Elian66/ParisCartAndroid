package com.newcart.newpariscart.Model;

public class User {
    private String cpf;
    private String saldo;
    private String pedidos;
    private String name;
    private String image;
    private String phoneNumber;
    private String address;
    private String senha;

    public User() {
    }

    public User(String name, String image, String phoneNumber, String address, String senha) {
        this.name = name;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.senha = senha;
        this.cpf = "";
        this.saldo = "0,00";
        this.pedidos = "0";
    }

    public String getPedidos() {
        return pedidos;
    }

    public void setPedidos(String pedidos) {
        this.pedidos = pedidos;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
