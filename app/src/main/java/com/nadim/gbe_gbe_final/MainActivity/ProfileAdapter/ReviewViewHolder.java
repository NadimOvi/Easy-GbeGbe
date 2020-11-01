package com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    ImageView imageListReview;
    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        imageListReview = itemView.findViewById(R.id.imageListReview);
    }
}
