package p3111.redgry.utils;

import java.io.*;

public class Serialization implements Serializable{
    public static <T> byte[] SerializeObject(T object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);) {
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Ошибка сериализации");
            e.printStackTrace();
        }
        return null;
    }


    public <T> T DeserializeObject(byte[] buffer) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);) {
            return (T) objectInputStream.readObject();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка десериализации");
            e.printStackTrace();
        }
        return null;
    }
}
