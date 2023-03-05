GREEN = '\033[92m'
RED = '\033[91m'
END = '\033[0m'


def solver(q, frame, alg='fifo') -> None:
    timer = 0
    time = 0
    if alg.lower() == 'fifo':
        clock = [-1] * len(frame)
        for i in q:
            timer += 1
            if i not in frame:
                val, idx = min((val, idx) for (idx, val) in enumerate(clock))
                clock[idx] = time
                time += 1
                frame[idx] = i
                print(f'Время={timer};', f'{RED}fault={1}{END};', f'страница={i};', f'кадр={frame}')
            else:
                print(f'Время={timer};', f'{GREEN}fault={0}{END};', f'страница={i};', f'кадр={frame}')
        print(f'Всего замен = {time}')
    if alg.lower() == 'lru':
        clock = [] if sum(frame) < 0 else frame[::1]
        for i in q:
            timer += 1
            if i not in clock:
                if len(clock) == len(frame):
                    clock.remove(clock[0])
                    clock.append(i)
                else:
                    clock.append(i)
                time += 1
                print(f'Время={timer};', f'{RED}fault={1}{END};', f'страница={i};', f'кадр={clock}')
            else:
                clock.remove(i)
                clock.append(i)
                print(f'Время={timer};', f'{GREEN}fault={0}{END};', f'страница={i};', f'кадр={clock}')
        print(f'Всего замен = {time}')


frameLen = 10
# q = [2, 15, 4, 14, 2, 18, 6, 12, 18, 17, 4, 8, 16, 6, 18, 8, 14, 0, 10, 18]
# frame = [13, 6, 14, 13, 4, 12, 11, 1, 9, 15]
# frame = [-1] * frameLen

# Шахов: https://sun9-32.userapi.com/impg/_CmFoMdIPCxgDwAcPcUqQc6rPdVQCxfZBjPA2g/UTA5u102wEc.jpg?size=2360x508&quality=96&sign=81223ce88bee39753eace4c3d8a7793e&type=album
# Ответ: 10
q = [5, 21, 1, 12, 20, 3, 16, 1, 14, 0, 4, 10, 13, 17 , 14, 0, 17, 1, 15, 5]
frame = [5, 15, 1, 17, 0, 14, 17, 13, 10, 4, 0]

solver(q, frame, 'lru')

