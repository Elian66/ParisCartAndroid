package com.newcart.newpariscart.Model;

public class Restaurant {
    private String name;
    private String image;
    private String cat;
    private String address;
    private String restId;
    private String status;
    private String entrega;

    public Restaurant() {
    }

    public Restaurant(String name, String image, String address, String restId, String status) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.restId = restId;
        this.status = status;
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
