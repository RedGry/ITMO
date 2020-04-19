package p3111.redgry.collection.helpers;

import com.opencsv.CSVWriter;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.collection.PersonDTO;
import p3111.redgry.utils.CsvReader;
import p3111.redgry.utils.CsvWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * Класс, реализующий сервис для коллекции StackPersonStorage.
 */
public class StackPersonStorageService implements StorageService{
    private static Storage<Long,Person> st;

    /**
     * Конструктор, который инциализирует коллекцию с которой работать.
     *
     * @param st коллекция, с которой будет работать сервис.
     */
    public StackPersonStorageService(Storage<Long,Person> st){
        StackPersonStorageService.st = st;
    }

    /**
     * Возвращает информаци о коллекции.
     * @return строка с информацией о коллекии.
     */
    public String info(){
        StringBuilder sb = new StringBuilder();
        sb.append("Время инциализации коллекции: ").append(st.getInitializationTime().toString()).append("\n");
        sb.append("Количество элементов в коллекции: ").append(st.size()).append("\n");
        sb.append("Тип коллекции: ").append(st.getCollectionClass().getName());
        return sb.toString();
    }

    /**
     * Возвращает все элементы коллекции.
     * @return Строку со всеми элементами коллекции.
     */
    @Override
    public String show() {
        StringBuilder sb = new StringBuilder("Элементов в коллекции: " + st.size()).append("\n");
        st.toList().forEach((key,person) -> sb.append(person.toString()).append("\n"));
        return sb.toString();
    }

    /**
     * Возвращает размер коллекции.
     * @return кол-во элементов в коллекции.
     */
    @Override
    public int size() {
        return st.size();
    }

    /**
     * Очищает коллекции.
     */
    @Override
    public void clear() {
        st.clear();
    }

    /**
     * Удаляет элемент коллекции по его ID.
     * @param id элемента коллекции.
     * @return True - удаление выполнено, False - удаление не было выполнено.
     */
    @Override
    public boolean removeById(long id) {
        HashMap<Long,Person> personToDelete = st.toList();
        if (!personToDelete.containsKey(id)) {
            return false;
        }
        personToDelete.remove(id);
        return true;
    }

    /**
     * Удаляет элемент коллекции по его Key.
     * @param key ключ введенный пользователем.
     * @return True - удаление выполнено, False - удаление не было выполнено.
     */
    @Override
    public boolean removeByKey(long key) {
        HashMap<Long,Person> personToDelete = st.toList();
        if (!personToDelete.containsKey(key)) {
            return false;
        }
        personToDelete.remove(key);
        return true;
    }

    /**
     * Удаляет все элементы коллекции, которые превышают заданный ключ.
     * @param key ключ ввденный пользователем.
     * @return True - удаление выполнено, False - удаление не было выполнено.
     */
    @Override
    public boolean removeGreaterKey(long key) {
        HashMap<Long,Person> personToDelete = st.toList();
        List<String> listKey = new ArrayList<>();
        if (personToDelete.isEmpty()) {
            return false;
        }
        for(int i = 0; personToDelete.keySet().toArray().length > i; i++ ) {
            long keyPerson = Long.parseLong(personToDelete.keySet().toArray()[i].toString());
            if (keyPerson > key) {
                listKey.add(personToDelete.keySet().toArray()[i].toString());
            }
        }
        if(!listKey.isEmpty()){
            for(int i = 0; listKey.toArray().length > i; i++){
                personToDelete.remove(Long.parseLong(listKey.toArray()[i].toString()));
                System.out.println("Персонаж с ключем: " + listKey.toArray()[i] + " удален.");
            }
        }
        return true;
    }

    /**
     * Удаляет все элементы коллекции, которые превышают заданную Дату Рождения.
     * @param date дата ввденная пользователем.
     * @return True - удаление выполнено, False - удаление не было выполнено.
     */
    @Override
    public boolean removeAnyByBirthday(String date) {
        HashMap<Long,Person> personToDelete = st.toList();
        List<String> listDate = new ArrayList<>();
        List<Long> listKey = new ArrayList<>(st.getPersons().keySet());
        int count = 0;
        personToDelete.forEach((x, y) -> {
            listDate.add(y.getBirthday().format(ISO_LOCAL_DATE));
        });
        for (int i = 0; listDate.toArray().length > i; i++){
            if (listDate.toArray()[i].toString().equals(date)){
                personToDelete.remove(listKey.toArray()[i]);
                count++;
                System.out.println("Персонаж с ключем: " + listKey.toArray()[i] + " удален.");
            }
        }
        if(count == 0){
            return false;
        }
        return true;
    }

    /**
     * Обновляет информацию об элементе коллекции.
     * @param id ID элемента коллекции.
     * @param person обьект.
     * @return True - обновление выполнено, False - данного элемента нет в коллекции.
     */
    @Override
    public boolean update(long id, Person person) {
        if (!removeById(id)) {
            return false;
        }
        person.setId(id);
        st.put(id, person);
        return true;
    }

