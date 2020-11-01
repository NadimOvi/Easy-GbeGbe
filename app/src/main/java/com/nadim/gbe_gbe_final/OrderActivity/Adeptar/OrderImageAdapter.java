package com.nadim.gbe_gbe_final.OrderActivity.Adeptar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.OrderActivity.UserOrderViewActivity;
import com.nadim.gbe_gbe_final.R;

import java.util.ArrayList;
import java.util.List;

public class OrderImageAdapter extends RecyclerView.Adapter<OrderImageViewHolder> {

    private Context context;
    private List<OrderImageDataSet> orderImageDataSet;

    public OrderImageAdapter(Context context, List<OrderImageDataSet> orderImageDataSet) {
        this.context = context;
        this.orderImageDataSet = orderImageDataSet;
    }

    @NonNull
    @Override
    public OrderImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.orderview_row_layout,parent,false);
        return new OrderImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderImageViewHolder holder, int position) {
        /*String currenData = orderImageDataSet.get(position).getImage();*/


        byte[] orderImage2;
        final String orderImageDataSets = orderImageDataSet.get(position).getImage();

        orderImage2= Base64.decode(orderImageDataSets,Base64.DEFAULT);

        Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);
        holder.orderViewHolderImage.setImageBitmap(decoded);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UserOrderViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",orderImageDataSets);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderImageDataSet.size();
    }
}
