package com.example.bookapp2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Activities.AddCategory;
import com.example.bookapp2.Activities.MyOrdersScreen;
import com.example.bookapp2.Adapter.MyCategoryAdapter;
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


public class CategoryMyStore extends Fragment {
    ArrayList<Book> list = new ArrayList<>();

    public  static ArrayList<String> bookid = new ArrayList<>();

    MyCategoryAdapter adapterItem;
    RecyclerView recyclerView;

    View view;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    public static GoogleSignInAccount acct;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("books");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_my_store, container, false);

        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.my_category);

        com.google.android.material.button.MaterialButton add = view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddCategory.class));
            }
        });
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(view.getContext(),googleSignInOptions);

        acct = GoogleSignIn.getLastSignedInAccount(view.getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        showData();

        ImageView notificate = view.findViewById(R.id.order_icon);
        notificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MyOrdersScreen.class));
            }
        });




        return view;


    }
    private void showData() {
        database.child(acct.getId()).addValueEventListener(new ValueEventListener() {
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
            bookid.add(item.getKey());
            Book book = item.getValue(Book.class);
            list.add(book);
        }
        adapterItem = new MyCategoryAdapter(view.getContext(), list);
        recyclerView.setAdapter(adapterItem);
    }
}