package com.example.rahul.app6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {
    private Spinner spinner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private Intent i;
    private EditText newVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        newVal = (EditText)findViewById(R.id.editText14);

        i = getIntent();
        spinner = (Spinner)findViewById(R.id.spinner2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);

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

    public void updateOrder(View view) {
        int count = 0;
        String orderID = i.getStringExtra("id");
        String newValue = newVal.getText().toString().trim();
        String field = String.valueOf(spinner.getSelectedItem());
        if (field.equals("Choose Field")) {
            Toast.makeText(this, "Choose Field to Update", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newValue.equals("")) {
            Toast.makeText(this, "Empty Input Field", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (field) {
            case "Seller Info":
                mReference.child("SellerInfo").child(orderID).setValue(newValue);
                count++;
                break;
            case "Order Description":
                mReference.child("Description").child(orderID).setValue(newValue);
                count++;
                break;
            case "Payment Due":
                if (!(TextUtils.isDigitsOnly(newValue))) {
                    Toast.makeText(this, "Only Digits are Allowed", Toast.LENGTH_SHORT).show();
                    newVal.setText("");
                    break;
                } else {
                    mReference.child("PaymentDue").child(orderID).setValue(newValue);
                    count++;
                    break;
                }
            case "Order Date":
                mReference.child("Date").child(orderID).setValue(newValue);
                count++;
                break;
        }
        if(count > 0) {
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent i2 = new Intent(UpdateActivity.this, DetailedOrderActivity.class);
            i2.putExtra("id", orderID);
            startActivity(i2);
            finish();
        }
    }
}
