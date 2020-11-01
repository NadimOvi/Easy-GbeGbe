package com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.OrderActivity.UserOrderViewActivity;
import com.nadim.gbe_gbe_final.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private ArrayList<String> arrayList;
    private Context c;

    public ReviewAdapter(ArrayList<String> arrayList, Context c) {
        this.arrayList = arrayList;
        this.c = c;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.review_row_layout,parent,false);
        ReviewViewHolder VH= new ReviewViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        byte[] orderImage2;
        final String currentdata= arrayList.get(position);

        orderImage2= Base64.decode(currentdata,Base64.DEFAULT);
        Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);
        holder.imageListReview.setImageBitmap(decoded);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(c, UserOrderViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",currentdata);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
