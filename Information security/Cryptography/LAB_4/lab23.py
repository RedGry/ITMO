import math

N = 385751370271
e1 = 365797
e2 = 1109663

C1 = '''
58541562205 
167003685579 
381877628242 
256218527098 
164244249864 
6588741823 
180308234660
174572441677 
259951955034 
378589342820 
319378579620
21405495597 
226860843155 
'''

C2 = '''
78032032470 
13064174635 
326727914830 
364066420370 
177576861402 
65863828523 
111437045566
124743274954
119577259869 
85769669875 
4688914942 
261002397567 
341722428571 
'''


# Расширенный алгоритм Евклида https://all-python.ru/raznoe/algoritm-evklida.html
def gcd_extended(num1, num2):
    if num1 == 0:
        return num2, 0, 1
    else:
        div, x, y = gcd_extended(num2 % num1, num1)
    return div, y - (num2 // num1) * x, x


def solver(N, e1, e2, C1, C2):
    print("==========================")
    print("========== DATA ==========")
    print("==========================")
    print(f"N = {N}")
    print(f"e1 = {e1}")
    print(f"e2 = {e2}")
    print(f"C1 = {C1}")
    print(f"C2 = {C2}")

    print("==========================")
    print("== SOLVE - keyless read ==")
    print("==========================")

    c1 = list(map(int, C1.split()))
    c2 = list(map(int, C2.split()))
    message = ""

    a, r, s = gcd_extended(e1, e2)

    print(f"(e1 * r) + (e2 * s) = ±1")
    print(f" r = {r},\n s = {s}", "\n")

    for i in range(len(c1)):
        c1r = pow(c1[i], r, N)
        c2s = pow(c2[i], s, N)
        m = (c1r * c2s) % N
        part = m.to_bytes(4, byteorder='big').decode('cp1251')
        message += part
        print(f"(C1[{i}]^r) mod N = {c1r}")
        print(f"(C2[{i}]^s) mod N = {c2s}")
        print(f"m{i} = ({c1r} * {c2s}) mod {N} = {m} => text({m}) = {part}", "\n")

    print(f"message = {message}")


solver(N, e1, e2, C1, C2)
