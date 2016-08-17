package com.example.hachimori.retrofitsample.model;

/**
 * Created by benhachimori on 2016/08/14.
 */
public class User {
    private String name;
    private String company;
    private String email;
    private String bio;
    private String created_at;
    private String updated_at;
    
    public String getName() {
        return name;
    }
    
    public String getCompany() {
        return company;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getBio() {
        return bio;
    }
    
    public String getCreatedAt() {
        return created_at;
    }
    
    public String getUpdatedAt() {
        return updated_at;
    }
}
