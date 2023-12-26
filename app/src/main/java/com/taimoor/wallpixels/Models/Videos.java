package com.taimoor.wallpixels.Models;

import java.io.Serializable;

public class Videos implements Serializable {

    private VideoDetails large;
    private VideoDetails medium;
    private VideoDetails small;
    private VideoDetails tiny;

    // Getters and Setters
    public VideoDetails getLarge() {
        return large;
    }

    public void setLarge(VideoDetails large) {
        this.large = large;
    }

    public VideoDetails getMedium() {
        return medium;
    }

    public void setMedium(VideoDetails medium) {
        this.medium = medium;
    }

    public VideoDetails getSmall() {
        return small;
    }

    public void setSmall(VideoDetails small) {
        this.small = small;
    }

    public VideoDetails getTiny() {
        return tiny;
    }

    public void setTiny(VideoDetails tiny) {
        this.tiny = tiny;
    }
}
