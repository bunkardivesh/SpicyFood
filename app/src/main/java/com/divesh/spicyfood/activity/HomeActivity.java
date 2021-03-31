package com.divesh.spicyfood.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.divesh.spicyfood.Model.FoodModel;
import com.divesh.spicyfood.R;
import com.divesh.spicyfood.Utility.DemoData;
import com.divesh.spicyfood.Utility.RecyclerViewClickListener;
import com.divesh.spicyfood.Utility.SessionManager;
import com.divesh.spicyfood.Utility.ZoomCardLayoutManager;
import com.divesh.spicyfood.adapter.PopularFoodAdapter;
import com.divesh.spicyfood.adapter.TrendingFoodAdapter;
import com.divesh.spicyfood.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
//Main screen of the application
public class HomeActivity extends AppCompatActivity {


    private ActivityHomeBinding binding;
    private RecyclerView trendingRV, popularRV;
    private List<FoodModel> foodModelList;
    private List<FoodModel> trendingList;
    private TrendingFoodAdapter trendingFoodAdapter;
    private PopularFoodAdapter popularFoodAdapter;
    private ImageView mSearch;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //using view binding to bind the views
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpRecyclerView();
        setTrendingData();
        setPopularData();
        intentMethods();

        //managing the drawer layout
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            private float scaleFactor = 6f;
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //to slide the main screen with drawer sliding
                float slideX = drawerView.getWidth() * slideOffset;
                binding.appBarMain.mainView.setTranslationX(slideX);
                binding.appBarMain.mainView.setScaleX(1 - (slideOffset / scaleFactor));
                binding.appBarMain.mainView.setScaleY(1 - (slideOffset / scaleFactor));

            }
        };

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager = new SessionManager(HomeActivity.this);
                sessionManager.logoutUser();
                finish();
            }
        });
    }


    private void setUpRecyclerView() {

        trendingRV = binding.appBarMain.homeLayout.recyclerviewTrending;
        ZoomCardLayoutManager layoutManager = new ZoomCardLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        trendingRV.setLayoutManager(layoutManager);
        trendingRV.setNestedScrollingEnabled(false);

        popularRV = binding.appBarMain.homeLayout.recyclerviewPopular;
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        popularRV.setLayoutManager(linearLayoutManager);
        popularRV.setNestedScrollingEnabled(false);

    }

    private void setTrendingData() {
        //getting trending food list from database
        trendingList = new ArrayList<>();
        trendingList = DemoData.getFoodData(this,"recommended");

        trendingFoodAdapter = new TrendingFoodAdapter(this, trendingList, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                sendToFoodDetailActivity(trendingList,position);
            }
        });
        trendingRV.setAdapter(trendingFoodAdapter);

    }

    private void sendToFoodDetailActivity(List<FoodModel> foodModelList, int position) {

        Intent foodIntent = new Intent(HomeActivity.this,FoodDetailActivity.class);
        foodIntent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) foodModelList);
        foodIntent.putExtra("position",position);
        startActivity(foodIntent);
    }

    private void setPopularData() {
        //getting popular food list from database
        foodModelList = new ArrayList<>();
        foodModelList = DemoData.getFoodData(this,"popular");

        popularFoodAdapter = new PopularFoodAdapter(this, foodModelList, new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                sendToFoodDetailActivity(foodModelList,position);
            }
        });
        popularRV.setAdapter(popularFoodAdapter);
    }
    //handle drawer button click
    public void onHamburgerClick(View view) {
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }
    private void intentMethods() {

        mSearch = binding.appBarMain.homeLayout.homeSearchFood;
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(HomeActivity.this,SearchActivity.class);
                startActivity(searchIntent);
            }
        });


    }

    public void sendToCart(View view) {
        Intent cartIntent = new Intent(HomeActivity.this,CartActivity.class);
        startActivity(cartIntent);
    }

    public void onProfileClick(View view) {
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    public void onOrderClick(View view) {
        Intent intent = new Intent(HomeActivity.this,OrderActivity.class);
        startActivity(intent);
    }

    public void onWishListClick(View view) {
        Intent intent = new Intent(HomeActivity.this,WishListActivity.class);
        startActivity(intent);
    }
}