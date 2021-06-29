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
    method = 0
    data = {}
    dots = []
    dots_h2 = []
    acc_y = []

    acc = 0
    h = 0
    n = 0
    p = 0

    tablet_solve = []

    def __init__(self, method, data, h, acc):
        self.method = method
        self.data = data
        self.h = h
        self.acc = acc
        self.acc_y = []
        self.dots = []
        self.dots_h2 = []
        self.n = int((data["b"] - data["a"]) / h) + 1
        self.tablet_solve = []
        if self.method == 1:
            self.p = 2
        else:
            self.p = 4

    def solve(self):
        if self.method == 1:
            self.dots_h2 = self.euler_method(self.data["fun"], self.data["a"], self.data["b"],
                                             self.data["y0"], self.h / 2)
            self.dots = self.euler_method(self.data["fun"], self.data["a"], self.data["b"], self.data["y0"], self.h)
            print("\n" + color.UNDERLINE + color.YELLOW + "Вычисление методом Эйлера:" + color.END)
        elif self.method == 2:
            self.dots_h2 = self.milne_method(self.data["fun"], self.data["a"], self.data["b"],
                                             self.data["y0"], self.h / 2)
            self.dots = self.milne_method(self.data["fun"], self.data["a"], self.data["b"], self.data["y0"], self.h)
            print("\n" + color.UNDERLINE + color.YELLOW + "Вычисление методом Милна:" + color.END)
        self.print_table()
        self.draw_graph()

    # Метод Эйлера
    def euler_method(self, fun, a, b, y0, h):
        dots = [(a, y0)]
        self.n = int((b - a) / h) + 1

        for i in range(1, self.n):
            x_prev = dots[i - 1][0]
            y_prev = dots[i - 1][1]
            x_cur = x_prev + h
            y_cur = y_prev + h / 2 * (fun(x_prev, y_prev) + fun(x_cur, y_prev + h * fun(x_prev, y_prev)))
            dots.append((x_cur, y_cur))

        return dots

    # Метод Миллна с использованием метода Рунге-Кутта для подсчета y1, y2, y3
    def milne_method(self, fun, a, b, y0, h):
        dots = [(a, y0)]
        fun_t = [fun(a, y0)]
        self.n = int((b - a) / h) + 1

        # Рунге-Кутта 4 порядка
        for i in range(1, 4):
            x_prev = dots[i - 1][0]
            y_prev = dots[i - 1][1]
            k1 = h * fun(x_prev,         y_prev)
            k2 = h * fun(x_prev + h / 2, y_prev + k1 / 2)
            k3 = h * fun(x_prev + h / 2, y_prev + k2 / 2)
            k4 = h * fun(x_prev + h,     y_prev + k3)

            x_cur = x_prev + h
            y_cur = y_prev + (k1 + 2 * k2 + 2 * k3 + k4) / 6

            dots.append((x_cur, y_cur))
            fun_t.append(fun(x_cur, y_cur))

        # Милн
        for i in range(4, self.n):
            x_cur = dots[i - 1][0] + h

            y_pred = dots[i - 4][1] + 4 * h / 3 * (2 * fun_t[i - 3] - fun_t[i - 2] + 2 * fun_t[i - 1])
            fun_t.append(fun(x_cur, y_pred))
            y_cor = dots[i - 2][1] + h / 3 * (fun_t[i - 2] + 4 * fun_t[i - 1] + fun_t[i])

            while self.acc < abs(y_cor - y_pred) / 29:
                y_pred = y_cor
                fun_t[i] = fun(x_cur, y_pred)
                y_cor = dots[i - 2][1] + h / 3 * (fun_t[i - 2] + 4 * fun_t[i - 1] + fun_t[i])

            dots.append((x_cur, y_cor))

        return dots

    # Вывод результата на экран
    def print_table(self):
        for i in range(len(self.dots)):
            acc_y = self.data["acc_fun"](self.dots[i][0])
            runge_acc = (self.dots[i][1] - self.dots_h2[2 * i][1]) / (2 ** self.p - 1)
            self.acc_y.append(acc_y)
            self.tablet_solve.append([i, self.dots[i][0], self.dots[i][1],
                                      runge_acc, abs(acc_y - self.dots[i][1]), acc_y])
        print(tabulate(self.tablet_solve,
                       headers=["№", "x", "y(x)", "Рунге", "Погрешность с точным", "Точное значение"],
                       tablefmt="grid", floatfmt="2.5f"))
        print()

    # Отрисовка графиков
    def draw_graph(self):
        try:
            ax = plt.gca()
            plt.grid()
            # Убираем видимость верхней и правой границы сетки
            ax.spines['top'].set_visible(False)
            ax.spines['right'].set_visible(False)

            x = np.array([dot[0] for dot in self.dots])
            y = np.array([dot[1] for dot in self.dots])

            acc_x = np.linspace(self.data["a"], self.data["b"], 100)
            acc_y = [self.data["acc_fun"](x) for x in acc_x]

            plt.title("Методом Эйлера") if self.method == 1 else plt.title("Методом Милна")
            # plt.plot(x, x * 0, color="black", linewidth=1)
            plt.plot(x, y, '-o', color='r', label='y(x)')
            plt.plot(acc_x, acc_y, color='g', label='acc_y(x)')
            plt.legend()
            plt.show()
            del x, y, acc_x, acc_y
        except ValueError:
            print(traceback.format_exc())
        except ZeroDivisionError:
            return
        except OverflowError:
            return
