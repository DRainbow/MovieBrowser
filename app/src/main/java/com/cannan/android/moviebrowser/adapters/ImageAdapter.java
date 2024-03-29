package com.cannan.android.moviebrowser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cannan.android.moviebrowser.R;
import com.cannan.android.moviebrowser.data.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ClassName: ImageAdapter
 * @Description:
 * @author: Cannan
 * @date: 2019-06-25 23:58
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageVH> {

    private Context mContext;

    /**
     * 数据源
     */
    private List<Movie> mMovieList;
    
    public ImageAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    @NonNull
    @Override
    public ImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageVH holder, int position) {
        Glide.with(mContext.getApplicationContext())
                .load(mMovieList.get(position).getImageUrl())
                .placeholder(R.drawable.ic_android_black_24dp)
                .thumbnail(0.2f)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class ImageVH extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        ImageVH(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_image);
        }
    }
}
