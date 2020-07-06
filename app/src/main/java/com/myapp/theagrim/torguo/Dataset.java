package com.myapp.theagrim.torguo;

import java.util.Date;

public class Dataset {

    private String Name;
    private Double Lattitude;
    private Double Longitude;
    private String Locality;
    private String City;
    private String State;
    private String Time;
    private String Landmark;
    private String Contact;
    private int Capacity;
    private boolean status;
    private boolean cutlery;
    private String type;
    private String date;
    private String owner;
    private String key;
    private int profilepic;


    public Dataset(String name, Double lattitude, Double longitude, String locality, String city, String state, String time, String landmark, String contact, int capacity, boolean status, boolean cutlery, String type, String date, String owner, String key, int profilepic) {
        Name = name;
        Lattitude = lattitude;
        Longitude = longitude;
        Locality = locality;
        City = city;
        State = state;
        Time = time;
        Landmark = landmark;
        Contact = contact;
        Capacity = capacity;
        this.status = status;
        this.cutlery = cutlery;
        this.type = type;
        this.date = date;
        this.owner = owner;
        this.key = key;
        this.profilepic = profilepic;
    }

    public int getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(int profilepic) {
        this.profilepic = profilepic;
    }

    public Dataset(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }



    public Double getLattitude() {
        return Lattitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getLocality() {
        return Locality;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getTime() {
        return Time;
    }

    public String getLandmark() {
        return Landmark;
    }

    public String getContact() {
        return Contact;
    }

    public int getCapacity() {
        return Capacity;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isCutlery() {
        return cutlery;
    }

    public String getType() {
        return type;
    }

    public void setLattitude(Double lattitude) {
        Lattitude = lattitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setCutlery(boolean cutlery) {
        this.cutlery = cutlery;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
