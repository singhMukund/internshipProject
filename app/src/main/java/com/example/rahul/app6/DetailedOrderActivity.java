package com.example.rahul.app6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailedOrderActivity extends AppCompatActivity {
    private Intent intent;
    private String orderID;
    private TextView id, seller, date, amount, due, description;
    FirebaseDatabase database;
    DatabaseReference reference;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_order);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        id = (TextView) findViewById(R.id.textView26);
        seller = (TextView) findViewById(R.id.textView27);
        date = (TextView) findViewById(R.id.textView28);
        amount = (TextView) findViewById(R.id.textView29);
        due = (TextView) findViewById(R.id.textView30);
        description = (TextView) findViewById(R.id.textView31);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        mProgressDialog = new ProgressDialog(DetailedOrderActivity.this);

        intent = getIntent();
        orderID = intent.getStringExtra("id");
        id.setText(orderID);

        mProgressDialog.setMessage("Loading Data...");
        mProgressDialog.show();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setSeller();
            setDate();
            setAmount();
            setDue();
            setDescription();
        }
        mProgressDialog.hide();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            case R.id.update_order:
                Intent i = new Intent(DetailedOrderActivity.this, UpdateActivity.class);
                i.putExtra("id",orderID);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

    public void setSeller() {
        reference.child("SellerInfo").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                seller.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DetailedOrderActivity", "Cancelled Msg");
            }
        });
    }
    public void setDate() {
        reference.child("Date").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                date.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("DetailedOrderActivity", "Back Button pressed");
            }
        });
    }
    public void setAmount() {
        reference.child("Amount").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                amount.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailedOrderActivity.this, "Database Error, Failed to Read Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setDue() {
        reference.child("PaymentDue").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                due.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailedOrderActivity.this, "Database Error, Failed to Read Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setDescription() {
        reference.child("Description").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                description.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailedOrderActivity.this, "Database Error, Failed to Read Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
