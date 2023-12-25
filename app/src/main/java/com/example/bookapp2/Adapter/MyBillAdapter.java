package com.example.bookapp2.Adapter;


import static com.example.bookapp2.Activities.MyBillActivity.bookbill;
import static com.example.bookapp2.Activities.ViewMyCart.acct;
import static com.example.bookapp2.Activities.ViewMyCart.bookcart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Model.Book;
import com.example.bookapp2.Model.Order;
import com.example.bookapp2.Model.OrderWithAdress;
import com.example.bookapp2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MyBillAdapter extends RecyclerView.Adapter<MyBillAdapter.MyViewHolder>  {

    Context context;

    public ArrayList<Order> OfferList;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  orderid, phone, address, amount;
        CardView card;

        public MyViewHolder(View view) {
            super(view);
            orderid = (TextView) view.findViewById(R.id.orderID);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
            amount = (TextView) view.findViewById(R.id.amount);
            card = (CardView) view.findViewById(R.id.cardview);
        }

    }

    public MyBillAdapter(Context context, ArrayList<Order> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,@SuppressLint("RecyclerView")  final int position) {
        final Order lists = OfferList.get(position);
        holder.orderid.setText(bookbill.get(position));
        holder.address.setText(lists.getAddress());
        holder.phone.setText(lists.getPhone());
        holder.amount.setText(String.valueOf(lists.getSum()));

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDetailsDialog(lists);
            }
        });

    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }
    private void showOrderDetailsDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Order Details");

        ArrayList<Book> items = order.getItems();

        // Tạo một StringBuilder để chứa thông tin chi tiết của tất cả các sách trong danh sách items
        StringBuilder itemsText = new StringBuilder();

        for (Book book : items) {
            itemsText.append("Book Name: ").append(book.getBookName()).append("\n");

            itemsText.append("Price: ").append(book.getPrice()).append("\n");
            itemsText.append("Quantity: ").append(book.getQuantity()).append("\n");
            itemsText.append("Selected: ").append(book.isSelected()).append("\n\n");
            // Thêm thông tin khác của sách nếu cần
        }

        builder.setMessage(itemsText.toString());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}


