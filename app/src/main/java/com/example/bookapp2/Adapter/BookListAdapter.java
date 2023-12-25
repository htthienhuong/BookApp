package com.example.bookapp2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Activities.DetailBook;
import com.example.bookapp2.Model.Book;
import com.example.bookapp2.Model.FilterCategory;
import com.example.bookapp2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> implements Filterable {

    Context context;

    public ArrayList<Book> OfferList, filterList;

    private FilterCategory filterCategory;
    @Override
    public Filter getFilter() {
        if(filterCategory == null){
            filterCategory = new FilterCategory(filterList,this);
        }
        return filterCategory;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, rating,author_name;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            author_name = (TextView) view.findViewById(R.id.author_name);
            imageView = (ImageView)view.findViewById(R.id.image);


        }

    }


    public BookListAdapter(Context context, ArrayList<Book> offerList) {
        this.OfferList = offerList;
        this.context = context;
        this.filterList = offerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommanded_list, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,@SuppressLint("RecyclerView")  final int position) {
        final Book lists = OfferList.get(position);
        holder.title.setText(lists.getBookName());
        holder.author_name.setText(lists.getAuthorName());
        Picasso.get().load(lists.getImageUrl()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(context, lists,position);
            }
        });

    }
    private void onClickGoToDetail(Context context, Book lists, int position) {
        if(lists != null){
            Intent intent = new Intent(context, DetailBook.class);
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


