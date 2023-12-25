package com.example.bookapp2.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookapp2.Activities.EditUserActivity;
import com.example.bookapp2.Activities.LoginScreen;
import com.example.bookapp2.Activities.MyBillActivity;
import com.example.bookapp2.Activities.ViewMyCart;
import com.example.bookapp2.Model.LocaleHelper;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.roughike.bottombar.BottomBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EbooksSettingFragment extends Fragment {

    TextView title, language;
    CircleImageView ava;
    BottomBar bottomBar;
    View view;
    Context context;
    private Switch darkModeSwitch;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_ebooks_setting, container, false);

        if(view != null){

            bottomBar = view.findViewById(R.id.bottombar);
            for (int i = 0; i < bottomBar.getTabCount(); i++)
            { bottomBar.getTabAtPosition(i).setGravity(Gravity.CENTER_VERTICAL); }

            title = view.findViewById(R.id.title);
            title.setText(R.string.setting);

            language = view.findViewById(R.id.language);
            language.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocaleHelper.showChangeLanguageDialog(
                            view.getContext(),getActivity()

                    );
                }
            });


            TextView editprofile = view.findViewById(R.id.edit_profile);
            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), EditUserActivity.class));
                }
            });

            googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(view.getContext(),googleSignInOptions);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());
            ava = view.findViewById(R.id.profileCircleImageView);
            TextView nameuser = view.findViewById(R.id.usernameTextView);
            if (acct != null) {
                Uri personPhoto = acct.getPhotoUrl();
                String name = acct.getDisplayName();
                Glide.with(this).load(personPhoto).into(ava);

                nameuser.setText(name);
            }


//            if(new DarkModePrefManager(view.getContext()).isNightMode()){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//            setDarkModeSwitch();


            TextView logout = view.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SignOut();
                }
            });

            TextView cart = view.findViewById(R.id.cart);
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), ViewMyCart.class));
                }
            });

            TextView bill = view.findViewById(R.id.bill);
            bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), MyBillActivity.class));

                }
            });

        }
        return view;
    }

//    private void setDarkModeSwitch(){
//        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
//        darkModeSwitch.setChecked(new DarkModePrefManager(view.getContext()).isNightMode());
//        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(view.getContext());
//                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                requireActivity().recreate();
//            }
//        });
//    }
        private void SignOut() {
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    requireActivity().finish();
                    startActivity(new Intent(view.getContext(), LoginScreen.class));
                }
            });
}



}
