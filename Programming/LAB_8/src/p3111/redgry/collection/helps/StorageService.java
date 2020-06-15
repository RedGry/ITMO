package p3111.redgry.collection.helps;

import p3111.redgry.collection.collection.Person;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public interface StorageService {
    String info();

    String show();

    int size();

    void clear();

    boolean removeById(long id);

    boolean removeByKey(long key);

    List<String> removeGreaterKey(long key);

    List<Long> removeAnyByBirthday(String date);

    boolean update(long id, Person person);

    boolean checkKey(long key);

    String CountLessThanLocation(double v);

    void add(Person person, Long key);

    String display();

    Person returnPerson();

    void addAll(LinkedHashMap<Long, Person> linkedHashMap);

    LinkedHashMap<Long, Person> list();
}
