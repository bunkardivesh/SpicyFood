package com.divesh.spicyfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.ZoomCardLayoutManager;
import com.divesh.spicyfood.adapter.FoodDetailAdapter;
import com.divesh.spicyfood.databinding.ActivityFoodDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {

    private ActivityFoodDetailBinding binding;
    private List<FoodModel> foodlist;
    private int position;
    private RecyclerView foodDetailRV;

    //------
    private int mScreenWidth = 0;
    private int mHeaderItemWidth = 0;
    private int mCellWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        foodlist = new ArrayList<>();
        foodlist = getIntent().getParcelableArrayListExtra("list");
        position = getIntent().getIntExtra("position",0);

        if (foodlist != null){
            setUpData();
        }else {
            Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.detailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpData() {

        foodDetailRV = binding.detailRecyclerview;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
       // ZoomCardLayoutManager layoutManager = new ZoomCardLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        foodDetailRV.setLayoutManager(layoutManager);
        foodDetailRV.setNestedScrollingEnabled(false);

        FoodDetailAdapter adapter = new FoodDetailAdapter(this,foodlist);
        foodDetailRV.setAdapter(adapter);
        foodDetailRV.scrollToPosition(position);

/*
        //----------------
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.mScreenWidth = displaymetrics.widthPixels;

        //calculate value on current device
        mCellWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
                .getDisplayMetrics());

        //get offset of list to the right (gap to the left of the screen from the left side of first item)
        final int mOffset = (this.mScreenWidth / 2) - (mCellWidth / 2);

        //HeaderItem width (blue rectangle in graphic)
        mHeaderItemWidth = mOffset + mCellWidth;
        //----------------

*/

        foodDetailRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                try {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE){
                        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();

                        String imageurl = foodlist.get(pos).getImageUrl();
                        Picasso.get().load(imageurl).into(binding.detailImageview);
                    }
                }catch (Exception e){
                    Log.e("FoodDetailActivity",e.getMessage());
                }
                //Toast.makeText(FoodDetailActivity.this,""+layoutManager.findFirstVisibleItemPosition(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

/*
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                //get first visible item
                View firstVisibleItem = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());

                int leftScrollXCalculated = 0;
                if (firstItemPosition == 0){
                    //if first item, get width of headerview (getLeft() < 0, that's why I Use Math.abs())
                    leftScrollXCalculated = Math.abs(firstVisibleItem.getLeft());
                }
                else{

                    //X-Position = Gap to the right + Number of cells * width - cell offset of current first visible item
                    //(mHeaderItemWidth includes already width of one cell, that's why I have to subtract it again)
                    leftScrollXCalculated = (mHeaderItemWidth - mCellWidth) + firstItemPosition  * mCellWidth + firstVisibleItem.getLeft();
                }

                Log.i("asdf","calculated X to left = " + leftScrollXCalculated);

                 //Toast.makeText(FoodDetailActivity.this,"wadwqd "+pos,Toast.LENGTH_SHORT).show();
*/

            }
        });

        Picasso.get().load(foodlist.get(position).getImageUrl()).into(binding.detailImageview);
    }


}