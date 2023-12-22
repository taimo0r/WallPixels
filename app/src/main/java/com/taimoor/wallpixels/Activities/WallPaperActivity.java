package com.taimoor.wallpixels.Activities;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

public class WallPaperActivity extends AppCompatActivity {

    ImageView imageViewWallpaper;
    FloatingActionButton fabDownload, fabWallpaper;
    Hit photo;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wall_paper);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        builder = new AlertDialog.Builder(this);


        imageViewWallpaper = findViewById(R.id.imageview_wallpaper);
        fabDownload = findViewById(R.id.fab_download);
        fabWallpaper = findViewById(R.id.fab_wallpaper);


        Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
        photo = (Hit) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getLargeImageURL()).placeholder(R.drawable.image).into(imageViewWallpaper);

        fabDownload.setOnClickListener(view -> {

            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to download this wallpaper to your Gallery?");

            builder.setPositiveButton("Yes", (dialog, which) -> downloadImg());

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        });


        fabWallpaper.setOnClickListener(view -> {

            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to set this image as your wallpaper?");

            builder.setPositiveButton("Yes", (dialog, which) -> setWallpaper());

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        });


    }

    private void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallPaperActivity.this);
        Bitmap bitmap = ((BitmapDrawable) imageViewWallpaper.getDrawable()).getBitmap();

        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(WallPaperActivity.this, "Wallpaper Set!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(WallPaperActivity.this, "Wallpaper cannot be set", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadImg() {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(photo.getWebformatURL());


        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("WallPixels_" + photo.getUser() + "_Pixabay")
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,  "WallPixels_" + photo.getUser() + ".jpg");

        downloadManager.enqueue(request);

        Toast.makeText(WallPaperActivity.this, "Downloading Started!", Toast.LENGTH_SHORT).show();

    }
}