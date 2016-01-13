package com.colander.scavenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by colander on 1/3/16.
 */
public class NodeImgRecyclerAdapter extends RecyclerView.Adapter<NodeImgRecyclerAdapter.ViewHolder> {

    private String[] dataSet;
    private ImageLoader imageLoader;
    private Context context;

    public NodeImgRecyclerAdapter(String[] dataSet, ImageLoader imageLoader, Context context) {
        this.dataSet = dataSet;
        this.imageLoader = imageLoader;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NetworkImageView v = (NetworkImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.node_img_thumbnail, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 1) {
            holder.imageView.setImageUrl("http://www.topzine.cz/wp-content/uploads/2011/11/miminka.jpg"/*dataSet[position]*/, imageLoader);
        } else {
            holder.imageView.setImageUrl("http://www.prague.eu/file/edee/object/1885/03.jpg"/*dataSet[position]*/, imageLoader);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra(FullImageActivity.IMAGE_URL, "http://www.topzine.cz/wp-content/uploads/2011/11/miminka.jpg");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView imageView;

        public ViewHolder(NetworkImageView v) {
            super(v);
            imageView = v;
        }
    }
}
