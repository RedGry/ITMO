import math
import matplotlib.pyplot as plt
import numpy as np
from tabulate import tabulate
import warnings
import colors as color

warnings.filterwarnings("ignore", category=RuntimeWarning)

if __name__ == "__main__":
    print(color.RED, "Этот файл нужен для вычислений, "
                     "если хотите запустить программу,"
                     "то запускайте \'main.py\'")


class Input:
    type_equation = 0
    type_method = 0
    type_input = 0
    a = 0
    b = 0
    accuracy = 0
    file_name = "file_name"

    def __init__(self, type_equation):
        self.type_equation = type_equation
        self.choose_boundaries()
        self.choose_accuracy()
        self.calculation()

    def choose_boundaries(self):
        while True:
            try:
                print(color.UNDERLINE + color.YELLOW, "Выберите формат ввода границ:", color.END)
                print('\t', "1. Файл", '\n',
                      '\t', "2. Консоль")
                self.type_input = int(input("Формат ввода: ").strip())

                if self.type_input == 1:
                    print(color.UNDERLINE + color.YELLOW, "Вы выбрали формат ввода границ через файл.", color.END)
                    print(color.BOLD + color.YELLOW, "Формат файла:", color.END)
                    print('\t', "1 строка: с границами выбранного сегмента, например: -10 10", '\n',
                          '\t', "2 строка: кол-во знаков после запятой для подсчета решения, например: 3")
                    print(color.BOLD + color.YELLOW,
                          "Введите полный путь к файлу, например (H://1/DOWLOAND/report.txt)", color.END)
                    self.file_name = input("Путь: ")
                    if answers_fun(self.type_equation, float(self.get_data_from_file()[0][0]),
                                   float(self.get_data_from_file()[0][1])) == 1:
                        self.a = float(self.get_data_from_file()[0][0])
                        self.b = float(self.get_data_from_file()[0][1])
                        print("Промежуток: [", self.a, ",", self.b, "]")
                        break
                    elif answers_fun(self.type_equation, float(self.get_data_from_file()[0][0]),
                                     float(self.get_data_from_file()[0][1])) == 0:
                        print(color.BOLD + color.RED,
                              "На промежутке нет корней, попробуйте выбрать другой промежуток!", color.END)
                        continue
                    else:
                        print(color.BOLD + color.RED,
                              "На промежутке находится несколько корней, Попробуйте выбрать другой промежуток!",
                              color.END)
                        continue

                elif self.type_input == 2:
                    print(color.UNDERLINE + color.YELLOW, "Вы выбрали формат ввода границ через консоль.", color.END)
                    print(color.BOLD + color.YELLOW, "Формат ввода границ сегмента, например: -10 10", color.END)
                    segment = list(input("Введите границы сегмента: ").split())
                    if len(segment) == 2 and float(segment[0].strip()) < float(segment[1].strip()):
                        if answers_fun(self.type_equation, float(segment[0].strip()), float(segment[1].strip())) == 1:
                            self.a = float(segment[0].strip())
                            self.b = float(segment[1].strip())
                            break
                        elif answers_fun(self.type_equation, float(segment[0].strip()), float(segment[1].strip())) == 0:
                            print(color.BOLD + color.RED,
                                  "На промежутке нет корней, попробуйте выбрать другой промежуток!", color.END)
                            continue
                        elif answers_fun(self.type_equation, float(segment[0].strip()),
                                         float(segment[1].strip())) == "inf":
                            print(color.BOLD + color.RED,
                                  "Промежуток поподает на участок, где функция не определена! Поменяйте промежуток.",
                                  color.END)
                            continue

                        else:
                            print(color.BOLD + color.RED,
                                  "На промежутке находится несколько корней, Попробуйте выбрать другой промежуток!",
                                  color.END)
                            continue
                    else:
                        get_ready_answer(1)
                        continue
            except ValueError:
                get_ready_answer(1)
            except TypeError:
                get_ready_answer(1)

    def choose_accuracy(self):
        while True:
            try:
                if self.type_input == 1:
                    if float(self.get_data_from_file()[1][0]) <= 0:
                        get_ready_answer(1)
                        continue
                    else:
                        self.accuracy = float(self.get_data_from_file()[1][0])
                        print("Точность: ", self.accuracy)
                        break
                elif self.type_input == 2:
                    print(color.UNDERLINE + color.YELLOW, "Введите кол-во знаков после запятой для подсчета решения.",
                          color.END)
                    accuracy = float(input("Точность: ").strip())
                    if accuracy <= 0:
                        get_ready_answer(1)
                        continue
                    else:
                        self.accuracy = accuracy
                        break
            except ValueError:
                get_ready_answer(1)
            except TypeError:
                get_ready_answer(1)

    def calculation(self):
        while True:
            try:
                calculator = MathMethodLogic(self.type_equation, self.a, self.b, self.accuracy)
                while True:
                    print(color.BOLD + color.YELLOW, "Выберите метод решения:", color.END)
                    print('\t', "1. Половинного деления", '\n',
                          '\t', "2. Метод Ньютона", '\n',
                          '\t', "3. Простой итерации")
                    type_method = int(input("Тип метода (цифра): ").strip())
                    if type_method == 1:
                        calculator.calc_method_bisection()
                        calculator.print_table(1)
                        break
                    elif type_method == 2:
                        calculator.calc_method_newton()
                        calculator.print_table(2)
                        break
                    elif type_method == 3:
                        calculator.calc_method_iter()
                        if calculator.status == 0:
                            calculator.print_table(3)
                        break
                    else:
                        get_ready_answer(1)
                        continue

                print_result(calculator)
                while True:
                    print("Сохранить вывод в файл?")
                    print('\t', "• Да", '\n',
                          '\t', "• Нет")
                    type_output = input("Введите Да/Нет: ").strip()
                    if type_output.lower() == "да":
                        with open("output.txt", "w") as file:
                            if type_method == 1:
                                calculator.bisection_table.insert(0, ["№ шага", "a", "b", "x", "f(a)", "f(b)", "f(x)",
                                                                      "|a-b|"])
                                file.write(tabulate(calculator.bisection_table, tablefmt="grid"))
                            elif type_method == 2:
                                calculator.newton_table.insert(0, ["№ итерации", "x(k)", "f(x(k))", "f'(x(k))", "x(k+1)",
                                                                   "|x(k) - x(k+1)|"])
                                file.write(tabulate(calculator.newton_table, tablefmt="grid"))
                            elif type_method == 3:
                                calculator.iter_table.insert(0, ["№ итерации", "x(k)", "f(x(k))", "x(k+1)", "\u03C6(x(k))",
                                                                 "|x(k) - x(k+1)|"])
                                file.write(tabulate(calculator.iter_table, tablefmt="grid"))
                            break
                    elif type_output.lower() == "нет":
                        break

                del calculator
                break
            except ValueError:
                get_ready_answer(1)
            except TypeError:
                get_ready_answer(5)

    def get_data_from_file(self):
        try:
            with open(self.file_name) as f:
                data = [list(map(float, row.split())) for row in f.readlines()]
                return data
        except FileNotFoundError:
            get_ready_answer(2)


