package com.nadim.gbe_gbe_final.OrderActivity.Adeptar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadim.gbe_gbe_final.OrderActivity.UserOrderViewActivity;
import com.nadim.gbe_gbe_final.R;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private List<OurDataSet> list;
    private Context context;


    public DataAdapter(List<OurDataSet> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.order_row_layout,viewGroup,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i) {
       /* dataViewHolder.isDeliver.setText(list.get(i).getStatus());*/
        final OurDataSet currentdata = list.get(i);

        dataViewHolder.trxId.setText(currentdata.getTrxId());
        dataViewHolder.locationName.setText(currentdata.getLocationName());
        dataViewHolder.isDeliver.setText(currentdata.getStatus());
        dataViewHolder.orderNote.setText(currentdata.getNote());

        List<OrderImageDataSet> orderImageDataSets=currentdata.getOrderImage();



        OrderImageAdapter orderImageAdapter = new OrderImageAdapter(context,orderImageDataSets);
        dataViewHolder.recyclerOrderView.setHasFixedSize(true);
        dataViewHolder.recyclerOrderView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        dataViewHolder.recyclerOrderView.setAdapter(orderImageAdapter);

       /* dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UserOrderViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",currentdata.getOrderImage());
                context.startActivity(intent);
            }
        });*/

      /*  if (currentdata.getImage()!=null && currentdata.getImage().length()>0){

            orderImage2= Base64.decode((currentdata.getImage()),Base64.DEFAULT);
            Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);


            *//*Picasso.get().load(String.valueOf(decoded)).fit().into(dataViewHolder.orderImage);*//*
            dataViewHolder.orderImage.setImageBitmap(decoded);
            Log.d("image",currentdata.getImage());

        }else{
            Toast.makeText(context, R.string.dataAdapterEmtypImage,Toast.LENGTH_SHORT).show();

        }
        dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, UserOrderViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image",currentdata.getImage());
                context.startActivity(intent);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
