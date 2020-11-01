package com.nadim.gbe_gbe_final.MainActivity.ProfileAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    ArrayList<String> list;
    Context c;

    public ProfileAdapter(ArrayList<String> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.profile_row_layout,parent,false);
        ProfileViewHolder VH= new ProfileViewHolder(v);
        return VH;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        byte[] orderImage2;
        String currentdata= list.get(position);

        orderImage2= Base64.decode(currentdata,Base64.DEFAULT);
        Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);
        holder.imageList.setImageBitmap(decoded);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
