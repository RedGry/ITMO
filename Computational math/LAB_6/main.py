import colors as color
import selector as select

if __name__ == "__main__":
    print(color.BOLD + color.RED, "Численное дифференцирование!", color.END)
    while True:
        try:
            print('\n', color.UNDERLINE + color.YELLOW, "Выберите метод дифференцирования или выход.", color.END)
            print(color.GREEN,
                  '\t', "1. Усовершенствованный метод Эйлера", '\n',
                  '\t', "2. Метод Милна", '\n',
                  '\t', "3. Выход!", color.END)
            choice = int(input("Введите номер дейсивя: ").strip())

            if choice == 1:
                new_input = select.Input(1)
            elif choice == 2:
                new_input = select.Input(2)
            elif choice == 3:
                print(color.BOLD + color.PURPLE, 'Удачи!', color.END)
                break
            else:
                print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
                continue
            del new_input
        except ValueError:
            print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
