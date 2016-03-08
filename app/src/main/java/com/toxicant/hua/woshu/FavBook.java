package com.toxicant.hua.woshu;

/**
 * Created by hua on 2016/3/8.
 */
public class FavBook {
    private String isbn;
    private String image;
    private String name;

    @Override
    public String toString() {
        return "FavBook{" +
                "isbn='" + isbn + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
