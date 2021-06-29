import colors as color
from solvers import Solver

if __name__ == "__main__":
    print(color.RED, "Этот файл нужен для вычислений, "
                     "если хотите запустить программу,"
                     "то запускайте \'main.py\'")


class Input:
    xy = []
    type_input = 0
    file_name = ""

    def __init__(self):
        self.type_input = 0
        self.xy = []
        self.file_name = ""
        self.input_xy()
        self.calculation()

    # Считывание исходных данных
    def input_xy(self):
        while True:
            try:
                print('\n', color.UNDERLINE + color.YELLOW, "Выберите формат ввода X и Y:", color.END)
                print('\t', "1. Файл", '\n',
                      '\t', "2. Консоль")
                self.type_input = int(input("Формат ввода: ").strip())

                if self.type_input == 1:
                    print('\n', color.UNDERLINE + color.YELLOW, "Вы выбрали формат ввода через файл.", color.END)
                    print(color.BOLD + color.YELLOW, "Формат файла:", color.END)
                    print('\t', "1 строка: не менее 12 точек по кординате Х", '\n',
                          '\t', "2 строка: не менее 12 точек по кординате Y", '\n',
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
                        print('\t', "1 строка: не менее 12 точек по кординате Х", '\n',
                              '\t', "2 строка: не менее 12 точек по кординате Y", '\n',
                              '\t', "Количество точек Х и Y должно быть одинаковое!")
                        x = list(input("Х: ").split())
                        y = list(input("Y: ").split())
                        if len(x) == len(y) and (len(x) and len(y)) >= 12:
                            for i in range(len(x)):
                                self.xy.append([float(x[i]), float(y[i])])
                            self.xy.sort()
                            # print("Координаты:", self.xy)
                            break
                        else:
                            get_ready_answer(1)
                            continue
                    break
                else:
                    get_ready_answer(1)
            except ValueError:
                get_ready_answer(1)
                # print("Ошибка ValueError")
            except TypeError:
                get_ready_answer(3)

    def calculation(self):
        while True:
            try:
                calculator = Solver(self.xy)
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
                if len(data[0]) == len(data[1]):  # and (len(data[0]) and len(data[1])) >= 12:
                    return data
                raise TypeError
        except FileNotFoundError:
            get_ready_answer(2)


def get_ready_answer(type_answer):
    answers = {
        1: color.BOLD + color.RED + "Неправильный ввод!" + color.END,
        2: color.BOLD + color.RED + "Файл не найден!" + color.END,
        3: color.BOLD + color.RED + "Неправильный формат данных в файле!" + color.END
    }
    print(answers.get(type_answer, color.BOLD + color.RED + "Неправильный выбор готового ответа!" + color.END))
