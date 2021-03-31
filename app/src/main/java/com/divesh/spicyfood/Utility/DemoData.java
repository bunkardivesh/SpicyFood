package com.divesh.spicyfood.Utility;

import android.content.Context;

import com.divesh.spicyfood.Model.FoodModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
//takes data from database and provide to the calling functions.
public class DemoData {

    public static List<FoodModel> getFoodData(Context context, String query){
        List<FoodModel> foodModelList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            JSONArray m_jArry = obj.getJSONArray(query);

            for (int i = 0; i < m_jArry.length(); i++) {

                FoodModel fooditem = new FoodModel();
                JSONObject jobject = m_jArry.getJSONObject(i);
                fooditem.setFoodName(jobject.getString("name"));
                fooditem.setImageUrl(jobject.getString("imageUrl"));
                fooditem.setFoodDesc(jobject.getString("note"));
                fooditem.setDeliveryInfo(jobject.getString("deliveryTime"));
                fooditem.setPrice(Double.valueOf(jobject.getString("price")));
                fooditem.setRating(Double.valueOf(jobject.getString("rating")));
                //Add your values in your `ArrayList` as below:

                foodModelList.add(fooditem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return foodModelList;

    }

    //getting data list from Json saved in assets folder
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("fooddata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
