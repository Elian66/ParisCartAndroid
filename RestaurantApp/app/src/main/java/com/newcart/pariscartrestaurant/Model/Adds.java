package com.newcart.pariscartrestaurant.Model;

public class Adds {
    private String name;
    private String price;
    private boolean checked;

    public Adds() {
    }

    public Adds(String name, String price, boolean checked) {
        this.name = name;
        this.price = price;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
