package com.example.oauth2.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  Map<String, User> repository = new ConcurrentHashMap<>();

  public void save(User user) {
    repository.put(user.getEmail(), user);
  }

  public User findBy(String email) {
    return repository.get(email);
  }

  public boolean exists(String email) {
    return repository.containsKey(email);
  }

}