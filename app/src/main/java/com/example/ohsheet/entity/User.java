package com.example.ohsheet.entity;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String password;
    private List<Song> listFavorite;

    public User() {
    }

    public User(String name, String password, List<Song> listFavorite) {
        this.name = name;
        this.password = password;
        this.listFavorite = listFavorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Song> getListFavorite() {
        return listFavorite;
    }

    public void setListFavorite(List<Song> listFavorite) {
        this.listFavorite = listFavorite;
    }
}
