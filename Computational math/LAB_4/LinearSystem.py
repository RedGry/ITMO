if __name__ == "__main__":
    print("Этот файл нужен для вычислений, "
          "если хотите запустить программу,"
          "то запускайте \'main.py\'")


class SolverSystem:
    n = 0
    x = []
    system = []
    det = 0
    swap = 0

    def __init__(self, n, system):
        self.n = n
        self.system = system
        self.x = []
        self.det = 0
        self.swap = 0

    def calculate(self):
        self.make_triangle()
        self.calc_vector_x()

    def check_diagonal(self, i):
        j = i
        while j < self.n:
            if (self.system[j][i] != 0) and (self.system[i][j] != 0):
                swap = self.system[j]
                self.system[j] = self.system[i]
                self.system[i] = swap
                self.swap += 1
                return
            j += 1
        print('Нет решений!')
        return ArithmeticError

    def make_triangle(self):
        try:
            i = 0
            while i < self.n:
                if self.system[i][i] == 0:
                    self.check_diagonal(i)
                m = i
                while m < self.n - 1:
                    a = -(self.system[m + 1][i] / self.system[i][i])
                    j = i
                    while j < self.n:
                        self.system[m + 1][j] += a * self.system[i][j]
                        j += 1
                    self.system[m + 1][-1] += a * self.system[i][-1]
                    m += 1
                k = 0
                line_sum = 0
                while k < self.n:
                    line_sum += self.system[i][k]
                    k += 1
                if line_sum == 0:
                    print('Данная система не совместима, решений нет!')
                    return ArithmeticError
                i += 1
        except ValueError:
            print('Некорректная работа с данными!')
            return

    def calc_vector_x(self):
        i = self.n - 2
        self.x.append(self.system[self.n - 1][-1] / self.system[self.n - 1][self.n - 1])
        while i > -1:
            k = self.n - 1
            val = self.system[i][-1]
            while k > i:
                val -= self.x[self.n - 1 - k] * self.system[i][k]
                k -= 1
            self.x.append(val / self.system[i][i])
            i -= 1

    def get_x(self):
        return self.x
