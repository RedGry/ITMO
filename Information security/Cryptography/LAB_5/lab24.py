from decimal import Decimal

N1 = 473302960111
N2 = 476210148031
N3 = 478258728547

C1 = '''
384927940677
473049749478 
98141220439 
47772742554 
85402795076 
49762300554 
243238759870 
132174590679 
394107604075 
292566652796 
394413369679 
176379334217 
425745574767 
279970734890
'''

C2 = '''
47337377053 
15502694428 
81559584886 
360290532716 
378412185459 
471133458035 
276394936545 
2116712669 
37111299200 
387986386867 
97786707059 
256442600412 
455327955288 
119517607360
'''

C3 = '''
342954751710 
440889851539 
67503329756 
462595462377 
84092175909 
57911552136 
60433527302 
25311956275 
370327609107 
296462225245 
241699085506 
465708091819 
454345671530 
210180151910
'''


def solver(N1, N2, N3, C1, C2, C3):
    print("=============================")
    print("=========== DATA ============")
    print("=============================")
    print(f"N1 = {N1}")
    print(f"N2 = {N2}")
    print(f"N3 = {N3}")
    print(f"C1 = {C1}")
    print(f"C2 = {C2}")
    print(f"C3 = {C3}")

    print("=============================")
    print("== SOLVE - Chinese theorem ==")
    print("=============================")

    c1 = list(map(int, C1.split()))
    c2 = list(map(int, C2.split()))
    c3 = list(map(int, C3.split()))
    message = ""

    M0 = N1 * N2 * N3
    m1 = N2 * N3
    m2 = N1 * N3
    m3 = N1 * N2
    n1 = pow(m1, -1, N1)
    n2 = pow(m2, -1, N2)
    n3 = pow(m3, -1, N3)

    print(f"M0 = N1 * N2 * N3 = {N1} * {N2} * {N3} = {M0}", "\n")
    print(f"m1 = N2 * N3 = {N2} * {N3} = {m1}")
    print(f"m2 = N1 * N3 = {N1} * {N3} = {m2}")
    print(f"m3 = N1 * N2 = {N1} * {N2} = {m3}", "\n")
    print(f"n1 = m1^(-1) mod N1 = {m1}^(-1) mod {N1} = {n1}")
    print(f"n2 = m2^(-1) mod N2 = {m2}^(-1) mod {N2} = {n2}")
    print(f"n3 = m3^(-1) mod N3 = {m3}^(-1) mod {N3} = {n3}", "\n")

    for i in range(len(c1)):
        S = (c1[i] * n1 * m1) + (c2[i] * n2 * m2) + (c3[i] * n3 * m3)
        SmodM0 = S % M0
        M = round(SmodM0 ** (Decimal(1 / 3)))
        part = M.to_bytes(4, byteorder='big').decode('cp1251')
        message += part
        print(f"S[{i}] = c1[{i}]*n1*m1 + c2[{i}]*n2*m2 + c3[{i}]*n3*m3 = {S}")
        print(f"SmodM0[{i}] = S[{i}] mod M0 = {SmodM0}")
        print(f"M = SmodM0[{i}]^(1/3) = {M} => text(M) = {part}", "\n")

    print(f"message = {message}")


solver(N1, N2, N3, C1, C2, C3)
