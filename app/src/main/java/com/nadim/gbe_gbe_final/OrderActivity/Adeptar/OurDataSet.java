package com.nadim.gbe_gbe_final.OrderActivity.Adeptar;

import android.net.Uri;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class OurDataSet {
   private String trxId;
   private String status;
   private String locationName;
   private String note;
   private ArrayList<OrderImageDataSet> orderImage;

    public OurDataSet() {
    }

    public OurDataSet(String trxId, String status, String locationName, String note, ArrayList<OrderImageDataSet> orderImage) {
        this.trxId = trxId;
        this.status = status;
        this.locationName = locationName;
        this.note = note;
        this.orderImage = orderImage;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<OrderImageDataSet> getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(ArrayList<OrderImageDataSet> orderImage) {
        this.orderImage = orderImage;
    }
}
