package p3111.redgry.collection.helpers;

import p3111.redgry.collection.collection.Person;

import java.io.IOException;

/**
 * Интерфейс для сервиса, который будет отвечать за всю бизнес-логику необходимую для приложения.
 */
public interface StorageService {


    String info();

    String show();

    int size();

    void clear();

    boolean removeById(long id);

    boolean removeByKey(long key);

    boolean removeGreaterKey(long key);

    boolean removeAnyByBirthday(String date);

    boolean update(long id, Person person);

    boolean checkKey(long key);

    String CountLessThanLocation(double v);

    void add(Person person, Long key);

    void save(String pathToFile) throws IOException;

    String display();

    Person returnPerson();
}
