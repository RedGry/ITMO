
import java.lang.String;
import java.lang.Math.*;

public class Main {
    public static void main(String[] args) {
        // Создаем место
        Place Com_Malish = new Place("Комната Малыша", new Coordinates(10, 10));

        // Создаем персонажей и обьекты
        OBJ persik = new OBJ("Персик");
        Human mama = new Human("Мама", Gender.female);
        Human karlson = new Human("Карлсон", Gender.male, HandType.podgy, 10, 10);
        Human malish = new Human("Малыш", Gender.male, HandType.small, 10, 10);

        // Добавляем способности персонажам ... (Coming soon)


        //Указываем действия персонажам
        System.out.println();
        mama.took(persik.x, persik.y);
        mama.put(persik.getX(2), persik.getY(5));
        System.out.println();
        karlson.see();
        karlson.jump("Персик", persik.x, persik.y);
        System.out.println();
        karlson.Compress(persik.getName());
        System.out.println();
    }
}
