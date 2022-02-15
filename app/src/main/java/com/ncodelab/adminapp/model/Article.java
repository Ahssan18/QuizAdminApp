package com.ncodelab.adminapp.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Article {

    String articleTitle, articleImageUrl,articleDescription,url,articleId;

    public Article() {
    }

    public Article(String articleTitle, String articleImageUrl, String articleDescription, String url,String articleId) {
        this.articleTitle = articleTitle;
        this.articleImageUrl = articleImageUrl;
        this.articleDescription = articleDescription;
        this.url = url;
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @ServerTimestamp
    private Date uploadTime;

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
