package com.example.bookapp2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bookapp2.MainActivity;
import com.example.bookapp2.Model.LocaleHelper;
import com.example.bookapp2.Model.User;
import com.example.bookapp2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInEmailActivity extends AppCompatActivity {
    ImageButton buttonLanguage;
    EditText emailEdit, passEdit;
    Button buttonLogin;
    TextView forgetPassword, buttonRegister;

    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static DatabaseReference currentUserReference;
    public static User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onCreate(SignInEmailActivity.this.getBaseContext());

        setContentView(R.layout.activity_sign_in_email);

        buttonLanguage = findViewById(R.id.language_button);
        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.showChangeLanguageDialog(
                        SignInEmailActivity.this, SignInEmailActivity.this
                );
            }
        });

        mAuth = FirebaseAuth.getInstance();

        emailEdit = findViewById(R.id.email);
        passEdit = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btnlogin);
        buttonRegister = findViewById(R.id.btnregis);
        forgetPassword = findViewById(R.id.forgotpassword);
        buttonLanguage = findViewById(R.id.language_button);

        buttonLogin.setOnClickListener(v -> login());
        buttonRegister.setOnClickListener(v -> register());
        forgetPassword.setOnClickListener(v -> forgetPassword());

    }

    private void register() {
        Intent i =new Intent(SignInEmailActivity.this,SignUpActivity.class);
        startActivity(i);
    }

    private void forgetPassword(){
        Intent i = new Intent(SignInEmailActivity.this,ForgotPassword.class);
        startActivity(i);
    }

    private void login() {
        String email,pass;
        email = emailEdit.getText().toString().replaceAll("\\s+", "");
        pass = passEdit.getText().toString().replaceAll("\\s+", "");

        // Check text edit of email address
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,
                    getResources().getString(R.string.please_enter_your_email),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check text edit of password
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this,
                    getResources().getString(R.string.please_enter_your_password),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Try sign-in with user-typed email and password
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseUser = SignUpActivity.mAuth.getCurrentUser();

                // Check if account is verified via email
                if (firebaseUser != null && firebaseUser.isEmailVerified()) {

                    // If account is verified, get the database reference
                    // and retrieve user's information
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.login_successfully),
                            Toast.LENGTH_SHORT
                    ).show();

                    // Get database reference
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    currentUserReference = database
                            .getReference("Users")
                            .child(firebaseUser.getUid());

                    // Retrieve user's information
                    currentUserReference
                            .addListenerForSingleValueEvent( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    SignInEmailActivity.currentUser = snapshot.getValue(User.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("LoginActivity", "get data failed");
                                }
                            });

                    // Move to MainActivity
                    Intent intent = new Intent(SignInEmailActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(SignInEmailActivity.this);

                } else {

                    // If account is not verified, show the Toast message to user
                    Toast.makeText(
                            getApplicationContext(),
                            getResources().getString(R.string.you_havent_verify_your_account),
                            Toast.LENGTH_LONG
                    ).show();
                }

            } else {

                // If sign-in unsuccessfully, show the Toast notification
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.cant_login),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}