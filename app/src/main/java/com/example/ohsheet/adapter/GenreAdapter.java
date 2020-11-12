package com.example.ohsheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Genre;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ImageAdapterHolder> {
    private Context mContext;
    private List<Genre> mUploads;

    private OnItemClicked onClick;

    public GenreAdapter(Context mContext, List<Genre> mUploads, OnItemClicked onClick) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ImageAdapterHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.customlayout_main, parent, false);
        return new ImageAdapterHolder(view, onClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterHolder holder, final int position) {
        Genre genreCurrent = mUploads.get(position);
        Picasso.get()
                .load(genreCurrent.getGenreImg())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public OnItemClicked onItemClicked;

        public ImageAdapterHolder(@NonNull View itemView, OnItemClicked onItemClicked) {
            super(itemView);
            this.onItemClicked = onItemClicked;

            imageView = itemView.findViewById(R.id.imageCategory);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClicked.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClicked{
        void onItemClick(int position);
    }

}
