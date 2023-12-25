package com.example.bookapp2.ModelClass;



public class RecommandedModelClass {


    Integer image;
    String title,rating,author_name;


    public RecommandedModelClass(Integer image, String title, String rating, String author_name) {
        this.image = image;
        this.title = title;
        this.rating = rating;
        this.author_name = author_name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
