import colors as color
import selector as select

if __name__ == "__main__":
    print(color.BOLD + color.RED, "Аппроксмация функции методом наименьших квадратов!", color.END)
    while True:
        print('\n', color.UNDERLINE + color.YELLOW, "Что хотите сделать?", color.END)
        print(color.GREEN,
              '\t', "1. Начать вычисление", '\n',
              '\t', "2. Выход!", color.END)
        choice = int(input("Введите номер дейсивя: ").strip())

        if choice == 1:
            new_input = select.Input()
            del new_input
            continue
        elif choice == 2:
            print(color.BOLD + color.PURPLE, 'Удачи!', color.END)
            break
        else:
            print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
            continue
