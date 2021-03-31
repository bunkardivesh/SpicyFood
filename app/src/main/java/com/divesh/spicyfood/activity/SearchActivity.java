package com.divesh.spicyfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.DemoData;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.adapter.TrendingFoodAdapter;
import com.divesh.spicyfood.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    private ActivitySearchBinding binding;


    private RecyclerView searchRV;
    private List<FoodModel> AllFoodList;
    private TrendingFoodAdapter foodAdapter;
    private List<FoodModel> newList;
    private List<FoodModel> copiedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareList();
        setUpRecyclerView();
        newList = new ArrayList<>();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")){
                    foodAdapter.updateData(new ArrayList<FoodModel>());
                }else {
                    binding.searchProgressBar.setVisibility(View.VISIBLE);
                    searchData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.searchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void searchData(String query) {
        foodAdapter.updateData(newList);
        binding.textNotFound.setVisibility(View.INVISIBLE);
        for (FoodModel foodModel: AllFoodList){

            if (foodModel.getFoodName().toLowerCase().contains(query.toLowerCase())){
                newList.add(foodModel);
            }
        }
        if (newList.size() != 0){
            foodAdapter.updateData(newList);
        }else {
            binding.textNotFound.setVisibility(View.VISIBLE);
        }
        copiedList = new ArrayList<FoodModel>(newList);
        newList.clear();
        binding.searchProgressBar.setVisibility(View.INVISIBLE);
    }

    private void setUpRecyclerView() {

        searchRV = binding.serchRecyclerview;
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        searchRV.setLayoutManager(layoutManager);
        searchRV.setNestedScrollingEnabled(false);

        foodAdapter = new TrendingFoodAdapter(this, new ArrayList<>(), new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {

                sendToFoodDetailActivity(copiedList,position);
            }
        });
        searchRV.setAdapter(foodAdapter);

    }

    private void prepareList() {

        try {

            AllFoodList = new ArrayList<>();
            AllFoodList = DemoData.getFoodData(this,"alldata");

        }catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void sendToFoodDetailActivity(List<FoodModel> foodModelList, int position) {

        Intent foodIntent = new Intent(SearchActivity.this,FoodDetailActivity.class);
        foodIntent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) foodModelList);
        foodIntent.putExtra("position",position);
        startActivity(foodIntent);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    // bgImageLogin.startAnimation(animationzoomout);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}