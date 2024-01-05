package com.taimoor.wallpixels.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.Adapters.CategoriesRecyclerAdapter;
import com.taimoor.wallpixels.Adapters.CuratedRecyclerAdapter;
import com.taimoor.wallpixels.ApiService.RequestManager;
import com.taimoor.wallpixels.Listeners.ApiResponseListener;
import com.taimoor.wallpixels.Listeners.ItemClickListener;
import com.taimoor.wallpixels.Listeners.onRecyclerClickListener;
import com.taimoor.wallpixels.Models.ApiResponse;
import com.taimoor.wallpixels.Models.CategoriesModel;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onRecyclerClickListener, ItemClickListener {

    CuratedRecyclerAdapter adapter;
    CategoriesRecyclerAdapter categoriesAdapter;
    RecyclerView recyclerViewHome, categoriesRecycler;
    ProgressDialog dialog;
    RequestManager manager;

    BottomNavigationView bottomNavigationView;

    String searchString = null;

    SwipeRefreshLayout refreshLayout;

    FloatingActionButton fabNext, fabPrev, fabSearchNext, fabSearchPrev;

    int pageNo = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerViewHome = findViewById(R.id.recycler_home);
        categoriesRecycler = findViewById(R.id.categories_recyclerView);
        fabNext = findViewById(R.id.fab_next);
        fabPrev = findViewById(R.id.fab_prev);
        fabSearchNext = findViewById(R.id.fab_search_next);
        fabSearchPrev = findViewById(R.id.fab_search_prev);
        refreshLayout = findViewById(R.id.swipe_refresh);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setSelectedItemId(R.id.wallpaper_activity);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.wallpaper_activity:
                    return true;
                case R.id.video_activity:
                    startActivity(new Intent(getApplicationContext(), VideosActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }

            return false;
        });


        if (!isConnected(this)) {
            showCustomDialog();
        }


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        manager = new RequestManager(MainActivity.this);
        manager.getWallpapers(listener, "1");

        List<CategoriesModel> list = new ArrayList<>();
        list.add(new CategoriesModel("4K"));
        list.add(new CategoriesModel("Aesthetic"));
        list.add(new CategoriesModel("Android"));
        list.add(new CategoriesModel("Art"));
        list.add(new CategoriesModel("Cars"));
        list.add(new CategoriesModel("Colourful"));
        list.add(new CategoriesModel("Dark"));
        list.add(new CategoriesModel("Flowers"));
        list.add(new CategoriesModel("Heart"));
        list.add(new CategoriesModel("Iphone"));
        list.add(new CategoriesModel("Love"));
        list.add(new CategoriesModel("Nature"));
        list.add(new CategoriesModel("Planets"));
        list.add(new CategoriesModel("Space"));


        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoriesAdapter = new CategoriesRecyclerAdapter(this, list, this);

        categoriesRecycler.setAdapter(categoriesAdapter);

        refreshLayout.setOnRefreshListener(() -> {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Loading...");
            dialog.show();
            manager = new RequestManager(MainActivity.this);
            if (searchString == null) {
                manager.getWallpapers(listener, "1");
            } else {
                manager.getSearchedWallpapers(searchListener, "1", searchString);
            }
            refreshLayout.setRefreshing(false);
        });

        fabNext.setOnClickListener(view -> {
            pageNo = pageNo + 1;
            String next_page = String.valueOf(pageNo);
            manager.getWallpapers(listener, next_page);
            if (pageNo > 1) {
                fabPrev.setVisibility(View.VISIBLE);
            }
            dialog.show();
        });


        fabPrev.setOnClickListener(view -> {
            pageNo = pageNo - 1;
            String prev_page = String.valueOf(pageNo);
            manager.getWallpapers(listener, prev_page);
            if (pageNo == 1) {
                fabPrev.setVisibility(View.GONE);
            }
            dialog.show();
        });

        fabSearchNext.setOnClickListener(view -> {
            pageNo = pageNo + 1;
            String next_page = String.valueOf(pageNo);
            manager.getSearchedWallpapers(searchListener, next_page, searchString);
            if (pageNo > 1) {
                fabSearchPrev.setVisibility(View.VISIBLE);
            }
            dialog.show();
        });


        fabSearchPrev.setOnClickListener(view -> {
            pageNo = pageNo - 1;
            String prev_page = String.valueOf(pageNo);
            manager.getSearchedWallpapers(searchListener, prev_page, searchString);
            if (pageNo == 1) {
                fabSearchPrev.setVisibility(View.GONE);
            }
            dialog.show();
        });

        getPermission();

    }


    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    private void showData(List<Hit> photos) {
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CuratedRecyclerAdapter(this, photos, this);

        recyclerViewHome.setAdapter(adapter);
        dialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manager.getSearchedWallpapers(searchListener, "1", query);
                searchString = query;

                fabNext.setVisibility(View.GONE);
                fabPrev.setVisibility(View.GONE);

                fabSearchNext.setVisibility(View.VISIBLE);

                dialog.show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    public final ApiResponseListener listener = new ApiResponseListener() {
        @Override
        public void onFetch(ApiResponse response, String message) {
            if (response.getHits().isEmpty()) {
                fabSearchPrev.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "No Wallpapers found", Toast.LENGTH_SHORT).show();
                pageNo = pageNo - 1;

                dialog.dismiss();
                return;
            }
            showData(response.getHits());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private final ApiResponseListener searchListener = new ApiResponseListener() {
        @Override
        public void onFetch(ApiResponse response, String message) {
            if (response.getHits().isEmpty()) {
                fabSearchPrev.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "No Wallpapers found!", Toast.LENGTH_SHORT).show();
                pageNo = pageNo - 1;
                dialog.dismiss();
                return;
            }
            showData(response.getHits());
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    //Check if the internet is connected or not.
    private boolean isConnected(MainActivity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());

    }

    //Show Internet connection dialog box
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Please check your internet connection before logging in")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(MainActivity.this, "Connect to internet to load wallpapers", Toast.LENGTH_SHORT).show());

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public View.OnClickListener onCLick(List<Hit> photo, int selectedPosition) {
        startActivity(new Intent(MainActivity.this, WallPaperActivity.class)
                .putExtra("photo", (Serializable) photo)
                .putExtra("selectedImagePosition",selectedPosition));
        return null;
    }

    @Override
    public void onItemClick(String category) {
        manager.getSearchedWallpapers(searchListener, "1", category);
        searchString = category;

        fabNext.setVisibility(View.GONE);
        fabPrev.setVisibility(View.GONE);
        fabSearchNext.setVisibility(View.VISIBLE);
        dialog.show();
    }
}