package com.localplatze.parisrider.Model;

public class Promo {
    private String image;
    private String text;

    public Promo() {
    }

    public Promo(String image, String text) {
        this.image = image;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
