package com.divesh.spicyfood.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodModel implements Parcelable {

    private String foodName;
    private String imageUrl;
    private String foodDesc;
    private Double rating;
    private Double price;
    private String deliveryInfo;

    public FoodModel() {
    }

    public FoodModel(String foodName, String imageUrl, String foodDesc, Double rating, Double price,String deliveryInfo) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.foodDesc = foodDesc;
        this.rating = rating;
        this.price = price;
        this.deliveryInfo = deliveryInfo;
    }


    protected FoodModel(Parcel in) {
        foodName = in.readString();
        imageUrl = in.readString();
        foodDesc = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        deliveryInfo = in.readString();
    }

    public static final Creator<FoodModel> CREATOR = new Creator<FoodModel>() {
        @Override
        public FoodModel createFromParcel(Parcel in) {
            return new FoodModel(in);
        }

        @Override
        public FoodModel[] newArray(int size) {
            return new FoodModel[size];
        }
    };

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodName);
        dest.writeString(imageUrl);
        dest.writeString(foodDesc);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        dest.writeString(deliveryInfo);
    }
}
