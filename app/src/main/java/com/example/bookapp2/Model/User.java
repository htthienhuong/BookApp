package com.example.bookapp2.Model;

import com.google.firebase.database.Exclude;

public class User {
    public String fullName, day,month, year, email, gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public  User(){}

    public User(String fullname, String day, String month, String year, String email,String gender){
        this.fullName = fullname;
        this.day = day;
        this.month = month;
        this.year = year;
        this.email = email;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Exclude
    public String getBirthday() {
        return String.format("%s/%s/%s", day, month, year);
    }

    @Exclude
    public void setBirthday(String birthday) {
        String[] parts = birthday
                .replaceAll("\\s+", "")
                .split("/");

        if (parts.length == 3) {
            this.day = parts[0];
            this.month = parts[1];
            this.year = parts[2];
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


//    @Exclude
//    public ArrayList<Fish_Item> getFavouriteFishItem() {
//        ArrayList<Fish_Item> favFish = new ArrayList<>();
//        for (Fish_Item fish : Fish_Item.allFishes) {
//            if (favouriteFishes.contains(fish.getClassLabel()))
//                favFish.add(fish);
//        }
//        return favFish;
//    }



//    @Exclude
//    public ArrayList<Fish_Item> getViewedFishItems() {
//        ArrayList<Fish_Item> viewedFish = new ArrayList<>();
//        for (Fish_Item fish : Fish_Item.allFishes) {
//            if (viewedFishes.contains(fish.getClassLabel()))
//                viewedFish.add(fish);
//        }
//        return viewedFish;
//    }


    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}