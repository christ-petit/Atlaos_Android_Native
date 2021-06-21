package org.atlaos.app.model;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class DescribeRecordModel extends ViewModel {

    private String storyName="";
    private String artist="";
    private String province="";
    private String district="";
    private String village="";
    private String type="";
    private String type_other="";
    private LatLng latLng;//=new LatLng(12,13);
    private Uri photoUri;//=Uri.parse("file:///storage/emulated/0/Download/290px-Bradypus.jpg");
    private Uri recordUri;// =Uri.parse("content://com.android.providers.downloads.documents/document/msf%3A44");
    private UUID uuid;

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_other() {
        return type_other;
    }

    public void setType_other(String type_other) {
        this.type_other = type_other;
    }



    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photo) {
        this.photoUri = photo;
    }

    public Uri getRecordUri() {
        return recordUri;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng=latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }
    public void setRecordUri(Uri recordUri) {
        this.recordUri = recordUri;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}

