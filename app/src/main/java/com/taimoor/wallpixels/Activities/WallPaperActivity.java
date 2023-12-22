package com.taimoor.wallpixels.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;

public class WallPaperActivity extends AppCompatActivity {

    ImageView imageViewWallpaper;
    FloatingActionButton fabDownload, fabWallpaper;
    Hit photo;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    TextView original, large, large2x, small, medium, portrait, landscape, tiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wall_paper);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        builder = new AlertDialog.Builder(this);


        imageViewWallpaper = findViewById(R.id.imageview_wallpaper);
        fabDownload = findViewById(R.id.fab_download);
        fabWallpaper = findViewById(R.id.fab_wallpaper);


        Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
        photo = (Hit) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getWebformatURL()).placeholder(R.drawable.image).into(imageViewWallpaper);


        fabDownload.setOnClickListener(view -> selectQuality());

        fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to set this image as your wallpaper?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setWallpaper();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }
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

    private void selectQuality() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(WallPaperActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.download_dialog, null);


        original = (TextView) dialogView.findViewById(R.id.original_txt);
        landscape = (TextView) dialogView.findViewById(R.id.landscape_txt);
        large = (TextView) dialogView.findViewById(R.id.large_txt);
        large2x = (TextView) dialogView.findViewById(R.id.large2x_txt);
        medium = (TextView) dialogView.findViewById(R.id.medium_txt);
        small = (TextView) dialogView.findViewById(R.id.small_txt);
        portrait = (TextView) dialogView.findViewById(R.id.portrait_txt);
        tiny = (TextView) dialogView.findViewById(R.id.tiny_txt);

        alert.setView(dialogView);
        alertDialog = alert.create();
        //  alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.windowAnimations = R.style.DownloadDialogAnimation;

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("original");
                alertDialog.dismiss();
            }
        });

        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("large");
                alertDialog.dismiss();
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("medium");
                alertDialog.dismiss();
            }
        });

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("small");
                alertDialog.dismiss();
            }
        });

        large2x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("large2x");
                alertDialog.dismiss();
            }
        });

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("portrait");
                alertDialog.dismiss();
            }
        });

        landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("landscape");
                alertDialog.dismiss();
            }
        });

        tiny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImg("tiny");
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }

    private void downloadImg(String quality) {

        DownloadManager downloadManager = null;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(photo.getWebformatURL());

//        if (quality.equals("original")) {
//            uri = Uri.parse(photo.getSrc().getOriginal());
//        } else if (quality.equals("large")) {
//            uri = Uri.parse(photo.getSrc().getLarge());
//        } else if (quality.equals("large2x")) {
//            uri = Uri.parse(photo.getSrc().getLarge2x());
//        } else if (quality.equals("medium")) {
//            uri = Uri.parse(photo.getSrc().getMedium());
//        } else if (quality.equals("small")) {
//            uri = Uri.parse(photo.getSrc().getSmall());
//        } else if (quality.equals("portrait")) {
//            uri = Uri.parse(photo.getSrc().getPortrait());
//        } else if (quality.equals("landscape")) {
//            uri = Uri.parse(photo.getSrc().getLandscape());
//        } else if (quality.equals("tiny")) {
//            uri = Uri.parse(photo.getSrc().getTiny());
//        } else {
//            Toast.makeText(this, "No Quality Selected", Toast.LENGTH_SHORT).show();
//            return;
//        }

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