import colors as color
import solvers

print(color.BOLD + color.RED, "Решатель Нелинейных Уравнений!", color.END)

while True:
    try:
        print('\n', color.UNDERLINE + color.YELLOW + "Выберите уравнение или выход (цифра):" + color.END)
        print(color.GREEN,
              '\t', "1: x^2 - 3 = 0", '\n',
              '\t', "2: 5/x - 2x = 0", '\n',
              '\t', "3: e^(2x) - 2 = 0", '\n',
              '\t', "4: -1.8x^3 - 2.94x^2 + 10.37x + 5.38 = 0", '\n',
              '\t', "5: Выход", color.END)

        choice = int(input("Вариант: ").strip())
        if choice == 1:
            new_input = solvers.Input(1)
            del new_input
            continue
        elif choice == 2:
            new_input = solvers.Input(2)
            del new_input
            continue
        elif choice == 3:
            new_input = solvers.Input(3)
            del new_input
            continue
        elif choice == 4:
            new_input = solvers.Input(4)
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
