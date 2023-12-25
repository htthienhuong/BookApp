package com.example.bookapp2.Activities;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookapp2.Model.User;
import com.example.bookapp2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity {
    RadioGroup radioGroup;

    RadioButton radioButton;
    CircleImageView ava;

    TextInputEditText nameEdit, emailEdit, birthdayTextEdit;

    Integer amount;
    final Calendar birthdayCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ava = findViewById(R.id.unsplash_jm);

        nameEdit = findViewById(R.id.txtname);
        emailEdit = findViewById(R.id.txtemail);
        birthdayTextEdit = findViewById(R.id.txtbirthday);
        radioGroup = findViewById(R.id.radiogroup);
        RadioButton radioBtnMale = findViewById(R.id.radio_male);
        RadioButton radioBtnFemale = findViewById(R.id.radio_female);

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
                                EditUserActivity.this,
                                birthdayPicker,
                                birthdayCalendar.get(Calendar.YEAR),
                                birthdayCalendar.get(Calendar.MONTH),
                                birthdayCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
                    }
                }
        );


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            String name = acct.getDisplayName();
            String Email = acct.getEmail();
            Glide.with(this).load(personPhoto).into(ava);

            nameEdit.setText(name);
            emailEdit.setText(Email);

        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(acct.getId());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                birthdayTextEdit.setText(user.getBirthday());
//                    autoCompleteTextView.setText(autoCompleteTextView.getAdapter().getItem(0).toString());

                String gender = user.getGender().toString();
                if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Nam") ){
                    radioBtnMale.setChecked(true);
                } else if (gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Nữ")) {
                    radioBtnFemale.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button update = findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");


                String email, name, day, month, year, birthday,gender,major;
                day = month = year = "";

                email = emailEdit.getText().toString().replaceAll("\\s+", "");
                name = nameEdit.getText().toString();
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
                User user = new User(name,day,month,year,email,gender);
                myRef.child(acct.getId()).setValue(user);
                Toast.makeText(EditUserActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
            }
        });

        View back = findViewById(R.id.arrow_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}