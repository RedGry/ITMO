package com.example.demo.repositories;

import com.example.demo.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {

    List<Point> getAllByLogin(String login);
    void deleteAllByLogin(String login);
}
