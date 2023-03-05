package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findByLogin(String login){
        return userRepository.getByLogin(login);
    }

    @Transactional
    public User findByLoginAndPassword(String login, String password){
        return userRepository.getByLoginAndPassword(login, HashUtil.digestPassword(password));
    }

    @Transactional
    public String register(String login, String password){
        try {
            User user = new User(login, HashUtil.digestPassword(password));
            userRepository.save(user);
            return "Success";
        }catch (Exception e){
            return "Error";
        }

    }

    @Transactional
    public String deleteByUser(User user){
        try {
            userRepository.delete(user);
            return "Success";
        }catch (Exception e){
            return "Error";
        }
    }
}
