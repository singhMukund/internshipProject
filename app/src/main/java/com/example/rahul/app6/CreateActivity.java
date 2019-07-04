package com.example.rahul.app6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
    }

    public void showOrderDetails(View view) {
        String value = "Order Details with Order ID " + String.valueOf(((EditText)findViewById(R.id.editText3)).getText()) + " Created at " + String .valueOf(((EditText)findViewById(R.id.editText6)).getText() + " by Username XYZABCD PANDEY.");
        Intent intent2 = new Intent(this, OrderActivity.class);
        intent2.putExtra("msg", value);
        startActivity(intent2);
    }
}
