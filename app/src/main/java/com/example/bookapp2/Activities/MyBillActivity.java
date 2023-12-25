package com.example.bookapp2.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Adapter.MyBillAdapter;
import com.example.bookapp2.Model.Order;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBillActivity extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    public static GoogleSignInAccount acct;
    public  static ArrayList<String> bookbill = new ArrayList<>();
    ArrayList<Order> list = new ArrayList<>();

    MyBillAdapter adapterItem;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        acct = GoogleSignIn.getLastSignedInAccount(this);

        recyclerView = findViewById(R.id.recyclerView);
        showData();
    }

    private void showData() {
        database.child(acct.getId()).child("bills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            Order book = item.getValue(Order.class);
            bookbill.add(item.getKey());
            list.add(book);
        }
        adapterItem = new MyBillAdapter(MyBillActivity.this, list);
        recyclerView.setAdapter(adapterItem);
    }
}