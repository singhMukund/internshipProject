package com.example.rahul.app6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private static EditText searchbar;
    private static TextView tv[] = new TextView[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        searchbar = (EditText)findViewById(R.id.editText2);
        for(int i =0; i < 4; i++) {
            tv[0] = (TextView)findViewById(R.id.textView3);
            tv[1] = (TextView)findViewById(R.id.textView32);
            tv[2] = (TextView)findViewById(R.id.textView33);
            tv[3] = (TextView)findViewById(R.id.textView34);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return true;
    }

    public void openOrder(View view) {
        String searchID = searchbar.getText().toString().trim();
        if(TextUtils.isEmpty(searchID)) {
            Toast.makeText(this,"Search Field Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Stack<String> st = new Stack<String>();
        stack_push(st);
        try {
            stack_pop(st);
        }catch (EmptyStackException e) {
            Log.i("MainActivity", "Empty Stack Msg");
        }


        Intent i = new Intent(getApplicationContext(), DetailedOrderActivity.class);
        i.putExtra("id", searchID);
        startActivity(i);
    }

    static void stack_push(Stack<String> stack)
    {
        stack.push("OrderID - " + searchbar.getText().toString().trim());
    }

    static void stack_pop(Stack<String> stack)
    {
        for(int i=0; i < 4; i++) {
            tv[i].setText((i+1) + ". " + stack.pop());
        }
    }
    public void createOrder(View view) {
        startActivity(new Intent(getApplicationContext(), CreateActivity.class));
    }
}
