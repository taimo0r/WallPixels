package com.taimoor.wallpixels.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taimoor.wallpixels.Adapters.FavouritesAdapter;
import com.taimoor.wallpixels.Database.AppDatabase;
import com.taimoor.wallpixels.Database.HitDao;
import com.taimoor.wallpixels.Listeners.FavouritesRecyclerClickListener;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Favourites extends AppCompatActivity implements FavouritesRecyclerClickListener {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewHome;
    FavouritesAdapter adapter;
    List<Hit> favorites;
    SwipeRefreshLayout refreshLayout;
    TextView addWallpapersText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        recyclerViewHome = findViewById(R.id.recycler_home);
        refreshLayout = findViewById(R.id.swipe_refresh);
        addWallpapersText = findViewById(R.id.add_wallpapers);


        bottomNavigationView.setSelectedItemId(R.id.favourites_activity);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.wallpaper_activity:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.video_activity:
                    startActivity(new Intent(getApplicationContext(), VideosActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.favourites_activity:
                    return true;
            }
            return false;
        });

        loadHitsFromDatabase();


        refreshLayout.setOnRefreshListener(() -> {
            loadHitsFromDatabase();
            refreshLayout.setRefreshing(false);
        });

    }

    private void showData() {

        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new FavouritesAdapter(this, favorites, this);
        recyclerViewHome.setAdapter(adapter);

        adapter.setHits(favorites);
    }

    private void loadHitsFromDatabase() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Favourites").build();
        HitDao hitDao = db.hitDao();

        hitDao.getAllFavorites().observe(this, hits -> {
            if (hits != null) {
                favorites = hits;
                showData();
                if (favorites.size() == 0) {
                    addWallpapersText.setVisibility(View.VISIBLE);
                } else {
                    addWallpapersText.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(Favourites.this, "An Unexpected error Occurred.", Toast.LENGTH_SHORT).show();
                adapter.setHits(new ArrayList<>());
            }
        });
    }

    @Override
    public View.OnClickListener onCLick(List<Hit> photo, int selectedPosition) {
        startActivity(new Intent(Favourites.this, WallPaperActivity.class)
                .putExtra("photo", (Serializable) photo)
                .putExtra("selectedImagePosition",selectedPosition));

        return null;
    }
}