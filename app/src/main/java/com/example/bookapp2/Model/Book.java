package com.example.bookapp2.Model;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookId;
    private String bookName;
    private String authorName;
    private String price;

    private String quantity;
    private boolean selected;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String description;
    private String imageUrl; // URL của ảnh trong Firebase Storage

    public Book() {
        // Empty constructor required for Firebase
    }

    public Book(String bookName, String authorName, String price, String description, String imageUrl,String quantity) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }
    public Book(String bookName, String authorName, String price, String description, String quantity) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.price = price;
        this.description = description;

        this.quantity = quantity;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // Getters and setters
}