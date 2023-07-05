package com.example.test.dto;

import com.google.gson.annotations.SerializedName;

public class DogDto {
    // 사진, 이름, 품종, Weight, Height, Temperament, Life span, BookMark_check
    Image image;
    int id;
    String name;
    String bred_for;
    Weight weight;
    Height height;

    @SerializedName("temperament")
    String temperament;
    @SerializedName("life_span")
    String lifeSpan;
    boolean bookmark_check;
    int bookmark_img;
    int index;

    public DogDto(int id, String name, String bred_for, String temperament, String lifeSpan, boolean bookmark_check, int bookmark_img, int index) {
        this.id = id;
        this.name = name;
        this.bred_for = bred_for;
        this.weight = weight;
        this.height = height;
        this.temperament = temperament;
        this.lifeSpan = lifeSpan;
        this.bookmark_check = bookmark_check;
        this.bookmark_img = bookmark_img;
        this.index = index;
    }

    public DogDto(DogDto other){
        this.id = other.id;
        this.name = other.name;
        this.bred_for = other.bred_for;
        this.weight = other.weight;
        this.height = other.height;
        this.temperament = other.temperament;
        this.lifeSpan = other.lifeSpan;
        this.bookmark_check = other.bookmark_check;
        this.bookmark_img = other.bookmark_img;
        this.index = other.index;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public String getBred_for() {
        return bred_for;
    }

    public void setBred_for(String bred_for) {
        this.bred_for = bred_for;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public String getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public boolean getBookmark_check() {
        return bookmark_check;
    }

    public void setBookmark_check(boolean bookmark_check) {
        this.bookmark_check = bookmark_check;
    }

    public int getBookmark_img() {
        return bookmark_img;
    }

    public void setBookmark_img(int bookmark_img) {
        this.bookmark_img = bookmark_img;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}


//sample data
//        {
//        "weight": {
//        "imperial": "6 - 13",  - 미국식
//        "metric": "3 - 6"      - 미터법
//        },
//        "height": {
//        "imperial": "9 - 11.5",
//        "metric": "23 - 29"
//        },
//        "id": 1,
//        "name": "Affenpinscher",   - 이름
//        "bred_for": "Small rodent hunting, lapdog",  - 품종
//        "breed_group": "Toy",
//        "life_span": "10 - 12 years",
//        "temperament": "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
//        "origin": "Germany, France",
//        "reference_image_id": "BJa4kxc4X",
//        "image": {
//        "id": "BJa4kxc4X",
//        "width": 1600,
//        "height": 1199,
//        "url": "https://cdn2.thedogapi.com/images/BJa4kxc4X.jpg"  - 이미지
//        }
//        }
