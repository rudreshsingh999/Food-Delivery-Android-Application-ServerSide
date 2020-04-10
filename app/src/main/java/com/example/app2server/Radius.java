package com.example.app2server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app2server.Common.Distance;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Radius extends AppCompatActivity {

    MaterialEditText distance;
    Button set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);

        distance = findViewById(R.id.dist);
        set = findViewById(R.id.set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = distance.getText().toString();
                Distance.distance = r;
                Toast.makeText(Radius.this, "Radius set to " + r, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
