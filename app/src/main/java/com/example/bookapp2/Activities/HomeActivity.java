package com.example.bookapp2.Activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp2.Adapter.RecommandedRecycleAdapter;
import com.example.bookapp2.Fragment.CategoryMyStore;
import com.example.bookapp2.Fragment.EbooksSettingFragment;
import com.example.bookapp2.Fragment.HomeFragment;
import com.example.bookapp2.ModelClass.RecommandedModelClass;
import com.example.bookapp2.R;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  {

    //  recommanded data


    private ArrayList<RecommandedModelClass> recommandedModelClasses;
    private RecyclerView recyclerView;
    private RecommandedRecycleAdapter bAdapter;

    private Integer image[]= {R.drawable.blink_imges,R.drawable.me_befor_you,R.drawable.how_to_win};
    private String title[] = {"Blink: The Power\n" +
            "of Thinking Wi…","Me Before You","How to Win \n" +
            "Friends and Inf…"};
    private String rating[] = {"4.5","4.0","3.2"};
    private String author_name[] =  {"Malcolm Gladwell","Jojo Moyes","Dale Carnegie"};

    public  static ArrayList<String> bookid = new ArrayList<>();


    //  top_50 data


    private ArrayList<RecommandedModelClass> recommandedModelClasses1;
    private RecyclerView recyclerView1;
    private RecommandedRecycleAdapter bAdapter1;

    private Integer image1[]= {R.drawable.cantbury,R.drawable.the_dreaming,R.drawable.the_beauty_purpose};
    private String title1[] = {"The Canterbury\n" +
            "Tales","The Dreaming\n" +
            "Reality","The Beauty of\n" +
            "Purpose in Life…"};
    private String rating1[] = {"4.0","4.0","4.5"};
    private String author_name1[] =  {"Geoffrey Chaucer","Noor Anand","Sahla Parveen"};



    BottomBar bottomBar;

    ImageView search_img;


    TextView title3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title3 = findViewById(R.id.title);
        title3.setText("eBooks");

        search_img = findViewById(R.id.buying_icon);
        search_img.setVisibility(View.VISIBLE);

        bottomBar = findViewById(R.id.bottombar);
        for (int i = 0; i < bottomBar.getTabCount(); i++) {
            bottomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL);
        }

        bottomBar.setOnTabSelectListener(tabId -> {
            Fragment selectedFragment = null;

            if (tabId == R.id.home_icon) {
                selectedFragment = new HomeFragment();

            } else if (tabId == R.id.account) {
                selectedFragment = new EbooksSettingFragment();
            } else if (tabId == R.id.transaction) {
                selectedFragment = new CategoryMyStore();
            }

            // Xử lý các tab khác nếu cần
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selectedFragment)
                        .commit();
            }
        });


        //        recommanded recyclerview code is here


        recyclerView = findViewById(R.id.recommanded_recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HomeActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recommandedModelClasses = new ArrayList<>();

        for (int i = 0; i < image.length; i++) {
            RecommandedModelClass beanClassForRecyclerView_contacts = new RecommandedModelClass(image[i], title[i], rating[i], author_name[i]);
            recommandedModelClasses.add(beanClassForRecyclerView_contacts);
        }
        bAdapter = new RecommandedRecycleAdapter(HomeActivity.this, recommandedModelClasses);
        recyclerView.setAdapter(bAdapter);


//        recyclerView1 = findViewById(R.id.top50_books_recyclerview);
//        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(HomeActivity.this, 3);
//        recyclerView1.setLayoutManager(layoutManager1);
//        recyclerView1.setItemAnimator(new DefaultItemAnimator());

//        recommandedModelClasses1 = new ArrayList<>();
//
//        for (int i = 0; i < image.length; i++) {
//            RecommandedModelClass beanClassForRecyclerView_contacts = new RecommandedModelClass(image1[i],title1[i],rating1[i],author_name1[i]);
//            recommandedModelClasses1.add(beanClassForRecyclerView_contacts);
//        }
//        bAdapter1 = new RecommandedRecycleAdapter(HomeActivity.this,recommandedModelClasses1);
//        recyclerView1.setAdapter(bAdapter1);
    }

}