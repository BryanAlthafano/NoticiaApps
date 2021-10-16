package com.example.noticiaapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticiaapp.Api.ApiClient;
import com.example.noticiaapp.Api.ApiInterface;
import com.example.noticiaapp.Model.Login.Login;
import com.example.noticiaapp.Model.Login.LoginData;
import com.example.noticiaapp.R;
import com.example.noticiaapp.Session.Session;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton btn_login;
    TextView link_buttom_text;
    ApiInterface apiInterface;
    String username, password;
    EditText et_username, et_password;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Button SignIn
        btn_login = findViewById(R.id.btn_sign_in);
        btn_login.setOnClickListener(this);

        //TextView Bottom SignUp
        link_buttom_text = findViewById(R.id.link_bottom_text);
        link_buttom_text.setOnClickListener(this);

        //EditText
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                signIn(username, password);
                break;
            case R.id.link_bottom_text:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
        }
    }

    private void signIn(String username, String password) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username, password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {

                    //Menyimpan Session
                    session = new Session(SignIn.this);
                    LoginData loginData = response.body().getData();
                    session.createLoginSession(loginData);

                    //Move
                    Toast.makeText(SignIn.this, "Selamat Datang "+ response.body().getData().getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignIn.this, Home.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignIn.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(SignIn.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}