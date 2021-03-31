package com.divesh.spicyfood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.OfflineDatabase;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.Utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FoodDetailAdapter extends RecyclerView.Adapter<FoodDetailAdapter.ItemHolder> {

    private Context context;
    private List<FoodModel> foodList;
    private OfflineDatabase offlineDatabase;

    public FoodDetailAdapter(Context context, List<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodDetailAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_detail,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDetailAdapter.ItemHolder holder, int position) {

        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodPrice.setText(String.valueOf(foodList.get(position).getPrice()));
        holder.foodRating.setRating(Float.valueOf(foodList.get(position).getRating().toString()));
        holder.foodDesc.setText(foodList.get(position).getFoodDesc());
      //  holder.foodDelInfo.setText(foodList.get(position).getFoodDesc());
        holder.btn_Addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemname = foodList.get(position).getFoodName();
        try{
            Bitmap src = Utility.getBitmapFromAssest(context,foodList.get(position).getImageUrl());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] pimage = baos.toByteArray();

        
            boolean result = Utility.saveToCart(context,v,itemname,String.valueOf(foodList.get(position).getPrice()),"1",pimage,
                    foodList.get(position).getFoodDesc());
        }catch (Exception e){
            Toast.makeText(context,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }


            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView foodName,foodPrice, foodDesc, foodDelInfo;
        RatingBar foodRating;
        AppCompatButton btn_Addtocart;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            foodName = (TextView) itemView.findViewById(R.id.foodname_rv);
            foodPrice = (TextView) itemView.findViewById(R.id.food_price_rv);
            foodRating = (RatingBar) itemView.findViewById(R.id.food_rating_rv);
            foodDesc = (TextView) itemView.findViewById(R.id.food_desc);
            foodDelInfo = (TextView) itemView.findViewById(R.id.food_del_info);
            btn_Addtocart = (AppCompatButton) itemView.findViewById(R.id.detail_add_to_cart);


        }
    }
}
