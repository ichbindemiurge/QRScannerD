package com.example.demiurge.qrscannerd;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.TimeZoneFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LinkResultActivity extends AppCompatActivity {
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LINK_RESULT_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_result);

        ImageView linkResView = (ImageView) findViewById(R.id.link_result_view);
        shareButton = (Button) findViewById(R.id.share_button);

        //byte[] byteArray = getIntent().getByteArrayExtra("image");
        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

       // Bundle resultBVC = getIntent().getBundleExtra("vcard");
        // String MECARDRES = getIntent().getStringExtra("mecard");

        byte[] byteArray = getIntent().getByteArrayExtra("showQrRes");
        Bitmap bitmapMeC = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        //Drawable drawable = new BitmapDrawable(getResources(), bmp);
        //linkResView.setImageDrawable(drawable);

        Bitmap emptyBitmap = Bitmap.createBitmap(bitmapMeC.getWidth(), bitmapMeC.getHeight(), bitmapMeC.getConfig());

       if(bitmapMeC.sameAs(emptyBitmap)) {
           Log.d("Bitmap", "empty");
        }
       else
        {
           Log.d("Bitmap Image", "Contains something");

        }


        linkResView.setImageBitmap(bitmapMeC);



       shareButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               shareBitmapImage();
           }
       });
    }


    
    private void shareBitmapImage()
    {
        File filePath = new File(getOutputMediaFile().toString());
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = filePath.getName().substring(filePath.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        sharingIntent.setType(type);
        sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(filePath));
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    private void shareIt() {
        File filePath = new File(getOutputMediaFile().toString());
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");

        File storageDirectory = new File(Environment.getExternalStorageDirectory() + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files" + "/Barcode_Image" + "CustomName" + ".jpg");

        Uri uri = Uri.fromFile(storageDirectory);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }



    public void saveBitmap(Bitmap bitmap)
    {
        File bitmapImageFile = getOutputMediaFile();

        if(bitmapImageFile == null)
        {
            Log.d("LINK_RES_AC", "Error creating media file. Check storage permissions");
            return;
        }

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(bitmapImageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (FileNotFoundException fnEx){
            Log.d("LINK_RES_ACT", "FILE NOT FOUND" + fnEx.getMessage());
        }
        catch (IOException ioEX){
            Log.d("LINK_RES_ACT", "Error accessing file" + ioEX.getMessage());
        }

    }


    private File getOutputMediaFile()
    {
        File storageDirectory = new File(Environment.getExternalStorageDirectory() + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!storageDirectory.exists()){
            if (!storageDirectory.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy__hh:mm:ss").format(new Date());
        //DateFormat timeStamp = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        //String dateString = timeStamp.format(new Date());

        File mediaFile;
        String bImageName = "Barcode_Image_" + timeStamp + ".jpg";
        mediaFile = new File(storageDirectory.getPath() + File.separator + bImageName);
        return mediaFile;

    }









}
