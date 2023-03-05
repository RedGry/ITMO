package p3111.redgry.collection.helpers;

import p3111.redgry.collection.collection.Person;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Интерфейс для коллекции.
 *
 * @param <K> ключ элемента коллекции.
 * @param <T> тип элементов коллекции.
 */
public interface Storage<K,T> {

    void clear();

    Date getInitializationTime();

    Class<?> getCollectionClass();

    int size();

    LinkedHashMap<Long, Person> toList();

    void put(K key, T person);

    void putAll(LinkedHashMap<Long, Person> linkedHashMap);

    LinkedHashMap<Long, T> getPersons();

    void remove(Long aLong, Person person);
}
