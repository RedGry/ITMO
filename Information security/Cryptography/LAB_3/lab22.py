N = 680953235477
e = 920197
C = '''
391097155052 
640128264104 
655783446185 
380882921502 
243151555158 
525608289811 
439378081915 
674406455075 
295448137012 
494853048412 
566308391875 
623790961908 
222667625162
'''


def solver(N, e, C):
    print("==========================")
    print("========== DATA ==========")
    print("==========================")
    print(f"N = {N}")
    print(f"e = {e}")
    print(f"C = {C}")

    print("==========================")
    print("== SOLVE - method reEnc ==")
    print("==========================")
    for y in list(map(int, C.split())):
        yi = pow(y, e, N)
        res = 0
        while yi != y:
            res = yi
            yi = pow(yi, e, N)
        print(res.to_bytes(4, byteorder='big').decode('cp1251'), end='')


solver(N, e, C)
