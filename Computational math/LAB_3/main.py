import colors as color
import selector as select

print(color.BOLD + color.RED, "Решатель интегралов!", color.END)

while True:
    try:
        print('\n', color.UNDERLINE + color.YELLOW + "Выберите функцию или выход (цифра):" + color.END)
        print(color.GREEN,
              '\t', "1: x^2 - 3", '\n',
              '\t', "2: 5/x - 2x", '\n',
              '\t', "3: e^(2x) - 2", '\n',
              '\t', "4: 2x^3 - 3x^2 + 5x - 9", '\n',
              '\t', "5: Выход", color.END)

        choice = int(input("Вариант: ").strip())
        if choice == 1:
            new_input = select.Input(1)
            del new_input
            continue
        elif choice == 2:
            new_input = select.Input(2)
            del new_input
            continue
        elif choice == 3:
            new_input = select.Input(3)
            del new_input
            continue
        elif choice == 4:
            new_input = select.Input(4)
            del new_input
            continue
        elif choice == 5:
            print(color.BOLD + color.PURPLE, 'Удачи!', color.END)
            break
        else:
            print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
            continue
    except TypeError:
        print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
        continue
    except ValueError:
        continue
