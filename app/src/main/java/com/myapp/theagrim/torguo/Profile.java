package com.myapp.theagrim.torguo;

public class Profile {
    private int imageNnum;
    private String name,
            address,occupation,contact;
    private int age;
    private int events;
    private int reviews;
    private int ratings;
    private String description;

    public Profile(){

    }


    public int getEvents() {
        return events;
    }

    public void setEvents(int events) {
        this.events = events;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profile(int imageNnum, String name, String address, String occupation, String contact, int age, int events, int reviews, int ratings, String description) {
        this.imageNnum = imageNnum;
        this.name = name;
        this.address = address;
        this.occupation = occupation;
        this.contact = contact;
        this.age = age;
        this.events = events;
        this.reviews = reviews;
        this.ratings = ratings;
        this.description = description;
    }

    public int getImageNnum() {
        return imageNnum;
    }

    public void setImageNnum(int imageNnum) {
        this.imageNnum = imageNnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
