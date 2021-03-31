package com.divesh.spicyfood.Utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.divesh.spicyfood.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utility {

    public static boolean saveToCart(Context context, View v,
                   String itemName, String price,String qty,
                   byte[] pimage, String desc)
    {
        OfflineDatabase offlineDatabase;

        boolean result = false;

        offlineDatabase = new OfflineDatabase(context);


        if (offlineDatabase.CheckIfNameExist(itemName)){
            Snackbar.make(v, "Item already exists in your cart!", Snackbar.LENGTH_SHORT).show();
        }else {

            boolean response = offlineDatabase.addtoCart(itemName,price,qty,pimage
                    ,desc);

            if (response){
                Snackbar snackbar = Snackbar.make(v, "Item Added To Cart", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                snackbar.show();
                result = true;

            }else {
                Toast.makeText(context, "Error: Try Again", Toast.LENGTH_SHORT).show();
            }

        }
            return result;
    }

    public static Bitmap getBitmapFromAssest(Context context,String filepath){

        AssetManager assetManager = context.getAssets();
        InputStream inputStream;
        String fileName = filepath.substring(22);
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (IOException e){

        }
        return bitmap;
    }
}
