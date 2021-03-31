package com.divesh.spicyfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.divesh.spicyfood.Model.CartData;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.CartTotalAmount;
import com.divesh.spicyfood.Utility.OfflineDatabase;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.adapter.CartAdapter;
import com.divesh.spicyfood.databinding.ActivityCartBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    OfflineDatabase offlineDatabase;
    List<CartData> cartDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        offlineDatabase = new OfflineDatabase(this);

        binding.cartBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();
        setTotalAmount();
    }

    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.rvCart.setLayoutManager(layoutManager);

        cartDataList = new ArrayList<>();
        cartDataList = offlineDatabase.getAllData();

        CartAdapter adapter = new CartAdapter(this, cartDataList, new CartTotalAmount() {
            @Override
            public void onClick() {
                setTotalAmount();
            }
        });

        binding.rvCart.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //  Toast.makeText(ListActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();


                String pr = cartDataList.get(position).getItemName();
                boolean deleterow = offlineDatabase.DeleteData(pr);



                if (deleterow) {
                    cartDataList.remove(position);
                    adapter.notifyDataSetChanged();
                    setTotalAmount();

                    Snackbar snackbar = Snackbar.make(binding.cartView, pr+" Removed from your Cart!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvCart);
    }
    private void setTotalAmount() {
    double amount = offlineDatabase.getTotalAmount();
    binding.btnCheckout.setText("Checkout("+amount+")");
    if (amount == 0){
        binding.btnCheckout.setEnabled(false);
    }else {
        binding.btnCheckout.setEnabled(true);
    }
    }
}