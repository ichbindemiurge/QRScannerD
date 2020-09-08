package com.example.demiurge.qrscannerd;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by demiurge on 06.02.18.
 */

public class CustomTextWatcher implements TextWatcher{

    public String customName;
    private String customPhone;
    private String customphoneError;
    private String customEmail;
    private String customText;
    public Boolean nameRes = true;

    private EditText editText;
    private TextInputLayout errorText;
    private Button genButton;




    CustomTextWatcher(View view, View errorView, Button button)
    {
        if (view instanceof EditText)
        {
            this.editText = (EditText) view;
        }

        if (errorView instanceof TextInputLayout)
        {
            this.errorText = (TextInputLayout) errorView;
        }

        else {
            throw new ClassCastException(
                    "view must be an instance Of EditText");
        }

        this.genButton = (Button) button;

    }

    private void buttonCheck(Boolean btnState)
    {
        if (btnState)
        {
            genButton.setEnabled(true);
        }
        else
        {
            genButton.setEnabled(false);
        }
    }

    private void editTextsIds(int id)
    {
        if(id == R.id.nameText)
        {
            customName = editText.getText().toString();

            if (!customName.isEmpty())
            {
                errorText.setError(null);
                genButton.setEnabled(true);
                Log.d("TEXT_WATCHER", "NAME_CHECK_PASSED");
            }
            else
            {
                errorText.setError("Name field can't be empty");
                genButton.setEnabled(false);
                Log.d("NAME_LISTENER", "NAME_CHECK_NOT_PASSED");

            }
        }

        if(id == R.id.phoneText)
        {
            customPhone = editText.getText().toString();

            if(!customPhone.isEmpty() && android.util.Patterns.PHONE.matcher(customPhone).matches())
            {
                errorText.setError(null);
                genButton.setEnabled(true);
                Log.d("TEXT_WATCHER", "Phone_Passed");
               // genButton.setEnabled(true);

            }
            else
            {
                errorText.setError("Wrong phone format");
                genButton.setEnabled(false);
                Log.d("TEXT_WATCHER", "Phone_Not_Passed");
            }

        }

        if( id == R.id.emailText)
        {
            customEmail = editText.getText().toString();

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(customEmail).matches())
            {
                errorText.setError(null);
                genButton.setEnabled(true);
                Log.d("TEXT_WATCHER", "EMAIL_PASSED");
            }
            else
            {
                errorText.setError("Wrong email format");
                genButton.setEnabled(false);
                Log.d("TEXT_WATCHER", "EMAIL_NOT_PASSED");
            }
        }

    }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {



        int id = editText.getId();
        editTextsIds(id);


        }





}
