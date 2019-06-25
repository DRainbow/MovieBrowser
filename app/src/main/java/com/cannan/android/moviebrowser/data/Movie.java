package com.cannan.android.moviebrowser.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @ClassName: Movie
 * @Description:
 * @author: Cannan
 * @date: 2019-06-23 15:10
 */
@Entity(tableName = "t_movie")
public class Movie {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "video_url")
    private String videoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
