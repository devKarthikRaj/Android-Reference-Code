package com.raj.allthingspalette;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.palette.graphics.Palette;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout rootLayout;
    TextView tvTitle;
    TextView tvBody;
    ImageView ivTestImage;
    Button btnNextSwatchButton;
    Button btnNextImageButton;

    //There are six types of swatches (color palette thingy) in total
    Palette.Swatch vibrantSwatch;
    Palette.Swatch lightVibrantSwatch;
    Palette.Swatch darkVibrantSwatch;
    Palette.Swatch mutedSwatch;
    Palette.Swatch lightMutedSwatch;
    Palette.Swatch darkMutedSwatch;

    int imageNum;
    int swatchNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.LL_root_layout);
        tvTitle = findViewById(R.id.TV_title);
        tvBody = findViewById(R.id.TV_body);
        ivTestImage = findViewById(R.id.IV_test_image);
        btnNextSwatchButton = findViewById(R.id.BTN_next_swatch_button);
        btnNextImageButton = findViewById(R.id.BTN_next_image_button);

        btnNextSwatchButton.setOnClickListener(this);
        btnNextImageButton.setOnClickListener(this);

        /*get drawable from the image displayed in the image view and then cast it to BitmapDrawable to convert it to
          BitmapDrawable and then get the bimap out of that BitmapDrawable */
        Bitmap bitmap = ((BitmapDrawable) ivTestImage.getDrawable()).getBitmap();

        //A palette can be generated asynchronously or synchronously
        //Asynchronous generation: Palette is generated via an async task by android
        //Synchronous generation: Palette is generated in an image loading thread specified by us (we have to have coded another thread for this?
        //Why palette is not generated in UI thread? Palette generation can take time and we don't want to hog and block the UI thread!
        //Here we will do it asynchronously

        //Max count value:
        //higher the max color count... the higher the chance u will find a color for each and every swatch and vice versa
        //higher the max color count... longer the time the palette will take to be generated and vice versa
        //default max color count val is 16
        //recommended max count value:
            //>use something betw 8 and 16 for landscape images
            //>higher value... something between 24 to 32 for images of faces

        Palette.from(bitmap).maximumColorCount(32).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                vibrantSwatch = palette.getVibrantSwatch();
                lightVibrantSwatch = palette.getLightVibrantSwatch();
                darkVibrantSwatch = palette.getDarkVibrantSwatch();
                mutedSwatch = palette.getMutedSwatch();
                lightMutedSwatch = palette.getLightMutedSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
            }
        });
    }

    public void nextSwatch(View v) {
        Palette.Swatch currentSwatch = null;

        switch(swatchNum) {
            case 0:
                currentSwatch = vibrantSwatch;
                tvTitle.setText("Vibrant");
                break;

            case 1:
                currentSwatch = lightVibrantSwatch;
                tvTitle.setText("Light Vibrant");
                break;

            case 2:
                currentSwatch = darkVibrantSwatch;
                tvTitle.setText("Dark Vibrant");
                break;

            case 3:
                currentSwatch = mutedSwatch;
                tvTitle.setText("Muted");
                break;

            case 4:
                currentSwatch = lightMutedSwatch;
                tvTitle.setText("Light Muted");
                break;

            case 5:
                currentSwatch = darkVibrantSwatch;
                tvTitle.setText("Dark Muted");
                break;
        }

        if(currentSwatch != null) {
            rootLayout.setBackgroundColor(currentSwatch.getRgb());
            tvTitle.setTextColor(currentSwatch.getTitleTextColor());
            tvBody.setTextColor(currentSwatch.getBodyTextColor());

            //Title text color is just a lighter version of the body text color cuz we dont need such an opaque color for the title cuz
            //the title font is definitely bigger than the body font
        }
        else {
            rootLayout.setBackgroundColor(Color.BLACK);
            tvTitle.setTextColor(Color.WHITE);
            tvBody.setTextColor(Color.WHITE);

            Toast.makeText(getBaseContext(), "Unable to obtain color palette for current swatch", Toast.LENGTH_SHORT).show();
        }

        if(swatchNum <5) {
            swatchNum++;
        } else {
            swatchNum = 0;
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BTN_next_swatch_button:
                nextSwatch(view);
                break;
            case R.id.BTN_next_image_button:
                if(imageNum < 4) {
                    imageNum++;
                    switch(imageNum) {
                        case 0:
                            ivTestImage.setImageDrawable(getResources().getDrawable(R.drawable.pic1));
                            break;
                        case 1:
                            ivTestImage.setImageDrawable(getResources().getDrawable(R.drawable.pic2));
                            break;
                        case 2:
                            ivTestImage.setImageDrawable(getResources().getDrawable(R.drawable.pic3));
                            break;
                        case 3:
                            ivTestImage.setImageDrawable(getResources().getDrawable(R.drawable.pic4));
                    }
                } else {
                    imageNum = 0;
                    ivTestImage.setImageDrawable(getResources().getDrawable(R.drawable.pic1));
                }
        }
    }
}