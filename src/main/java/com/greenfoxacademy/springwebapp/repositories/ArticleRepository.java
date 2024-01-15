package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  @Query("SELECT a FROM Article a WHERE a.title LIKE %:title% OR a.content LIKE %:content%")
  List<Article> findByTitleContainingOrContentContaining(@Param("title") String title, @Param("content") String content);
}
