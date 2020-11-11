package com.example.ohsheet.entity;

import java.util.List;

public class Song {
    private String id;
    private String title;
    private String writer;
    private String sheet;
    private String content;
    private int level;
    private String listGenre;
    private String createBy;
    private String createDate;
    private String linkMusic;

    public Song() {
    }

    public Song(String title, String writer, String sheet, int level, String createDate, String linkMusic) {
        this.title = title;
        this.writer = writer;
        this.sheet = sheet;
        this.level = level;
        this.createDate = createDate;
        this.linkMusic = linkMusic;
    }

    public Song(String title, String writer, String sheet, String content, int level, String createDate, String linkMusic) {
        this.title = title;
        this.writer = writer;
        this.sheet = sheet;
        this.content = content;
        this.level = level;
        this.createDate = createDate;
        this.linkMusic = linkMusic;
    }

    public Song(String title, String writer, String sheet,String linkMusic, String content) {
        this.title = title;
        this.writer = writer;
        this.sheet = sheet;
        this.linkMusic = linkMusic;
        this.content = content;

    }

    public Song(String id, String title, String writer) {
        this.id = id;
        this.title = title;
        this.writer = writer;
    }

    public Song(String title, String writer, String sheet, String content, int level, String listGenre, String createBy, String createDate, String linkMusic) {
        this.title = title;
        this.writer = writer;
        this.sheet = sheet;
        this.content = content;
        this.level = level;
        this.listGenre = listGenre;
        this.createBy = createBy;
        this.createDate = createDate;
        this.linkMusic = linkMusic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getListGenre() {
        return listGenre;
    }

    public void setListGenre(String listGenre) {
        this.listGenre = listGenre;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLinkMusic() {
        return linkMusic;
    }

    public void setLinkMusic(String linkMusic) {
        this.linkMusic = linkMusic;
    }

    @Override
    public String toString() {
        return title;
    }
}
