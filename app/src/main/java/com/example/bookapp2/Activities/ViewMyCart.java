package com.example.bookapp2.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Adapter.MyCartAdapter;
import com.example.bookapp2.Model.Book;
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

public class ViewMyCart extends AppCompatActivity implements MyCartAdapter.OnTotalPriceChangedListener{
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    public static GoogleSignInAccount acct;
    public  static ArrayList<String> bookcart = new ArrayList<>();

    MyCartAdapter adapterItem;

    RecyclerView recyclerView;
    ArrayList<Book> list = new ArrayList<>();

    Double total;



    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_cart);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        acct = GoogleSignIn.getLastSignedInAccount(this);

        recyclerView = findViewById(R.id.recyclerView);
        showData();

        CheckBox allCheckBox = findViewById(R.id.all);
        allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RecyclerView recyclerView = findViewById(R.id.recyclerView);

                MyCartAdapter adapter = (MyCartAdapter) recyclerView.getAdapter();

                adapter.checkAllItems(isChecked);
                updateTotalPrice();

            }
        });

        ImageView arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ImageView deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMyCart.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MyCartAdapter adapter = (MyCartAdapter) recyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.deleteSelectedItems(); // Xóa các mục đã chọn từ Firebase
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Người dùng chọn hủy, không làm gì cả
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button buy = findViewById(R.id.buying);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBillAndRemoveItems();
            }
        });

    }
    public void createBillAndRemoveItems() {
        // Tạo AlertDialog để nhập thông tin địa chỉ và số điện thoại
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập thông tin");

        // Sử dụng layout custom hoặc các thành phần (EditText, TextView, etc.) để người dùng nhập địa chỉ và số điện thoại
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText addressEditText = view.findViewById(R.id.etAddress);
                EditText phoneEditText = view.findViewById(R.id.etPhone);

                String address = addressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                // Tạo một mục bill mới trên Firebase
                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(acct.getId())
                        .child("bills")
                        .push(); // Tạo mục con mới

                // Thêm thông tin vào mục bill
                billRef.child("address").setValue(address);
                billRef.child("phone").setValue(phone);
                billRef.child("sum").setValue(total);

                MyCartAdapter adapter = (MyCartAdapter) recyclerView.getAdapter();

                billRef.child("items").setValue(adapter.getSelectedItems(address,phone)); // Đặt giá trị các mục đã chọn vào bill

                // Xóa các mục đã chọn khỏi cart
                adapter.deleteSelectedItems();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showData() {
        database.child(acct.getId()).child("cart").addValueEventListener(new ValueEventListener() {
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
            Book book = item.getValue(Book.class);
            bookcart.add(item.getKey());
            list.add(book);
        }
        adapterItem = new MyCartAdapter(ViewMyCart.this, list);
        adapterItem.setOnTotalPriceChangedListener(this); // "this" là instance của Activity hoặc Fragment

        recyclerView.setAdapter(adapterItem);
    }

    @Override
    public void onTotalPriceChanged(double totalPrice) {
        TextView sumTextView = findViewById(R.id.sum);
        total = totalPrice;
        sumTextView.setText(String.valueOf(totalPrice));
    }
    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        TextView sumTextView = findViewById(R.id.sum);
        total = totalPrice;
        sumTextView.setText(String.valueOf(totalPrice));
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Book book : list) {
            if (book.isSelected()) {
                total += Double.parseDouble(book.getPrice());
            }
        }
        return total;
    }
}