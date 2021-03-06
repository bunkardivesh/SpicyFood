package com.divesh.spicyfood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.Utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class TrendingFoodAdapter extends RecyclerView.Adapter<TrendingFoodAdapter.ItemHolder> {

    private Context context;
    private List<FoodModel> foodList;
    private RecyclerViewClickListener listener;

    public TrendingFoodAdapter(Context context, List<FoodModel> foodList, RecyclerViewClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrendingFoodAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout1,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingFoodAdapter.ItemHolder holder, int position) {

        holder.foodName.setText(foodList.get(position).getFoodName());
        holder.foodPrice.setText(String.valueOf(foodList.get(position).getPrice()));
        holder.foodRating.setRating(Float.parseFloat(foodList.get(position).getRating().toString()));

        String imgurl = foodList.get(position).getImageUrl();
        Picasso.get().load(imgurl).into(holder.foodImage);

        holder.itemView.setOnClickListener(v -> listener.onClick(position,holder.foodImage));

        holder.addToCart.setOnClickListener(v -> {

            String itemname = foodList.get(position).getFoodName();

            Bitmap src = ((BitmapDrawable) holder.foodImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] pimage = baos.toByteArray();


            boolean result = Utility.saveToCart(context,v,itemname,String.valueOf(foodList.get(position).getPrice()),"1",pimage,
                    foodList.get(position).getFoodDesc());

            /*if (offlineDatabase.CheckIfNameExist(itemname)){
                Snackbar.make(v, "Item already exists in your cart!", Snackbar.LENGTH_SHORT).show();
            }else {




                    boolean response = offlineDatabase.addtoCart(itemname,String.valueOf(foodList.get(position).getPrice()),"1",pimage
                            ,foodList.get(position).getFoodDesc());

                    if (response){
                        Snackbar snackbar = Snackbar.make(v, "Item Added To Cart", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.green));
                        snackbar.show();

                    }else {
                        Toast.makeText(context, "Error: Try Again", Toast.LENGTH_SHORT).show();
                    }

            }*/
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName,foodPrice;
        RatingBar foodRating;
        ImageButton addToCart;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.cart_image);
            foodName = itemView.findViewById(R.id.foodname_rv);
            foodPrice = itemView.findViewById(R.id.food_price_rv);
            foodRating = itemView.findViewById(R.id.food_rating_rv);
            addToCart = itemView.findViewById(R.id.add_to_car_rv);


        }
    }

    public void updateData(List<FoodModel> mItems){

        if (mItems!=null){
            foodList.clear();
            foodList.addAll(mItems);
            notifyDataSetChanged();
        }

    }
}
