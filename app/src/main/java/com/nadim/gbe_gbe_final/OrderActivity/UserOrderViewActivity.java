package com.nadim.gbe_gbe_final.OrderActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.nadim.gbe_gbe_final.R;
import com.squareup.picasso.Picasso;

public class UserOrderViewActivity extends AppCompatActivity {

    String image;
    private ImageView imageView;
    byte[] orderImage2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_view);
        image=getIntent().getStringExtra("image");
        imageView= findViewById(R.id.viewImage);

        orderImage2= Base64.decode(image,Base64.DEFAULT);
        Bitmap decoded= BitmapFactory.decodeByteArray(orderImage2,0,orderImage2.length);

        imageView.setImageBitmap(decoded);
      /*  imageView.setImageBitmap(BitmapFactory.decodeFile(image));*/


       /* Picasso.get().load(image).into(imageView);
*/
    }
}
