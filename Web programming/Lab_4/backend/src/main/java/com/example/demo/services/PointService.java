package com.example.demo.services;


import com.example.demo.model.Point;
import com.example.demo.repositories.PointRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }
    @Transactional
    public List<Point> getAllPointByLogin(String login){
        return pointRepository.getAllByLogin(login);
    }

    @Transactional
    public void addPointByLogin(Point point, String login){
        point.setLogin(login);
        pointRepository.save(point);
    }

    @Transactional
    public void addPoint(Point point){
        pointRepository.save(point);
    }

    @Transactional
    public void deleteAllByLogin(String login){
        pointRepository.deleteAllByLogin(login);
    }
}

