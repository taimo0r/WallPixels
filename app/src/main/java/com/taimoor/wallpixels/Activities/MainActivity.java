package com.taimoor.wallpixels.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.Adapters.CategoriesRecyclerAdapter;
import com.taimoor.wallpixels.Adapters.CuratedRecyclerAdapter;
import com.taimoor.wallpixels.Listeners.CuratedResponseListener;
import com.taimoor.wallpixels.Listeners.ItemClickListener;
import com.taimoor.wallpixels.Listeners.SearchResponseListener;
import com.taimoor.wallpixels.Listeners.onRecyclerClickListener;
import com.taimoor.wallpixels.Models.CategoriesModel;
import com.taimoor.wallpixels.Models.CuratedApiResponse;
import com.taimoor.wallpixels.Models.Photo;
import com.taimoor.wallpixels.Models.SearchApiResponse;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.RequestManager;

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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

        // Storage Permissions


        if (!isConnected(this)) {
            showCustomDialog();
            //    return;
        }


        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        manager = new RequestManager(this);
        manager.getCuratedWallpapers(listener, "1");

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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Loading...");
                dialog.show();
                manager = new RequestManager(MainActivity.this);
                if (searchString == null) {
                    manager.getCuratedWallpapers(listener, "1");
                } else {
                    manager.searchCuratedWallpapers(responseListener, "1", searchString);
                }
                refreshLayout.setRefreshing(false);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo + 1;
                String next_page = String.valueOf(pageNo);
                manager.getCuratedWallpapers(listener, next_page);
                if (pageNo > 1) {
                    fabPrev.setVisibility(View.VISIBLE);
                }
                Toast.makeText(MainActivity.this, "" + pageNo, Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });


        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo - 1;
                String prev_page = String.valueOf(pageNo);
                if (pageNo == 1) {
                    fabPrev.setVisibility(View.GONE);
                }
                Toast.makeText(MainActivity.this, "" + pageNo, Toast.LENGTH_SHORT).show();
                manager.getCuratedWallpapers(listener, prev_page);

                dialog.show();
            }
        });

        fabSearchNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo + 1;
                String next_page = String.valueOf(pageNo);
                manager.searchCuratedWallpapers(responseListener, next_page, searchString);
                if (pageNo > 1) {
                    fabSearchPrev.setVisibility(View.VISIBLE);
                }
                Toast.makeText(MainActivity.this, "" + pageNo, Toast.LENGTH_SHORT).show();
                dialog.show();
            }
        });


        fabSearchPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo - 1;
                String prev_page = String.valueOf(pageNo);
                if (pageNo == 1) {
                    fabSearchPrev.setVisibility(View.GONE);
                }
                Toast.makeText(MainActivity.this, "" + pageNo, Toast.LENGTH_SHORT).show();
                manager.searchCuratedWallpapers(responseListener, prev_page, searchString);
                dialog.show();
            }
        });

        getPermission();

    }


    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private final CuratedResponseListener listener = new CuratedResponseListener() {
        @Override
        public void onFetch(CuratedApiResponse response, String message) {

            if (response.getPhotos().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "No Wallpapers found", Toast.LENGTH_SHORT).show();
                return;
            }
            pageNo = response.getPage();
            showData(response.getPhotos());
        }


        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private void showData(List<Photo> photos) {
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CuratedRecyclerAdapter(this, photos, this);

        recyclerViewHome.setAdapter(adapter);
        dialog.dismiss();
    }


    @Override
    public void onCLick(Photo photo) {
        startActivity(new Intent(MainActivity.this, WallPaperActivity.class)
                .putExtra("photo", photo));
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
                manager.searchCuratedWallpapers(responseListener, "1", query);
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

    private final SearchResponseListener responseListener = new SearchResponseListener() {
        @Override
        public void onFetch(SearchApiResponse response, String message) {
            if (response.getPhotos().isEmpty()) {
                Toast.makeText(MainActivity.this, "No Image found!", Toast.LENGTH_SHORT).show();
                return;
            }
            pageNo = response.getPage();
            int res = response.getPhotos().size();
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            showData(response.getPhotos());
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

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    //Show Internet connection dialog box
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("Please check your internet connection before logging in")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Connect to internet to load wallpapers", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onItemClick(String category) {
        manager.searchCuratedWallpapers(responseListener, "1", category);
        searchString = category;

        fabNext.setVisibility(View.GONE);
        fabPrev.setVisibility(View.GONE);
        fabSearchNext.setVisibility(View.VISIBLE);
        dialog.show();
    }
}