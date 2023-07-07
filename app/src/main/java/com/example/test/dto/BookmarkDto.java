package com.example.test.dto;

public class BookmarkDto {

    public int id;

    public String name;

    public String bredFor;
    public String img;

    public boolean bookmarkCheck;

    public BookmarkDto() {
        // Empty constructor
    }

    public BookmarkDto(BookmarkDto other) {
        this.id = other.id;
        this.name = other.name;
        this.bredFor = other.bredFor;
        this.img = other.img;
        this.bookmarkCheck = other.bookmarkCheck;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBredFor() {
        return bredFor;
    }

    public void setBredFor(String bredFor) {
        this.bredFor = bredFor;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String id) {
        this.img = img;
    }

    public boolean getBookmarkCheck() {
        return bookmarkCheck;
    }

    public void setBookmarkCheck(boolean bookmarkCheck) {
        this.bookmarkCheck = bookmarkCheck;
    }
}
