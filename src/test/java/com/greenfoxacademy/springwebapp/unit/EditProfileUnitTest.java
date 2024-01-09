package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditProfileUnitTest {

  private UserRepository userRepository;
  private UserServiceImpl userService;

  public EditProfileUnitTest() {
    userRepository = Mockito.mock(UserRepository.class);
    userService = new UserServiceImpl(userRepository);
  }

  /*@Test
     void generateArticlesDTO_ReturnsAllArticlesInDTO() {
    var repo = Mockito.mock(ArticleRepository.class);
    Mockito.when(repo.findAll()).thenReturn(
        List.of(new Article("test article", "this is an amazing test article."),
            new Article("test article no.2", "this is the test article no.2"),
            new Article("test article no.3", "this is the test article no.3"))
    );

    ArticleServiceImpl articleService = new ArticleServiceImpl(repo);
    ArticlesDTO articlesDTO = new ArticlesDTO();
    articlesDTO.add(new Article("test article", "this is an amazing test article."));
    articlesDTO.add(new Article("test article no.2", "this is the test article no.2"));
    articlesDTO.add(new Article("test article no.3", "this is the test article no.3"));

    ArticlesDTO actualArticlesDTO = articleService.generateArticlesDTO();
    assertEquals(articlesDTO.getArticles().size(), actualArticlesDTO.getArticles().size());
  }
*/

  @Test
  public void editUserInformation_EditsUserInformation_ReturnsEditedUser() {
    var repo = Mockito.mock(UserRepository.class);
    Mockito.when(repo.findById(1)).thenReturn(
        Optional.of(new User("testUser", "testuser@gmail.com", "testuser123", "USER")));

    UserServiceImpl userService = new UserServiceImpl(repo);

    User testUser = new User("testUser", "testuser@gmail.com", "testuser123", "USER");
    User actualuser =  userService.editUserInformation(testUser);
    assertEquals(testUser, actualuser);

  }
}