    /**
     * Возвращает кол-во элементов в коллекции, которые меньше чем произведение X,Y,Z Локации экзепляра.
     * @param v число введенное пользователем.
     * @return Количество элементов меньше v.
     */
    @Override
    public String CountLessThanLocation(double v) {
        Integer count = 0;
        StringBuilder sb = new StringBuilder("Элементов в коллекции меньше по произведению координат локации равному " + v + ": " );
        StringBuilder sb2 = new StringBuilder();
        st.toList().forEach((key,person) -> sb2.append(person.getLocation().getX() * person.getLocation().getY() * person.getLocation().getZ() < v).append(";"));
        for (int i = 0; sb2.toString().split(";").length > i; i++){
            //System.out.println(sb2.toString().split(";")[i]);
            if (sb2.toString().split(";")[i].equals("true")){
                count++;
            }
        }
        sb.append(count);
        return sb.toString();
    }

    /**
     * Создает новый экземпряр Person в коллекции.
     * @param person обьект.
     * @param key ключ.
     */
    @Override
    public void add(Person person, Long key) {
        st.put(key, person);
    }

    @Override
    public void save1(String pathToFile) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            PersonDTO[] person = new PersonDTO[st.size()];
            st.toList().values().stream().map(PersonDTO::new).collect(Collectors.toList()).toArray(person);
            CsvWriter.saveCollectionToFile(person, pathToFile);
        } catch (IOException e){
            throw e;
        }
    }

    /**
     * Сохраняет коллекцию в файл CSV
     * @param pathToFile путь к файлу, в который мы хотим сохранить.
     * @throws IOException Пробрасывается от команды, в случае если команда работает с I/O и произошла ошибка.
     */
    @Override
    public void save(String pathToFile) throws IOException {
        try(OutputStreamWriter fw1 = new OutputStreamWriter(new FileOutputStream(pathToFile)); CSVWriter writer = new CSVWriter(fw1, CsvReader.getDelimiter().charAt(0), '"', '"', "\n")){
            for (Person person : st.getPersons().values()){
                writer.writeNext(new String[]{
                        person.getName(),
                        String.valueOf(person.getCoordinates().getX()),
                        String.valueOf(person.getCoordinates().getY()),
                        String.valueOf(person.getHeight()),
                        person.getBirthday().format(ISO_LOCAL_DATE),
                        person.getPassportID(),
                        person.getHairColor().getRus(),
                        String.valueOf(person.getLocation().getX()),
                        String.valueOf(person.getLocation().getY()),
                        String.valueOf(person.getLocation().getZ()),
                        person.getLocation().getName()});
            }
        } catch (IOException e){
            System.out.print("");
        }
    }

    /**
     * Выводит список ключей колекции отсортированный в порядке возрастания.
     */
    @Override
    public void display() {
        HashMap<Long,Person> personList = st.toList();
        List<Long> listKey = new ArrayList<>(st.getPersons().keySet());
        /**
        personList.forEach((x, y) -> {
            listKey.add(y.getId());
        });
         */
        Collections.sort(listKey);
        if (!personList.isEmpty()) {
            for (long key : listKey) {
                System.out.println(key);
                /**
                System.out.println("id элемента: " + personList.get(listKey.get(i)).getId());
                System.out.println("    Имя: " + personList.get(listKey.get(i)).getName());
                System.out.println("    Расположение по X: " + personList.get(listKey.get(i)).getCoordinates().getX());
                System.out.println("    Расположение по Y: " + personList.get(listKey.get(i)).getCoordinates().getY());
                System.out.println("    Дата инициализации персонажа: " + personList.get(listKey.get(i)).getCreationDate().format(ISO_LOCAL_DATE));
                System.out.println("    Рост: " + personList.get(listKey.get(i)).getHeight());
                System.out.println("    Дата рождения: " + toStringWithNull(personList.get(listKey.get(i)).getBirthday().format(ISO_LOCAL_DATE)));
                System.out.println("    ID паспорта: " + toStringWithNull(personList.get(listKey.get(i)).getPassportID()));
                System.out.println("    Цвет Волос: " + personList.get(listKey.get(i)).getHairColor().getRus());
                System.out.println("    Название Местности: " + personList.get(listKey.get(i)).getLocation().getName());
                System.out.println("    Расположение по X в " + personList.get(listKey.get(i)).getLocation().getName() + ": " + personList.get(listKey.get(i)).getLocation().getX());
                System.out.println("    Расположение по Y в " + personList.get(listKey.get(i)).getLocation().getName() + ": " + personList.get(listKey.get(i)).getLocation().getY());
                System.out.println("    Расположение по Z в " + personList.get(listKey.get(i)).getLocation().getName() + ": " + personList.get(listKey.get(i)).getLocation().getZ());
                */
            }
        } else System.out.println("Коллекция пуста.");
    }

    private String toStringWithNull(Object o){
        if (o == null) return "отсутствует";
        return o.toString();
    }

    /**
     * Проверяет сущетсвует ли обьект в коллекции с данным ключем.
     * @param key ключ.
     * @return True - не существует, False -  существует.
     */
    @Override
    public boolean checkKey(long key){
        return !st.getPersons().containsKey(key);
    }
}
