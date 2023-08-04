package com.example.testcoffee;

public class Cart {
    String product_name;
    Float product_price;
    int product_qty;

    public Cart() {
    }

    public Cart(String product_name, Float product_price, int product_qty) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_qty = product_qty;
    }
    //Use when adding different items into cart
    public void add(Product otherItem)
    {
        this.product_qty = this.product_qty + otherItem.product_qty;
    }

    //Use when removing certain qty of items
    public void subtract(Product otherItem)
    {
        this.product_qty = this.product_qty - otherItem.product_qty;
    }

    //Use for quick item search
    public int hashCode()
    {
        int price = Math.round(product_price); //Convert price to an int
        return product_name.hashCode() + price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_name='" + product_name + '\'' +
                ", product_price=" + product_price +
                ", product_qty=" + product_qty +
                '}';
    }

}