# END class Input

class MathMethodLogic:
    previous_count = 0  # Для запоминания предыдущего значения вычислений
    x0 = 0
    x1 = 0
    lambda_param = 0  # Значение лямбды для итер. метода

    status = 0
    solvable = 1
    type_equation = 0
    type_solver = 0
    a = 0
    b = 0
    steps = 0
    accuracy = 0
    result = 0
    segments = []

    bisection_table = []
    newton_table = []
    iter_table = []

    def __init__(self, type_equation, a, b, accuracy):
        self.type_equation = type_equation
        self.a = a
        self.b = b
        self.accuracy = accuracy
        self.segments = []
        self.steps = 0
        self.solvable = 1
        self.status = 0
        self.previous_count = 0
        self.result = 0
        self.accuracy = math.pow(10, -1 * accuracy)
        self.x0 = a
        self.x1 = b

    def calc_method_bisection(self):
        self.bisection_table = []
        self.type_solver = 1
        self.steps = 0
        if abs(self.a - self.b) > self.accuracy:
            while True:
                self.steps += 1
                x = (self.a + self.b) / 2
                self.bisection_table.append(
                    [self.steps, self.a, self.b, x,
                     self.function(self.a), self.function(self.b), self.function(x),
                     abs(self.a - self.b)]
                )
                count_x = [x]
                self.segments.append(count_x)
                if self.function(self.a) * self.function(x) > 0:
                    self.a = x
                else:
                    self.b = x
                if abs(self.a - self.b) <= self.accuracy:
                    self.steps += 1
                    x = (self.a + self.b) / 2
                    self.result = x
                    self.bisection_table.append(
                        [self.steps, self.a, self.b, x,
                         self.function(self.a), self.function(self.b), self.function(x),
                         abs(self.a - self.b)]
                    )
                    break
        else:
            self.status = 2
            print_result(self)

    def calc_method_newton(self):
        self.newton_table = []
        self.type_solver = 2
        self.steps = 0

        # Выбор приближения:
        self.result = self.select_approximation()

        while True:
            self.steps += 1
            if self.solvable and self.steps < 250000:
                self.solve_newton()
                self.newton_table.append(
                    [self.steps, self.previous_count, self.function(self.previous_count),
                     self.first_derivative(self.previous_count), self.result, abs(self.result - self.previous_count)]
                )
                count_x = [self.result, self.previous_count]
                count_y = [0, self.function(self.previous_count)]
                segment = [count_x, count_y]
                self.segments.append(segment)
                if abs(self.result - self.previous_count) < self.accuracy and abs(
                        self.function(self.result)) < self.accuracy and (self.a <= self.result <= self.b):
                    self.accuracy = abs(self.result - self.previous_count)
                    break
                else:
                    continue
            else:
                if self.steps == 250000:
                    self.status = 3
                else:
                    self.status = 1
                break

    def calc_method_iter(self):
        self.iter_table = []
        self.type_solver = 3
        self.steps = 0

        self.lambda_param = 1 / max(self.first_derivative(self.a), self.first_derivative(self.b))
        self.result = self.select_approximation_iter()   # Выбор приближения если выполняется условие f(x)*f''(x) > 0

        self.dop_info_iter(self.a)  # Выводит всю информацию для левого значения промежутка
        self.dop_info_iter(self.b)  # Выводит всю информацию для правого значения промежутка

        if abs(1 - self.lambda_param * self.first_derivative(self.result)) < 1:
            while True:
                self.steps += 1
                if self.solvable and self.steps < 250000:
                    self.previous_count = self.result
                    self.result = self.previous_count - self.lambda_param * self.function(self.previous_count)

                    # Для заполнения таблицы
                    self.iter_table.append(
                        [self.steps, self.previous_count, self.function(self.previous_count), self.result,
                         self.result + self.lambda_param * self.function(self.result),
                         abs(self.previous_count - self.result)]
                    )

                    # Для точек на графике
                    count_x = [self.previous_count, self.previous_count]
                    count_y = [self.function(self.previous_count), self.result]
                    segment = [count_x, count_y]
                    self.segments.append(segment)

                    # Условие выхода
                    if abs(self.result - self.previous_count) < self.accuracy and abs(
                            self.function(self.result)) < self.accuracy:
                        self.accuracy = abs(self.result - self.previous_count)
                        break
                    else:
                        continue
                else:
                    if self.steps == 250000:
                        self.status = 3
                    else:
                        self.status = 1
                    break
        else:
            self.status = 2

    # Значение функции в точке
    def function(self, x):
        try:
            if self.type_equation == 1:
                return math.pow(x, 2) - 3
            elif self.type_equation == 2:
                return 5 / x - 2 * x
            elif self.type_equation == 3:
                return math.exp(2 * x) - 2
            elif self.type_equation == 4:
                return -1.8 * math.pow(x, 3) - 2.94 * math.pow(x, 2) + 10.37 * x + 5.38
        except ZeroDivisionError:
            return self.function(x + 1e-8)
        except OverflowError:
            self.status = 3

    # Значение Первой производной функции в точке
    def first_derivative(self, x):
        try:
            if self.type_equation == 1:
                return 2 * x
            elif self.type_equation == 2:
                return -5 * math.pow(x, -2) - 2
            elif self.type_equation == 3:
                return 2 * math.exp(2 * x)
            elif self.type_equation == 4:
                return -5.4 * math.pow(x, 2) - 5.88 * x + 10.37
        except ZeroDivisionError:
            return self.first_derivative(x + 1e-8)

    # Значение Второй производной функции в точке
    def second_derivative(self, x):
        try:
            if self.type_equation == 1:
                return 2
            elif self.type_equation == 2:
                return 10 * math.pow(x, -3)
            elif self.type_equation == 3:
                return 4 * math.exp(2 * x)
            elif self.type_equation == 4:
                return -10.8 * x - 5.88
        except ZeroDivisionError:
            return self.second_derivative(x + 1e-8)

    # Выбор приближения
    def select_approximation(self):
        if (self.function(self.a) * self.second_derivative(self.a)) > 0:
            return self.a
        elif (self.function(self.b) * self.second_derivative(self.b)) > 0:
            return self.b

    def select_approximation_iter(self):
        if (abs(1 - self.lambda_param * self.first_derivative(self.a)) < 1) and (abs(1 - self.lambda_param * self.first_derivative(self.b)) < 1):
            x = np.linspace(self.a, self.b, 100)
            for i in x:
                #print(i, ": ", self.function(i), self.second_derivative(i), 1 - self.lambda_param * self.first_derivative(i))
                #if (self.function(i) * self.second_derivative(i) > 0) and (abs(1 - self.lambda_param * self.first_derivative(i)) < 1):
                # if abs(1 - self.lambda_param * self.first_derivative(i)) < 1:
                if self.function(i) * self.second_derivative(i) > 0:
                    return i

    def solve_newton(self):
        try:
            self.previous_count = self.result
            self.result = self.result - self.function(self.result) / self.first_derivative(self.result)
        except TypeError:
            self.result = self.result - self.function(self.result + 1e-8) / self.first_derivative(self.result + 1e-8)
        except ValueError:
            self.result = self.result - self.function(self.result + 1e-8) / self.first_derivative(self.result + 1e-8)
        except ZeroDivisionError:
            self.result = self.result - self.function(self.result + 1e-8) / self.first_derivative(self.result + 1e-8)

    # Вывод таблицы
    def print_table(self, type_table):
        if type_table == 1:
            print(color.BOLD + color.YELLOW, "Метод половинного деления:", color.END)
            print(tabulate(self.bisection_table, headers=["№ шага", "a", "b", "x", "f(a)", "f(b)", "f(x)", "|a-b|"],
                           tablefmt="grid", floatfmt="2.5f"))
        elif type_table == 2:
            print(color.BOLD + color.YELLOW, "Метод Ньютона:", color.END)
            print(tabulate(self.newton_table,
                           headers=["№ итерации", "x(k)", "f(x(k))", "f'(x(k))", "x(k+1)", "|x(k) - x(k+1)|"],
                           tablefmt="grid", floatfmt="2.5f"))
        elif type_table == 3:
            print(color.BOLD + color.YELLOW, "Метод простой итерации:", color.END)
            print(tabulate(self.iter_table,
                           headers=["№ итерации", "x(k)", "f(x(k))", "x(k+1)", "\u03C6(x(k+1))", "|x(k) - x(k+1)|"],
                           tablefmt="grid", floatfmt="2.5f"))

    # Вывод дополнительной информации для метода простых итераций
    def dop_info_iter(self, x):
        print("x = ", x)
        print("f(x) = ", self.function(x))
        print("f''(x) = ", self.second_derivative(x))
        print("f(x) * f''(x) > 0 ? ", (self.function(x) * self.second_derivative(x)) > 0 if "Да" else "Нет")
        print("\u03C6'(x) = ", 1 - self.lambda_param * self.first_derivative(x))
        print("|\u03C6'(x)| < 1 ? ", abs(1 - self.lambda_param * self.first_derivative(x)) < 1 if "Да" else "Нет")
        print("\u03BB = ", 1 / self.first_derivative(x), "(Значение, если взять точку x для вчисления \u03BB, "
                                                         "для подсчета \u03C6(x) взято макс значение по модулю)")
        print('')


