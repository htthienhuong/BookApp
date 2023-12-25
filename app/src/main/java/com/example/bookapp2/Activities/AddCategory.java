package com.example.bookapp2.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddCategory extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    ImageView cover;
    private Uri imageUri; // Đường dẫn của ảnh đã chọn
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    EditText titile, author, priceBook, desciptionEdit,tvQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        cover = findViewById(R.id.cover_iv_details);

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        Button btnMinus = findViewById(R.id.removeBtn);
        Button btnPlus = findViewById(R.id.addBtn);
        tvQuantity = findViewById(R.id.itemQuanEt);

        final int[] quantity = {0};

        btnMinus.setOnClickListener(view -> {
            if (quantity[0] > 0) {
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        btnPlus.setOnClickListener(view -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
        });

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        titile = findViewById(R.id.title_tv_details);
        author = findViewById(R.id.authors_tv_details);
        priceBook = findViewById(R.id.price_tv_details);
        desciptionEdit = findViewById(R.id.description_tv_details);


        Button addBook = findViewById(R.id.add_book);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName = titile.getText().toString();
                String authorName = author.getText().toString();
                String price = priceBook.getText().toString();
                String description = desciptionEdit.getText().toString();
                String quantityB = tvQuantity.getText().toString();


                uploadImageToFirebaseStorage(imageUri, bookName, authorName, price, description,quantityB,acct.getId());
                Toast.makeText(AddCategory.this,"Success",Toast.LENGTH_LONG).show();
                onBackPressed();

            }
        });



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            cover.setImageURI(imageUri);

        }
    }
    private void uploadImageToFirebaseStorage(Uri imageUri, String bookName, String authorName, String price, String description,String quantity ,String userID) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images/" + UUID.randomUUID().toString()); // Tạo tên duy nhất cho ảnh

        imagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL của ảnh từ Firebase Storage
                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Tạo đối tượng sách và thêm vào Firebase Realtime Database
                        Book newBook = new Book(bookName, authorName, price, description,imageUrl,quantity);
                        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books").child(userID);
                        booksRef.push().setValue(newBook);
                    });
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi tải ảnh lên Firebase Storage
                });

    }
}