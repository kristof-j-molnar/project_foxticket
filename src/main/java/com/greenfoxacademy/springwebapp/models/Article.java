package com.greenfoxacademy.springwebapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Articles")
public class Article {
<<<<<<< HEAD

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String content;
  @Column(name = "PUBLISHDATE")
  @JsonProperty("publish_date")
  private Instant publishDate;

  public Article() {
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
}
=======
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String content;
  @JsonProperty("publish_date")
  private LocalDate publishDate;

  public Article() {
    this.publishDate = LocalDate.now();
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

  public LocalDate getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(LocalDate publishDate) {
    this.publishDate = publishDate;
  }
}
>>>>>>> 6af8150 (add styling changes and evironment variables in application.properties)
