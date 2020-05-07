package com.myapp.theagrim.torguo;

public class DataTransfer {
    String email;
    String country;
    String name;
    String uri;
    DataTransfer(){

    }
    DataTransfer(String email,String country,String name,String uri){
        this.email=email;
        this.country=country;
        this.name=name;
        this.uri=uri;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
