package com.taimoor.wallpixels.Models;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {

    public int id;
    public int width;
    public int height;
    public int duration;
    public String full_res;
    public List<String> tags;
    public String url;
    public String image;
    public String avg_color;
    public User user;
    public List<VideoFile> video_files;
    public List<VideoPicture> video_pictures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFull_res() {
        return full_res;
    }

    public void setFull_res(String full_res) {
        this.full_res = full_res;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAvg_color() {
        return avg_color;
    }

    public void setAvg_color(String avg_color) {
        this.avg_color = avg_color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<VideoFile> getVideo_files() {
        return video_files;
    }

    public void setVideo_files(List<VideoFile> video_files) {
        this.video_files = video_files;
    }

    public List<VideoPicture> getVideo_pictures() {
        return video_pictures;
    }

    public void setVideo_pictures(List<VideoPicture> video_pictures) {
        this.video_pictures = video_pictures;
    }



}
