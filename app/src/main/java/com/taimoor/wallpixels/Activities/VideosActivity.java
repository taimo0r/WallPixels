package com.taimoor.wallpixels.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taimoor.wallpixels.Adapters.VideosRecyclerAdapter;
import com.taimoor.wallpixels.Listeners.ApiResponseListener;
import com.taimoor.wallpixels.Listeners.VideoRecyclerClickListener;
import com.taimoor.wallpixels.Models.ApiResponse;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.ApiService.RequestManager;

import java.util.List;

public class VideosActivity extends AppCompatActivity implements VideoRecyclerClickListener {

    private BottomNavigationView bottomNavigationView;
    private VideosRecyclerAdapter adapter;
    private RecyclerView recyclerViewHome;
    private FloatingActionButton fabNext, fabPrev;
    private ProgressDialog dialog;
    private SwipeRefreshLayout refreshLayout;
    private String searchString;
    private boolean isSearch = false;

    private int pageNo = 1;
    private RequestManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        getSupportActionBar().hide(); // Optimized to hide the action bar

        initUI();
        setupBottomNavigationView();
        setupRefreshLayout();
        fetchVideos("1"); // Initial fetch
    }

    private void initUI() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        recyclerViewHome = findViewById(R.id.videos_recycler);
        fabNext = findViewById(R.id.fab_next_video);
        fabPrev = findViewById(R.id.fab_prev_video);
        refreshLayout = findViewById(R.id.swipe_refresh_videos);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        setupPaginationButtons();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setSelectedItemId(R.id.video_activity);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.wallpaper_activity:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.video_activity:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setupPaginationButtons() {
        fabNext.setOnClickListener(view -> handlePagination(true));
        fabPrev.setOnClickListener(view -> handlePagination(false));
    }

    private void handlePagination(boolean isNext) {
        pageNo = isNext ? pageNo + 1 : pageNo - 1;
        String page = String.valueOf(pageNo);
        fetchVideos(page);
        fabPrev.setVisibility(pageNo > 1 ? View.VISIBLE : View.GONE);
        dialog.show();
    }

    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(() -> {
            fetchVideos("1");
            refreshLayout.setRefreshing(false);
        });
    }

    private void fetchVideos(String page) {
        if (manager == null) {
            manager = new RequestManager(getApplicationContext());
        }
        if (!isSearch) {
            manager.getVideoWallpapers(apiResponseListener, page);
        } else {
            manager.getSearchedVideoWallpapers(apiResponseListener, page, searchString);
        }
    }

    private final ApiResponseListener apiResponseListener = new ApiResponseListener() {
        @Override
        public void onFetch(ApiResponse response, String message) {
            if (response.getHits().isEmpty()) {
                dialog.dismiss();
                Toast.makeText(VideosActivity.this, "No videos to show!", Toast.LENGTH_SHORT).show();
                return;
            }
            pageNo = 1;
            showVideos(response.getHits());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(VideosActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };

    private void showVideos(List<Hit> videos) {
        if (adapter == null) {
            recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
            adapter = new VideosRecyclerAdapter(this, videos, this);
            recyclerViewHome.setAdapter(adapter);
        } else {
            adapter.updateVideos(videos); // Update videos instead of resetting adapter
        }
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        setupSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView(MenuItem menuItem) {
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString = query;
                isSearch = true;
                fetchVideos("1");
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Implement if needed for real-time search
                return false;
            }
        });
    }

    @Override
    public void onVideoClick(String url, String user) {
        Intent intent = new Intent(VideosActivity.this, FullScreenVideo.class);
        intent.putExtra("video", url);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss(); // Avoid window leaks
        }
    }
}
