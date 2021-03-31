package com.divesh.spicyfood.Model;

public class CartData {
    String itemName;
    String price;
    String itemQty;
    byte[] pimage;
    String desc;

    public CartData(String itemName, String price, String itemQty, byte[] pimage, String desc) {
        this.itemName = itemName;
        this.price = price;
        this.itemQty = itemQty;
        this.pimage = pimage;
        this.desc = desc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public byte[] getPimage() {
        return pimage;
    }

    public void setPimage(byte[] pimage) {
        this.pimage = pimage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
