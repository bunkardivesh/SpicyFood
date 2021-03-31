package com.divesh.spicyfood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.divesh.spicyfood.Model.CartData;
import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.CartTotalAmount;
import com.divesh.spicyfood.Utility.OfflineDatabase;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.Utility.Utility;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemHolder> {

    private Context context;
    private List<CartData> foodList;
    private CartTotalAmount listener;
    private OfflineDatabase offlineDatabase;

    int count;

    public CartAdapter(Context context, List<CartData> foodList, CartTotalAmount listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
        offlineDatabase = new OfflineDatabase(context);
    }

    @NonNull
    @Override
    public CartAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ItemHolder holder, int position) {

        holder.foodName.setText(foodList.get(position).getItemName());
        holder.foodPrice.setText(foodList.get(position).getPrice());
        holder.qty.setText(foodList.get(position).getItemQty());

        byte[] bitmapdata = foodList.get(position).getPimage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        holder.foodImage.setImageBitmap(bitmap);

        String finalprice = String.valueOf(Double.valueOf(foodList.get(position).getPrice()) * Double.valueOf(foodList.get(position).getItemQty()));
        holder.finalPrice.setText(finalprice);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView foodImage;
        TextView foodName, foodPrice, qty, finalPrice;
        ImageButton btn_plus, btn_minus;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = (ImageView) itemView.findViewById(R.id.cart_image);
            foodName = (TextView) itemView.findViewById(R.id.cart_name);
            foodPrice = (TextView) itemView.findViewById(R.id.cart_price);
            qty = (TextView) itemView.findViewById(R.id.cart_qty);
            btn_plus = (ImageButton) itemView.findViewById(R.id.cart_btn_add);
            btn_minus = (ImageButton) itemView.findViewById(R.id.cart_btn_minus);
            finalPrice = (TextView) itemView.findViewById(R.id.cart_final_price);

            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.cart_btn_add:
                    count = Integer.parseInt(qty.getText().toString().trim());

                    if (count <= 1) {

                        btn_minus.setEnabled(true);
                        btn_minus.setClickable(true);

                    }

                    if (count>=10){
                        Snackbar snackbar = Snackbar.make(v,"You can select upto 10 quantity only", BaseTransientBottomBar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(context.getResources().getColor(R.color.red));
                        snackbar.show();
                    }else {
                        count++;
                        qty.setText(String.valueOf(count));
                        String itemname = foodName.getText().toString();
                        String itemprice = foodPrice.getText().toString();
                        String mqty = qty.getText().toString();

                        finalPrice.setText(String.valueOf(Double.valueOf(itemprice)*Double.valueOf(mqty)));

                        boolean isupdate = offlineDatabase.UpdateData(itemname, mqty);
                        if (isupdate == true) {
                            listener.onClick();

                        } else {
                            //handle error upadation in value
                        }
                    }

                    break;

                case R.id.cart_btn_minus:
                    count = Integer.parseInt(qty.getText().toString().trim());

                    if (count <= 1) {

                        btn_minus.setEnabled(false);
                        btn_minus.setClickable(false);

                    } else {
                        count--;

                        qty.setText(String.valueOf(count));
                        String mItemname = foodName.getText().toString();
                        String nqty = qty.getText().toString();
                        String price = foodPrice.getText().toString();
                        finalPrice.setText(String.valueOf(Double.valueOf(price)*Double.valueOf(nqty)));


                        boolean isupdat = offlineDatabase.UpdateData(mItemname, nqty);
                        if (isupdat == true) {
                            listener.onClick();
                        }
                        break;
                    }
            }
        }

        public void updateData(List<CartData> mItems) {

            if (mItems != null) {
                foodList.clear();
                foodList.addAll(mItems);
                notifyDataSetChanged();
            }

        }

        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
    }
}
