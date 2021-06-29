import warnings
import colors as color
import numpy as np
from tabulate import tabulate
import matplotlib.pyplot as plt
import traceback

warnings.filterwarnings("ignore", category=RuntimeWarning)

if __name__ == "__main__":
    print(color.RED, "Этот файл нужен для вычислений, "
                     "если хотите запустить программу,"
                     "то запускайте \'main.py\'")


class Solver:
    x = []
    y = []
    n = 0
    x0 = 0
    method = 0
    status_save = 0
    answer = 0

    def __init__(self, method: int, data, x: int):
        self.n = len(data)
        self.x0 = x
        self.method = method
        self.status_save = 0

        # Парсировка значений данных
        self.x = [data[i][0] for i in range(len(data))]
        self.y = [data[i][1] for i in range(len(data))]

    def solve(self):
        self.answer = self.lagrange(self.x0) if self.method == 1 else self.newton(self.x0)

        print('\n', color.UNDERLINE + color.YELLOW, "Выберите дествие:", color.END)
        print('\t', "1. Вывести на экран результат", '\n',
              '\t', "2. Сохранить в файл результат")
        while True:
            try:
                self.status_save = int(input("Выберите действие: ").strip())
                if self.status_save == 1:
                    self.print_result()
                    break
                elif self.status_save == 2:
                    self.save_in_file()
                    break
            except TypeError:
                continue
            except ValueError:
                continue
        self.draw_graph(self.lagrange) if self.method == 1 else self.draw_graph(self.newton)

    # Вычисление значения в точке с помощью многослена Лагранджа
    def lagrange(self, x_cur=x0):
        answer = 0

        for i in range(self.n):
            polynomial = 1
            # Вычисляем l(i) = П (x - x(j)) / (x(i) - x(j))
            for j in range(self.n):
                if i != j:
                    polynomial *= (x_cur - self.x[j]) / (self.x[i] - self.x[j])

            # Вычисляем L += y(i) * l(i)
            answer += self.y[i] * polynomial
        return answer

    # Вычисление значения в точке с помощью многослена Ньютона
    def newton(self, x_cur=x0):
        x = np.copy(self.x)
        c = np.copy(self.y)
        m = self.n

        # Вычисление коэффициентов f(x0, x1), ... f(x0, ..., xn)
        # Вычисляем до разделенной разности n порядка
        # Сначала 1-го порядка, потом 2-го и т.д., если это возможно
        for k in range(1, self.n):
            c[k:m] = (c[k:m] - c[k - 1]) / (x[k:m] - x[k - 1])

        # Вычисляем значение в точке
        # Идем от обратного (изнутри -> наружу), если вынести, пример:
        # с0 + (x - x0) * [c1 + (x - x1) * [c2 + (x - x2) * c3]], если раскроем скобки то получим нашу формулу:
        # с0 + с1 * (x - x0) + c2 * (x - x1) * (x - x0) + c3 * (x - x2) * (x - x1) * (x - x0)
        n = self.n - 1  # Степень полинома
        answer = c[n]
        for k in range(1, n + 1):
            answer = c[n - k] + (x_cur - self.x[n - k]) * answer
        return answer

    # Вывод результата на экран
    def print_result(self):
        print()
        print(color.UNDERLINE + color.YELLOW, "Таблица введенных данных (X и Y):", color.END)
        print(self.get_tablet(), "\n")

        if self.method == 1:
            print(color.UNDERLINE + color.YELLOW, "Значение функции используя многочлен Лагранжа", color.END)
        elif self.method == 2:
            print(color.UNDERLINE + color.YELLOW, "Значение функции используя многочлен Ньютона с разделенными "
                                                  "разностями", color.END)
        print('\t', f'{color.BOLD + color.GREEN}f({self.x0}) ={color.END} {self.answer}')
        print()

    # Сохранение результата в файл
    def save_in_file(self):
        with open("output.txt", "w") as file:
            print("Таблица введенных данных (X и Y):", file=file)
            print(self.get_tablet(), file=file)
            print("", file=file)

            if self.method == 1:
                print("Значение функции используя многочлен Лагранжа", file=file)
            elif self.method == 2:
                print("Значение функции используя многочлен Ньютона с разделенными разностями", file=file)
            print('\t', f'f({self.x0}) = {self.answer}', file=file)

    # Создание таблицы данных (X и Y)
    def get_tablet(self):
        x = list.copy(self.x)
        y = list.copy(self.y)
        tablet = [x, y]
        tablet[0].insert(0, "x")
        tablet[1].insert(0, "f(x)")
        return tabulate(tablet,
                        tablefmt="grid", floatfmt="2.5f")

    # Отрисовка графиков
    def draw_graph(self, fi_fun):
        try:
            ax = plt.gca()
            plt.grid()
            # Убираем видимость верхней и правой границы сетки
            ax.spines['top'].set_visible(False)
            ax.spines['right'].set_visible(False)

            x = np.linspace(self.x[0], self.x[-1], 100)
            y = [fi_fun(i) for i in x]

            plt.title("Многочлен Лагранжа") if self.method == 1 else plt.title("Многочлен Ньютона")
            plt.plot(x, x * 0, color="black", linewidth=1)
            plt.plot(self.x, self.y, 'o', color='r', label='Исходные точки')
            plt.plot(x, y, color='b', label='Приблизительная функция')
            plt.plot(self.x0, self.answer, '*', color='g', markersize=10, label='Значение в заданной точке (ответ)')
            plt.legend()
            if self.status_save == 2:
                plt.savefig("graph.png")
            else:
                plt.show()
            del x
            del y
        except ValueError:
            print(traceback.format_exc())
        except ZeroDivisionError:
            return
        except OverflowError:
            return
