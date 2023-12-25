package com.example.bookapp2.Adapter;

import static com.example.bookapp2.Activities.ViewMyCart.acct;
import static com.example.bookapp2.Activities.ViewMyCart.bookcart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Model.Book;
import com.example.bookapp2.Model.OrderWithAdress;
import com.example.bookapp2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder>  {

    Context context;

    public ArrayList<Book> OfferList;

    private OnTotalPriceChangedListener totalPriceChangedListener;

    public void setOnTotalPriceChangedListener(OnTotalPriceChangedListener listener) {
        this.totalPriceChangedListener = listener;
    }

    public interface OnTotalPriceChangedListener {
        void onTotalPriceChanged(double totalPrice);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price,quantity;
        ImageView imageView;
        CheckBox yourCheckBox;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            quantity = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);
            yourCheckBox = (CheckBox) view.findViewById(R.id.purchaseCheckBox);
        }

    }

    public MyCartAdapter(Context context, ArrayList<Book> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,@SuppressLint("RecyclerView")  final int position) {
        final Book lists = OfferList.get(position);
        holder.title.setText(lists.getBookName());
        holder.price.setText(lists.getPrice());
        holder.quantity.setText(lists.getQuantity());
        Picasso.get().load(lists.getImageUrl()).into(holder.imageView);
        holder.yourCheckBox.setChecked(lists.isSelected());

        holder.yourCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lists.setSelected(isChecked); // Cập nhật trạng thái chọn sách
                double totalPrice = calculateTotalPrice();
                if (totalPriceChangedListener != null) {
                    totalPriceChangedListener.onTotalPriceChanged(totalPrice); // Gửi thông điệp đến Activity/Fragment
                }
            }
        });

    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Book book : OfferList) {
            if (book.isSelected()) {
                total += Double.parseDouble(book.getPrice()); // Cộng giá sách vào tổng tiền
            }
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }
    public void checkAllItems(boolean isChecked) {
        for (Book book : OfferList) {
            book.setSelected(isChecked);
        }
        notifyDataSetChanged(); // Thông báo cho RecyclerView về sự thay đổi
    }
    public void deleteSelectedItems() {
        ArrayList<Integer> selectedPositions = new ArrayList<>();
        for (int i = 0; i < OfferList.size(); i++) {
            Book book = OfferList.get(i);
            if (book.isSelected()) {
                selectedPositions.add(i); // Thêm vị trí của mục được chọn
            }
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(acct.getId()).child("cart");
        for (Integer pos : selectedPositions) {
            database.child(bookcart.get(pos)).removeValue(); // Xóa mục từ Firebase
            OfferList.remove(pos); // Xóa mục khỏi danh sách hiển thị
        }

        notifyDataSetChanged(); // Cập nhật RecyclerView sau khi xóa
    }

    public ArrayList<Book> getSelectedItems(String address, String phone) {
        ArrayList<Book> selectedItems = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("books");

        for (int i = 0; i < OfferList.size(); i++) {
            Book book = OfferList.get(i);
            if (book.isSelected()) {
                selectedItems.add(book);
                int finalI = i;
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            for (DataSnapshot bookId : item.getChildren()) {
                                if (Objects.equals(bookId.getKey(), bookcart.get(finalI))) {

                                    Book bookWare = bookId.getValue(Book.class);
                                    int quantity = Integer.parseInt(bookWare.getQuantity()) - Integer.parseInt(book.getQuantity());

                                    // Update Quantity of Book
                                    database.child(item.getKey()).child(bookId.getKey()).child("quantity").setValue(String.valueOf(quantity));
                                    book.setBookId(bookcart.get(finalI)); // Đặt vị trí của book trong OfferList làm bookId


                                    //Create Orders
                                    OrderWithAdress order = new OrderWithAdress();
                                    order.setAddress(address);
                                    order.setPhone(phone);
                                    order.setBook(book); // Set thông tin sách cho đơn hàng

                                    // Thêm đối tượng OrderWithAddress này vào ordersDB
                                    DatabaseReference ordersDB = FirebaseDatabase.getInstance().getReference("orders");
                                    ordersDB.child(item.getKey()).push().setValue(order);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        }
        return selectedItems;
    }



}


