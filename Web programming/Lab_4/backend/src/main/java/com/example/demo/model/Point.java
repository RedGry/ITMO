package com.example.demo.model;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.demo.model.Point.*;



@Entity @Data
@Table(name = "points")
@NamedQueries({
        @NamedQuery(name = FIND_BY_LOGIN, query = "SELECT p FROM Point p WHERE p.login = :login"),
        @NamedQuery(name = FIND_ALL, query = "SELECT p FROM Point p"),
        @NamedQuery(name = DELETE_ALL_BY_LOGIN, query = "DELETE FROM Point p WHERE p.login = :login")
})
public class Point implements Serializable{


    public static final String FIND_ALL = "Point.findAll";
    public static final String FIND_BY_LOGIN = "Point.findByLogin";
    public static final String DELETE_ALL_BY_LOGIN = "Point.deleteAll";

    @Id
    @Column(nullable = false, unique = true, name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.time = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(new Date(System.currentTimeMillis()));
        checkResult(x, y, r);
    }

    //Text [-3 ... 5] для координаты по оси X,
    //Text [-5 ... 3] для координаты по оси Y,
    //Text [-3 ... 5] для задания радиуса области

    @Column @NotNull @Min(value = -3) @Max(value = 5) private double x;

    @Column @NotNull @Min(value = -5) @Max(value = 3) private double y;

    @Column @NotNull @Min(value = -3) @Max(value = 5) private double r;

    @Column(nullable = false)
    private boolean result;

    @Column(name = "currenttime")
    private String time;

    @Column(name = "owner")
    private String login;


    public Point() {

    }

    private void checkResult(double x, double y, double r){
        this.result = positiveRadius(x, y, r) || negativeRadius(x, y, r);

    }

    private boolean positiveRadius(double x, double y, double r){
        return ((  (x >= 0 && y >= 0 && x*x + y*y <= r*r/4)// 1 Четверть
                || (x <= 0 && y >= 0 && x >= -r/2 && y <= r)// 2 Четверть
                || (x >= 0 && y <= 0 && y >= x/2 - r/2 && y >= -r/2 && x <= r))// 4 Четверть
              ) && (r >= 0 && r <= 5);
    }


    private boolean negativeRadius(double x, double y, double r){
        double r1 = Math.abs(r);
        double x1 = -x;
        double y1 = -y;
        return ((  (x1 <= 0 && y1 >= 0 && x1 >= -r1 && y1 <= r1/2 && y1<=x1/2 + r1/2)// 2 Четверть
                || (x1 <= 0 && y1 <= 0 && x1*x1 + y1*y1 <= r1*r1/4)// 3 Четверть
                || (x1 >= 0 && y1 <= 0 && y1 >= -r1 && x1<= r1/2))// 4 Четверть
              ) && (r < 0 && r >= -3);
    }

}
