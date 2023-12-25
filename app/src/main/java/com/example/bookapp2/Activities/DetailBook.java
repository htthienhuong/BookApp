package com.example.bookapp2.Activities;

import static com.example.bookapp2.Fragment.HomeFragment.bookid;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp2.Model.Book;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailBook extends AppCompatActivity {
    Book bookdetail;
    Bundle bundle;
    int position;
    TextView titile, author, priceBook, desciptionEdit,tvQuantity;
    ImageView cover;
    EditText selectQuantity;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        bookdetail = (Book) bundle.get("object_book");
        position =  getIntent().getIntExtra("posBookId",0);

        titile = findViewById(R.id.title_tv_details);
        author = findViewById(R.id.authors_tv_details);
        priceBook = findViewById(R.id.price_tv_details);
        desciptionEdit = findViewById(R.id.description_tv_details);
        tvQuantity = findViewById(R.id.amount);
        cover = findViewById(R.id.cover_iv_details);

        titile.setText(bookdetail.getBookName());
        author.setText(bookdetail.getAuthorName());
        priceBook.setText(bookdetail.getPrice());
        desciptionEdit.setText(bookdetail.getDescription());
        tvQuantity.setText(bookdetail.getQuantity());
        Picasso.get().load(bookdetail.getImageUrl()).into(cover);

        Button btnMinus = findViewById(R.id.removeBtn);
        Button btnPlus = findViewById(R.id.addBtn);

        selectQuantity = findViewById(R.id.itemQuanEt);
        final int[] quantity = {1};

        btnMinus.setOnClickListener(view -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                selectQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        btnPlus.setOnClickListener(view -> {
            if(quantity[0] < Integer.parseInt(bookdetail.getQuantity())){
                quantity[0]++;
                selectQuantity.setText(String.valueOf(quantity[0]));
            }
        });


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        Button addToCart = findViewById(R.id.add_to_card);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double sum = Double.parseDouble(bookdetail.getPrice())*Double.parseDouble(selectQuantity.getText().toString());

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("bookName",""+bookdetail.getBookName());
                hashMap.put("quantity",""+selectQuantity.getText().toString());
                hashMap.put("price",""+sum);
                hashMap.put("imageUrl",""+bookdetail.getImageUrl());

                DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("users").child(acct.getId());
                booksRef.child("cart").child(bookid.get(position)).setValue(hashMap);
                Toast.makeText(DetailBook.this,"Success to add to cart",Toast.LENGTH_LONG).show();

            }
        });

    }
}