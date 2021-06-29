import math
import numpy as np

from tabulate import tabulate


class Trapezoid:
    type_equation = 0
    start = 0
    a = 0
    b = 0
    steps = 0
    accuracy = 0
    n = 4
    h = 0

    result = 0
    inaccuracy = 0

    xy = []
    table = []

    def __init__(self, a, b, accuracy, type_equation):
        self.a = a
        self.b = b
        self.start = a
        self.accuracy = math.pow(10, -1 * accuracy)
        self.type_equation = type_equation

    def calc(self):
        self.table = []
        self.xy = []
        self.steps = 0
        self.n = self.check_n()
        y_sum = 0

        self.h = (self.b - self.a) / self.n

        while self.steps != self.n + 1:
            self.xy.append([self.a, self.function(self.a)])
            self.table.append([self.steps, self.xy[self.steps][0], self.xy[self.steps][1]])
            if 0 < self.steps < self.n:
                y_sum += self.xy[self.steps][1]
            self.a += self.h
            self.steps += 1
            if self.steps > 250000:
                self.result = self.h * ((self.xy[0][1] + self.xy[-1][1]) / 2 + y_sum)
                self.inaccuracy = abs(self.max_value_fun() *
                                      math.pow(self.b - self.start, 3) / (12 * math.pow(self.n, 2)))
                print('\t', "Метод трапеций:")
                self.print_result()
                print("Число вычислений привысило 250000 шагов, интеграл вычислен от " + str(self.start)
                      + " до " + str(self.xy[-1][0]) + "!")
                raise ValueError

        self.result = self.h * ((self.xy[0][1] + self.xy[-1][1]) / 2 + y_sum)
        self.inaccuracy = abs(self.max_value_fun() *
                              (math.pow(self.b - self.start, 3) / (12 * math.pow(self.n, 2))))
        self.print_table()
        self.print_result()

    def check_n(self):
        n = abs(math.pow(self.max_value_fun() * math.pow(self.b - self.a, 3) / 12 / self.accuracy, 0.5)) // 1
        if n % 2 == 1:
            n += 1
        else:
            n += 2
        if n <= 4:
            return 4
        else:
            return int(n)

    def max_value_fun(self):
        x = np.linspace(self.start, self.b, 100000)
        maximum = [abs(self.second_derivative(i)) for i in x]
        return max(maximum)

    def function(self, x):
        try:
            if self.type_equation == 1:
                return math.pow(x, 2) - 3
            elif self.type_equation == 2:
                return 5 / x - 2 * x
            elif self.type_equation == 3:
                return math.exp(2 * x) - 2
            elif self.type_equation == 4:
                return 2 * math.pow(x, 3) - 3 * math.pow(x, 2) + 5 * x - 9
        except ZeroDivisionError:
            raise TypeError
            # return 0 * float("inf")

    def second_derivative(self, x):
        try:
            if self.type_equation == 1:
                return 2
            elif self.type_equation == 2:
                return 10 / math.pow(x, 3)
            elif self.type_equation == 3:
                return 4 * math.exp(2 * x)
            elif self.type_equation == 4:
                return 12 * x - 6
        except ZeroDivisionError:
            return self.second_derivative(x + 1e-8)

    def print_table(self):
        print('\t', "Метод трапеций:")
        print(tabulate(self.table, headers=["№ шага", "x", "y"],
                       tablefmt="grid", floatfmt="2.5f"))

    def print_result(self, n=0):
        print("I:", self.result)
        if n == 0:
            print("R(n): ", self.inaccuracy)
        else:
            print("R(" + str(n) + "):", self.inaccuracy)
        print("Число разбиений:", self.n)
