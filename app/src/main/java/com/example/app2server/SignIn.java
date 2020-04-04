package com.example.app2server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app2server.Common.Common;
import com.example.app2server.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    DatabaseReference users;
    FirebaseDatabase db;
    EditText edtPhone, edtPassword;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edit_password);
        edtPhone = (MaterialEditText)findViewById(R.id.edit_phone);

        signIn = findViewById(R.id.signin);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtPhone.getText().toString(), edtPassword.getText().toString());

            }
        });
    }

    private void signInUser(String phone, String password) {
        final String localPhone  = phone;
        final String localPassword = password;

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists()) {
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    System.out.println(user.getName());
                    if(Boolean.parseBoolean(user.getIsStaff())) {
                        if(user.getPassword().equals(localPassword)) {
                            Intent homeIntent = new Intent(SignIn.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                        }
                        else {
                            Toast.makeText(SignIn.this, "Wrong Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        System.out.println(user.getName());
                        Toast.makeText(SignIn.this, "Not a staff account.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignIn.this, "User doesn't exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
