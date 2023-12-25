package com.example.bookapp2.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookapp2.Model.User;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpForGoogleAccount extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static DatabaseReference currentUserReference;
    public static User currentUser;
    private EditText emailEdit, passEdit, fullName, birthdayTextEdit;
    private ProgressBar progressBar;
    private Button buttonRegister;
    GoogleSignInAccount account;

    final Calendar birthdayCalendar = Calendar.getInstance();
    RadioButton radioButton;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_for_google_account);

        emailEdit = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        birthdayTextEdit = findViewById(R.id.birthday_EditText);
        buttonRegister = findViewById(R.id.btnregis);
        radioGroup = findViewById(R.id.radiogroup);



        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String Name = account.getDisplayName();
            String Email = account.getEmail();

            emailEdit.setText(Email);
            fullName.setText(Name);

        }
        DatePickerDialog.OnDateSetListener birthdayPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                birthdayCalendar.set(Calendar.YEAR, year);
                birthdayCalendar.set(Calendar.MONTH, month);
                birthdayCalendar.set(Calendar.DAY_OF_MONTH, day);

                // Update birthday edit text
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                birthdayTextEdit.setText(dateFormat.format(birthdayCalendar.getTime()));
            }
        };

        birthdayTextEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(
                                SignUpForGoogleAccount.this,
                                birthdayPicker,
                                birthdayCalendar.get(Calendar.YEAR),
                                birthdayCalendar.get(Calendar.MONTH),
                                birthdayCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
                    }
                }
        );

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        String email, pass, name, day, month, year, birthday,gender,major;
        day = month = year = "";

        email = emailEdit.getText().toString().replaceAll("\\s+", "");
        name = fullName.getText().toString();

        gender = radioButton.getText().toString();


        birthday = birthdayTextEdit.getText().toString();
        String[] dateParts = birthday.split("/");

        boolean validBirthday = false;
        if (dateParts.length == 3) {
            day = dateParts[0];
            month = dateParts[1];
            year = dateParts[2];
            validBirthday = true;
        }
        User user = new User(name, day, month, year, email,gender);
        myRef.child(account.getId()).setValue(user);

        Intent i =new Intent(SignUpForGoogleAccount.this, HomeActivity.class);
        startActivity(i);
    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

}