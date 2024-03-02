package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login=findViewById(R.id.buttonLogin);
        ImageButton back=findViewById(R.id.btnBack);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        back.setVisibility(View.INVISIBLE);
        passwordEditText.setTextIsSelectable(false);
        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        showPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If the checkbox is checked, show the password, otherwise hide it
                if (isChecked) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}