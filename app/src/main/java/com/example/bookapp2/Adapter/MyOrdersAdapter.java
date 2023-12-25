package com.example.bookapp2.Adapter;

import static com.example.bookapp2.Activities.MyOrdersScreen.acct;
import static com.example.bookapp2.Activities.MyOrdersScreen.bookorder;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.util.Objects;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder>  {

    Context context;

    public ArrayList<OrderWithAdress> OfferList;

    private OnTotalPriceChangedListener totalPriceChangedListener;

    public void setOnTotalPriceChangedListener(OnTotalPriceChangedListener listener) {
        this.totalPriceChangedListener = listener;
    }

    public interface OnTotalPriceChangedListener {
        void onTotalPriceChanged(double totalPrice);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price,quantity, address, phone;
        ImageView imageView;
        CheckBox yourCheckBox;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            quantity = (TextView) view.findViewById(R.id.rating);
            imageView = (ImageView)view.findViewById(R.id.image);
            yourCheckBox = (CheckBox) view.findViewById(R.id.purchaseCheckBox);
            address = (TextView) view.findViewById(R.id.address);
            phone = (TextView) view.findViewById(R.id.phone);
        }

    }

    public MyOrdersAdapter(Context context, ArrayList<OrderWithAdress> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,@SuppressLint("RecyclerView")  final int position) {
        final OrderWithAdress lists = OfferList.get(position);
        holder.title.setText(lists.getBook().getBookName());
        holder.price.setText(lists.getBook().getPrice());
        holder.quantity.setText(lists.getBook().getQuantity());
        Picasso.get().load(lists.getBook().getImageUrl()).into(holder.imageView);
//        holder.yourCheckBox.setChecked(lists.isSelected());
        holder.address.setText(lists.getAddress());
        holder.phone.setText(lists.getPhone());
        holder.yourCheckBox.setChecked(lists.isSelected());
        holder.yourCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lists.setSelected(isChecked); // Cập nhật trạng thái chọn sách
            }
        });
    }

    @Override
    public int getItemCount() {
        return OfferList.size();

    }
    public void deleteSelectedItems() {
        ArrayList<Integer> selectedPositions = new ArrayList<>();
        for (int i = 0; i < OfferList.size(); i++) {
            OrderWithAdress book = OfferList.get(i);
            if (book.isSelected()) {
                selectedPositions.add(i); // Thêm vị trí của mục được chọn
            }
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("orders").child(acct.getId());
        for (Integer pos : selectedPositions) {
            database.child(bookorder.get(pos)).removeValue(); // Xóa mục từ Firebase
            OfferList.remove(pos); // Xóa mục khỏi danh sách hiển thị
        }

        notifyDataSetChanged(); // Cập nhật RecyclerView sau khi xóa
    }


}


