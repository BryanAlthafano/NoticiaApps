package com.example.noticiaapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noticiaapp.R;
import com.example.noticiaapp.Session.Session;

public class Profile extends AppCompatActivity {

    Session session;
    TextView tv_fullname, tv_username, tv_email;
    String fullname, username, email;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Session
        session = new Session(Profile.this);

        //TextView
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);

        //Show Data
        fullname = session.getUser().get("fullname");
        username = session.getUser().get("username");
        email = session.getUser().get("email");

        tv_fullname.setText(fullname);
        tv_username.setText(username);
        tv_email.setText(email);

        //Button Back
        btn_back = findViewById(R.id.iv_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Home.class);
                startActivity(intent);
            }
        });
    }

}