package p3111.redgry.collection.helps;

import p3111.redgry.collection.collection.Person;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class StackPersonStorage implements Storage<Long, Person> {
    private LinkedHashMap<Long,Person> persons = new LinkedHashMap<>();
    private static final HashSet<Integer> idList = new HashSet<>();
    private final Date creationDate;
    private final Set<Long> idSet = new HashSet<>();
    private long maxId = 0;


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

    public void setPersons(Person persons){
        this.persons.put(persons.getId(), persons);
    }

    @Override
    public void putAll(LinkedHashMap<Long, Person> linkedHashMap){
        persons.putAll(linkedHashMap);
    }


    @Override
    public void clear(){
        persons.clear();
    }


    @Override
    public Date getInitializationTime(){
        return creationDate;
    }

    @Override
    public Class<?> getCollectionClass() {
        return persons.getClass();
    }


    @Override
    public int size() {
        return persons.size();
    }


    @Override
    public LinkedHashMap<Long, Person> toList() {
        return persons;
    }


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
    public static long numberGeneratePlusOne(){
        count++;
        return count;
    }
}
