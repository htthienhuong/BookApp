package com.example.bookapp2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Activities.EditMyBook;
import com.example.bookapp2.Model.Book;
import com.example.bookapp2.R;
import com.squareup.picasso.Picasso;

import java.util.List;




public class MyCategoryAdapter extends RecyclerView.Adapter<MyCategoryAdapter.MyViewHolder> {

    Context context;

    private List<Book> OfferList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, rating,author_name;
        ImageView imageView;
        CardView book;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            author_name = (TextView) view.findViewById(R.id.author_name);
            rating = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);
            book = (CardView) view.findViewById(R.id.bookcover);

        }

    }


    public MyCategoryAdapter(Context context, List<Book> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_list, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Book lists = OfferList.get(position);
        holder.title.setText(lists.getBookName());
        holder.rating.setText(lists.getQuantity());
        holder.author_name.setText(lists.getAuthorName());
        Picasso.get().load(lists.getImageUrl()).into(holder.imageView);

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(context, lists,position);
            }
        });

    }

    private void onClickGoToDetail(Context context, Book lists, int position) {
        if(lists != null){
            Intent intent = new Intent(context, EditMyBook.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_book",lists);
            intent.putExtras(bundle);
            intent.putExtra("posBookId", position);
            context.startActivity(intent);
        }

    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


