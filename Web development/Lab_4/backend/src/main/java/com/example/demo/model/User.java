package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity @Data @NoArgsConstructor
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u"),
        @NamedQuery(name = User.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM User u WHERE u.login = :login AND u.password = :password"),

    }
)
public class User implements Serializable {

    public static final String FIND_ALL = "User.findAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "User.findByLoginAndPassword";

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    @Column @NotNull
    private String password;

    @Column @NotNull
    private String login;


    public void setLogin(String login) {
        this.login = login;
    }

    @Id
    public String getLogin() {
        return login;
    }
}
