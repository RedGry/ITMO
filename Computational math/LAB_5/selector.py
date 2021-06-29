import colors as color
import math
from solvers import Solver

if __name__ == "__main__":
    print(color.RED, "Этот файл нужен для вычислений, "
                     "если хотите запустить программу,"
                     "то запускайте \'main.py\'")


class Input:
    xy = []
    segment = []
    type_method = 0
    type_input = 0
    file_name = ""
    x = 0
    n = 0

    def __init__(self, type_method):
        self.type_method = type_method
        self.type_input = 0
        self.n = 0
        self.x = 0
        self.xy = []
        self.segment = []
        self.file_name = ""
        self.input_xy()
        self.input_argument()
        self.calculation()

    # Считывание исходных данных
    def input_xy(self):
        while True:
            try:
                print('\n', color.UNDERLINE + color.YELLOW, "Выберите формат ввода X и Y:", color.END)
                print('\t', "1. Файл", '\n',
                      '\t', "2. Консоль", '\n',
                      '\t', "3. Получить данные из функции")
                self.type_input = int(input("Формат ввода: ").strip())

                if self.type_input == 1:
                    print('\n', color.UNDERLINE + color.YELLOW, "Вы выбрали формат ввода через файл.", color.END)
                    print(color.BOLD + color.YELLOW, "Формат файла:", color.END)
                    print('\t', "1 строка: не менее 5 точек по кординате Х", '\n',
                          '\t', "2 строка: не менее 5 точек по кординате Y", '\n',
                          '\t', "Количество точек Х и Y должно быть одинаковое!")
                    print(color.BOLD + color.YELLOW,
                          "Введите полный путь к файлу, например (H://1/DOWLOAND/report.txt)", color.END)
                    self.file_name = input("Путь: ")
                    data = self.get_data_from_file()
                    for i in range(len(data[0])):
                        self.xy.append([data[0][i], data[1][i]])
                    self.xy.sort()
                    # print("Координаты:", self.xy)
                    break

                elif self.type_input == 2:
                    print('\n', color.UNDERLINE + color.YELLOW, "Вы выбрали формат ввода через консоль.", color.END)
                    while True:
                        print(color.BOLD + color.YELLOW, "Формат ввода:", color.END)
                        print('\t', "1 строка: не менее 5 точек по кординате Х", '\n',
                              '\t', "2 строка: не менее 5 точек по кординате Y", '\n',
                              '\t', "Количество точек Х и Y должно быть одинаковое!")
                        x = list(input("Х: ").split())
                        y = list(input("Y: ").split())
                        if len(x) == len(y) and (len(x) and len(y)) >= 5:
                            for i in range(len(x)):
                                self.xy.append([float(x[i]), float(y[i])])
                            self.xy.sort()
                            # print("Координаты:", self.xy)
                            break
                        else:
                            get_ready_answer(1)
                            continue
                    break

                elif self.type_input == 3:
                    print('\n', color.UNDERLINE + color.YELLOW, "Вы выбрали получение данных через функцию.", color.END)
                    while True:
                        print(color.BOLD + color.YELLOW, "Выберите функцию:", color.END)
                        print('\t', "1. √x", '\n',
                              '\t', "2. x²", '\n',
                              '\t', "3. sin(x)")
                        choice = int(input("Ваш выбор: ").strip())
                        func = get_fun(choice)
                        if func is None:
                            get_ready_answer(4)
                            continue
                        self.input_segment()
                        self.input_n()
                        self.xy = make_data(func, self.segment[0], self.segment[1], self.n)
                        self.xy.sort()
                        break
                    break
                else:
                    get_ready_answer(1)
            except ValueError:
                get_ready_answer(1)
                # print("Ошибка ValueError")
            except TypeError:
                get_ready_answer(3)

    def input_segment(self):
        print('\n', color.UNDERLINE + color.YELLOW, "Введите границы отрезка", color.END)
        print('\t', "Пример: 4 5")
        while True:
            try:
                self.segment = list(map(float, input("Границы: ").split()))
                if len(self.segment) != 2:
                    raise ValueError
                if self.segment[0] > self.segment[1]:
                    self.segment[0], self.segment[1] = self.segment[1], self.segment[0]
                break
            except ValueError:
                get_ready_answer(1)
                continue

    def input_n(self):
        if self.type_method == 2:
            print('\n', color.UNDERLINE + color.YELLOW, "Введите количество узлов интерполяции, не более 40", color.END)
        else:
            print('\n', color.UNDERLINE + color.YELLOW, "Введите количество узлов интерполяции", color.END)
        while True:
            try:
                self.n = int(input("Кол-во узлов: ").strip())
                if 2 > self.n:
                    get_ready_answer(5)
                    continue
                if self.n > 40 and self.type_method == 2:
                    get_ready_answer(6)
                    continue
                break
            except ValueError:
                get_ready_answer(1)
                continue

    def input_argument(self):
        print('\n', color.UNDERLINE + color.YELLOW, "Введите значение агрумента для интерполирования", color.END)
        while True:
            try:
                self.x = float(input("Значение: "))
                break
            except ValueError:
                get_ready_answer(1)
                continue

    def calculation(self):
        while True:
            try:
                calculator = Solver(self.type_method, self.xy, self.x)
                calculator.solve()
                del calculator
                break
            except TypeError:
                break
            except ValueError:
                break

    def get_data_from_file(self):
        try:
            with open(self.file_name) as f:
                data = [list(map(float, row.split())) for row in f.readlines()]
                if len(data[0]) == len(data[1]) and len(data[0]) >= 5 and len(data[1]) >= 5:
                    return data
                raise TypeError
        except FileNotFoundError:
            get_ready_answer(2)


# Выбор функции и получение значений
def get_fun(fun_id):
    if fun_id == 1:
        return lambda x: math.sqrt(x)
    elif fun_id == 2:
        return lambda x: math.pow(x, 2)
    elif fun_id == 3:
        return lambda x: math.sin(x)
    return None


def make_data(func, a, b, n):
    data = []
    h = (b - a) / (n - 1)
    for i in range(n):
        data.append([a, func(a)])
        a += h
    return data


def get_ready_answer(type_answer):
    answers = {
        1: color.BOLD + color.RED + "Неправильный ввод!" + color.END,
        2: color.BOLD + color.RED + "Файл не найден!" + color.END,
        3: color.BOLD + color.RED + "Неправильный формат данных в файле!" + color.END,
        4: color.BOLD + color.RED + "Фукнции нет в списке!" + color.END,
        5: color.BOLD + color.RED + "Количество узлов должно быть числом и больше 1!" + color.END,
        6: color.BOLD + color.RED + "Для метода Ньютона кол-во узлов не должно превышать 40!" + color.END,
    }
    print(answers.get(type_answer, color.BOLD + color.RED + "Неправильный выбор готового ответа!" + color.END))
