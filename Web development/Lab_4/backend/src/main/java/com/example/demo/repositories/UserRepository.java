package com.example.demo.repositories;

import com.example.demo.model.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User getByLogin(String login);
    User getByLoginAndPassword(String login, String password);

}
