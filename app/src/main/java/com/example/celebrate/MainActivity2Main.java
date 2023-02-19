package com.example.celebrate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity2Main extends AppCompatActivity {
    Button selectImage;
    ImageView imageToShow;
    ImageView tempImage;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;
    Uri tempSelection;
    Bitmap result,maskBitMap;
    ArrayList<ImageView> img = new ArrayList<>(4);
    ArrayList<Integer> imgResource =new ArrayList<>();

    TextView imgOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_main);


        selectImage = findViewById(R.id.selectImage);
        Button test = findViewById(R.id.goToTest);

        //temp for the testing the the masking
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2Main.this,maskTesting.class));
            }
        });

        //initializing array for the given font to work
        imageToShow=findViewById(R.id.imageShow);
        imgResource.add(R.drawable.user_image_frame_1);
        imgResource.add(R.drawable.user_image_frame_2);
        imgResource.add(R.drawable.user_image_frame_3);
        imgResource.add(R.drawable.user_image_frame_4);

        //initializng selection from gallery
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
            }
        });
    }

    //acsessing the images from gallery
    private void selectImageFromGallery() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    //creating result from the galery
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageToShow.setImageURI(selectedImageUri);
                    //creating shape choice pop-up
                    chooseShape();
                }
            }
        }
    }

    private void chooseShape() {
        //dialog box initalaization
        Dialog dialog = new Dialog(MainActivity2Main.this);
        dialog.setContentView(R.layout.dailog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        tempImage = dialog.findViewById(R.id.chooseImage);
        Button finalizeImage = dialog.findViewById(R.id.choiceDone);
        ImageButton closeDialog =dialog.findViewById(R.id.closeDialog);

        //creating array of all image button for shape in different usage state
        img.add(dialog.findViewById(R.id.heart));
        img.add(dialog.findViewById(R.id.square));
        img.add(dialog.findViewById(R.id.oval));
        img.add(dialog.findViewById(R.id.rect));
        imgOriginal=dialog.findViewById(R.id.orignalTextBox);

        /*creating the initalization for each button in
        this is for small option if max create function uses multiple data (DRY concept)
         */

        imgOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempImage.setImageURI(selectedImageUri);
            }
        });

        img.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMask(0);
            }
        });

        img.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMask(1);
            }
        });

        img.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMask(2);
            }
        });

        img.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMask(3);
            }
        });


        //dialog initlization after choosing image
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                tempImage.setImageURI(selectedImageUri);
                tempSelection=selectedImageUri;
            }
        });
        dialog.show();

        //finishing choice for the task
        finalizeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = tempImage.getDrawable();
                imageToShow.setImageDrawable(drawable);
                dialog.dismiss();
            }
        });

        //close button for dialog box
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    //masking for the images
    private void makeMask(int i){
            try {
                Bitmap original, mask;

                mask = BitmapFactory.decodeResource(getResources(), imgResource.get(i));
                original= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);

                if (original != null) {
                    int hgt = original.getHeight();
                    int wdt = original.getWidth();

                    result = Bitmap.createBitmap(wdt, hgt, Bitmap.Config.ARGB_8888);
                    maskBitMap = Bitmap.createScaledBitmap(mask, wdt, hgt, true);

                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                    canvas.drawBitmap(original, 0, 0, null);
                    canvas.drawBitmap(maskBitMap, 0, 0, paint);

                    paint.setXfermode(null);
                    paint.setStyle(Paint.Style.STROKE);
                }
            } catch (OutOfMemoryError | IOException e) {
                e.printStackTrace();
            }
        tempImage.setImageBitmap(result);
        }

    //treating this activity as main
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}