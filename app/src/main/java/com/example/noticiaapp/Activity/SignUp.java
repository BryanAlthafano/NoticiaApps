package com.example.noticiaapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticiaapp.Api.ApiClient;
import com.example.noticiaapp.Api.ApiInterface;
import com.example.noticiaapp.Model.Register.Register;
import com.example.noticiaapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    TextView link_buttom_text;
    EditText et_fullname, et_username, et_email, et_password;
    TextInputLayout inputFullName, input_Username, input_Email, input_Password;
    AppCompatButton btn_signUp;
    String fullname, username, email, password;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //TextView Bottom SignIn
        link_buttom_text = findViewById(R.id.link_bottom_text);
        link_buttom_text.setOnClickListener(this);

        //TextInputLayout
        inputFullName = findViewById(R.id.text_input_fullname);
        input_Username = findViewById(R.id.text_input_username);
        input_Email = findViewById(R.id.text_input_email);
        input_Password = findViewById(R.id.text_input_password);

        //EditText
        et_fullname = findViewById(R.id.et_fullname);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        //Button SignUp
        btn_signUp = findViewById(R.id.btn_sign_up);
        btn_signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                fullname = et_fullname.getText().toString();
                username = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                signUp(fullname, username, email, password);
                break;
            case R.id.link_bottom_text:
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void signUp(String fullname, String username, String email, String password) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Register> registerCall = apiInterface.registerResponse(fullname, username, email, password);
        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                    Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(SignUp.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}