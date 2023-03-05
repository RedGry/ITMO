package Utils;

import model.Dot;
import javax.enterprise.inject.Model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Model
public class DBManager implements Serializable {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Data");
    EntityManager entityManager = entityManagerFactory.createEntityManager();


    //Добавить точку в БД
    public boolean addPoint(Dot dot){
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(dot);
            entityManager.getTransaction().commit();
            return true;
        }catch (Exception e){
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    //Получение 50 точек из БД
    public ArrayList<Dot> getPoints(){
        try {
            entityManager.getTransaction().begin();
            List dotList = entityManager.createQuery("SELECT dote FROM Dot dote")
                    .setMaxResults(50)
                    .getResultList();
            entityManager.getTransaction().commit();
            return (ArrayList<Dot>) dotList;
        }catch (Exception e){
            return new ArrayList<Dot>();
        }
    }

}
