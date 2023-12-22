package com.taimoor.wallpixels.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.Adapters.CategoriesRecyclerAdapter;
import com.taimoor.wallpixels.Adapters.VideosRecyclerAdapter;
import com.taimoor.wallpixels.Listeners.ApiResponseListener;
import com.taimoor.wallpixels.Listeners.ItemClickListener;
import com.taimoor.wallpixels.Listeners.VideoRecyclerClickListener;
import com.taimoor.wallpixels.Models.ApiResponse;
import com.taimoor.wallpixels.Models.CategoriesModel;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.ApiService.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class VideosActivity extends AppCompatActivity implements VideoRecyclerClickListener, ItemClickListener {

    BottomNavigationView bottomNavigationView;
    CategoriesRecyclerAdapter categoriesAdapter;
    VideosRecyclerAdapter adapter;
    RecyclerView recyclerViewHome, categoriesRecycler;
    FloatingActionButton fabNext, fabPrev;
    ProgressDialog dialog;
    SwipeRefreshLayout refreshLayout;
    String searchString;
    Boolean isSearch = false;

    int pageNo = 1;
    RequestManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        recyclerViewHome = findViewById(R.id.videos_recycler);
        categoriesRecycler = findViewById(R.id.categories_recyclerView_video);
        fabNext = findViewById(R.id.fab_next_video);
        fabPrev = findViewById(R.id.fab_prev_video);
        refreshLayout = findViewById(R.id.swipe_refresh_videos);


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


        bottomNavigationView.setSelectedItemId(R.id.video_activity);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.wallpaper_activity:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.video_activity:
                        return true;
                }
                return false;
            }
        });

        dialog = new ProgressDialog(VideosActivity.this);
        dialog.setTitle("Loading...");
        dialog.show();

        manager = new RequestManager(VideosActivity.this);
        manager.getVideoWallpapers(listener, "1");


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialog = new ProgressDialog(VideosActivity.this);
                dialog.setTitle("Loading...");
                dialog.show();

                manager = new RequestManager(VideosActivity.this);
                manager.getVideoWallpapers(listener, "1");
                refreshLayout.setRefreshing(false);
            }
        });


        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo + 1;
                String next_page = String.valueOf(pageNo);
                if (!isSearch) {
                    manager.getVideoWallpapers(listener, next_page);
                } else {
                    manager.getSearchedVideoWallpapers(searchListener, next_page, searchString);
                }

                if (pageNo > 1) {
                    fabPrev.setVisibility(View.VISIBLE);
                }
                dialog.show();
            }
        });


        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = pageNo - 1;
                String prev_page = String.valueOf(pageNo);
                if (!isSearch) {
                    manager.getVideoWallpapers(listener, prev_page);
                } else {
                    manager.getSearchedVideoWallpapers(searchListener, prev_page, searchString);
                }
                if (pageNo == 1) {
                    fabPrev.setVisibility(View.GONE);
                }

                dialog.show();
            }
        });
    }

    private final ApiResponseListener listener = new ApiResponseListener() {
        @Override
        public void onFetch(ApiResponse response, String message) {
            if (response.getHits().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(VideosActivity.this, "No videos to show!", Toast.LENGTH_SHORT).show();
                return;
            }
            showVideos(response.getHits());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(VideosActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private final ApiResponseListener searchListener = new ApiResponseListener() {
        @Override
        public void onFetch(ApiResponse response, String message) {
            if (response.getHits().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(VideosActivity.this, "No videos to show!", Toast.LENGTH_SHORT).show();
                return;
            }
            showVideos(response.getHits());
            isSearch = true;
        }

        @Override
        public void onError(String message) {
            Toast.makeText(VideosActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };


    private void showVideos(List<Hit> videos) {
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new VideosRecyclerAdapter(this, videos, this);
        recyclerViewHome.setAdapter(adapter);
        dialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manager.getSearchedVideoWallpapers(searchListener, "1", query);
                searchString = query;
                if(pageNo > 1) {
                    pageNo = 1;
                    fabPrev.setVisibility(View.GONE);
                }
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

    @Override
    public void onItemClick(String category) {
        manager.getSearchedVideoWallpapers(searchListener, "1", category);
        searchString = category;
        if(pageNo > 1) {
            pageNo = 1;
            fabPrev.setVisibility(View.GONE);
        }
        fabPrev.setVisibility(View.GONE);

        dialog.show();

    }

    @Override
    public void onVideoClick(String url, String user) {
        startActivity(new Intent(VideosActivity.this, FullScreenVideo.class)
                .putExtra("video", url)
                .putExtra("user", user));
    }
}
