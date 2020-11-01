package com.nadim.gbe_gbe_final.OrderActivity.Adeptar;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.R;

public class OrderImageViewHolder extends RecyclerView.ViewHolder {

    String image1;
    ImageView orderViewHolderImage;

    public OrderImageViewHolder(@NonNull View itemView) {
        super(itemView);

        orderViewHolderImage = itemView.findViewById(R.id.orderViewImage);

    }
}
