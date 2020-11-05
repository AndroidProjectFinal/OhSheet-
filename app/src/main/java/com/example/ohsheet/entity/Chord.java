package com.example.ohsheet.entity;

public class Chord {
    private String id;
    private String name;
    private String link;

    public Chord() {
    }

    public Chord(String name, String link) {
        this.name = name;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
