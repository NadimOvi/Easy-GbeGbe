package com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    ImageView imageList,imageDelete;
    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        imageList = itemView.findViewById(R.id.imageList);
    }
}
