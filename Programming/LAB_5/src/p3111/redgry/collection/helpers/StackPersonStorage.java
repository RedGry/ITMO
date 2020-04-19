package p3111.redgry.collection.helpers;

import p3111.redgry.collection.collection.Person;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Реализация Storage. Хранит Персонажей в стэке.
 */
public class StackPersonStorage implements Storage<Long,Person> {
    private LinkedHashMap<Long,Person> persons = new LinkedHashMap<>();
    private static final HashSet<Integer> idList = new HashSet<>();
    private final Date creationDate;
    private final Set<Long> idSet = new HashSet<>();
    private long maxId = 0;

    /**
     * Конструктор, который инциализирует время создания обьекта.
     */
    public StackPersonStorage(){
        creationDate = new Date();
    }

    public LinkedHashMap<Long, Person> getPersons(){
        return persons;
    }

    @Override
    public void remove(Long aLong, Person person) {
          persons.remove(person.getId(), person);
    }

    public void setPersons(LinkedHashMap<Long, Person> persons){
        this.persons = persons;
    }

    /**
     * Метод отвечающий за очитку коллекции.
     */
    @Override
    public void clear(){
        persons.clear();
    }

    /**
     * Метод возвращающий дату создания обьекта.
     * @return дата создания обькта.
     */
    @Override
    public Date getInitializationTime(){
        return creationDate;
    }

    @Override
    public Class<?> getCollectionClass() {
        return persons.getClass();
    }

    /**
     * Возвращает количество экземпляров в коллекции.
     * @return кол-во экземпляров.
     */
    @Override
    public int size() {
        return persons.size();
    }

    /**
     * Преобразовывает коллекцию в LinkedHashMap.
     *
     * @return LinkedHashMap, с элементами из стэка
     */
    @Override
    public LinkedHashMap<Long, Person> toList() {
        return persons;
    }

    /**
     * Добавляет персонажа в стэк на определнную позицию.
     * @param key ключ экземпряра.
     * @param person персонаж, который будет добавлен в стэк.
     */
    @Override
    public void put(Long key, Person person) {
        while (idSet.contains(person.getId())){
            person.setId(maxId);
            maxId = Math.max(maxId, person.getId() + 1);
        }
        persons.put(key, person);
        idSet.add(person.getId());
    }

    /**
     * Генератор ID для Person (принцип - рандомное число).
     *
     * @return сгенерированный ID.
     */
    public static int numberGenerate(){
        int id = (int) (Math.random() * 200000);
        boolean False = true;
        while (False){
            //System.out.println(id + " " + idList);
            if (idList.contains(id)){
                id = (int) (Math.random() * 300000);
            }
            else {
                False = false;
                idList.add(id);
            }
        }
        return id;
    }

    private static int count = 0;

    /**
     * Генератор ID для Person (принцип - 1++).
     *
     * @return сгенерированный ID.
     */
    public static int numberGeneratePlusOne(){
        count++;
        return count;
    }
}
