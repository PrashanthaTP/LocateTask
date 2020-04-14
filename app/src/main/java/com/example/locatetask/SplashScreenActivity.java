package com.example.locatetask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SplashScreenActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE,MODE_PRIVATE);

        if(sharedPreferences.getBoolean(AppConstantsHelper.APP_OPENED_FIRST_TIME,Boolean.TRUE))
        {

            setContentView(R.layout.intro_screen);

            MaterialButton startButton = findViewById(R.id.startAppButton);
            final TextInputEditText userNameEditText = findViewById(R.id.userNameEdiText);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = userNameEditText.getText().toString().trim();
                    if(userName.length()==0)
                    {
                        CustomMessages.showSnackBar(v,"Please enter your Name");
                    }
                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(AppConstantsHelper.APP_OPENED_FIRST_TIME,Boolean.FALSE);
                        editor.apply();
                        storeUserName(userName);
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        finish();
                    }
                }
            });
        }
        else{
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }
    private void storeUserName(String userName)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstantsHelper.APP_SPECIFIC_SHARED_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstantsHelper.USERNAME,userName);
        editor.apply();
    }
}
