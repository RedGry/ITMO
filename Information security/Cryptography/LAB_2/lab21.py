import math

N = 7533841359567
e = 3063167
C = '''
20373576587572
48282448633797
2859826820449
30302044163645
30736783387104
5008734894376
23296448238734
41172678840173
58656690066465
44574048719827
21962937148701
38826220113907
'''


def solver(N, e, C):
    print("==========================")
    print("========== DATA ==========")
    print("==========================")
    print(f"N = {N}")
    print(f"e = {e}")
    print(f"C = {C}")

    print("==========================")
    print("== SOLVE - method Ferma ==")
    print("==========================")
    message = ""

    n = int(math.sqrt(N) // 1 + 1)
    print(f"n = [sqrt(N)] + 1 = {n}")

    i = 0
    while True:
        i += 1
        t = n + i
        # print(f"t{i} = n + {i} = {t}")
        w = t ** 2 - N
        # print(f"w{i} = t{i} ^ 2 - N = {t ** 2} - {N} = {w}")
        sqrt_w = math.sqrt(w)
        if sqrt_w % 1 == 0:
            sqrt_w = int(sqrt_w)
            # print(f"sqrt(w) = {sqrt_w}", "\n")
            break
        else:
            pass
            # print(f"sqrt(w) = {sqrt_w} - error", "\n")

    p = t + sqrt_w
    q = t - sqrt_w
    phi = round((p - 1) * (q - 1))
    d = pow(e, -1, phi)

    print(f"p = t + sqrt(w) = {t} + {sqrt_w} = {p}")
    print(f"q = t - sqrt(w) = {t} - {sqrt_w} = {q}")
    print(f"Phi(N) = (p - 1) * (q - 1) = ({p - 1}) * ({q - 1}) = {phi}")
    print(f"d = e^(-1) mod Phi(N) = {e}^(-1) mod {phi} = {d}", "\n")

    for i, c in enumerate(C.split()):
        m = pow(int(c), d, N)
        part = m.to_bytes(4, byteorder='big').decode('cp1251')
        print(f'm{i} = C[{i}]^d mod N = {c}^{d} mod {N} = {m} => text({m}) = {part}')
        message += part
    print(f"message = {message}")


solver(N, e, C)
