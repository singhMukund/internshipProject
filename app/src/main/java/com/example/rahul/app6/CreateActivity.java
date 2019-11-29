package com.example.rahul.app6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CreateActivity extends AppCompatActivity {
    private EditText orderID, sellerInfo, orderDate, orderDesc, orderAmount, paymentDue;
    private ProgressDialog mProgressDialog;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        orderID = (EditText)findViewById(R.id.editText3);
        sellerInfo = (EditText)findViewById(R.id.editText5);
        orderDate = (EditText) findViewById(R.id.editText6);
        orderDesc = (EditText)findViewById(R.id.editText7);
        orderAmount = (EditText)findViewById(R.id.editText8);
        paymentDue = (EditText)findViewById(R.id.editText9);

        mProgressDialog = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(CreateActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void saveOrderData(View view) {
        String orderid = orderID.getText().toString().trim();
        String sellerinfo = sellerInfo.getText().toString().trim();
        String orderdate = orderDate.getText().toString().trim();
        String orderdesc = orderDesc.getText().toString().trim();
        String amount = orderAmount.getText().toString().trim();
        String due = paymentDue.getText().toString().trim();

        if(TextUtils.isEmpty(orderid)){
            Toast.makeText(CreateActivity.this, "OrderID Field can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(sellerinfo)){
            Toast.makeText(CreateActivity.this, "Seller Info Field can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(orderdate)){
            Toast.makeText(CreateActivity.this, "Date Field can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(amount) || !(TextUtils.isDigitsOnly(amount))){
            Toast.makeText(CreateActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            orderAmount.setText("");
            return;
        }else if(TextUtils.isEmpty(orderdesc)) {
            Toast.makeText(CreateActivity.this, "Description can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(due)|| !(TextUtils.isDigitsOnly(due))) {
            Toast.makeText(CreateActivity.this, "Invalid Input", Toast.LENGTH_SHORT).show();
            paymentDue.setText("");
            return;
        }
        int a = Integer.parseInt(amount);
        int b = Integer.parseInt(due);
        if(b > a) {
            Toast.makeText(this, "Payment Due can't be greater than Order Amount.", Toast.LENGTH_SHORT).show();
            paymentDue.setText("");
            return;
        }

        mProgressDialog.setMessage("Saving Order...");
        mProgressDialog.show();

        ref.child("SellerInfo").child(orderid).setValue(sellerinfo);
        ref.child("Date").child(orderid).setValue(orderdate);
        ref.child("Description").child(orderid).setValue(orderdesc);
        ref.child("Amount").child(orderid).setValue(amount);
        ref.child("PaymentDue").child(orderid).setValue(due);

        mProgressDialog.hide();

        Intent i = new Intent(this, OrderActivity.class);
        String message = "Order with Order ID " + orderid + " created at " + orderdate + " by ";
        i.putExtra("msg", message);
        i.putExtra("id", orderid);
        startActivity(i);
        finish();
    }
}
