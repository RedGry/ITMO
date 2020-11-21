package model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity // Данный бин (класс) является сущностью
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Dot implements Serializable {


    @Id
    @Column(nullable = false, unique = true, name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(nullable = false, name = "x")
    private Double x;

    @Column(nullable = false, name = "y")
    private Double y;

    @Column(nullable = false, name = "r")
    private Double r = 0.1D;

    @Column(nullable = false, name = "result")
    private boolean result;

    @Column(nullable = false, name = "currenttime")
    private String currentTime;

    public String getStringResult(){
        if(this.result){
            return "Попал!";
        }else{
            return "Мимо!";
        }
    }
}



