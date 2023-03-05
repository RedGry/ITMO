package com.example.demo.controllers;


import com.example.demo.model.Point;
import com.example.demo.model.User;
import com.example.demo.requests.PointDTO;
import com.example.demo.services.PointService;
import com.example.demo.services.UserService;
import com.example.demo.utils.sec.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/point")
public class PointController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final PointService pointService;

    @Autowired
    public PointController(UserService userService, JWTUtil jwtUtil, PointService pointService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.pointService = pointService;
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> addPoint(@Valid @RequestBody PointDTO data, HttpServletRequest req){
        User user = userService.findByLogin(jwtUtil.getUsername(jwtUtil.resolveToken(req)));
        Point point = shittyMethod(data);
        point.setLogin(user.getLogin());
        pointService.addPoint(point);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<Point>> getPoints(HttpServletRequest req){
        User user = userService.findByLogin(jwtUtil.getUsername(jwtUtil.resolveToken(req)));
        return new ResponseEntity<>(pointService.getAllPointByLogin(user.getLogin()), HttpStatus.OK);
    }


    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> deletePoints(HttpServletRequest req){
        User user = userService.findByLogin(jwtUtil.getUsername(jwtUtil.resolveToken(req)));
        pointService.deleteAllByLogin(user.getLogin());
        return new ResponseEntity<>("Все ваши точки удалены", HttpStatus.OK);
    }


    private Point shittyMethod(PointDTO p){
        @NotNull @Min(value = -3) @Max(value = 5)double x = Double.parseDouble(p.getX());
        @NotNull @Min(value = -5) @Max(value = 3)double y = Double.parseDouble(p.getY());
        @NotNull @Min(value = -3) @Max(value = 5)double r = Double.parseDouble(p.getR());
        return new Point(x, y, r);
    }



}
