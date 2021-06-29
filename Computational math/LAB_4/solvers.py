import warnings
import math
import colors as color
import numpy as np
from tabulate import tabulate
import matplotlib.pyplot as plt
import traceback
from LinearSystem import SolverSystem

warnings.filterwarnings("ignore", category=RuntimeWarning)

if __name__ == "__main__":
    print(color.RED, "Этот файл нужен для вычислений, "
                     "если хотите запустить программу,"
                     "то запускайте \'main.py\'")


class Solver:
    x = []
    y = []
    n = 0

    fi_y = []
    coefficients = []

    deviation = [0, 0, 0, 0, 0]
    std_deviation = []
    R_sec = []

    tablet = []
    best_eq_id = -1

    status_eq = [0, 0, 0]
    status_save = 0

    # Линейная функций (0):
    line_eq = [0, 0, 0, 0]  # x, x^2, y, xy
    '''
        x^2 * a + x * b = xy
        x   * a + n * b = y
    '''

    # Полиномиальная функция 2-й степени (1):
    sec_eq = [0, 0, 0, 0, 0, 0, 0]  # x, x^2, x^3, x^4, y, xy, x^2y
    '''
        n   * a + x   * b + x^2 * c = y
        x   * a + x^2 * b + x^3 * c = xy
        x^2 * a + x^3 * b + x^4 * c = x^2y
    '''

    # Экспонециальная функция (2):
    exp_eq = [0, 0, 0, 0]  # x, x^2, ln(y), x*ln(y)
    '''
        x^2 * a + x * b = x*ln(y)
        x   * a + n * b = ln(y)
    '''

    # Логарифмическая функция (3):
    log_eq = [0, 0, 0, 0]  # ln(x), (ln(x))^2, y, y*ln(x)
    '''
        (ln(x))^2 * a + ln(x) * b = y*ln(x)
        ln(x)     * a + n     * b = y
    '''

    # Степенная функция (4):
    pow_eq = [0, 0, 0, 0]  # ln(x), (ln(x))^2, ln(y), ln(y)*ln(x)
    '''
        (ln(x))^2 * a + ln(x) * b = ln(y)*ln(x)
        ln(x)     * a + n     * b = ln(y)
    '''

    def __init__(self, data):
        self.n = len(data)
        self.x = []
        self.y = []
        self.fi_y = []
        self.deviation = [0, 0, 0, 0, 0]
        self.std_deviation = []
        self.R_sec = []
        self.coefficients = []
        self.tablet = []
        self.best_eq_id = -1
        self.status_eq = [0, 0, 0]
        self.line_eq = [0, 0, 0, 0]
        self.sec_eq = [0, 0, 0, 0, 0, 0, 0]
        self.exp_eq = [0, 0, 0, 0]
        self.log_eq = [0, 0, 0, 0]
        self.pow_eq = [0, 0, 0, 0]

        # Подсчитываем нужные коэффиценты для нахождения (a, b, c) уравнений
        for i in range(self.n):

            self.line_eq[0] += data[i][0]
            self.line_eq[1] += data[i][0] ** 2
            self.line_eq[2] += data[i][1]
            self.line_eq[3] += data[i][1] * data[i][0]

            self.sec_eq[0] += data[i][0]
            self.sec_eq[1] += data[i][0] ** 2
            self.sec_eq[2] += data[i][0] ** 3
            self.sec_eq[3] += data[i][0] ** 4
            self.sec_eq[4] += data[i][1]
            self.sec_eq[5] += data[i][1] * data[i][0]
            self.sec_eq[6] += data[i][1] * data[i][0] ** 2

            if data[i][1] > 0:
                self.exp_eq[0] += data[i][0]
                self.exp_eq[1] += data[i][0] ** 2
                self.exp_eq[2] += math.log(data[i][1])
                self.exp_eq[3] += math.log(data[i][1]) * data[i][0]
            else:
                self.status_eq[0] = 1
            if data[i][0] > 0:
                self.log_eq[0] += math.log(data[i][0])
                self.log_eq[1] += math.log(data[i][0]) ** 2
                self.log_eq[2] += data[i][1]
                self.log_eq[3] += data[i][1] * math.log(data[i][0])
            else:
                self.status_eq[1] = 1
            if data[i][0] > 0 and data[i][1] > 0:
                self.pow_eq[0] += math.log(data[i][0])
                self.pow_eq[1] += math.log(data[i][0]) ** 2
                self.pow_eq[2] += math.log(data[i][1])
                self.pow_eq[3] += math.log(data[i][1]) * math.log(data[i][0])
            else:
                self.status_eq[2] = 1
            self.x = [data[i][0] for i in range(len(data))]
            self.y = [data[i][1] for i in range(len(data))]

    def solve(self):
        self.calc_coefficients()
        self.calc_fi_y()
        self.calc_deviation()
        self.calc_std_deviation()
        self.acc_approximation([0, 0])
        print('\n', color.UNDERLINE + color.YELLOW, "Выберите дествие:", color.END)
        print('\t', "1. Вывести на экран результат", '\n',
              '\t', "2. Сохранить в файл результат")
        while True:
            try:
                choice = int(input("Выберите действие: ").strip())
                if choice == 1:
                    self.print_result()
                    self.status_save = 0
                    break
                elif choice == 2:
                    self.save_in_file()
                    self.status_save = 1
                    break
            except TypeError:
                continue
            except ValueError:
                continue
        self.draw_graph()

        # Коэффициенты
        '''
        print("Линейная:", self.coefficients[0], '\n',
              "Квадратная:", self.coefficients[1], '\n',
              "Экспонециальная:", self.coefficients[2], '\n',
              "Логарифмическая:", self.coefficients[3], '\n',
              "Степенная:", self.coefficients[4])
        '''

    # Вычисление коэффициентов для функций
    def calc_coefficients(self):
        self.coefficients.append([(self.n * self.line_eq[3] - self.line_eq[2] * self.line_eq[0]) /
                                  # -----------------------------------------------------------
                                  (self.n * self.line_eq[1] - self.line_eq[0] ** 2),
                                  #############################################################
                                  (self.line_eq[2] * self.line_eq[1] - self.line_eq[3] * self.line_eq[0]) /
                                  # -----------------------------------------------------------
                                  (self.n * self.line_eq[1] - self.line_eq[0] ** 2)])

        solve = SolverSystem(3, [[self.n, self.sec_eq[0], self.sec_eq[1], "|", self.sec_eq[4]],
                                 [self.sec_eq[0], self.sec_eq[1], self.sec_eq[2], "|", self.sec_eq[5]],
                                 [self.sec_eq[1], self.sec_eq[2], self.sec_eq[3], "|", self.sec_eq[6]]])
        solve.calculate()
        self.coefficients.append(solve.get_x())

        if self.status_eq[0] == 0:
            self.coefficients.append([math.exp((self.exp_eq[2] * self.exp_eq[1] - self.exp_eq[3] * self.exp_eq[0]) /
                                               # -----------------------------------------------------------
                                               (self.n * self.exp_eq[1] - self.exp_eq[0] ** 2)),
                                      #############################################################
                                      (self.n * self.exp_eq[3] - self.exp_eq[2] * self.exp_eq[0]) /
                                      # -----------------------------------------------------------
                                      (self.n * self.exp_eq[1] - self.exp_eq[0] ** 2)])
        else:
            self.coefficients.append([100, 100])
        if self.status_eq[1] == 0:
            self.coefficients.append([(self.n * self.log_eq[3] - self.log_eq[2] * self.log_eq[0]) /
                                      # -----------------------------------------------------------
                                      (self.n * self.log_eq[1] - self.log_eq[0] ** 2),
                                      #############################################################
                                      (self.log_eq[2] * self.log_eq[1] - self.log_eq[3] * self.log_eq[0]) /
                                      # -----------------------------------------------------------
                                      (self.n * self.log_eq[1] - self.log_eq[0] ** 2)])
        else:
            self.coefficients.append([100, 100])
        if self.status_eq[2] == 0:
            self.coefficients.append([math.exp((self.pow_eq[2] * self.pow_eq[1] - self.pow_eq[3] * self.pow_eq[0]) /
                                               # -----------------------------------------------------------
                                               (self.n * self.pow_eq[1] - self.pow_eq[0] ** 2)),
                                      #############################################################
                                      (self.n * self.pow_eq[3] - self.pow_eq[2] * self.pow_eq[0]) /
                                      # -----------------------------------------------------------
                                      (self.n * self.pow_eq[1] - self.pow_eq[0] ** 2)])
        else:
            self.coefficients.append([100, 100])

    # Вычисление y для каждой из функций
    def calc_fi_y(self):
        for x in self.x:
            if [0, 1, 1] == self.status_eq:
                self.fi_y.append([self.coefficients[0][0] * x + self.coefficients[0][1],
                                  self.coefficients[1][0] * x ** 2 + self.coefficients[1][1] * x + self.coefficients[1][2],
                                  self.coefficients[2][0] * math.exp(self.coefficients[2][1] * x),
                                  0,
                                  0])
            elif [1, 0, 1] == self.status_eq:
                self.fi_y.append([self.coefficients[0][0] * x + self.coefficients[0][1],
                                  self.coefficients[1][0] * x ** 2 + self.coefficients[1][1] * x + self.coefficients[1][2],
                                  0,
                                  self.coefficients[3][0] * math.log(x) + self.coefficients[3][1],
                                  0])
            elif [1, 1, 1] == self.status_eq:
                self.fi_y.append([self.coefficients[0][0] * x + self.coefficients[0][1],
                                  self.coefficients[1][0] * x ** 2 + self.coefficients[1][1] * x + self.coefficients[1][2],
                                  0,
                                  0,
                                  0])
            else:
                self.fi_y.append([self.coefficients[0][0] * x + self.coefficients[0][1],
                                  self.coefficients[1][0] * x ** 2 + self.coefficients[1][1] * x + self.coefficients[1][2],
                                  self.coefficients[2][0] * math.exp(self.coefficients[2][1] * x),
                                  self.coefficients[3][0] * math.log(x) + self.coefficients[3][1],
                                  self.coefficients[4][0] * x ** self.coefficients[4][1]])

    # Вычисленине меры отклонения
    def calc_deviation(self):
        for i in range(self.n):
            self.deviation[0] += (self.fi_y[i][0] - self.y[i]) ** 2
            self.deviation[1] += (self.fi_y[i][1] - self.y[i]) ** 2
            if [0, 0, 0] != self.status_eq:
                if [0, 1, 1] == self.status_eq:
                    self.deviation[2] += (self.fi_y[i][2] - self.y[i]) ** 2
                else:
                    self.deviation[2] = 100
                if [1, 0, 1] == self.status_eq:
                    self.deviation[3] += (self.fi_y[i][3] - self.y[i]) ** 2
                else:
                    self.deviation[3] = 100
                if ([1, 1, 1] or [1, 0, 1] or [0, 1, 1]) == self.status_eq:
                    self.deviation[4] = 100
                else:
                    self.deviation[4] += (self.fi_y[i][4] - self.y[i]) ** 2
            else:
                self.deviation[2] += (self.fi_y[i][2] - self.y[i]) ** 2
                self.deviation[3] += (self.fi_y[i][3] - self.y[i]) ** 2
                self.deviation[4] += (self.fi_y[i][4] - self.y[i]) ** 2

    # Вычисление среднеквадратичного отклонения
    def calc_std_deviation(self):
        for i in range(len(self.deviation)):
            self.std_deviation.append(math.sqrt(self.deviation[i] / self.n))

    # Нахождение наиболее точного уравнения
    def best_eq(self):
        val, idx = min((val, idx) for (idx, val) in enumerate(self.std_deviation))
        self.best_eq_id = idx
        best_eq = {
            0: f'Линейная: {self.coefficients[0][0]} * x + {self.coefficients[0][1]}',
            1: f'Квадратичная: {self.coefficients[1][0]} * x^2 + {self.coefficients[1][1]} * x + {self.coefficients[1][2]}',
            2: f'Экспоненциальная: {self.coefficients[2][0] if self.status_eq[0] != 1 else ""} * e^({self.coefficients[2][1] if self.status_eq[0] != 1 else ""} * x)',
            3: f'Логарифмическая: {self.coefficients[3][0] if self.status_eq[1] != 1 else ""} * ln(x) + {self.coefficients[3][1] if self.status_eq[1] != 1 else ""}',
            4: f'Степенная: {self.coefficients[4][0] if self.status_eq[2] != 1 else ""} * x^({self.coefficients[4][1] if self.status_eq[2] != 1 else ""})'
        }
        return best_eq.get(idx)

    # Вычисление достоверности аппроксимации
    def acc_approximation(self, count_fun):
        count_fun.append(self.status_eq[0])
        count_fun.append(self.status_eq[1])
        count_fun.append(self.status_eq[2])
        num_fun = 0
        for i in count_fun:
            if i == 0:
                numerator = 0
                denominator_1, denominator_2 = 0, 0
                for j in range(self.n):
                    numerator += (self.y[j] - self.fi_y[j][num_fun]) ** 2
                    denominator_1 += self.fi_y[j][num_fun] ** 2
                    denominator_2 += self.fi_y[j][num_fun]
                try:
                    acc = 1 - numerator / (denominator_1 - (denominator_2 ** 2) / self.n)
                except ZeroDivisionError:
                    self.R_sec.append(1)
                    continue
                self.R_sec.append(acc)
            else:
                self.R_sec.append("-")
            num_fun += 1

    # Вычисление коэффициента пиросна для линейного уравнения
    def pirson_coef(self) -> float:
        x_avg = sum(self.x) / self.n
        y_avg = sum(self.y) / self.n
        numerator = 0
        denominator_1, denominator_2 = 0, 0
        for i in range(self.n):
            numerator += (self.x[i] - x_avg) * (self.y[i] - y_avg)
            denominator_1 += (self.x[i] - x_avg) ** 2
            denominator_2 += (self.y[i] - y_avg) ** 2
        return numerator / math.sqrt(denominator_1 * denominator_2) if (denominator_1 != 0 and denominator_2 != 0) else "inf"

    def print_best_coef(self):
        eq = {
            0: "Линейная: ",
            1: "Квадратичная: ",
            2: "Экспоненциальная: ",
            3: "Логарифмическая: ",
            4: "Степенная: "
        }
        print("\n", color.UNDERLINE + color.YELLOW, "Лучшее среднеквадратичное отклонение:", color.END)
        self.best_eq()
        for i in range(5):
            if i == self.best_eq_id:
                print("\t", color.UNDERLINE + color.GREEN, eq.get(i), self.std_deviation[i], color.END)
                continue
            print("\t", eq.get(i), self.std_deviation[i])

    # Вывод результата на экран
    def print_result(self):
        print()
        print(color.UNDERLINE + color.YELLOW + "Полученные апроксимирующие функции:" + color.END, '\n',
              '\t', f'Линейная: {self.coefficients[0][0]} * x + {self.coefficients[0][1]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Мера отклонения: {color.END} {self.deviation[0]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Среднеквадратичное отклонение: {color.END} {self.std_deviation[0]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Достоверность аппроксинации: {color.END} {self.R_sec[0]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Коэффициент Пирсона: {color.END} {self.pirson_coef()}', '\n\n',

              '\t', f'Квадратичная: {self.coefficients[1][0]} * x^2 + {self.coefficients[1][1]} * x + {self.coefficients[1][2]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Мера отклонения: {color.END} {self.deviation[1]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Среднеквадратичное отклонение: {color.END} {self.std_deviation[1]}', '\n',
              '\t\t', f'{color.BOLD + color.GREEN} Достоверность аппроксинации: {color.END} {self.R_sec[1]}', '\n')
        if self.status_eq[0] == 0:
            print('\t', f'Экспоненциальная: {self.coefficients[2][0]} * e^({self.coefficients[2][1]} * x)', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Мера отклонения: {color.END} {self.deviation[2]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Среднеквадратичное отклонение: {color.END} {self.std_deviation[2]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Достоверность аппроксинации: {color.END} {self.R_sec[2]}', '\n')
        if self.status_eq[1] == 0:
            print('\t', f'Логарифмическая: {self.coefficients[3][0]} * ln(x) + {self.coefficients[3][1]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Мера отклонения: {color.END} {self.deviation[3]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Среднеквадратичное отклонение: {color.END} {self.std_deviation[3]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Достоверность аппроксинации: {color.END} {self.R_sec[3]}', '\n')
        if self.status_eq[2] == 0:
            print('\t', f'Степенная: {self.coefficients[4][0]} * x^({self.coefficients[4][1]})', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Мера отклонения: {color.END} {self.deviation[4]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Среднеквадратичное отклонение: {color.END} {self.std_deviation[4]}', '\n',
                  '\t\t', f'{color.BOLD + color.GREEN} Достоверность аппроксинации: {color.END} {self.R_sec[4]}', '\n')
        print(color.UNDERLINE + color.YELLOW + "Таблица подсчетов:" + color.END)
        print(self.get_tablet())
        self.print_best_coef()
        print(f'\n{color.BOLD + color.GREEN} Лучшей аппроксимирующей функцией является: {color.END} '
              f'\n\t {self.best_eq()}')
        print()

    # Сохранение результата в файл
    def save_in_file(self):
        with open("output.txt", "w") as file:
            print("Полученные апроксимирующие функции:", '\n',
                  '\t', f'Линейная: {self.coefficients[0][0]} * x + {self.coefficients[0][1]}', '\n',
                  '\t\t', f'Мера отклонения: {self.deviation[0]}', '\n',
                  '\t\t', f'Среднеквадратичное отклонение: {self.std_deviation[0]}', '\n',
                  '\t\t', f'Достоверность аппроксинации: {self.R_sec[0]}', '\n',
                  '\t\t', f'Коэффициент Пирсона: {self.pirson_coef()}', '\n\n',

                  '\t', f'Квадратичная: {self.coefficients[1][0]} * x^2 + {self.coefficients[1][1]} * x + {self.coefficients[1][2]}', '\n',
                  '\t\t', f'Мера отклонения: {self.deviation[1]}', '\n',
                  '\t\t', f'Среднеквадратичное отклонение: {self.std_deviation[1]}', '\n',
                  '\t\t', f'Достоверность аппроксинации: {self.R_sec[1]}', '\n',
                  file=file)
            if self.status_eq[0] == 0:
                print('\t', f'Экспоненциальная: {self.coefficients[2][0]} * e^({self.coefficients[2][1]} * x)', '\n',
                      '\t\t', f'Мера отклонения: {self.deviation[2]}', '\n',
                      '\t\t', f'Среднеквадратичное отклонение: {self.std_deviation[2]}', '\n',
                      '\t\t', f'Достоверность аппроксинации: {self.R_sec[2]}', '\n',
                      file=file)
            if self.status_eq[1] == 0:
                print('\t', f'Логарифмическая: {self.coefficients[3][0]} * ln(x) + {self.coefficients[3][1]}', '\n',
                      '\t\t', f'Мера отклонения: {self.deviation[3]}', '\n',
                      '\t\t', f'Среднеквадратичное отклонение: {self.std_deviation[3]}', '\n',
                      '\t\t', f'Достоверность аппроксинации: {self.R_sec[3]}', '\n',
                      file=file)
            if self.status_eq[2] == 0:
                print('\t', f'Степенная: {self.coefficients[4][0]} * x^({self.coefficients[4][1]})', '\n',
                      '\t\t', f'Мера отклонения: {self.deviation[4]}', '\n',
                      '\t\t', f'Среднеквадратичное отклонение: {self.std_deviation[4]}', '\n',
                      '\t\t', f'Достоверность аппроксинации: {self.R_sec[4]}',
                      file=file)
            print("\n", "Таблица подсчетов:", file=file)
            print(self.get_tablet(), file=file)

            print("\n", f'Лучшей аппроксимирующей функцией является: \n\t {self.best_eq()}', file=file)

    # Создание таблицы вычислений
    def get_tablet(self):
        for i in range(self.n):
            if self.status_eq[0] == 0 and self.status_eq[1] == 0 and self.status_eq[2] == 0:
                self.tablet.append([i + 1, self.x[i], self.y[i],
                                    self.fi_y[i][0], self.fi_y[i][1], self.fi_y[i][2], self.fi_y[i][3], self.fi_y[i][4]])
            elif self.status_eq[0] == 0 and self.status_eq[1] == 1 and self.status_eq[2] == 1:
                self.tablet.append([i + 1, self.x[i], self.y[i],
                                    self.fi_y[i][0], self.fi_y[i][1], self.fi_y[i][2], "-", "-"])
            elif self.status_eq[0] == 1 and self.status_eq[1] == 0 and self.status_eq[2] == 1:
                self.tablet.append([i + 1, self.x[i], self.y[i],
                                    self.fi_y[i][0], self.fi_y[i][1], "-", self.fi_y[i][3], "-"])
            else:
                self.tablet.append([i + 1, self.x[i], self.y[i],
                                    self.fi_y[i][0], self.fi_y[i][1], "-", "-", "-"])
        return tabulate(self.tablet,
                        headers=["№", "x", "f(x)", "Лин. рег.", "Квад. рег.", "Эксп. рег.", "Лог. рег.", "Степ. рег."],
                        tablefmt="grid", floatfmt="2.5f")

    # Отрисовка графиков
    def draw_graph(self):
        try:
            ax = plt.gca()
            plt.grid()
            # Убираем видимость верхней и правой границы сетки
            ax.spines['top'].set_visible(False)
            ax.spines['right'].set_visible(False)

            x = np.linspace(self.x[0], self.x[-1] + 0.5, 100)
            equations = {
                1: [(self.coefficients[0][0] * i + self.coefficients[0][1]) for i in x],
                2: [(self.coefficients[1][0] * i ** 2 + self.coefficients[1][1] * i + self.coefficients[1][2]) for i in x],
                3: [(self.coefficients[2][0] * math.exp(self.coefficients[2][1] * i)) for i in x] if self.status_eq[0] != 1 else 0,
                4: [(self.coefficients[3][0] * math.log(i) + self.coefficients[3][1]) for i in x] if self.status_eq[1] != 1 else 0,
                5: [(self.coefficients[4][0] * i ** self.coefficients[4][1]) for i in x] if self.status_eq[2] != 1 else 0
            }
            plt.title("Графики аппроксимирующих функций")
            plt.plot(x, equations[1], color='g', linewidth=2, alpha=0.3, label="Линейная") if self.best_eq_id != 0 else plt.plot(x, equations[1], color='g', linewidth=2, alpha=1, label="Линейная")
            plt.plot(x, equations[2], color='c', linewidth=2, alpha=0.3, label="Квадратная") if self.best_eq_id != 1 else plt.plot(x, equations[2], color='c', linewidth=2, alpha=1, label="Квадратная")
            if self.status_eq[0] == 0:
                plt.plot(x, equations[3], color='m', linewidth=2, alpha=0.3, label="Экспоненциальная") if self.best_eq_id != 2 else plt.plot(x, equations[3], color='m', linewidth=2, alpha=1, label="Экспоненциальная")
            if self.status_eq[1] == 0:
                plt.plot(x, equations[4], color='y', linewidth=2, alpha=0.3, label="Логарифмическая") if self.best_eq_id != 3 else plt.plot(x, equations[4], color='y', linewidth=2, alpha=1, label="Логарифмическая")
            if self.status_eq[2] == 0:
                plt.plot(x, equations[5], color='b', linewidth=2, alpha=0.3, label="Степенная") if self.best_eq_id != 4 else plt.plot(x, equations[5], color='b', linewidth=2, alpha=1, label="Степенная")
            plt.plot(x, x * 0, color="black", linewidth=1)
            for i in range(self.n):
                plt.scatter(self.x[i], self.y[i], color="red", s=35)
            plt.legend()
            if self.status_save == 1:
                plt.savefig("graph.png")
            else:
                plt.show()
            del x
        except ValueError:
            print(traceback.format_exc())
        except ZeroDivisionError:
            return
        except OverflowError:
            return
