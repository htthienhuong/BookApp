package com.example.bookapp2.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bookapp2.Adapter.MyCartAdapter;
import com.example.bookapp2.Adapter.MyOrdersAdapter;
import com.example.bookapp2.Model.Book;
import com.example.bookapp2.Model.OrderWithAdress;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrdersScreen extends AppCompatActivity {
    public  static ArrayList<String> bookorder = new ArrayList<>();


    DatabaseReference database = FirebaseDatabase.getInstance().getReference("orders");
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    public static GoogleSignInAccount acct;
    MyOrdersAdapter adapterItem;
    final int PERMISSION_REQUEST_CODE =112;


    RecyclerView recyclerView;
    ArrayList<OrderWithAdress> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_screen);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        acct = GoogleSignIn.getLastSignedInAccount(this);

        recyclerView = findViewById(R.id.recyclerView);
        showData();


        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(acct.getId());

        ChildEventListener ordersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Đây là nơi xử lý khi có một đơn hàng mới được thêm vào Firebase
                // dataSnapshot chứa dữ liệu của đơn hàng mới được thêm vào

                // Hiển thị thông báo có đơn hàng mới
                // Ví dụ: sử dụng Notification để thông báo đơn hàng mới
                sendNotification(MyOrdersScreen.this,"Có đơn hàng mới", "Đã nhận được đơn hàng mới!");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Xử lý khi có thay đổi trong đơn hàng (nếu cần)
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            // Các phương thức còn lại của ChildEventListener cũng có thể được xử lý tùy theo nhu cầu
        };

        ordersRef.addChildEventListener(ordersChildEventListener);
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")){
                getNotificationPermission();
            }
        }
        ImageView done = findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrdersScreen.this);
                builder.setMessage("Tiếp nhận đơn hàng ?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MyOrdersAdapter adapter = (MyOrdersAdapter) recyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.deleteSelectedItems(); // Xóa các mục đã chọn từ Firebase
                        }
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
        });
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
    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }
    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            OrderWithAdress book = item.getValue(OrderWithAdress.class);
            Log.d("itemm",item.getKey());
            bookorder.add(item.getKey());
            list.add(book);
        }
        adapterItem = new MyOrdersAdapter(MyOrdersScreen.this, list);
        recyclerView.setAdapter(adapterItem);
    }
    private void sendNotification(Context context, String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                .setSmallIcon(R.mipmap.ic_launcher) //icon
                .setContentTitle(title) //tittle
                .setAutoCancel(true)//swipe for delete
                .setContentText(message); //content

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}