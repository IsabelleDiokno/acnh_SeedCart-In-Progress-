package com.example.testcoffee;

public class Product {


    String product_name;
    Float product_price;
    int product_qty;

    public Product() {

    }

    public Product(String product_name, Float product_price, int product_qty) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_qty = product_qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }
}
