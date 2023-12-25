package com.example.bookapp2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.ModelClass.RecommandedModelClass;
import com.example.bookapp2.R;

import java.util.List;




public class RecommandedRecycleAdapter extends RecyclerView.Adapter<RecommandedRecycleAdapter.MyViewHolder> {

    Context context;

    private List<RecommandedModelClass> OfferList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, rating,author_name;
        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            author_name = (TextView) view.findViewById(R.id.author_name);
            rating = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);


        }

    }


    public RecommandedRecycleAdapter(Context context, List<RecommandedModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommanded_list, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final RecommandedModelClass lists = OfferList.get(position);
        holder.title.setText(lists.getTitle());
        holder.author_name.setText(lists.getAuthor_name());
        holder.imageView.setImageResource(lists.getImage());



    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


