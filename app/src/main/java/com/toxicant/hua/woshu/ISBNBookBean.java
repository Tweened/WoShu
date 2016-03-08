package com.toxicant.hua.woshu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hua on 2016/3/7.
 */
public class ISBNBookBean implements Serializable{

    /**
     * max : 10
     * numRaters : 2
     * average : 0.0
     * min : 0
     */

    private RatingEntity rating;//评分
    private String subtitle;
    private String pubdate;//出版日期
    private String origin_title;
    private String image;//图片的url
    private String binding;
    private String catalog;
    private String pages;
    private String id;//书本id
    private String publisher;//出版社
    private String isbn10;
    private String isbn13;
    private String title;//标题
    private String url;
    private String alt_title;//副标题
    private String price;//价格
    private List<String> author;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String summary;//概要

    public void setRating(RatingEntity rating) {
        this.rating = rating;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public RatingEntity getRating() {
        return rating;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public String getImage() {
        return image;
    }

    public String getBinding() {
        return binding;
    }

    public String getCatalog() {
        return catalog;
    }

    public String getPages() {
        return pages;
    }

    public String getId() {
        return id;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getAuthor() {
        return author;
    }

    public static class RatingEntity implements Serializable {
        private int max;
        private int numRaters;
        private String average;
        private int min;

        public void setMax(int max) {
            this.max = max;
        }

        public void setNumRaters(int numRaters) {
            this.numRaters = numRaters;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public int getNumRaters() {
            return numRaters;
        }

        public String getAverage() {
            return average;
        }

        public int getMin() {
            return min;
        }
    }
}
