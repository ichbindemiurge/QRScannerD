package com.example.demiurge.qrscannerd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.*;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class EncodingActivity extends AppCompatActivity {

    private String taglogMsg = "BMP_ENCODING_ACTIVITY";
    static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";



    public void createBarcode (String qrToEncode) throws WriterException {

        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);

       // String encoding = DEFAULT_BYTE_MODE_ENCODING;
        //com.google.zxing.common.CharacterSetECI eci = CharacterSetECI.UTF8;
        //hints.put(EncodeHintType.CHARACTER_SET, eci);

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //hints.put(EncodeHintType.MARGIN, 1);


        BitMatrix bMatrix = null;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();


        bMatrix = multiFormatWriter.encode(qrToEncode, BarcodeFormat.QR_CODE, 200, 200, hints);


        Bitmap imageBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
        for (int y = 0; y < 200; y++){
            for (int x = 0; x < 200; x++){
                imageBitmap.setPixel(x, y, bMatrix.get(x,y) ? BLACK : WHITE);
            }
        }

        Bitmap emptyBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
        if (imageBitmap.sameAs(emptyBitmap)) {
            //throw new IllegalArgumentException("Found empty contents");
        }
        else{
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent encodedLink = new Intent(EncodingActivity.this, LinkResultActivity.class);
        encodedLink.putExtra("showQrRes",byteArray);
        startActivity(encodedLink);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ENCODING_ACTIVITY", "ON_CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoding);

        String toEncode = getIntent().getStringExtra("qrencode");
        

        if(!toEncode.isEmpty()) {

            try {
                createBarcode(toEncode);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
    }
}
