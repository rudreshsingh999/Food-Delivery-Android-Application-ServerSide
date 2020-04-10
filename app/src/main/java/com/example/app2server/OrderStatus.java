package com.example.app2server;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app2server.Common.Common;
import com.example.app2server.Common.Distance;
import com.example.app2server.Interface.ItemClickListener;
import com.example.app2server.Model.Request;
import com.example.app2server.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    final String r = "why";
    FirebaseRecyclerAdapter <Request, OrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner spinner;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    Button search;
    MaterialEditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        if(checkPermission(Manifest.permission.SEND_SMS)) {

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
        // Firebase
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        // Init
        recyclerView = (RecyclerView) findViewById(R.id.listOrders);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        double val = Double.parseDouble(Distance.distance);
        loadOrders(val);

    }

    private void loadOrders(final double dist) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {
                if(Double.parseDouble(request.getDistance()) < dist) {
                    orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                    orderViewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                    orderViewHolder.txtOrderAddress.setText(request.getAddress());
                    orderViewHolder.txtOrderPhone.setText(request.getPhone());


                    orderViewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            // Implemented to fix crashes when this item is clicked
                        }
                    });
                }
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected (MenuItem item)
    {
        if(item.getTitle().equals(Common.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        else if (item.getTitle().equals(Common.DELETE))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        Toast.makeText(this, ""+key, Toast.LENGTH_SHORT).show();
        requests.child(key).removeValue();
    }

    private boolean checkPermission(String sendSms) {
        int checkPermission = ContextCompat.checkSelfPermission(this, sendSms);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void showUpdateDialog(String key, final Request item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout,null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed", "On my way", "Shipped");

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                System.out.println(item.getPhone());
                System.out.println(localKey);
                System.out.println(Common.currentUser.getName());
                System.out.println(Common.currentUser.getPhone());
                System.out.println(item.getDistance());
                requests.child(localKey).setValue(item);
                Random rand = new Random();
                int otp = rand.nextInt(10000000);
                String msg = "Your order with OrderId#"+ localKey + " is confirmed.\n";
                String msg1 = "It will be delivered to you by :\nDelivery Person Name : "+ Common.currentUser.getName()+"\nDelivery Person Phone Number : "+ Common.currentUser.getPhone() + "\n";
                String msg2 = "Your OTP is : "+ otp;
                msg = msg + msg2;
                System.out.println(msg);
                if(checkPermission(Manifest.permission.SEND_SMS)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(item.getPhone(), null, msg, null, null);
                    smsManager.sendTextMessage(item.getPhone(), null, msg1, null, null);
                }
                else {
                    Toast.makeText(OrderStatus.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }
}
