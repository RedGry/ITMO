package p3111.redgry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serialization{
    public static <T> byte[] SerializeObject(T object, String login, String password) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);) {
            objectOutputStream.writeUTF(login);
            objectOutputStream.writeUTF(password);
            objectOutputStream.writeObject(object);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Ошибка сериализации");
            e.printStackTrace();
        }
        return null;
    }
}
