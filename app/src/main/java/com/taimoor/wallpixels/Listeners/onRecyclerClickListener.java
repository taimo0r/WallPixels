package com.taimoor.wallpixels.Listeners;

import android.view.View;

import com.taimoor.wallpixels.Models.Hit;

import java.util.List;

public interface onRecyclerClickListener {

    View.OnClickListener onCLick(List<Hit> photo, int selectedPosition);

}
