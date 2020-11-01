package com.nadim.gbe_gbe_final.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OderProduct {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("uploadTime")
    @Expose
    private String uploadTime;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("trxId")
    @Expose
    private String trxId;
    @SerializedName("isDeliver")
    @Expose
    private Boolean isDeliver;
    @SerializedName("userId")
    @Expose
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public Boolean getIsDeliver() {
        return isDeliver;
    }

    public void setIsDeliver(Boolean isDeliver) {
        this.isDeliver = isDeliver;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
