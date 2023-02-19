package com.example.celebrate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class maskTesting extends AppCompatActivity {
    Bitmap result,maskBitMap;
    ImageView testImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_testing);

        testImage=findViewById(R.id.testImage);
        Bitmap bitmap = makeMask();
    }
    private Bitmap makeMask() {
        try{
            Bitmap original,mask;

            mask= BitmapFactory.decodeResource(getResources(),R.drawable.user_image_frame_1);
            original=BitmapFactory.decodeResource(getResources(),R.drawable.image2);

            if(original!=null){
                int hgt=original.getHeight();
                int wdt = original.getWidth();

                result=Bitmap.createBitmap(wdt,hgt,Bitmap.Config.ARGB_8888);
                maskBitMap = Bitmap.createScaledBitmap(mask,wdt,hgt,true);

                Canvas canvas = new Canvas(result);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                canvas.drawBitmap(original,0,0,null);
                canvas.drawBitmap(maskBitMap,0,0,paint);

                paint.setXfermode(null);
                paint.setStyle(Paint.Style.STROKE);
            }
        }catch (OutOfMemoryError e){

            e.printStackTrace();
        }
        testImage.setImageBitmap(result);
        return result;
    }

}