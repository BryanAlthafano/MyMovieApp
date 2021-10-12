package com.example.ptsganjil202111rpl1bryan6;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MovieModel extends RealmObject implements Comparable {

    @PrimaryKey
    private Integer id;
    private Boolean favorite;
    private String name;
    private String description;
    private String releaseDate;
    private String image;
    private String header;
    private String popularity;

    public MovieModel(Integer id, Boolean favorite, String name, String description, String releaseDate, String image, String header, String popularity) {
        this.id = id;
        this.favorite = favorite;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.image = image;
        this.header = header;
        this.popularity = popularity;
    }

    public MovieModel() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    @Override
    public int compareTo(Object o) {
        int compare = ((MovieModel)o).getId();
        return this.id-compare;
    }
}
