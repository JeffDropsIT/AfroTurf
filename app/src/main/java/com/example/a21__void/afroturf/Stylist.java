package com.example.a21__void.afroturf;



public class Stylist{


    private String name, gender, contactNumber;
    private int rating;

    public Stylist(String name){
        this.name= name;
    }
    public Stylist(String name, int rating){
        this.name = name;
        this.rating = rating;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}


