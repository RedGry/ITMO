import math
from tabulate import tabulate
import matplotlib.pyplot as plt
import numpy as np


def read_file_data():
    while True:
        path = input("Введите путь к файлу: ").strip()
        try:
            with open(path) as f:
                data = [list(map(float, row.split())) for row in f.readlines()]
                return data[0]
        except FileNotFoundError:
            print("Файл не найден!", '\n')
        except ValueError:
            print("Неправильный формат файла!", '\n')


def draw_graph(fun_data, fun):
    ax = plt.gca()
    plt.grid()

    ax.spines['top'].set_visible(False)
    ax.spines['right'].set_visible(False)

    x, y = zip(*fun_data)

    if fun == "ap":
        x_new = list(x)
        y_new = list(y)

        plt.title("График Эмпирической функции распределения")
        plt.plot([x_new[0] - 0.5, x_new[0]], [y_new[0], y_new[0]], color='r')
        plt.plot([x_new[-1], x_new[-1] + 0.5], [y_new[-1], y_new[-1]], color='r')
        for i in range(len(x_new) - 1):
            plt.plot([x_new[i], x_new[i + 1]], [y_new[i + 1], y_new[i + 1]], color='r')

    elif fun == "pol":
        plt.title("Полигон частот")
        plt.plot(x, y, '-o')

    plt.savefig(f"graph_{fun}.png")
    plt.show()


def draw_histogram(data):
    x, y = zip(*data)

    fig, ax = plt.subplots()
    ax.bar(x, y)

    ax.set_facecolor("seashell")
    fig.set_facecolor("floralwhite")
    fig.set_figwidth(12)
    fig.set_figheight(6)

    plt.title("Гистограмма относительных частот")
    plt.savefig("histogram.png")
    plt.show()


def info_about_data(data):
    data.sort()
    n = len(data)
    maximum = data[-1]
    minimum = data[0]
    scale = maximum - minimum
    interval_count = round(1 + math.log(n, 2), 0)
    h = scale / interval_count
    x_start = minimum - h / 2
    m = sum(data) / n
    data_2 = [i ** 2 for i in data]
    d = sum(data_2) / n - m ** 2
    q = math.sqrt(d)
    return n, maximum, minimum, scale, interval_count, h, x_start, m, d, q


def statistic_param(data):
    m_sum = 0
    d_sum = 0
    for i in range(len(data)):
        m_sum += data[i][2] * data[i][4]
        d_sum += data[i][2] ** 2 * data[i][4]
    d_sum -= m_sum ** 2
    q_avg = math.sqrt(d_sum)
    d_spec = len(data) / (len(data) - 1) * d_sum
    q_spec = math.sqrt(d_spec)
    return m_sum, d_sum, q_avg, d_spec, q_spec


def print_ap_fun(data, fun_param):
    count = 0
    draw_fun = []
    print('\n', "Эмпирическая функция:")
    for i in fun_param:
        i = round(i, 4)
        if i == 0:
            draw_fun.append((data[0], i))
            print("\t", i, f", при x <= {data[0]}")
        elif i == 1:
            draw_fun.append((data[-1], i))
            print("\t", i, f", при x > {data[-1]}")
            break
        else:
            draw_fun.append((data[count], i))
            print("\t", i, f", при {prev} < x <= {data[count]}")
        prev = data[count]
        count += 1
    return draw_fun


data = read_file_data()

# Вычисление статистических параметров данных
n, maximum, minimum, scale, interval_count, h, x_start, m, d, q = info_about_data(data)

print("Вариационный ряд:", data)
print("Максимальное зн.:", maximum)
print("Минимальное зн.:", minimum)
print("Размах:", round(scale, 4), '\n')
print("Мат. Ожидание:", round(m, 4))
print("Дисперсия:", round(d, 4))
print("Среднеквадратичное отклонение:", round(q, 4))

# Вычисление и построение эмпирической функции
fun_param = []
fun_param_new = []
data_un = np.unique(data)
for i in data_un:
    fun_param.append(data.count(i) / n)

p = 0
for i in range(len(fun_param)):
    fun_param_new.append(round(p, 4))
    p += fun_param[i]
    if i + 1 == len(fun_param):
        fun_param_new.append(round(p, 4))

draw_fun_data = print_ap_fun(data_un, fun_param_new)
draw_graph(draw_fun_data, "ap")

# Группировка значений
print()
print("Кол-во интервалов:", interval_count)
print("Величина интервала:", round(h, 4))
print("x(нач.) =", x_start, '\n')

new_data = []
polygon_data = []
histogram_data = []
prev = 0

while x_start < maximum:
    prev = x_start
    x_start = round(x_start + h, 4)
    count = 0
    avg = (prev + x_start) / 2
    for i in data:
        if prev <= i < x_start + 0.0001:
            count += 1
    W = count / n
    p_w = W / h
    polygon_data.append((avg, count))
    histogram_data.append((f"[{round(prev, 2)}; {round(x_start, 2)})", p_w))
    new_data.append([prev, x_start, avg, count, W, p_w])

new_data = [[round(i, 4) for i in data1] for data1 in new_data]

print("\n\t", "Группировка значений:")
print(tabulate(new_data,
               headers=["Интервал левое", "Интервал правое", "Сред. зн.",
                        "Частота n(i)", "Относительная частота", "Плотность от. частот"],
               tablefmt="grid", floatfmt="2.5f"))

draw_graph(polygon_data, "pol")
draw_histogram(histogram_data)

# Вычисление статистических параметров группировки данных
m_sum, d_sum, q_avg, d_spec, q_spec = statistic_param(new_data)

print("Выборочное среднее:", round(m_sum, 4))
print("Выборочная дисперсия:", round(d_sum, 4))
print("Выборочное среднеквадратичное отклонение:", round(q_avg, 4))
print("Исправленная выборочная дисперсия:", round(d_spec, 4))
print("Исправленное выборочное среднеквадратичное отклонение:", round(q_spec, 4))

# Вычисление эмпиритрической функции
# param = 0
# fun_param = []
# for i in range(len(new_data)):
#     fun_param.append(param)
#     param += new_data[i][4]
#     if i + 1 == len(new_data):
#         fun_param.append(param)
#
# count = -1
# draw_ap_fun = []
# print()
# print("Эмпирическая функция:")
# for i in fun_param:
#     i = round(i, 4)
#     if i == 0:
#         draw_ap_fun.append((new_data[0][0], i))
#         print("\t", i, f", при x <= {new_data[0][0]}")
#     elif i == 1:
#         draw_ap_fun.append((new_data[-1][1], i))
#         print("\t", i, f", при x > {new_data[-1][0]}")
#     else:
#         draw_ap_fun.append((new_data[count][1], i))
#         print("\t", i, f", при {new_data[count][0]} < x <= {new_data[count][1]}")
#     count += 1
#
# draw_graph(draw_ap_fun, "ap")
