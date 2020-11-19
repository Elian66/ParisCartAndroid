package com.localplatze.parisrider.Model;

public class User {
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