# END class MathMethodLogic

def get_ready_answer(type_answer):
    answers = {
        1: color.BOLD + color.RED + "Неправильный ввод!" + color.END,
        2: color.BOLD + color.RED + "Файл не найден!" + color.END,
        3: color.BOLD + color.RED + "Нет решений." + color.END,
        4: color.BOLD + color.RED + "Конкретного решения нет, или его не существует!" + color.END,
        5: color.BOLD + color.RED + "Условие сходимости на выбраном отрезке не выполнен!" + color.END,
        6: color.BOLD + color.RED + "Количетво итераций слишком большое и решение не было найдено!" + color.END,
        7: color.BOLD + color.RED + "Начальное приближение было выбрано плохо, решение не было найдено!" + color.END
    }
    print(answers.get(type_answer, color.BOLD + color.RED + "Неправильный выбор готового ответа!" + color.END))


# Создание графика расчетов
def draw_graph(calculator):
    try:
        ax = plt.gca()
        plt.grid()
        # Убираем видимость верхней и правой границы сетки
        ax.spines['top'].set_visible(False)
        ax.spines['right'].set_visible(False)

        minimum = calculator.a
        maximum = calculator.b
        lambda_p = calculator.lambda_param
        id_num = 1
        for segment in calculator.segments:
            # Точки для первого метода
            if calculator.type_solver == 1:
                minimum = calculator.x0
                maximum = calculator.x1
                plt.scatter(segment[0], 0, color="blue", s=20)
                plt.annotate("x[" + str(id_num) + "]",
                             (segment[0], 0),
                             textcoords="offset points",
                             xytext=(0, 5),
                             ha="center")
                id_num += 1
            # Касательные для второго метода
            elif calculator.type_solver == 2:
                if segment[0][0] < minimum:
                    minimum = segment[0][0]
                elif segment[0][1] > maximum:
                    maximum = segment[0][1]
                plt.plot(segment[0], segment[1], color="blue")
                plt.annotate("x[" + str(id_num) + "]",
                             (segment[0][0], 0),
                             textcoords="offset points",
                             xytext=(0, 5),
                             ha="center")
                id_num += 1
            # Увеличение размера графика, если не все вмещается
            elif calculator.type_solver == 3:
                if segment[0][0] < minimum:
                    minimum = segment[0][0]
                elif segment[0][1] > maximum:
                    maximum = segment[0][1]

        x = np.linspace(minimum, maximum, 100)
        equations = {
            1: ["f(x) = x^2 - 3", [(math.pow(i, 2) - 3) for i in x]],
            2: ["f(x) = 5/x - 2x", [(5 / i - 2 * i) for i in x]],
            3: ["f(x) = e^(2x) - 2", [(math.exp(2 * i) - 2) for i in x]],
            4: ["f(x) = -1.8x^3 - 2.94x^2 + 10.37x + 5.38",
                [(-1.8 * math.pow(i, 3) - 2.94 * math.pow(i, 2) + 10.37 * i + 5.38) for i in x]]
        }

        # Вывод графика для метода итераций
        fi_equations = {
            1: ["\u03C6(x) = x + " + str(lambda_p) + " * (x^2 - 3)",
                [(i + lambda_p * (math.pow(i, 2) - 3)) for i in x]],
            2: ["\u03C6(x) = x + " + str(lambda_p) + " * (5/x - 2x)",
                [(i + lambda_p * (5 / i - 2 * i)) for i in x]],
            3: ["\u03C6(x) = x + " + str(lambda_p) + " * (e^(2x) - 2)",
                [(i + lambda_p * (math.exp(2 * i) - 2)) for i in x]],
            4: ["\u03C6(x) = x + " + str(lambda_p) + " * (-1.8x^3 - 2.94x^2 + 10.37x + 5.38)",
                [(i + lambda_p * (-1.8 * math.pow(i, 3) - 2.94 * math.pow(i, 2) + 10.37 * i + 5.38)) for i in x]]
        }

        if calculator.type_solver == 3:
            plt.plot(x, fi_equations[calculator.type_equation][1], color="red", linewidth=2)
            for segment in calculator.segments:
                # plt.vlines(segment[0], segment[1][0], segment[1][1], color="blue")
                # Точка на f(x)
                plt.scatter(segment[0][0], segment[1][0], color="blue", s=20)
                plt.annotate("x[" + str(id_num) + "]",
                             (segment[0][0], segment[1][0]),
                             textcoords="offset points",
                             xytext=(0, 5),
                             ha="center")
                # Точка на fi(x)
                plt.scatter(segment[0][1], segment[1][1], color="blue", s=20)
                plt.annotate("\u03C6(x[" + str(id_num) + "])",
                             (segment[0][1], segment[1][1]),
                             textcoords="offset points",
                             xytext=(0, 5),
                             ha="center")
                id_num += 1

        # Настройка графика
        plt.title("График: " + equations[calculator.type_equation][0])
        plt.plot(x, equations[calculator.type_equation][1], color="green", linewidth=2)
        plt.plot(x, 0 * x, color="black", linewidth=1)
        plt.scatter(calculator.result, 0, color="red", s=50)

        # Вывод графика
        plt.show()
        del x
    except ValueError:
        return
    except ZeroDivisionError:
        return
    except OverflowError:
        return


