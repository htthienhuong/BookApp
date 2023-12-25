package com.example.bookapp2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Activities.ViewMyCart;
import com.example.bookapp2.Adapter.BookListAdapter;
import com.example.bookapp2.Adapter.RecommandedRecycleAdapter;
import com.example.bookapp2.Model.Book;
import com.example.bookapp2.ModelClass.RecommandedModelClass;
import com.example.bookapp2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    //  recommanded data

    ArrayList<Book> list = new ArrayList<>();
    public  static ArrayList<String> bookid = new ArrayList<>();

    private ArrayList<RecommandedModelClass> recommandedModelClasses;
    private RecyclerView recyclerView;
    private RecommandedRecycleAdapter bAdapter;

    private Integer image[]= {R.drawable.blink_imges,R.drawable.me_befor_you,R.drawable.how_to_win};
    private String title[] = {"Blink: The Power" ,"Me Before You","How to Win"};
    private String rating[] = {"4.5","4.0","3.2"};
    private String author_name[] =  {"Malcolm Gladwell","Jojo Moyes","Dale Carnegie"};



    //  top_50 data
    private RecyclerView recyclerView1;


    BookListAdapter adapterItem;

    View view;
    BottomBar bottomBar;


    public HomeFragment(){super(R.layout.activity_home);}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        bottomBar = findViewById(R.id.bottombar);
//        for (int i = 0; i < bottomBar.getTabCount(); i++)
//        { bottomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL); }
//
//
//        title = findViewById(R.id.title);
//        title.setText("Settings");
//
//

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        if(view != null){

            bottomBar = view.findViewById(R.id.bottombar);
            for (int i = 0; i < bottomBar.getTabCount(); i++)
            { bottomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL); }

//            recyclerView = view.findViewById(R.id.recommanded_recyclerview);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//            recommandedModelClasses = new ArrayList<>();
//
//            for (int i = 0; i < image.length; i++) {
//                RecommandedModelClass beanClassForRecyclerView_contacts = new RecommandedModelClass(image[i],title[i],rating[i],author_name[i]);
//                recommandedModelClasses.add(beanClassForRecyclerView_contacts);
//            }
//            bAdapter = new RecommandedRecycleAdapter(view.getContext(),recommandedModelClasses);
//            recyclerView.setAdapter(bAdapter);

            //        recommanded recyclerview code is here

            recyclerView1 = view.findViewById(R.id.top50_books_recyclerview);
            RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getActivity(),3);
            recyclerView1.setLayoutManager(layoutManager1);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());

            DatabaseReference database = FirebaseDatabase.getInstance().getReference("books");

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {

                                for (DataSnapshot item : bookSnapshot.getChildren()) {
                                    bookid.add(item.getKey());
                                    Log.d("item",item.getKey());
                                    Book book = item.getValue(Book.class);
                                    list.add(book);
                                }

                            }
                            adapterItem = new BookListAdapter(view.getContext(), list);
                            recyclerView1.setAdapter(adapterItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi nếu có
                    }
                });

            EditText search = view.findViewById(R.id.search);

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        adapterItem.getFilter().filter(charSequence);

                    }catch (Exception e){

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        ImageView buying_icon = view.findViewById(R.id.buying_icon);
        buying_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ViewMyCart.class));
            }
        });

        return view;

    }




}
