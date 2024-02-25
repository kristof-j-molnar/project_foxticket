package com.greenfoxacademy.springwebapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "articles")
@JsonIgnoreProperties(value = {"deleted"})
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String content;
  @Column(name = "PUBLISHDATE")
  @JsonProperty("publish_date")
  private Instant publishDate;

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  public Article() {
    isDeleted = false;
    this.publishDate = Instant.now();
  }

  public Article(String title, String content) {
    this();
    this.title = title;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Instant getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(Instant publishDate) {
    this.publishDate = publishDate;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }
}