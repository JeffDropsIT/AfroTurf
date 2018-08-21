package com.example.a21__void.afroturf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPathContent {
    public static final String UP_REVIEWS = "Reviews", UP_BOOKINGS = "Bookings", UP_INBOX  = "Inbox"
            , UP_PAYMENT_METHOD = "Payment Method"
            , UP_ABOUT = "About"
            , UP_LOGOUT = "Logout";

    public static final List<UserPath> ITEMS = new ArrayList<UserPath>();
    static {
        ITEMS.add(new UserPath(UP_REVIEWS, UP_REVIEWS,"All the Salons/Stylist review you've made", R.drawable.ic_reviews));
        ITEMS.add(new UserPath(UP_BOOKINGS, UP_BOOKINGS, "Past and future bookings",R.drawable.ic_bookings));
        ITEMS.add(new UserPath(UP_INBOX, UP_INBOX, "All your notification",R.drawable.ic_inbox));
        ITEMS.add(new UserPath(UP_PAYMENT_METHOD, UP_PAYMENT_METHOD,"Set payment method", R.drawable.ic_payment));
        ITEMS.add(new UserPath(UP_ABOUT, UP_ABOUT, "All about us as Afroturf", R.drawable.ic_about));
        ITEMS.add(new UserPath(UP_LOGOUT, UP_LOGOUT, "Logout of this account.",R.drawable.ic_logout));
    }

    public static class UserPath {
        public final String Id;
        public final String Name;
        public final String Description;
        public final int ResIcon;

        public UserPath(String id, String name, String description, int resIcon) {
            this.Id = id;
            this.Name = name;
            this.Description = description;
            this.ResIcon = resIcon;
        }
    }
}
