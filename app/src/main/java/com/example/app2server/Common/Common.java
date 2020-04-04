package com.example.app2server.Common;
import com.example.app2server.Model.User;

public class Common {

    public static User currentUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST = 71;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Order placed.";
        else if(status.equals("1"))
            return "On my Way!";
        else
            return "Order shipped.";
    }
}
