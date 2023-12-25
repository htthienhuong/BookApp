package com.example.bookapp2.Model;

import java.util.ArrayList;

public class Order {
    private String address;
    private ArrayList<Book> items;
    private String phone;
    private Long sum;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Book> getItems() {
        return items;
    }

    public void setItems(ArrayList<Book> items) {
        this.items = items;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
// Constructors, getters, and setters
    // Đảm bảo tạo constructor và các phương thức getter và setter cho các trường dữ liệu
}