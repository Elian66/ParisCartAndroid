package com.newcart.newpariscart.Model;

public class Food {
    private String categoryId;
    private String image;//
    private String price;//
    private String name;//
    private String restId;
    private String ingredientes;

    public Food(String categoryId, String image, String price, String name, String restId) {
        this.categoryId = categoryId;
        this.image = image;
        this.price = price;
        this.name = name;
        this.restId = restId;
    }

    public Food() {
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }
}
