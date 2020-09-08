package com.example.demiurge.qrscannerd;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.NotFoundException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;

import java.util.ArrayList;
import java.util.Locale;

public class MeCardResultHandlerActivity extends AppCompatActivity {

    private Button addContact;

    public static ParsedResult parsedResult;
    Intent intent;
    LinearLayout linearLayout;

    public static final String[] PHONE_KEYS = {
            ContactsContract.Intents.Insert.PHONE,
            ContactsContract.Intents.Insert.SECONDARY_PHONE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE
    };

    public static final String[] PHONE_TYPE_KEYS = {
            ContactsContract.Intents.Insert.PHONE_TYPE,
            ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
            ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
    };

    public static final String[] EMAIL_KEYS = {
            ContactsContract.Intents.Insert.EMAIL,
            ContactsContract.Intents.Insert.SECONDARY_EMAIL,
            ContactsContract.Intents.Insert.TERTIARY_EMAIL
    };



    public MeCardResultHandlerActivity()
    {

    }

    public ParsedResult getParsedResult() {
        return parsedResult;
    }

    public void setParsedResult(ParsedResult parsedResult) {
        this.parsedResult = parsedResult;
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_card_result_handler);

        Toolbar toolbarHistory = (Toolbar) findViewById(R.id.read_app_bar);
        setSupportActionBar(toolbarHistory);

        ActionBar backButton = getSupportActionBar();
        backButton.setDisplayHomeAsUpEnabled(true);

        addContact = (Button) findViewById(R.id.add_contact_button_mecard);
        linearLayout = (LinearLayout) findViewById(R.id.linear_lay);
        linearLayout.setOrientation(LinearLayout.VERTICAL);



        fillInMecardFields();

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    TextView createATextView(int layout_widh, int layout_height, int align,
                             String text, int fontSize, int textStyle, int margin, int padding) {

        TextView textView_item_name = new TextView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT;
        RelativeLayout.LayoutParams _params = new RelativeLayout.LayoutParams(
                layout_widh, layout_height);

        _params.setMargins(margin, margin, margin, margin);
        _params.addRule(align);
        textView_item_name.setLayoutParams(_params);

        textView_item_name.setText(text);
        textView_item_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        textView_item_name.setTextColor(Color.parseColor("#000000"));
        // textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        textView_item_name.setPadding(padding, padding, padding, padding);
        textView_item_name.setTypeface(null, textStyle);

        return textView_item_name;

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    final void addContact(String[] names, String[] phoneNumbers, String[] emails, String note,
                          String address, String org, String title) {


        intent = new Intent(ContactsContract.Intents.Insert.ACTION, Contacts.People.CONTENT_URI);
        putExtra(intent, ContactsContract.Intents.Insert.NAME, names != null ? names[0] : null);
        //contactName.setText(names != null ? names[0] : null);
        //contactName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
               // LinearLayout.LayoutParams.WRAP_CONTENT));
        //linearLayout.addView(contactName);
        linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                names != null ? names[0] : null, 20, 1, 10, 20));



        int phoneCount = Math.min((phoneNumbers != null) ? phoneNumbers.length : 0,
                PHONE_KEYS.length);
        for (int x = 0; x < phoneCount; x++) {
            putExtra(intent, PHONE_KEYS[x], phoneNumbers[x]);
            //phoneNumMeCard.setText(phoneNumbers[x]);
            linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                    phoneNumbers[x], 20, 0,10, 20));

        }

        int emailCount = Math.min((emails != null) ? emails.length : 0, EMAIL_KEYS.length);
        for (int x = 0; x < emailCount; x++) {
            putExtra(intent, EMAIL_KEYS[x], emails[x]);
            //emailMecard.setText(emails[x]);

            linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                    emails[x], 20, 0, 10, 20));

        }

        putExtra(intent, ContactsContract.Intents.Insert.NOTES, note);
        //noteMecard.setText(note);

        putExtra(intent, ContactsContract.Intents.Insert.POSTAL, address);
        //streetMecard.setText(address);

        putExtra(intent, ContactsContract.Intents.Insert.COMPANY, org);
        //companyName.setText(org);

        putExtra(intent, ContactsContract.Intents.Insert.JOB_TITLE, title);
        //titleMecard.setText(title);

        linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                note, 20, 0, 10, 20));
        linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                address, 20, 0, 10, 20));
        linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                org, 20,0, 10, 20));
        linearLayout.addView(createATextView(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT,
                title, 20, 0, 10, 20));



    }



    private static void putExtra(Intent intent, String key, String value) {
        if (value != null && !value.isEmpty()) {
            intent.putExtra(key, value);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void fillInMecardFields()
    {
        AddressBookParsedResult mecard = (AddressBookParsedResult) parsedResult;
        Log.d("checkparser", mecard.getDisplayResult());

        String[] addresses = mecard.getAddresses();
        String address1 = addresses == null || addresses.length < 1 ? null : addresses[0];

        addContact(mecard.getNames(), mecard.getPhoneNumbers(),
                mecard.getEmails(), mecard.getNote(),
                address1, mecard.getOrg(),
                mecard.getTitle());

    }

}