# Вывод результатов расчетов
def print_result(calculator):
    if calculator.solvable == 1:
        if calculator.status == 0:
            print()
            print(color.YELLOW + "Корень уравнения: " + color.END, calculator.result)
            print(color.YELLOW + "Количество итераций: " + color.END, calculator.steps)
            print(color.YELLOW + "Точность ответа: " + color.END, calculator.accuracy)
            print(color.YELLOW + "Значение f(x) в корне: " + color.END,
                  calculator.function(calculator.result))
            if calculator.type_solver == 3:
                print(color.YELLOW + "Значение \u03C6(x) в корне: " + color.END,
                      calculator.result + calculator.lambda_param * calculator.function(calculator.result))

            draw_graph(calculator)
        elif calculator.status == 1:
            get_ready_answer(4)
        elif calculator.status == 2:
            get_ready_answer(5)
        elif calculator.status == 3:
            get_ready_answer(6)
        elif calculator.status == 4:
            get_ready_answer(7)
        elif calculator.status == 5:
            get_ready_answer(4)
    else:
        get_ready_answer(3)


# Проверка корня на выбранном промежутки для функций
def answers_fun(type_equation, a, b):
    equations_answers = {
        1: [-1.732, 1.732],
        2: [-1.581, 1.581],
        3: [0.347],
        4: [-3.158, -0.474, 1.998]
    }
    if type_equation == 1:
        if a < equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[1] < b:
            return 2
        elif a > equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[1] > b:
            return 0
        else:
            return 1
    elif type_equation == 2:
        if a < equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[1] < b:
            return 2
        elif a > equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[1] > b:
            return 0
        elif a < 0 < b:
            return "inf"
        else:
            return 1
    elif type_equation == 3:
        if a > equations_answers.get(type_equation)[0] or equations_answers.get(type_equation)[0] > b:
            return 0
        else:
            return 1
    elif type_equation == 4:
        if (a < equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[2] < b) or (
                a < equations_answers.get(type_equation)[0] and equations_answers.get(type_equation)[1] < b) or (
                a < equations_answers.get(type_equation)[1] and equations_answers.get(type_equation)[2] < b):
            return 2
        elif b < equations_answers.get(type_equation)[0] or a > equations_answers.get(type_equation)[2] or (
                a > equations_answers.get(type_equation)[0] and b < equations_answers.get(type_equation)[1]) or (
                a > equations_answers.get(type_equation)[1] and b < equations_answers.get(type_equation)[2]):
            return 0
        else:
            return 1
