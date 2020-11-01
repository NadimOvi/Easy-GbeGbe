package com.nadim.gbe_gbe_final.PostOrderObjectClass;

import java.util.ArrayList;
import java.util.Date;

public class OrderProductClass {
    int UserId;
    String Latitude;
    String Longitude;
    String LocationName;
    String note;
    ArrayList<ArrayImageClass> OrderImages;

    public OrderProductClass(int userId, String latitude, String longitude, String locationName, String note, ArrayList<ArrayImageClass> orderImages) {
        UserId = userId;
        Latitude = latitude;
        Longitude = longitude;
        LocationName = locationName;
        this.note = note;
        OrderImages = orderImages;
    }

    public OrderProductClass() {
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<ArrayImageClass> getOrderImages() {
        return OrderImages;
    }

    public void setOrderImages(ArrayList<ArrayImageClass> orderImages) {
        OrderImages = orderImages;
    }
}
