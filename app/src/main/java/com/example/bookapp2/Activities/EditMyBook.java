package com.example.bookapp2.Activities;

import static com.example.bookapp2.Fragment.CategoryMyStore.acct;
import static com.example.bookapp2.Fragment.CategoryMyStore.bookid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp2.Model.Book;
import com.example.bookapp2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class EditMyBook extends AppCompatActivity {

    Book bookdetail;
    public static final int PICK_IMAGE = 1;
    Bundle bundle;
    EditText titile, author, priceBook, desciptionEdit,tvQuantity;
    ImageView cover;
    private Uri imageUri; // Đường dẫn của ảnh đã chọn
    int position;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_book);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        progressBar = findViewById(R.id.progressBar);

// Ẩn ProgressBar
        progressBar.setVisibility(View.GONE);
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
        tvQuantity = findViewById(R.id.itemQuanEt);
        cover = findViewById(R.id.cover_iv_details);

        Button btnMinus = findViewById(R.id.removeBtn);
        Button btnPlus = findViewById(R.id.addBtn);
        tvQuantity = findViewById(R.id.itemQuanEt);



        titile.setText(bookdetail.getBookName());
        author.setText(bookdetail.getAuthorName());
        priceBook.setText(bookdetail.getPrice());
        desciptionEdit.setText(bookdetail.getDescription());
        tvQuantity.setText(bookdetail.getQuantity());
        Picasso.get().load(bookdetail.getImageUrl()).into(cover);

        final int[] quantity = {Integer.parseInt(bookdetail.getQuantity())};

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

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        Button update = findViewById(R.id.update_book);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String bookName = titile.getText().toString();
                String authorName = author.getText().toString();
                String price = priceBook.getText().toString();
                String description = desciptionEdit.getText().toString();
                String quantityB = tvQuantity.getText().toString();

                if(imageUri == null){
                    Book newBook = new Book(bookName, authorName, price, description,bookdetail.getImageUrl(),quantityB);
                    DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books").child(acct.getId());

                    booksRef.child(bookid.get(position)).setValue(newBook);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditMyBook.this,"Success",Toast.LENGTH_LONG).show();

                }
                else {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imagesRef = storageRef.child("images/" + UUID.randomUUID().toString()); // Tạo tên duy nhất cho ảnh

                    imagesRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Lấy URL của ảnh từ Firebase Storage
                                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    // Tạo đối tượng sách và thêm vào Firebase Realtime Database
                                    Book newBook = new Book(bookName, authorName, price, description,imageUrl,quantityB);
                                    DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books").child(acct.getId());

                                    booksRef.child(bookid.get(position)).setValue(newBook);
                                });
                            })
                            .addOnFailureListener(e -> {
                                // Xử lý lỗi khi tải ảnh lên Firebase Storage
                            });
                    Toast.makeText(EditMyBook.this,"Success",Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);

                }


            }
        });

        ImageView delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            cover.setImageURI(imageUri);

        }
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa cuốn sách này không?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Xóa cuốn sách từ Firebase ở đây
                deleteBook(bookid.get(position));
                onBackPressed();
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
    private void deleteBook(String bookId) {
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference().child("books").child(acct.getId());
        booksRef.child(bookId).removeValue();
    }
}