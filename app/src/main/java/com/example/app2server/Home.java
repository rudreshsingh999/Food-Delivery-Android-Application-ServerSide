package com.example.app2server;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app2server.Common.Common;
import com.example.app2server.Common.Distance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    TextView txtfullName;
    MaterialEditText distance;
    Button set;
    private ActionBarDrawerToggle abdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        distance = findViewById(R.id.dist);
        set = findViewById(R.id.set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = distance.getText().toString();
                Distance.distance = r;
                Toast.makeText(Home.this, "Radius set to " + r + " Km", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Set Delivery Radius");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Distance.distance.equals("0"))
                    Toast.makeText(getApplicationContext(), "Set valid delivery radius to view available orders.", Toast.LENGTH_SHORT).show();
                else {
                    Intent orderIntent = new Intent(Home.this, OrderStatus.class);
                    startActivity(orderIntent);
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
        )
                .setDrawerLayout(drawer)
                .build();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.Open, R.string.Close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        txtfullName = headerView.findViewById(R.id.txtfullName);
        txtfullName.setText(Common.currentUser.getName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_orders) {
            Intent orders = new Intent (Home.this, OrderStatus.class);
            startActivity(orders);
        }
        if(id == R.id.nav_about) {
            Intent about = new Intent (Home.this, About.class);
            startActivity(about);
        }
        if(id == R.id.nav_signout) {
            Intent sign = new Intent(Home.this, SignIn.class);
            startActivity(sign);
        }
        if(id == R.id.nav_map) {
            startActivity(new Intent(Home.this, Radius.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
