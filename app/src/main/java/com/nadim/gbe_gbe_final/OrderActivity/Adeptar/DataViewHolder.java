package com.nadim.gbe_gbe_final.OrderActivity.Adeptar;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.util.Base64;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.nadim.gbe_gbe_final.R;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.List;

public class DataViewHolder extends RecyclerView.ViewHolder {

    TextView trxId,isDeliver,locationName,uploadTime,orderNote;
    ImageView orderImage;
    RecyclerView recyclerOrderView;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        trxId=itemView.findViewById(R.id.trxId);
        isDeliver=itemView.findViewById(R.id.isDeliver);
        locationName=itemView.findViewById(R.id.locationName);
        /*orderImage=itemView.findViewById(R.id.orderImage);*/
        orderNote = itemView.findViewById(R.id.noteAndTime);

        recyclerOrderView =itemView.findViewById(R.id.recyclerOrderView);
        /*uploadTime=itemView.findViewById(R.id.uploadTime);

        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
        String currentTime = dateformat.format(Calendar.getInstance().getTime());
        uploadTime.setText(currentTime);*/
        /*orderImage2= Base64.decode(String.valueOf(orderImage),Base64.DEFAULT);
        Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);*/
/*        image1 = Base64.decode(image,Base64.DEFAULT);
        Bitmap decoded= BitmapFactory.decodeByteArray(image1,0,image1.length);*/

    }

}
