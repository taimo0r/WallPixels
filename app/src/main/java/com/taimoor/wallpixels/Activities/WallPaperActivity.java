package com.taimoor.wallpixels.Activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.taimoor.wallpixels.Models.Hit;
import com.taimoor.wallpixels.R;
import com.taimoor.wallpixels.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class WallPaperActivity extends AppCompatActivity {

    ImageView imageViewWallpaper;
    CircleImageView userImage;
    FloatingActionButton fabDownload, fabWallpaper;
    Hit photo;
    Dialog dialog;
    Button cancelBtn, confirmBtn;
    TextView descTextDialog, username, downloads, views, likes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wall_paper);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewWallpaper = findViewById(R.id.imageview_wallpaper);
        fabDownload = findViewById(R.id.fab_download);
        fabWallpaper = findViewById(R.id.fab_wallpaper);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.userName);
        downloads = findViewById(R.id.downloadCount);
        views = findViewById(R.id.viewsCount);
        likes = findViewById(R.id.likesCount);

        dialog = new Dialog(WallPaperActivity.this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        cancelBtn = dialog.findViewById(R.id.cancel_button);
        confirmBtn = dialog.findViewById(R.id.confirm_action);
        descTextDialog = dialog.findViewById(R.id.descText);


        Toast.makeText(this, "Loading....", Toast.LENGTH_SHORT).show();
        photo = (Hit) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getLargeImageURL()).into(imageViewWallpaper);

        Picasso.get().load(photo.getUserImageURL()).error(R.drawable.user_icon).into(userImage);
        username.setText(photo.getUser());
        downloads.setText(Utils.convertToKilo(photo.getDownloads()));
        views.setText(Utils.convertToKilo(photo.getViews()));
        likes.setText(Utils.convertToKilo(photo.getLikes()));

        fabDownload.setOnClickListener(view -> {

            descTextDialog.setText("Do you want to download this wallpaper to your Gallery?");

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadImg();
                    dialog.dismiss();
                }
            });
            cancelBtn.setOnClickListener(view1 -> dialog.dismiss());

            dialog.show();
        });


        fabWallpaper.setOnClickListener(view -> {

            descTextDialog.setText("Do you want to set this image as your wallpaper?");

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setWallpaper();
                    dialog.dismiss();
                }
            });
            cancelBtn.setOnClickListener(view13 -> dialog.dismiss());

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

                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "WallPixels_" + photo.getUser() + ".jpg");

        downloadManager.enqueue(request);

        Toast.makeText(WallPaperActivity.this, "Downloading Started!", Toast.LENGTH_SHORT).show();

    }
}