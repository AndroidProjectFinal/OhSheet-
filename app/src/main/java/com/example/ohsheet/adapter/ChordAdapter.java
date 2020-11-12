

package com.example.ohsheet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohsheet.R;
import com.example.ohsheet.entity.Chord;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChordAdapter extends RecyclerView.Adapter<ChordAdapter.ImageAdapterHolder> {
    private Context mContext;
    private List<Chord> mUploads;

    public ChordAdapter(Context mContext, List<Chord> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ChordAdapter.ImageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_chord, parent, false);

        return new ChordAdapter.ImageAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChordAdapter.ImageAdapterHolder holder, int position) {
        Chord chord = mUploads.get(position);
        Picasso.get()
                .load(chord.getLink())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageAdapterHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgChord);
        }
    }
}
